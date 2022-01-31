package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.ConfigLayer;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultDotConfiguration implements ConfigLayer, DotConfiguration {

  @SwitchSetting
  private boolean enabled = true;

  @SliderSetting(min = 1, max = 10)
  private int thickness = 1;

  public int getThickness() {
    return this.thickness;
  }

  public boolean isEnabled() {
    return this.enabled;
  }
}
