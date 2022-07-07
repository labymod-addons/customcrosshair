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
public class CrosshairCrossFormRenderer implements CrosshairFormRenderer {

  private final CustomCrosshair addon;
  private final RenderPipeline renderPipeline;

  @Inject
  public CrosshairCrossFormRenderer(CustomCrosshair addon, RenderPipeline renderPipeline) {
    this.addon = addon;
    this.renderPipeline = renderPipeline;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    AppearanceConfiguration appearance = this.addon.configuration().appearance();
    float width = appearance.width();
    float height = appearance.height();
    float thickness = appearance.thickness();
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
    RectangleRenderer renderer = this.renderPipeline.rectangleRenderer();

    Rectangle rectangle = DefaultRectangle.relative(x, y, width, height);
    renderer.renderOutline(stack, rectangle, outlineColor,
        this.addon.configuration().appearance().outlineThickness());
    renderer.renderRectangle(stack, rectangle, color);
  }
}
