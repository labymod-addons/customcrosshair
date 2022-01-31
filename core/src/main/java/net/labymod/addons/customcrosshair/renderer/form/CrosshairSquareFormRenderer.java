package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.bounds.DefaultRectangle;

@Singleton
public class CrosshairSquareFormRenderer implements CrosshairFormRenderer {

  private final RectangleRenderer rectangleRenderer;
  private final CustomCrosshairConfiguration config;

  @Inject
  public CrosshairSquareFormRenderer(DefaultCustomCrosshairConfiguration config,
      RectangleRenderer rectangleRenderer) {
    this.config = config;
    this.rectangleRenderer = rectangleRenderer;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {

    float thickness = this.config.getAppearanceConfiguration().getThickness();

    this.renderSquareWithThickness(stack, x, y, gap,
        thickness + this.config.getAppearanceConfiguration().getOutlineThickness(), outlineColor);

    this.renderSquareWithThickness(stack, x, y, gap, thickness, color);
  }

  private void renderSquareWithThickness(Stack stack, float x, float y, float gap, float thickness,
      int color) {

    float halfThickness = thickness / 2;
    float doubleGap = gap * 2;

    //top
    this.rectangleRenderer.renderRectangle(stack,
        DefaultRectangle.relative(x - halfThickness - gap, y - halfThickness - gap, doubleGap,
            thickness), color);

    //bottom
    this.rectangleRenderer.renderRectangle(stack,
        DefaultRectangle.relative(x - halfThickness - gap, y + gap - halfThickness,
            doubleGap + thickness, thickness), color);

    //left
    this.rectangleRenderer.renderRectangle(stack,
        DefaultRectangle.relative(x - halfThickness - gap, y - halfThickness - gap, thickness,
            doubleGap + thickness), color);

    //right
    this.rectangleRenderer.renderRectangle(stack,
        DefaultRectangle.relative(x + gap - halfThickness, y - halfThickness - gap, thickness,
            doubleGap + thickness), color);
  }
}
