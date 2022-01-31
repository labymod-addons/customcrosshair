package net.labymod.addons.customcrosshair.renderer.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.api.client.render.draw.CircleRenderer;
import net.labymod.api.client.render.matrix.Stack;

@Singleton
public class CrosshairDotFormRenderer implements CrosshairFormRenderer {

  private final CircleRenderer circleRenderer;
  private final DefaultCustomCrosshairConfiguration config;

  @Inject
  public CrosshairDotFormRenderer(CircleRenderer circleRenderer,
      DefaultCustomCrosshairConfiguration config) {
    this.circleRenderer = circleRenderer;
    this.config = config;
  }

  @Override
  public void render(Stack stack, float x, float y, float gap, int color, int outlineColor) {
    this.circleRenderer.render(stack, x, y,
        this.config.getDotConfiguration().getThickness() + config.getAppearanceConfiguration()
            .getOutlineThickness(),
        outlineColor, true);

    this.circleRenderer.render(stack, x, y, this.config.getDotConfiguration().getThickness(), color,
        true);
  }
}
