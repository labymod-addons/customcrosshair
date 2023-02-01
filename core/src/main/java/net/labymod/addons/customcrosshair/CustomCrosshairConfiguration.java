/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.customcrosshair;

import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasPreset;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@ConfigName("settings")
public class CustomCrosshairConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @DropdownSetting
  private final ConfigProperty<CrosshairCanvasPreset> type = ConfigProperty.createEnum(
      CrosshairCanvasPreset.DEFAULT
  );

  @SwitchSetting
  private final ConfigProperty<Boolean> displayInThirdPerson = new ConfigProperty<>(false);

  @SliderSetting(min = 0, max = 360, steps = 45)
  private final ConfigProperty<Integer> rotation = new ConfigProperty<>(0);

  @SettingSection("coloring")
  @SwitchSetting
  private final ConfigProperty<Boolean> vanillaBlending = new ConfigProperty<>(true);

  @SettingRequires(value = "vanillaBlending", invert = true)
  @ColorPickerSetting(chroma = true)
  private final ConfigProperty<Color> color = new ConfigProperty<>(
      Color.ofRGB(1.0F, 1.0F, 1.0F)
  );

  @SettingRequires(value = "vanillaBlending", invert = true)
  @SwitchSetting
  private final ConfigProperty<Boolean> dynamicColor = new ConfigProperty<>(false);

  @SettingRequires("dynamicColor")
  @ColorPickerSetting(chroma = true)
  private final ConfigProperty<Color> neutralColor = new ConfigProperty<>(
      Color.ofRGB(0.0F, 1.0F, 0.0F)
  );

  @SettingRequires("dynamicColor")
  @ColorPickerSetting(chroma = true)
  private final ConfigProperty<Color> hostileColor = new ConfigProperty<>(
      Color.ofRGB(1.0F, 0.0F, 0.0F)
  );

  @SettingRequires("dynamicColor")
  @ColorPickerSetting(chroma = true)
  private final ConfigProperty<Color> playerColor = new ConfigProperty<>(
      Color.ofRGB(1.0F, 1.0F, 0.0F)
  );

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<CrosshairCanvasPreset> type() {
    return this.type;
  }

  public ConfigProperty<Boolean> displayInThirdPerson() {
    return this.displayInThirdPerson;
  }

  public ConfigProperty<Integer> rotation() {
    return this.rotation;
  }

  public ConfigProperty<Boolean> vanillaBlending() {
    return this.vanillaBlending;
  }

  public ConfigProperty<Color> color() {
    return this.color;
  }

  public ConfigProperty<Boolean> dynamicColor() {
    return this.dynamicColor;
  }

  public ConfigProperty<Color> neutralColor() {
    return this.neutralColor;
  }

  public ConfigProperty<Color> hostileColor() {
    return this.hostileColor;
  }

  public ConfigProperty<Color> playerColor() {
    return this.playerColor;
  }
}
