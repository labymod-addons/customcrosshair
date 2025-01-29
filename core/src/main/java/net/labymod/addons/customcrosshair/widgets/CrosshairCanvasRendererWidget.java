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

package net.labymod.addons.customcrosshair.widgets;

import java.util.Objects;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasRenderer;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.widget.AbstractWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.matrix.Stack;
import org.jetbrains.annotations.NotNull;

@AutoWidget
public class CrosshairCanvasRendererWidget extends AbstractWidget<Widget> {

  private static final CrosshairCanvasRenderer CANVAS_RENDERER = new CrosshairCanvasRenderer();
  private CrosshairCanvas canvas;

  public CrosshairCanvasRendererWidget(@NotNull final CrosshairCanvas canvas) {
    this.update(canvas);
  }

  @Override
  public void renderWidget(ScreenContext context) {
    super.renderWidget(context);
    final Bounds bounds = this.bounds();
    final float x = bounds.getX();
    final float y = bounds.getY();
    CANVAS_RENDERER.renderColored(context.stack(), this.canvas, x, y);
  }

  public @NotNull CrosshairCanvasRendererWidget update(@NotNull final CrosshairCanvas canvas) {
    Objects.requireNonNull(canvas, "Canvas cannot be null");
    this.canvas = canvas;
    return this;
  }
}
