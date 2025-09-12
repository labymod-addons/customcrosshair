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

package net.labymod.addons.customcrosshair.canvas;

import net.labymod.addons.customcrosshair.CustomCrosshairPrograms;
import net.labymod.api.Textures;
import net.labymod.api.client.gui.screen.state.ScreenCanvas;
import net.labymod.api.client.gui.screen.state.states.GuiTextureSet;
import net.labymod.api.util.Color;

public class CrosshairCanvasRenderer {

  private static final int DEFAULT_COLOR = Color.WHITE.get();

  public void renderVanillaBlended(
      final ScreenCanvas screenCanvas,
      final CrosshairCanvas crosshairCanvas,
      final float minX,
      final float minY
  ) {
    this.renderVanillaBlended(screenCanvas, crosshairCanvas, minX, minY, DEFAULT_COLOR);
  }

  public void renderVanillaBlended(
      final ScreenCanvas screenCanvas,
      final CrosshairCanvas crosshairCanvas,
      final float minX,
      final float minY,
      final int color
  ) {
    this.render(screenCanvas, crosshairCanvas, minX, minY, color);
  }

  private void render(
      final ScreenCanvas screenCanvas,
      final CrosshairCanvas crosshairCanvas,
      final float minX,
      final float minY,
      final int color
  ) {
    final GuiTextureSet textureSet = GuiTextureSet.single(Textures.WHITE);
    final boolean[] pixels = crosshairCanvas.getPixels();
    for (int x = 0; x < CrosshairCanvas.SIZE; x++) {
      for (int y = 0; y < CrosshairCanvas.SIZE; y++) {
        if (pixels[x + CrosshairCanvas.SIZE * y]) {
          screenCanvas.submitGuiBlit(
              CustomCrosshairPrograms.VANILLA_BLENDED,
              textureSet,
              minX + x, minY + y, 1, 1,
              0.0F, 0.0F, 1.0F, 1.0F,
              color
          );
        }
      }
    }
  }

  public void renderColored(
      final ScreenCanvas screenCanvas,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY
  ) {
    this.renderColored(screenCanvas, canvas, minX, minY, DEFAULT_COLOR);
  }

  public void renderColored(
      final ScreenCanvas screenCanvas,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY,
      final int color
  ) {
    final boolean[] pixels = canvas.getPixels();
    for (int x = 0; x < CrosshairCanvas.SIZE; x++) {
      for (int y = 0; y < CrosshairCanvas.SIZE; y++) {
        if (pixels[x + CrosshairCanvas.SIZE * y]) {
          screenCanvas.submitRelativeRect(minX + x, minY + y, 1, 1, color);
        }
      }
    }
  }
}
