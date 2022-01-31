package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.configuration.loader.Config;

public interface CustomCrosshairConfiguration extends Config {

  boolean isEnabled();

  AppearanceConfiguration getAppearanceConfiguration();

  DotConfiguration getDotConfiguration();

  HighlightConfiguration getHighlightConfiguration();

  SpecialConfiguration getSpecialConfiguration();

  VisibilityConfiguration getVisibilityConfiguration();

}
