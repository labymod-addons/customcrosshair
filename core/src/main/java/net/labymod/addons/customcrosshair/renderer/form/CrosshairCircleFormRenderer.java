package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.configuration.AppearanceConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.RenderPipeline;
import net.labymod.api.client.render.draw.CircleRenderer;
import net.labymod.api.client.render.matrix.Stack;

@Singleton
public class CrosshairCircleFormRenderer implements CrosshairFormRenderer {

  private final CustomCrosshair addon;
  private final RenderPipeline renderPipeline;

  @Inject
  public CrosshairCircleFormRenderer(CustomCrosshair addon, RenderPipeline renderPipeline) {
    this.addon = addon;
    this.renderPipeline = renderPipeline;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    AppearanceConfiguration appearance = this.addon.configuration().appearance();
    float endRadius = gap + appearance.thickness();
    float outlineEndRadius = endRadius + appearance.outlineThickness();

    CircleRenderer circleRenderer = this.renderPipeline.circleRenderer();
    circleRenderer.pos(x, y).donutRadius(endRadius, outlineEndRadius).color(outlineColor)
        .render(stack);
    circleRenderer.pos(x, y).donutRadius(gap, endRadius).color(color)
        .render(stack);
  }
}
