package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultDotConfiguration extends Config implements DotConfiguration {

  @SwitchSetting
  private boolean enabled = true;

  @SliderSetting(min = 1, max = 10)
  private int thickness = 1;

  public int thickness() {
    return this.thickness;
  }

  public boolean enabled() {
    return this.enabled;
  }
}
