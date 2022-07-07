package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.configuration.AppearanceConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.RenderPipeline;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.bounds.DefaultRectangle;
import net.labymod.api.util.bounds.Rectangle;

@Singleton
public class CrosshairArrowFormRenderer implements
    CrosshairFormRenderer {

  private final CustomCrosshair addon;
  private final RenderPipeline renderPipeline;

  @Inject
  public CrosshairArrowFormRenderer(CustomCrosshair addon, RenderPipeline renderPipeline) {
    this.addon = addon;
    this.renderPipeline = renderPipeline;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    y += gap;

    AppearanceConfiguration appearance = this.addon.configuration().appearance();
    float thickness = appearance.thickness();

    this.renderTriangle(stack, x, y, thickness + appearance.outlineThickness(), outlineColor);
    this.renderTriangle(stack, x, y, thickness, color);
  }

  private void renderTriangle(Stack stack, float x, float y, float thickness, int color) {
    float halfThickness = thickness / 2f;
    x -= halfThickness;
    y -= halfThickness;

    int height = this.addon.configuration().appearance().height();
    Rectangle centerRectangle = DefaultRectangle.relative(x, y, thickness, thickness);
    Rectangle rectangle = DefaultRectangle.relative(x, y + thickness, thickness, height);

    stack.translate(centerRectangle.getCenterX(), centerRectangle.getCenterY(), 0);
    stack.rotate(45, 0, 0, 1);
    stack.translate(centerRectangle.getCenterX() * -1, centerRectangle.getCenterY() * -1, 0);

    RectangleRenderer renderer = this.renderPipeline.rectangleRenderer();
    renderer.renderRectangle(stack, centerRectangle, color);
    renderer.renderRectangle(stack, rectangle, color);

    stack.translate(centerRectangle.getCenterX(), centerRectangle.getCenterY(), 0);
    stack.rotate(-90, 0, 0, 1);
    stack.translate(centerRectangle.getCenterX() * -1, centerRectangle.getCenterY() * -1, 0);
    renderer.renderRectangle(stack, rectangle, color);

    stack.translate(centerRectangle.getCenterX(), centerRectangle.getCenterY(), 0);
    stack.rotate(45, 0, 0, 1);
    stack.translate(centerRectangle.getCenterX() * -1, centerRectangle.getCenterY() * -1, 0);
  }
}
