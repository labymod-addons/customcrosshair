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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Base64;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A crosshair as it is shared in the Laby Chat: the canvas and the settings that change how it
 * looks. Whether the addon is switched on is not part of the payload, a received crosshair always
 * is. Holds no client state, so it can be read and checked on any thread.
 */
public final class CrosshairShare {

  public static final String CANVAS_KEY = "canvas";
  public static final String VALUES_KEY = "values";

  /**
   * An encoded canvas at the full size is 45 characters long, anything far above is not one.
   */
  static final int MAX_ENCODED_CANVAS_LENGTH = 64;

  private static final int OPAQUE = 0xFF000000;
  private static final int VANILLA_BLENDING_COLOR = 0xFFFFFFFF;

  private final CrosshairCanvas canvas;
  private final Settings settings;
  private final boolean enabled;

  private CrosshairShare(CrosshairCanvas canvas, Settings settings, boolean enabled) {
    this.canvas = canvas.copy();
    this.settings = settings;
    this.enabled = enabled;
  }

  public static @NotNull CrosshairShare of(
      @NotNull CrosshairCanvas canvas,
      @NotNull Settings settings,
      boolean enabled
  ) {
    return new CrosshairShare(canvas, settings, enabled);
  }

  /**
   * Reads a received crosshair. The canvas must be one this addon encoded, every known setting
   * must have the type and range its setting allows, a missing one takes its default and unknown
   * ones are ignored.
   *
   * @return the crosshair, switched on, or {@code null} if anything in it is invalid
   */
  public static @Nullable CrosshairShare read(@NotNull JsonObject data) {
    String encoded = ShareJson.string(data.get(CANVAS_KEY));
    CrosshairCanvas canvas = encoded == null ? null : decodeCanvas(encoded);
    if (canvas == null) {
      return null;
    }

    if (!(data.get(VALUES_KEY) instanceof JsonObject values)) {
      return null;
    }

    Settings settings = Settings.read(values);
    return settings == null ? null : new CrosshairShare(canvas, settings, true);
  }

  /**
   * Decodes a canvas only if it has exactly the shape {@link CrosshairCanvas#encode} writes: the
   * prefix, valid base64, an odd size of at most {@link CrosshairCanvas#SIZE} and one bit per pixel
   * of that size. The decoder itself checks none of this.
   *
   * @return the canvas, or {@code null} if the text is not such a canvas
   */
  static @Nullable CrosshairCanvas decodeCanvas(@NotNull String encoded) {
    if (encoded.length() > MAX_ENCODED_CANVAS_LENGTH
        || !encoded.startsWith(CrosshairCanvas.ENCODED_PREFIX)) {
      return null;
    }

    byte[] bytes;
    try {
      bytes = Base64.getDecoder().decode(
          encoded.substring(CrosshairCanvas.ENCODED_PREFIX.length())
      );
    } catch (IllegalArgumentException e) {
      return null;
    }

    if (bytes.length == 0) {
      return null;
    }

    int size = bytes[0];
    if (size < 1 || size > CrosshairCanvas.SIZE || size % 2 == 0) {
      return null;
    }

    if (bytes.length != (size * size + 7) / 8 + 1) {
      return null;
    }

    return CrosshairCanvas.decode(encoded);
  }

  public @NotNull JsonObject write() {
    JsonObject data = new JsonObject();
    data.addProperty(CANVAS_KEY, CrosshairCanvas.encode(this.canvas));
    data.add(VALUES_KEY, this.settings.write());
    return data;
  }

  /**
   * This crosshair with another canvas, for example the one drawn in the editor.
   */
  public @NotNull CrosshairShare withCanvas(@NotNull CrosshairCanvas canvas) {
    return new CrosshairShare(canvas, this.settings, this.enabled);
  }

  /**
   * The canvas as ARGB pixels, row by row: drawn pixels in the crosshair color, white with vanilla
   * blending, without the rgb effect and opaque so the card always shows it, the rest
   * transparent.
   */
  public int @NotNull [] previewPixels() {
    int color = this.settings.vanillaBlending()
        ? VANILLA_BLENDING_COLOR
        : this.settings.color().argb() | OPAQUE;
    boolean[] pixels = this.canvas.getPixels();
    int[] argb = new int[pixels.length];
    for (int i = 0; i < pixels.length; i++) {
      argb[i] = pixels[i] ? color : 0;
    }

    return argb;
  }

  /**
   * A copy of the canvas.
   */
  public @NotNull CrosshairCanvas canvas() {
    return this.canvas.copy();
  }

  public @NotNull Settings settings() {
    return this.settings;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * The settings of the addon that change how the crosshair looks, named like the fields of the
   * config.
   */
  public record Settings(
      boolean displayInThirdPerson,
      int rotation,
      boolean vanillaBlending,
      @NotNull ShareColor color,
      int outlineThickness,
      @NotNull ShareColor outlineColor,
      boolean dynamicColor,
      @NotNull ShareColor neutralColor,
      @NotNull ShareColor hostileColor,
      @NotNull ShareColor playerColor
  ) {

    // The ranges of the sliders in the config
    public static final int MIN_ROTATION = 0;
    public static final int MAX_ROTATION = 360;
    public static final int MIN_OUTLINE_THICKNESS = 0;
    public static final int MAX_OUTLINE_THICKNESS = 4;

    /**
     * The defaults of the config.
     */
    public static final Settings DEFAULTS = new Settings(
        false,
        0,
        true,
        ShareColor.of(0xFFFFFFFF),
        0,
        ShareColor.of(0xFF000000),
        false,
        ShareColor.of(0xFF00FF00),
        ShareColor.of(0xFFFF0000),
        ShareColor.of(0xFFFFFF00)
    );

    private static final String DISPLAY_IN_THIRD_PERSON = "displayInThirdPerson";
    private static final String ROTATION = "rotation";
    private static final String VANILLA_BLENDING = "vanillaBlending";
    private static final String COLOR = "color";
    private static final String OUTLINE_THICKNESS = "outlineThickness";
    private static final String OUTLINE_COLOR = "outlineColor";
    private static final String DYNAMIC_COLOR = "dynamicColor";
    private static final String NEUTRAL_COLOR = "neutralColor";
    private static final String HOSTILE_COLOR = "hostileColor";
    private static final String PLAYER_COLOR = "playerColor";

    /**
     * @return the settings, or {@code null} if one of them has the wrong type or is out of range
     */
    static @Nullable Settings read(@NotNull JsonObject values) {
      Boolean displayInThirdPerson = bool(values, DISPLAY_IN_THIRD_PERSON,
          DEFAULTS.displayInThirdPerson);
      Integer rotation = integer(values, ROTATION, DEFAULTS.rotation, MIN_ROTATION, MAX_ROTATION);
      Boolean vanillaBlending = bool(values, VANILLA_BLENDING, DEFAULTS.vanillaBlending);
      ShareColor color = color(values, COLOR, DEFAULTS.color, true);
      Integer outlineThickness = integer(values, OUTLINE_THICKNESS, DEFAULTS.outlineThickness,
          MIN_OUTLINE_THICKNESS, MAX_OUTLINE_THICKNESS);
      ShareColor outlineColor = color(values, OUTLINE_COLOR, DEFAULTS.outlineColor, false);
      Boolean dynamicColor = bool(values, DYNAMIC_COLOR, DEFAULTS.dynamicColor);
      ShareColor neutralColor = color(values, NEUTRAL_COLOR, DEFAULTS.neutralColor, true);
      ShareColor hostileColor = color(values, HOSTILE_COLOR, DEFAULTS.hostileColor, true);
      ShareColor playerColor = color(values, PLAYER_COLOR, DEFAULTS.playerColor, true);
      if (displayInThirdPerson == null || rotation == null || vanillaBlending == null
          || color == null || outlineThickness == null || outlineColor == null
          || dynamicColor == null || neutralColor == null || hostileColor == null
          || playerColor == null) {
        return null;
      }

      return new Settings(displayInThirdPerson, rotation, vanillaBlending, color,
          outlineThickness, outlineColor, dynamicColor, neutralColor, hostileColor, playerColor);
    }

    /**
     * These settings kept inside the ranges of the sliders, so a hand edited config still shares
     * as a valid one.
     */
    public @NotNull Settings clamped() {
      return new Settings(
          this.displayInThirdPerson,
          clamp(this.rotation, MIN_ROTATION, MAX_ROTATION),
          this.vanillaBlending,
          this.color.clamped(),
          clamp(this.outlineThickness, MIN_OUTLINE_THICKNESS, MAX_OUTLINE_THICKNESS),
          this.outlineColor.clamped(),
          this.dynamicColor,
          this.neutralColor.clamped(),
          this.hostileColor.clamped(),
          this.playerColor.clamped()
      );
    }

    @NotNull JsonObject write() {
      JsonObject values = new JsonObject();
      values.addProperty(DISPLAY_IN_THIRD_PERSON, this.displayInThirdPerson);
      values.addProperty(ROTATION, this.rotation);
      values.addProperty(VANILLA_BLENDING, this.vanillaBlending);
      values.add(COLOR, this.color.write());
      values.addProperty(OUTLINE_THICKNESS, this.outlineThickness);
      values.add(OUTLINE_COLOR, this.outlineColor.write());
      values.addProperty(DYNAMIC_COLOR, this.dynamicColor);
      values.add(NEUTRAL_COLOR, this.neutralColor.write());
      values.add(HOSTILE_COLOR, this.hostileColor.write());
      values.add(PLAYER_COLOR, this.playerColor.write());
      return values;
    }

    private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
    }

    private static @Nullable Boolean bool(JsonObject values, String key, boolean fallback) {
      JsonElement element = values.get(key);
      return element == null ? Boolean.valueOf(fallback) : ShareJson.bool(element);
    }

    private static @Nullable Integer integer(JsonObject values, String key, int fallback,
        int min, int max) {
      JsonElement element = values.get(key);
      return element == null ? Integer.valueOf(fallback) : ShareJson.integer(element, min, max);
    }

    private static @Nullable ShareColor color(JsonObject values, String key,
        ShareColor fallback, boolean allowChroma) {
      JsonElement element = values.get(key);
      return element == null ? fallback : ShareColor.read(element, allowChroma);
    }
  }
}
