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
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Strict readers for single JSON values of a received payload.
 */
final class ShareJson {

  private ShareJson() {
  }

  /**
   * @return the whole number, or {@code null} if the element is no number, has a fraction or lies
   * outside the range
   */
  static @Nullable Integer integer(@NotNull JsonElement element, long min, long max) {
    if (!(element instanceof JsonPrimitive primitive) || !primitive.isNumber()) {
      return null;
    }

    double value = primitive.getAsDouble();
    if (value != Math.rint(value) || value < min || value > max) {
      return null;
    }

    return (int) value;
  }

  /**
   * @return the number, or {@code null} if the element is no finite number inside the range
   */
  static @Nullable Double number(@NotNull JsonElement element, double min, double max) {
    if (!(element instanceof JsonPrimitive primitive) || !primitive.isNumber()) {
      return null;
    }

    double value = primitive.getAsDouble();
    return value >= min && value <= max ? value : null;
  }

  static @Nullable Boolean bool(@NotNull JsonElement element) {
    return element instanceof JsonPrimitive primitive && primitive.isBoolean()
        ? primitive.getAsBoolean()
        : null;
  }

  static @Nullable String string(@Nullable JsonElement element) {
    return element instanceof JsonPrimitive primitive && primitive.isString()
        ? primitive.getAsString()
        : null;
  }
}
