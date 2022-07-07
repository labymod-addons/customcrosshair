package net.labymod.addons.customcrosshair;

import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairArrowFormRenderer;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairCircleFormRenderer;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairCrossFormRenderer;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairDefaultFormRenderer;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairDotFormRenderer;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairSquareFormRenderer;

public enum CrosshairType implements CrosshairRenderType {
  DEFAULT(CrosshairDefaultFormRenderer.class),
  CROSS(CrosshairCrossFormRenderer.class),
  CIRCLE(CrosshairCircleFormRenderer.class),
  SQUARE(CrosshairSquareFormRenderer.class),
  ARROW(CrosshairArrowFormRenderer.class),
  DOT(CrosshairDotFormRenderer.class);

  private final Class<? extends CrosshairFormRenderer> rendererClass;

  CrosshairType(Class<? extends CrosshairFormRenderer> rendererClass) {
    this.rendererClass = rendererClass;
  }

  @Override
  public Class<? extends CrosshairFormRenderer> rendererClass() {
    return this.rendererClass;
  }
}
