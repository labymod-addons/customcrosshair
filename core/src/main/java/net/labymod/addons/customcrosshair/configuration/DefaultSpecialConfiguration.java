package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultSpecialConfiguration extends Config implements SpecialConfiguration {

  @SwitchSetting
  private boolean dynamicBow = true;

  @SwitchSetting
  private boolean rainbow = true;

  @SliderSetting(min = 1, max = 10)
  private int rainbowSpeed = 5;

  public boolean dynamicBow() {
    return this.dynamicBow;
  }

  public boolean rainbow() {
    return this.rainbow;
  }

  public int rainbowSpeed() {
    return this.rainbowSpeed;
  }

}
