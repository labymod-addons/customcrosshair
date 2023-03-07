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

import net.labymod.addons.customcrosshair.CustomCrosshairConfiguration;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.LivingEntity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.matrix.Stack;

public class CrosshairCanvasIngameRenderer extends CrosshairCanvasRenderer {

  private final Minecraft minecraft;

  public CrosshairCanvasIngameRenderer(final Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  public void render(final Stack stack, final CustomCrosshairConfiguration configuration) {
    final Bounds bounds = this.minecraft.minecraftWindow().bounds();
    // Mojang calculates the position the same way
    final float x = (int) ((bounds.getWidth() - 15) / 2F);
    final float y = (int) ((bounds.getHeight() - 15) / 2F);

    stack.push();
    final int degrees = configuration.rotation().get();
    if (degrees != 0 && degrees != 360) {
      final float translationX = x + CrosshairCanvas.SIZE / 2.0F;
      final float translationY = y + CrosshairCanvas.SIZE / 2.0F;
      stack.translate(translationX, translationY, 0);
      stack.rotate(degrees, 0, 0, 1);
      stack.translate(translationX * -1, translationY * -1, 0);
    }

    this.renderCanvas(
        stack,
        configuration,
        x,
        y
    );

    stack.pop();
  }


  private void renderCanvas(
      final Stack stack,
      final CustomCrosshairConfiguration configuration,
      final float minX,
      final float minY
  ) {
    final CrosshairCanvas canvas = configuration.canvas().get();
    if (canvas == null) {
      return;
    }

    if (configuration.vanillaBlending().get()) {
      this.renderVanillaBlended(stack, canvas, minX, minY);
      return;
    }

    this.renderColored(stack, canvas, minX, minY, this.getColor(configuration));
  }

  private int getColor(final CustomCrosshairConfiguration configuration) {
    final Entity targetEntity = this.minecraft.getTargetEntity();
    if (!configuration.dynamicColor().get() || !(targetEntity instanceof LivingEntity)) {
      return configuration.color().get().get();
    }

    if (targetEntity instanceof Player) {
      return configuration.playerColor().get().get();
    }

    return ((LivingEntity) targetEntity).isHostile() ? configuration.hostileColor().get().get()
        : configuration.neutralColor().get().get();
  }
}
