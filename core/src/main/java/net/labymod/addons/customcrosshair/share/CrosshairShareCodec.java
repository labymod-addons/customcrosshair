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

import com.google.gson.JsonObject;
import net.labymod.api.labyconnect.share.ShareCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The payload of a shared crosshair: {@code {"canvas": ..., "values": {...}}}, read strictly by
 * {@link CrosshairShare#read(JsonObject)}.
 */
final class CrosshairShareCodec implements ShareCodec<CrosshairShare> {

  static final int VERSION = 1;

  @Override
  public int version() {
    return VERSION;
  }

  @Override
  public @NotNull JsonObject write(@NotNull CrosshairShare value) {
    return value.write();
  }

  @Override
  public @Nullable CrosshairShare read(@NotNull JsonObject data, int version) {
    // Version 1 is the only format so far
    return version == VERSION ? CrosshairShare.read(data) : null;
  }
}
