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
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A color setting as it is shared, in the JSON shape of the client's color type adapter: a plain
 * ARGB number, or {@code {value, chroma, chromaSpeed}} for the rgb effect.
 *
 * @param argb        the color without the rgb effect
 * @param chroma      whether the rgb effect is on
 * @param chromaSpeed the speed of the rgb effect
 */
public record ShareColor(int argb, boolean chroma, float chromaSpeed) {

  public static final float DEFAULT_CHROMA_SPEED = 1.0F;
  // The range of the speed slider in the color picker
  public static final float MIN_CHROMA_SPEED = 0.25F;
  public static final float MAX_CHROMA_SPEED = 5.0F;

  private static final String VALUE_KEY = "value";
  private static final String CHROMA_KEY = "chroma";
  private static final String CHROMA_SPEED_KEY = "chromaSpeed";

  public static @NotNull ShareColor of(int argb) {
    return new ShareColor(argb, false, DEFAULT_CHROMA_SPEED);
  }

  /**
   * Reads a received color.
   *
   * @param allowChroma whether the setting offers the rgb effect
   * @return the color, or {@code null} if it is malformed or out of range
   */
  public static @Nullable ShareColor read(@NotNull JsonElement element, boolean allowChroma) {
    if (element instanceof JsonPrimitive) {
      Integer argb = ShareJson.integer(element, Integer.MIN_VALUE, Integer.MAX_VALUE);
      return argb == null ? null : of(argb);
    }

    if (!(element instanceof JsonObject object)) {
      return null;
    }

    JsonElement valueElement = object.get(VALUE_KEY);
    Integer argb = valueElement == null
        ? null
        : ShareJson.integer(valueElement, Integer.MIN_VALUE, Integer.MAX_VALUE);
    if (argb == null) {
      return null;
    }

    boolean chroma = false;
    JsonElement chromaElement = object.get(CHROMA_KEY);
    if (chromaElement != null) {
      Boolean value = ShareJson.bool(chromaElement);
      if (value == null || value && !allowChroma) {
        return null;
      }

      chroma = value;
    }

    float chromaSpeed = DEFAULT_CHROMA_SPEED;
    JsonElement speedElement = object.get(CHROMA_SPEED_KEY);
    if (speedElement != null) {
      Double speed = ShareJson.number(speedElement, MIN_CHROMA_SPEED, MAX_CHROMA_SPEED);
      if (speed == null) {
        return null;
      }

      chromaSpeed = speed.floatValue();
    }

    return new ShareColor(argb, chroma, chromaSpeed);
  }

  public @NotNull JsonElement write() {
    boolean defaultSpeed = this.chromaSpeed == DEFAULT_CHROMA_SPEED;
    if (!this.chroma && defaultSpeed) {
      return new JsonPrimitive(this.argb);
    }

    JsonObject object = new JsonObject();
    object.addProperty(VALUE_KEY, this.argb);
    if (this.chroma) {
      object.addProperty(CHROMA_KEY, true);
    }

    if (!defaultSpeed) {
      object.addProperty(CHROMA_SPEED_KEY, this.chromaSpeed);
    }

    return object;
  }

  /**
   * This color with the speed kept inside the range of the color picker, so a hand edited config
   * still shares as a valid one.
   */
  public @NotNull ShareColor clamped() {
    float speed = Float.isNaN(this.chromaSpeed)
        ? DEFAULT_CHROMA_SPEED
        : Math.max(MIN_CHROMA_SPEED, Math.min(MAX_CHROMA_SPEED, this.chromaSpeed));
    return speed == this.chromaSpeed ? this : new ShareColor(this.argb, this.chroma, speed);
  }
}
