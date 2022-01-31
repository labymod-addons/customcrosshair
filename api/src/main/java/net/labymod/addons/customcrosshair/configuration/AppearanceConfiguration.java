package net.labymod.addons.customcrosshair.configuration;

import net.labymod.addons.customcrosshair.CrosshairRenderType;

public interface AppearanceConfiguration {

  int getWidth();

  int getHeight();

  int getThickness();

  int getColor();

  int getOutlineThickness();

  int getOutlineColor();

  int getGap();

  CrosshairRenderType getType();

  int getRotation();
}
