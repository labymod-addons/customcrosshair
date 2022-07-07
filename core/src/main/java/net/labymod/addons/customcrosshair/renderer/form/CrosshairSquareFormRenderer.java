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

@Singleton
public class CrosshairSquareFormRenderer implements CrosshairFormRenderer {

  private final CustomCrosshair addon;
  private final RenderPipeline renderPipeline;

  @Inject
  public CrosshairSquareFormRenderer(CustomCrosshair addon, RenderPipeline renderPipeline) {
    this.addon = addon;
    this.renderPipeline = renderPipeline;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    AppearanceConfiguration appearance = this.addon.configuration().appearance();
    float thickness = appearance.thickness();

    this.renderSquareWithThickness(stack, x, y, gap, thickness + appearance.outlineThickness(),
        outlineColor);
    this.renderSquareWithThickness(stack, x, y, gap, thickness, color);
  }

  private void renderSquareWithThickness(Stack stack, float x, float y, float gap, float thickness,
      int color) {

    RectangleRenderer renderer = this.renderPipeline.rectangleRenderer();

    float halfThickness = thickness / 2;
    float doubleGap = gap * 2;

    //top
    renderer.renderRectangle(stack,
        DefaultRectangle.relative(x - halfThickness - gap, y - halfThickness - gap, doubleGap,
            thickness), color);

    //bottom
    renderer.renderRectangle(stack,
        DefaultRectangle.relative(x - halfThickness - gap, y + gap - halfThickness,
            doubleGap + thickness, thickness), color);

    //left
    renderer.renderRectangle(stack,
        DefaultRectangle.relative(x - halfThickness - gap, y - halfThickness - gap, thickness,
            doubleGap + thickness), color);

    //right
    renderer.renderRectangle(stack,
        DefaultRectangle.relative(x + gap - halfThickness, y - halfThickness - gap, thickness,
            doubleGap + thickness), color);
  }
}
