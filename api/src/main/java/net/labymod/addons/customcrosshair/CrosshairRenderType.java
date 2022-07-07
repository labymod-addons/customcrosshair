package net.labymod.addons.customcrosshair;

import net.labymod.addons.customcrosshair.renderer.CrosshairFormRenderer;

public interface CrosshairRenderType {

  Class<? extends CrosshairFormRenderer> rendererClass();

}
