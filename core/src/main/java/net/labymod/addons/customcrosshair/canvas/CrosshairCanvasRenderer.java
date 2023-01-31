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
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.LivingEntity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.gfx.color.blend.GFXBlendParameter;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.draw.batch.BatchRectangleRenderer;
import net.labymod.api.client.render.gl.GLConstants;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.vertex.phase.RenderPhase;
import net.labymod.api.client.render.vertex.shard.RenderShards;
import net.labymod.api.util.Color;
import org.jetbrains.annotations.NotNull;

public class CrosshairCanvasRenderer {

  @NotNull
  public static final RenderPhase NO_TEXTURED_RECTANGLE_PHASE = RenderPhase.builder()
      .name("rectangle_phase")
      .vertexFormat(Laby.references().oldVertexFormatRegistry().getPositionColor())
      .mode(GLConstants.QUADS)
      .addShard(RenderShards.NO_TEXTURING)
      .build();

  private static final RectangleRenderer RECTANGLE_RENDERER = Laby.labyAPI().renderPipeline()
      .rectangleRenderer();

  private static final int DEFAULT_COLOR = Color.WHITE.get();

  private final Minecraft minecraft;

  public CrosshairCanvasRenderer(Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  public void render(Stack stack, CustomCrosshairConfiguration configuration) {
    Bounds bounds = this.minecraft.minecraftWindow().bounds();
    // Mojang calculates the position the same way
    float x = ((int) bounds.getWidth() - 15) / 2;
    float y = ((int) bounds.getHeight() - 15) / 2;

    stack.push();
    int degrees = configuration.rotation().get().getDegrees();
    if (degrees != 0 && degrees != 360) {
      float translationX = x + CrosshairCanvas.SIZE / 2.0F;
      float translationY = y + CrosshairCanvas.SIZE / 2.0F;
      stack.translate(translationX, translationY, 0);
      stack.rotate(degrees, 0, 0, 1);
      stack.translate(translationX * -1, translationY * -1, 0);
    }

    boolean vanillaBlending = configuration.vanillaBlending().get();
    if (vanillaBlending) {
      Laby.gfx().blendSeparate(
          GFXBlendParameter.ONE_MINUS_DESTINATION_COLOR,
          GFXBlendParameter.ONE_MINUS_SOURCE_COLOR,
          GFXBlendParameter.ONE,
          GFXBlendParameter.ZERO
      );
    }

    this.renderCanvas(
        stack,
        x,
        y,
        vanillaBlending,
        configuration
    );

    stack.pop();
  }


  private void renderCanvas(
      Stack stack,
      float minX,
      float minY,
      boolean vanillaBlending,
      CustomCrosshairConfiguration configuration
  ) {
    CrosshairCanvasPreset preset = configuration.type().get();
    CrosshairCanvas canvas = preset.getCanvas();
    if (canvas == null) {
      return;
    }

    boolean[] pixels = canvas.getPixels();

    BatchRectangleRenderer batch;
    if (vanillaBlending) {
      batch = RECTANGLE_RENDERER.beginBatch(stack, NO_TEXTURED_RECTANGLE_PHASE);
    } else {
      batch = RECTANGLE_RENDERER.beginBatch(stack);
    }

    int color = vanillaBlending ? DEFAULT_COLOR : this.getColor(configuration);
    for (int x = 0; x < CrosshairCanvas.SIZE; x++) {
      for (int y = 0; y < CrosshairCanvas.SIZE; y++) {
        if (pixels[x + CrosshairCanvas.SIZE * y]) {
          batch.pos(minX + x, minY + y).size(1, 1).color(color).build();
        }
      }
    }

    batch.upload();
  }

  private int getColor(CustomCrosshairConfiguration configuration) {
    Entity targetEntity = this.minecraft.getTargetEntity();
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
