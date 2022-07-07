package net.labymod.addons.customcrosshair.configuration;

public interface CustomCrosshairConfiguration {

  boolean enabled();

  AppearanceConfiguration appearance();

  DotConfiguration dot();

  HighlightConfiguration highlight();

  SpecialConfiguration special();

  VisibilityConfiguration visibility();

}
