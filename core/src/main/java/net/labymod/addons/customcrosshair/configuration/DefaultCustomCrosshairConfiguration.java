package net.labymod.addons.customcrosshair.configuration;

import com.google.inject.Singleton;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.impl.AddonConfig;
import net.labymod.api.models.Implement;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@Singleton
@ConfigName("settings")
@Implement(CustomCrosshairConfiguration.class)
public class DefaultCustomCrosshairConfiguration extends AddonConfig implements
    CustomCrosshairConfiguration {

  @SwitchSetting
  private boolean enabled = true;

  private DefaultAppearanceConfiguration appearance = new DefaultAppearanceConfiguration();

  private DefaultDotConfiguration dot = new DefaultDotConfiguration();

  private DefaultHighlightConfiguration highlight = new DefaultHighlightConfiguration();

  private DefaultSpecialConfiguration special = new DefaultSpecialConfiguration();

  private DefaultVisibilityConfiguration visibility = new DefaultVisibilityConfiguration();

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public AppearanceConfiguration getAppearanceConfiguration() {
    return this.appearance;
  }

  @Override
  public DotConfiguration getDotConfiguration() {
    return this.dot;
  }

  @Override
  public HighlightConfiguration getHighlightConfiguration() {
    return this.highlight;
  }

  @Override
  public SpecialConfiguration getSpecialConfiguration() {
    return this.special;
  }

  @Override
  public VisibilityConfiguration getVisibilityConfiguration() {
    return this.visibility;
  }
}
