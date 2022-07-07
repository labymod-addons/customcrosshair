package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultVisibilityConfiguration extends Config implements VisibilityConfiguration {

  @SwitchSetting
  private boolean displayInFirstPerson = true;

  @SwitchSetting
  private boolean displayInThirdPerson = true;

  @Override
  public boolean displayInFirstPerson() {
    return this.displayInFirstPerson;
  }

  @Override
  public boolean displayInThirdPerson() {
    return this.displayInThirdPerson;
  }
}
