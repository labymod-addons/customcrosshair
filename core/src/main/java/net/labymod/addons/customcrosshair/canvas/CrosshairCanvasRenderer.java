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

import net.labymod.api.Laby;
import net.labymod.api.client.gfx.color.blend.GFXBlendParameter;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.draw.batch.BatchRectangleRenderer;
import net.labymod.api.client.render.gl.GLConstants;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.vertex.phase.RenderPhase;
import net.labymod.api.client.render.vertex.shard.RenderShards;
import net.labymod.api.util.Color;

public class CrosshairCanvasRenderer {

  private static final RenderPhase NO_TEXTURED_RECTANGLE_PHASE = RenderPhase.builder()
      .name("rectangle_phase")
      .vertexFormat(Laby.references().oldVertexFormatRegistry().getPositionColor())
      .mode(GLConstants.QUADS)
      .addShard(RenderShards.NO_TEXTURING)
      .build();

  private static final RectangleRenderer RECTANGLE_RENDERER = Laby.labyAPI().renderPipeline()
      .rectangleRenderer();

  private static final int DEFAULT_COLOR = Color.WHITE.get();

  public void renderVanillaBlended(
      final Stack stack,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY
  ) {
    this.renderVanillaBlended(stack, canvas, minX, minY, DEFAULT_COLOR);
  }

  public void renderVanillaBlended(
      final Stack stack,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY,
      final int color
  ) {
    Laby.gfx().blendSeparate(
        GFXBlendParameter.ONE_MINUS_DESTINATION_COLOR,
        GFXBlendParameter.ONE_MINUS_SOURCE_COLOR,
        GFXBlendParameter.ONE,
        GFXBlendParameter.ZERO
    );

    final BatchRectangleRenderer batchRenderer = RECTANGLE_RENDERER.beginBatch(
        stack,
        NO_TEXTURED_RECTANGLE_PHASE
    );

    this.render(batchRenderer, canvas, minX, minY, color);
    batchRenderer.upload();

    Laby.gfx().defaultBlend();
  }

  public void renderColored(
      final Stack stack,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY
  ) {
    this.renderColored(stack, canvas, minX, minY, DEFAULT_COLOR);
  }

  public void renderColored(
      final Stack stack,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY,
      final int color
  ) {
    final BatchRectangleRenderer batchRenderer = RECTANGLE_RENDERER.beginBatch(stack);
    this.render(batchRenderer, canvas, minX, minY, color);
    batchRenderer.upload();
  }

  private void render(
      final BatchRectangleRenderer batchRenderer,
      final CrosshairCanvas canvas,
      final float minX,
      final float minY,
      final int color
  ) {
    final boolean[] pixels = canvas.getPixels();
    for (int x = 0; x < CrosshairCanvas.SIZE; x++) {
      for (int y = 0; y < CrosshairCanvas.SIZE; y++) {
        if (pixels[x + CrosshairCanvas.SIZE * y]) {
          batchRenderer.pos(minX + x, minY + y).size(1, 1).color(color).build();
        }
      }
    }
  }
}
