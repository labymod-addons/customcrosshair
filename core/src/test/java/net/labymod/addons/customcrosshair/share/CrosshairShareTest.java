/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.customcrosshair.share;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.Base64;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasPreset;
import net.labymod.addons.customcrosshair.share.CrosshairShare.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class CrosshairShareTest {

  private static final Settings CUSTOM = new Settings(
      true,
      90,
      false,
      new ShareColor(0xFF3366FF, true, 2.5F),
      3,
      ShareColor.of(0x80000000),
      true,
      ShareColor.of(0xFF00FF00),
      new ShareColor(0xFFFF0000, false, 0.5F),
      ShareColor.of(0xFFFFFF00)
  );

  @ParameterizedTest
  @EnumSource(CrosshairCanvasPreset.class)
  void everyPresetSurvivesTheRoundTrip(CrosshairCanvasPreset preset) {
    CrosshairShare share = CrosshairShare.of(preset.getCanvas(), CUSTOM, false);

    CrosshairShare read = CrosshairShare.read(share.write());

    assertNotNull(read);
    assertEquals(preset.getCanvas(), read.canvas());
    assertEquals(CUSTOM, read.settings());
  }

  @Test
  void anEmptyCanvasSurvivesTheRoundTrip() {
    CrosshairShare read = CrosshairShare.read(
        CrosshairShare.of(new CrosshairCanvas(), Settings.DEFAULTS, true).write()
    );

    assertNotNull(read);
    assertEquals(new CrosshairCanvas(), read.canvas());
  }

  @Test
  void aReceivedCrosshairIsSwitchedOnAndTheSwitchIsNotSent() {
    JsonObject data = CrosshairShare.of(canvas(), Settings.DEFAULTS, false).write();

    assertFalse(data.getAsJsonObject(CrosshairShare.VALUES_KEY).has("enabled"));
    CrosshairShare read = CrosshairShare.read(data);
    assertNotNull(read);
    assertTrue(read.isEnabled());
  }

  @Test
  void writesColorsInTheShapeOfTheConfig() {
    JsonObject values = CrosshairShare.of(canvas(), CUSTOM, true).write()
        .getAsJsonObject(CrosshairShare.VALUES_KEY);

    assertEquals(JsonParser.parseString(
        "{\"value\":" + 0xFF3366FF + ",\"chroma\":true,\"chromaSpeed\":2.5}"
    ), values.get("color"));
    assertEquals(new JsonPrimitive(0x80000000), values.get("outlineColor"));
    assertEquals(JsonParser.parseString(
        "{\"value\":" + 0xFFFF0000 + ",\"chromaSpeed\":0.5}"
    ), values.get("hostileColor"));
  }

  @Test
  void readsTheValuesOfASavedConfig() {
    // A settings.json of the addon, with the keys that are not shared
    JsonObject values = JsonParser.parseString("{\"enabled\":false,"
        + "\"canvas\":\"LMCH-CRAgQIDwHwIECBAA\",\"displayInThirdPerson\":false,\"rotation\":0,"
        + "\"vanillaBlending\":true,\"color\":-1,\"dynamicColor\":false,"
        + "\"neutralColor\":-16711936,\"hostileColor\":-65536,\"playerColor\":-256,"
        + "\"type\":\"DEFAULT\",\"configVersion\":1}").getAsJsonObject();

    CrosshairShare read = CrosshairShare.read(data("LMCH-CRAgQIDwHwIECBAA", values));

    assertNotNull(read);
    assertEquals(Settings.DEFAULTS, read.settings());
  }

  @Test
  void missingValuesTakeTheirDefaults() {
    CrosshairShare read = CrosshairShare.read(data(encoded(), new JsonObject()));

    assertNotNull(read);
    assertEquals(Settings.DEFAULTS, read.settings());
  }

  @Test
  void ignoresUnknownKeys() {
    JsonObject data = CrosshairShare.of(canvas(), CUSTOM, true).write();
    data.addProperty("future", 1);
    data.getAsJsonObject(CrosshairShare.VALUES_KEY).addProperty("futureSetting", "x");

    CrosshairShare read = CrosshairShare.read(data);

    assertNotNull(read);
    assertEquals(CUSTOM, read.settings());
  }

  @Test
  void rejectsAMissingOrMalformedPart() {
    JsonObject noCanvas = new JsonObject();
    noCanvas.add(CrosshairShare.VALUES_KEY, new JsonObject());
    assertNull(CrosshairShare.read(noCanvas));

    JsonObject numberCanvas = new JsonObject();
    numberCanvas.addProperty(CrosshairShare.CANVAS_KEY, 1);
    numberCanvas.add(CrosshairShare.VALUES_KEY, new JsonObject());
    assertNull(CrosshairShare.read(numberCanvas));

    JsonObject noValues = new JsonObject();
    noValues.addProperty(CrosshairShare.CANVAS_KEY, encoded());
    assertNull(CrosshairShare.read(noValues));

    assertNull(CrosshairShare.read(data(encoded(), new JsonPrimitive("values"))));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "LMCH-",
      "LMCH-!!!!",
      // A code of another client, never written by this addon
      "LCCH-9-AAAAAAAAAAA=",
      "lmch-CRAgQIDwHwIECBAA",
      // Too long for any canvas
      "LMCH-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
  })
  void rejectsTextThatIsNoEncodedCanvas(String encoded) {
    assertNull(CrosshairShare.read(data(encoded, new JsonObject())));
  }

  @Test
  void rejectsSizesTheEncoderNeverWrites() {
    // Even, zero, negative and larger than the canvas
    for (int size : new int[]{0, -1, 2, 14, 17, 127}) {
      int length = size > 0 ? (size * size + 7) / 8 : 1;
      assertNull(CrosshairShare.read(data(raw(size, length), new JsonObject())), "size " + size);
    }
  }

  @Test
  void rejectsAPixelCountThatDoesNotMatchTheSize() {
    // 15 x 15 pixels need 29 bytes
    assertNull(CrosshairShare.read(data(raw(15, 28), new JsonObject())));
    assertNull(CrosshairShare.read(data(raw(15, 30), new JsonObject())));
    assertNotNull(CrosshairShare.read(data(raw(15, 29), new JsonObject())));
  }

  @Test
  void rejectsSettingsOfTheWrongType() {
    assertRejected("displayInThirdPerson", new JsonPrimitive("true"));
    assertRejected("vanillaBlending", new JsonPrimitive(1));
    assertRejected("dynamicColor", JsonNull.INSTANCE);
    assertRejected("rotation", new JsonPrimitive("90"));
    assertRejected("rotation", new JsonPrimitive(true));
    assertRejected("outlineThickness", new JsonObject());
    assertRejected("color", new JsonPrimitive("#FFFFFF"));
    assertRejected("color", JsonParser.parseString("[1]"));
    assertRejected("neutralColor", JsonParser.parseString("{\"chroma\":true}"));
    assertRejected("hostileColor", JsonParser.parseString("{\"value\":\"1\"}"));
    assertRejected("playerColor", JsonParser.parseString("{\"value\":1,\"chroma\":1}"));
    assertRejected("color", JsonParser.parseString("{\"value\":1,\"chromaSpeed\":\"1\"}"));
  }

  @Test
  void rejectsSettingsOutOfRange() {
    assertRejected("rotation", new JsonPrimitive(-1));
    assertRejected("rotation", new JsonPrimitive(361));
    assertRejected("rotation", new JsonPrimitive(45.5));
    assertRejected("outlineThickness", new JsonPrimitive(5));
    assertRejected("outlineThickness", new JsonPrimitive(-1));
    assertRejected("color", new JsonPrimitive(4294967295L));
    assertRejected("color", new JsonPrimitive(0.5));
    assertRejected("color", JsonParser.parseString("{\"value\":1,\"chromaSpeed\":0.1}"));
    assertRejected("color", JsonParser.parseString("{\"value\":1,\"chromaSpeed\":6}"));
    // The outline color has no rgb effect
    assertRejected("outlineColor", JsonParser.parseString("{\"value\":1,\"chroma\":true}"));
  }

  @Test
  void acceptsTheEdgesOfEveryRange() {
    JsonObject values = new JsonObject();
    values.addProperty("rotation", 360);
    values.addProperty("outlineThickness", 4);
    values.add("color", JsonParser.parseString(
        "{\"value\":" + Integer.MIN_VALUE + ",\"chroma\":true,\"chromaSpeed\":5}"
    ));
    values.add("outlineColor", JsonParser.parseString("{\"value\":1,\"chroma\":false}"));
    values.add("playerColor", JsonParser.parseString("{\"value\":1,\"chromaSpeed\":0.25}"));

    CrosshairShare read = CrosshairShare.read(data(encoded(), values));

    assertNotNull(read);
    assertEquals(360, read.settings().rotation());
    assertEquals(4, read.settings().outlineThickness());
    assertEquals(new ShareColor(Integer.MIN_VALUE, true, 5F), read.settings().color());
    assertEquals(ShareColor.of(1), read.settings().outlineColor());
    assertEquals(new ShareColor(1, false, 0.25F), read.settings().playerColor());
  }

  @Test
  void clampsAHandEditedConfigIntoTheSliderRanges() {
    Settings settings = new Settings(false, 720, true, new ShareColor(1, true, 9F), -3,
        ShareColor.of(1), false, ShareColor.of(1), ShareColor.of(1), ShareColor.of(1)).clamped();

    assertEquals(360, settings.rotation());
    assertEquals(0, settings.outlineThickness());
    assertEquals(ShareColor.MAX_CHROMA_SPEED, settings.color().chromaSpeed());
  }

  @Test
  void thePreviewShowsTheCanvasInTheCrosshairColor() {
    CrosshairCanvas canvas = canvas();
    Settings colored = new Settings(false, 0, false, ShareColor.of(0x403366FF), 0,
        ShareColor.of(0), false, ShareColor.of(0), ShareColor.of(0), ShareColor.of(0));

    int[] pixels = CrosshairShare.of(canvas, colored, true).previewPixels();

    assertEquals(CrosshairCanvas.SIZE * CrosshairCanvas.SIZE, pixels.length);
    boolean[] drawn = canvas.getPixels();
    for (int i = 0; i < pixels.length; i++) {
      assertEquals(drawn[i] ? 0xFF3366FF : 0, pixels[i], "pixel " + i);
    }
  }

  @Test
  void thePreviewIsWhiteWithVanillaBlending() {
    int[] pixels = CrosshairShare.of(canvas(), Settings.DEFAULTS, true).previewPixels();
    int center = CrosshairCanvas.SIZE * (CrosshairCanvas.SIZE / 2) + CrosshairCanvas.SIZE / 2;

    assertEquals(0xFFFFFFFF, pixels[center]);
    assertEquals(0, pixels[0]);
  }

  @Test
  void keepsItsOwnCopyOfTheCanvas() {
    CrosshairCanvas canvas = canvas();
    CrosshairShare share = CrosshairShare.of(canvas, Settings.DEFAULTS, true);

    canvas.disablePixel(7, 7);
    share.canvas().disablePixel(7, 6);

    assertArrayEquals(canvas().getPixels(), share.canvas().getPixels());
  }

  private static void assertRejected(String key, JsonElement value) {
    JsonObject values = new JsonObject();
    values.add(key, value);
    assertNull(CrosshairShare.read(data(encoded(), values)), key + " = " + value);
  }

  private static CrosshairCanvas canvas() {
    return CrosshairCanvasPreset.DEFAULT.getCanvas().copy();
  }

  private static String encoded() {
    return CrosshairCanvas.encode(canvas());
  }

  /**
   * An encoded canvas of the given size byte and number of pixel bytes, every pixel drawn.
   */
  private static String raw(int size, int pixelBytes) {
    byte[] bytes = new byte[pixelBytes + 1];
    bytes[0] = (byte) size;
    for (int i = 1; i < bytes.length; i++) {
      bytes[i] = (byte) 0xFF;
    }

    return CrosshairCanvas.ENCODED_PREFIX + Base64.getEncoder().encodeToString(bytes);
  }

  private static JsonObject data(String canvas, JsonElement values) {
    JsonObject data = new JsonObject();
    data.addProperty(CrosshairShare.CANVAS_KEY, canvas);
    data.add(CrosshairShare.VALUES_KEY, values);
    return data;
  }
}
