package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.bounds.DefaultRectangle;
import net.labymod.api.util.bounds.Rectangle;

@Singleton
public class CrosshairArrowFormRenderer implements
    CrosshairFormRenderer {

  private final RectangleRenderer rectangleRenderer;
  private final CustomCrosshairConfiguration config;

  @Inject
  public CrosshairArrowFormRenderer(CustomCrosshairConfiguration config,
      RectangleRenderer rectangleRenderer) {
    this.rectangleRenderer = rectangleRenderer;
    this.config = config;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    y += gap;

    float thickness = this.config.getAppearanceConfiguration().getThickness();

    this.renderTriangle(stack, x, y, thickness + this.config.getAppearanceConfiguration()
        .getOutlineThickness(), outlineColor);
    this.renderTriangle(stack, x, y, thickness, color);
  }

  private void renderTriangle(Stack stack, float x, float y, float thickness, int color) {
    float halfThickness = thickness / 2f;
    x -= halfThickness;
    y -= halfThickness;

    int height = this.config.getAppearanceConfiguration().getHeight();
    Rectangle centerRectangle = DefaultRectangle.relative(x, y, thickness, thickness);
    Rectangle rectangle = DefaultRectangle.relative(x, y + thickness, thickness, height);

    stack.translate(centerRectangle.getCenterX(), centerRectangle.getCenterY(), 0);
    stack.rotate(45, 0, 0, 1);
    stack.translate(centerRectangle.getCenterX() * -1, centerRectangle.getCenterY() * -1, 0);
    this.rectangleRenderer.renderRectangle(stack, centerRectangle, color);
    this.rectangleRenderer.renderRectangle(stack, rectangle, color);

    stack.translate(centerRectangle.getCenterX(), centerRectangle.getCenterY(), 0);
    stack.rotate(-90, 0, 0, 1);
    stack.translate(centerRectangle.getCenterX() * -1, centerRectangle.getCenterY() * -1, 0);
    this.rectangleRenderer.renderRectangle(stack, rectangle, color);

    stack.translate(centerRectangle.getCenterX(), centerRectangle.getCenterY(), 0);
    stack.rotate(45, 0, 0, 1);
    stack.translate(centerRectangle.getCenterX() * -1, centerRectangle.getCenterY() * -1, 0);
  }
}
