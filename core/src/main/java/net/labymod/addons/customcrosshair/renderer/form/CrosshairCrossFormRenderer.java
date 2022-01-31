package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.bounds.DefaultRectangle;
import net.labymod.api.util.bounds.Rectangle;

@Singleton
public class CrosshairCrossFormRenderer implements CrosshairFormRenderer {

  private final RectangleRenderer rectangleRenderer;
  private final CustomCrosshairConfiguration config;

  @Inject
  public CrosshairCrossFormRenderer(
      DefaultCustomCrosshairConfiguration config,
      RectangleRenderer rectangleRenderer) {
    this.config = config;
    this.rectangleRenderer = rectangleRenderer;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {

    float width = this.config.getAppearanceConfiguration().getWidth();
    float height = this.config.getAppearanceConfiguration().getHeight();
    float thickness = this.config.getAppearanceConfiguration().getThickness();
    float halfThickness = thickness / 2;

    //bottom
    this.renderOutlinedRectangle(stack, x - halfThickness, y - 0.5f + gap, thickness, 4 + height,
        color, outlineColor);

    //top
    this.renderOutlinedRectangle(stack, x - halfThickness, y - 3.5f - gap - height, thickness,
        4 + height, color, outlineColor);

    //right
    this.renderOutlinedRectangle(stack, x - 0.5f + gap, y - halfThickness, 4 + width, thickness,
        color, outlineColor);

    //left
    this.renderOutlinedRectangle(stack, x - 3.5f - gap - width, y - halfThickness, 4 + width,
        thickness, color, outlineColor);
  }

  public void renderOutlinedRectangle(Stack stack, float x, float y, float width, float height,
      int color, int outlineColor) {
    Rectangle rectangle = DefaultRectangle.relative(x, y, width, height);
    this.rectangleRenderer.renderOutline(stack, rectangle, outlineColor,
        this.config.getAppearanceConfiguration().getOutlineThickness());
    this.rectangleRenderer.renderRectangle(stack, rectangle, color);
  }
}
