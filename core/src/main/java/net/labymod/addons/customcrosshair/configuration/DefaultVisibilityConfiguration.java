package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.ConfigLayer;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultVisibilityConfiguration implements ConfigLayer, VisibilityConfiguration{

  @SwitchSetting
  private boolean displayInFirstPerson = true;

  @SwitchSetting
  private boolean displayInThirdPerson = true;

  @SwitchSetting
  private boolean displayInDebug = true;

  @Override
  public boolean isDisplayFirstPerson() {
    return this.displayInFirstPerson;
  }

  @Override
  public boolean isDisplayThirdPerson() {
    return this.displayInThirdPerson;
  }

  @Override
  public boolean isDisplayDebug() {
    return this.displayInDebug;
  }
}
