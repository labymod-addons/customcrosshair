package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.ConfigLayer;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultSpecialConfiguration implements SpecialConfiguration, ConfigLayer {

  @SwitchSetting
  private boolean dynamicBow = true;

  @SwitchSetting
  private boolean rainbow = true;

  @SliderSetting(min = 1, max = 10)
  private int rainbowSpeed = 5;

  public boolean isDynamicBow() {
    return this.dynamicBow;
  }

  public boolean isRainbow() {
    return this.rainbow;
  }

  public int getRainbowSpeed() {
    return this.rainbowSpeed;
  }

}
