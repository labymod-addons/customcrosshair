package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.models.Implement;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@ConfigName("settings")
@Implement(CustomCrosshairConfiguration.class)
public class DefaultCustomCrosshairConfiguration extends Config implements
    CustomCrosshairConfiguration {

  @SwitchSetting
  private boolean enabled = true;

  private DefaultAppearanceConfiguration appearance = new DefaultAppearanceConfiguration();

  private DefaultDotConfiguration dot = new DefaultDotConfiguration();

  private DefaultHighlightConfiguration highlight = new DefaultHighlightConfiguration();

  private DefaultSpecialConfiguration special = new DefaultSpecialConfiguration();

  private DefaultVisibilityConfiguration visibility = new DefaultVisibilityConfiguration();

  @Override
  public boolean enabled() {
    return this.enabled;
  }

  @Override
  public AppearanceConfiguration appearance() {
    return this.appearance;
  }

  @Override
  public DotConfiguration dot() {
    return this.dot;
  }

  @Override
  public HighlightConfiguration highlight() {
    return this.highlight;
  }

  @Override
  public SpecialConfiguration special() {
    return this.special;
  }

  @Override
  public VisibilityConfiguration visibility() {
    return this.visibility;
  }
}
