package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.draw.CircleRenderer;
import net.labymod.api.client.render.matrix.Stack;

@Singleton
public class CrosshairCircleFormRenderer implements CrosshairFormRenderer {

  private final DefaultCustomCrosshairConfiguration config;
  private final CircleRenderer circleRenderer;

  @Inject
  public CrosshairCircleFormRenderer(CircleRenderer circleRenderer,
      DefaultCustomCrosshairConfiguration config) {
    this.config = config;
    this.circleRenderer = circleRenderer;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    float endRadius = gap + this.config.getAppearanceConfiguration().getThickness();
    float outlineEndRadius =
        endRadius + this.config.getAppearanceConfiguration().getOutlineThickness();

    this.circleRenderer.renderDonut(stack, x, y, endRadius, outlineEndRadius, outlineColor, true);
    this.circleRenderer.renderDonut(stack, x, y, gap, endRadius, color, true);
  }


}
