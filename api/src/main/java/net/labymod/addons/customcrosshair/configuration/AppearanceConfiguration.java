package net.labymod.addons.customcrosshair.configuration;

import net.labymod.addons.customcrosshair.CrosshairRenderType;

public interface AppearanceConfiguration {

  int width();

  int height();

  int thickness();

  int color();

  int outlineThickness();

  int outlineColor();

  int gap();

  CrosshairRenderType type();

  int rotation();
}
