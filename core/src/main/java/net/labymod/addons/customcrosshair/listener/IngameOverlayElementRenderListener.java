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
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasIngameRenderer;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.options.Perspective;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent.OverlayElementType;
import net.labymod.api.event.client.render.world.RenderWorldEvent;
import net.labymod.api.loader.MinecraftVersions;

public class IngameOverlayElementRenderListener {

  private static final boolean V1_8_9_WORKAROUND = MinecraftVersions.V1_8_9.orOlder();
  private final CrosshairCanvasIngameRenderer canvasRenderer;
  private final CustomCrosshair addon;
  private final Minecraft minecraft;

  public IngameOverlayElementRenderListener(
      final CustomCrosshair addon,
      final Minecraft minecraft
  ) {
    this.addon = addon;
    this.minecraft = minecraft;
    this.canvasRenderer = new CrosshairCanvasIngameRenderer(minecraft);
  }

  @Subscribe(Priority.LATE)
  public void onRender(final IngameOverlayElementRenderEvent event) {
    if (event.phase() != Phase.PRE || this.minecraft.options().isDebugEnabled()) {
      return;
    }

    ScreenContext context = event.screenContext();
    if (V1_8_9_WORKAROUND) {
      if (event.elementType() == OverlayElementType.CROSSHAIR) {
        event.setCancelled(true);
        this.renderCustomCrosshair(context);
      }
    } else {
      if (event.elementType() == OverlayElementType.CROSSHAIR) {
        event.setCancelled(true);
      } else if (event.elementType() == OverlayElementType.BOSS_BAR) { //Workaround: the crosshair won't render if the player is in third person, but boss bars are always visible (even when no boss bar is present).
        this.renderCustomCrosshair(context);
      }
    }
  }

  private void renderCustomCrosshair(ScreenContext context) {
    final CustomCrosshairConfiguration configuration = this.addon.configuration();
    final Perspective perspective = this.minecraft.options().perspective();
    if (perspective != Perspective.FIRST_PERSON && !configuration.displayInThirdPerson()
        .get()) {
      return;
    }

    this.canvasRenderer.render(context, configuration);
  }
}
