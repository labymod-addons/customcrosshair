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

import java.util.function.Supplier;
import net.labymod.addons.customcrosshair.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Entry point to Laby Chat sharing. Nothing here touches the share API before
 * {@link #register(Supplier)} ran, so the rest of the addon can ask {@link #isAvailable()} on any
 * client.
 */
public final class CrosshairChatShare {

  /**
   * The registry of the share API, present from Laby Client 49 on.
   */
  public static final String SHARE_API_CLASS =
      "net.labymod.api.labyconnect.share.ChatShareRegistry";

  private static @Nullable CrosshairShareable shareable;

  private CrosshairChatShare() {
  }

  /**
   * Registers the crosshair kind. Throws a {@link LinkageError} on a client without the share API.
   */
  public static void register(@NotNull Supplier<CustomCrosshairConfiguration> configuration) {
    if (shareable != null) {
      return;
    }

    // Every call into the share API sits in CrosshairShareable: verifying this class must not
    // need any share API type, it is loaded on clients without it too
    shareable = CrosshairShareable.register(configuration);
  }

  public static boolean isAvailable() {
    return shareable != null;
  }

  /**
   * Lets the player pick a friend and sends them a crosshair with the given canvas. Called on the
   * render thread.
   *
   * @param received the received crosshair the canvas was drawn on, or {@code null} to send the
   *                 configured settings
   */
  public static void share(@NotNull CrosshairCanvas canvas, @Nullable CrosshairShare received) {
    CrosshairShareable kind = shareable;
    if (kind == null) {
      return;
    }

    CrosshairShare base = received == null ? kind.export() : received;
    kind.share(base.withCanvas(canvas));
  }
}
