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

package net.labymod.addons.customcrosshair.widgets.edit;

import java.awt.Color;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import net.labymod.api.client.gui.lss.property.LssProperty;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.state.ScreenCanvas;
import net.labymod.api.client.gui.screen.widget.AbstractWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.draw.batch.BatchRectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.ColorUtil;
import net.labymod.api.util.color.format.ColorFormat;

@AutoWidget
public class EditCrosshairCanvasWidget extends AbstractWidget<Widget> {

  private final RectangleRenderer rectangleRenderer;
  private final CrosshairCanvas canvas;

  private final LssProperty<Integer> canvasBorderColor = new LssProperty<>(Color.GRAY.getRGB());
  private final LssProperty<Integer> canvasActiveColor = new LssProperty<>(Color.WHITE.getRGB());

  private float pixelWidth;
  private float pixelHeight;

  private float lastPixelX = -1;
  private float lastPixelY = -1;
  private CrosshairCanvas draggingCanvas;

  public EditCrosshairCanvasWidget(final CrosshairCanvas canvas) {
    super();
    this.canvas = canvas;
    this.rectangleRenderer = this.labyAPI.renderPipeline().rectangleRenderer();
  }

  @Override
  public void updateBounds() {
    super.updateBounds();

    final Bounds bounds = this.bounds();
    final int size = CrosshairCanvas.SIZE;
    this.pixelWidth = (bounds.getWidth() - size - 1) / size;
    this.pixelHeight = (bounds.getHeight() - size - 1) / size;
  }

  @Override
  public void renderWidget(ScreenContext context) {
    super.renderWidget(context);

    final Bounds bounds = this.bounds();
    final float width = bounds.getWidth();
    final float height = bounds.getHeight();

    int x = (int) bounds.getX();
    int y = (int) bounds.getY();

    final int borderColor = this.canvasBorderColor.get();

    ScreenCanvas canvas = context.canvas();

    // top line
    canvas.submitRelativeRect(x, y, width, 1, borderColor);

    // left line
    canvas.submitRelativeRect(x++, y, 1, height - 1, borderColor);

    final int size = CrosshairCanvas.SIZE;
    final int centerIndex = this.canvas.getCenterX() + size * this.canvas.getCenterY();
    final int activeColor = this.canvasActiveColor.get();
    final boolean[] pixels = this.canvas.getPixels();
    for (int canvasY = 0; canvasY < size; canvasY++) {
      for (int canvasX = 0; canvasX < size; canvasX++) {
        final float pixelX = x + canvasX * this.pixelWidth;
        final float pixelY = y + canvasY * this.pixelHeight;
        final int index = canvasX + size * canvasY;
        if (index == centerIndex) {
          canvas.submitRelativeRect(
              pixelX, pixelY,
              this.pixelWidth, this.pixelHeight,
              ColorFormat.ARGB32.pack(0.098F, 0.098F, 0.098F, 0.1F)
          );
        }

        if (pixels[index]) {
          canvas.submitRelativeRect(
              pixelX, pixelY,
              this.pixelWidth, this.pixelHeight,
              activeColor
          );
        }

        canvas.submitRelativeRect(
            pixelX + this.pixelWidth, pixelY,
            1, this.pixelHeight + 1,
            borderColor
        );

        canvas.submitRelativeRect(
            pixelX, pixelY + this.pixelHeight,
            this.pixelWidth, 1,
            borderColor
        );

        if (canvasX == size - 1) {
          x = (int) bounds.getX() + 1;
          y++;
        } else {
          x++;
        }
      }
    }
  }

  @Override
  public boolean mouseClicked(final MutableMouse mouse, final MouseButton mouseButton) {
    final Boolean active;
    if (mouseButton == MouseButton.LEFT) {
      active = true;
    } else if (mouseButton == MouseButton.RIGHT) {
      active = false;
    } else if (mouseButton == MouseButton.MIDDLE) {
      active = null;
    } else {
      return false;
    }

    this.draggingCanvas = this.canvas.copy();
    return this.setPixel(mouse.getX(), mouse.getY(), active);
  }

  @Override
  public boolean mouseDragged(
      final MutableMouse mouse,
      final MouseButton mouseButton,
      final double deltaX,
      final double deltaY
  ) {
    final Boolean active;
    if (mouseButton == MouseButton.LEFT) {
      active = true;
    } else if (mouseButton == MouseButton.RIGHT) {
      active = false;
    } else if (mouseButton == MouseButton.MIDDLE) {
      active = null;
    } else {
      return false;
    }

    return this.setPixel(mouse.getX(), mouse.getY(), active);
  }

  @Override
  public boolean mouseReleased(final MutableMouse mouse, final MouseButton mouseButton) {
    this.lastPixelX = -1;
    this.lastPixelY = -1;
    this.draggingCanvas = null;

    return super.mouseReleased(mouse, mouseButton);
  }

  public LssProperty<Integer> canvasBorderColor() {
    return this.canvasBorderColor;
  }

  public LssProperty<Integer> canvasActiveColor() {
    return this.canvasActiveColor;
  }

  public void update(final CrosshairCanvas canvas) {
    canvas.copyPixelsTo(this.canvas);
  }

  public CrosshairCanvas canvas() {
    return this.canvas;
  }

  private boolean setPixel(final int mouseX, final int mouseY, final Boolean active) {
    final Bounds bounds = this.bounds();
    final int x = (int) bounds.getX();
    final int y = (int) bounds.getY();

    final int size = CrosshairCanvas.SIZE;
    final int absoluteMouseX = mouseX - x;
    final int absoluteMouseY = mouseY - y;

    final int canvasX = (int) ((absoluteMouseX - (int) ((absoluteMouseX) / this.pixelWidth) - 1)
        / this.pixelWidth);
    final int canvasY = (int) ((absoluteMouseY - (int) ((absoluteMouseY) / this.pixelHeight) - 1)
        / this.pixelHeight);
    if (canvasX < 0 || canvasX >= size || canvasY < 0 || canvasY >= size) {
      return false;
    }

    if (this.lastPixelX == canvasX && this.lastPixelY == canvasY) {
      return false;
    }

    if (active == null) {
      if (this.draggingCanvas == null) {
        this.canvas.togglePixel(canvasX, canvasY);
      } else {
        final boolean pixelActive = this.draggingCanvas.isPixelActive(canvasX, canvasY);
        if (pixelActive) {
          this.canvas.disablePixel(canvasX, canvasY);
        } else {
          this.canvas.enablePixel(canvasX, canvasY);
        }
      }
    } else if (active && (this.draggingCanvas == null || !this.draggingCanvas.isPixelActive(canvasX,
        canvasY))) {
      this.canvas.enablePixel(canvasX, canvasY);
    } else if (!active && (this.draggingCanvas == null || this.draggingCanvas.isPixelActive(canvasX,
        canvasY))) {
      this.canvas.disablePixel(canvasX, canvasY);
    }

    this.lastPixelX = canvasX;
    this.lastPixelY = canvasY;
    return true;
  }

  public CrosshairCanvas saveDraft() {
    return this.canvas;
  }
}
