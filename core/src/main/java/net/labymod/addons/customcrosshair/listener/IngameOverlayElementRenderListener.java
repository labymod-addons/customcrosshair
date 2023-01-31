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

package net.labymod.addons.customcrosshair.listener;

import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasRenderer;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.options.Perspective;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent.OverlayElementType;

public class IngameOverlayElementRenderListener {

  private final CrosshairCanvasRenderer canvasRenderer;
  private final CustomCrosshair addon;
  private final Minecraft minecraft;

  public IngameOverlayElementRenderListener(
      CustomCrosshair addon,
      Minecraft minecraft
  ) {
    this.addon = addon;
    this.minecraft = minecraft;
    this.canvasRenderer = new CrosshairCanvasRenderer(minecraft);
  }

  @Subscribe(Priority.LATE)
  public void onRender(IngameOverlayElementRenderEvent event) {
    if (event.phase() != Phase.PRE || event.isCancelled()
        || event.elementType() != OverlayElementType.CROSSHAIR) {
      return;
    }

    CustomCrosshairConfiguration configuration = this.addon.configuration();
    Perspective perspective = this.minecraft.options().perspective();
    if (perspective != Perspective.FIRST_PERSON && !configuration.displayInThirdPerson().get()) {
      return;
    }

    event.setCancelled(true);
    this.canvasRenderer.render(event.stack(), configuration);
  }
}
