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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasPreset;
import net.labymod.addons.customcrosshair.share.CrosshairShare.Settings;
import net.labymod.api.labyconnect.share.ShareCodec;
import org.junit.jupiter.api.Test;

class CrosshairShareCodecTest {

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

  private final ShareCodec<CrosshairShare> codec = new CrosshairShareCodec();

  @Test
  void startsAtVersionOne() {
    assertEquals(1, this.codec.version());
  }

  @Test
  void writesThePayloadOfTheFirstRelease() {
    // Messages are kept forever: this payload has to stay readable as it is
    CrosshairShare share = CrosshairShare.of(
        CrosshairCanvasPreset.DEFAULT.getCanvas(),
        Settings.DEFAULTS,
        false
    );

    assertEquals(JsonParser.parseString("{\"canvas\":\"LMCH-CRAgQIDwHwIECBAA\",\"values\":{"
        + "\"displayInThirdPerson\":false,\"rotation\":0,\"vanillaBlending\":true,"
        + "\"color\":-1,\"outlineThickness\":0,\"outlineColor\":-16777216,"
        + "\"dynamicColor\":false,\"neutralColor\":-16711936,\"hostileColor\":-65536,"
        + "\"playerColor\":-256}}"), this.codec.write(share));
  }

  @Test
  void readsWhatItWrites() {
    CrosshairShare share = CrosshairShare.of(
        CrosshairCanvasPreset.CROSS.getCanvas(),
        CUSTOM,
        false
    );

    CrosshairShare read = this.codec.read(this.codec.write(share), this.codec.version());

    assertNotNull(read);
    assertEquals(share.canvas(), read.canvas());
    assertEquals(CUSTOM, read.settings());
  }

  @Test
  void readsStrictly() {
    JsonObject data = this.codec.write(CrosshairShare.of(
        CrosshairCanvasPreset.DEFAULT.getCanvas(),
        Settings.DEFAULTS,
        true
    ));
    data.getAsJsonObject(CrosshairShare.VALUES_KEY).addProperty("rotation", 361);

    assertNull(this.codec.read(data, this.codec.version()));
  }

  @Test
  void readsNoVersionItNeverWrote() {
    JsonObject data = this.codec.write(CrosshairShare.of(
        CrosshairCanvasPreset.DEFAULT.getCanvas(),
        Settings.DEFAULTS,
        true
    ));

    assertNull(this.codec.read(data, 0));
  }
}
