package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.RenderPipeline;
import net.labymod.api.client.render.draw.CircleRenderer;
import net.labymod.api.client.render.matrix.Stack;

@Singleton
public class CrosshairDotFormRenderer implements CrosshairFormRenderer {

  private final CustomCrosshair addon;
  private final RenderPipeline renderPipeline;

  @Inject
  public CrosshairDotFormRenderer(CustomCrosshair addon, RenderPipeline renderPipeline) {
    this.addon = addon;
    this.renderPipeline = renderPipeline;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    float thickness = this.addon.configuration().dot().thickness();
    float outlineThickness = this.addon.configuration().appearance().outlineThickness();

    CircleRenderer circleRenderer = this.renderPipeline.circleRenderer();
    circleRenderer.pos(x, y).radius(thickness + outlineThickness).color(outlineColor).render(stack);
    circleRenderer.pos(x, y).radius(thickness).color(color).render(stack);
  }
}
