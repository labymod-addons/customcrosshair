package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.bounds.DefaultRectangle;

@Singleton
public class CrosshairDefaultFormRenderer implements CrosshairFormRenderer {

  private final RectangleRenderer rectangleRenderer;

  @Inject
  public CrosshairDefaultFormRenderer(
      RectangleRenderer rectangleRenderer) {
    this.rectangleRenderer = rectangleRenderer;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    //top
    this.rectangleRenderer.renderRectangle(stack,
        DefaultRectangle.relative(x - 0.5f, y - 4.5f, 1, 9), color);

    //left
    this.rectangleRenderer.renderRectangle(stack,
        DefaultRectangle.relative(x - 4.5f, y - 0.5f, 9, 1), color);
  }
}
