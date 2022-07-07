package net.labymod.addons.customcrosshair.configuration;

import net.labymod.addons.customcrosshair.CrosshairType;
import net.labymod.api.client.gui.screen.widget.widgets.input.DropdownWidget.DropdownSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.util.ColorUtil;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultAppearanceConfiguration extends Config implements AppearanceConfiguration {

  @DropdownSetting
  private CrosshairType type = CrosshairType.DEFAULT;

  @ColorPickerSetting
  private int color = ColorUtil.toValue(0, 0, 0);

  @SliderSetting(min = 1, max = 10)
  private int thickness = 1;

  @SliderSetting(min = 1, max = 50)
  private int width = 1;

  @SliderSetting(min = 1, max = 50)
  private int height = 1;

  @SliderSetting(min = 1, max = 20)
  private int gap = 1;

  @SliderSetting(min = 0, max = 360)
  private int rotation = 0;

  @SliderSetting(min = 0, max = 10)
  private int outlineThickness = 0;

  @ColorPickerSetting
  private int outlineColor = ColorUtil.toValue(0, 0, 0);

  public int width() {
    return this.width;
  }

  public int height() {
    return this.height;
  }

  public int thickness() {
    return this.thickness;
  }

  public int color() {
    return this.color;
  }

  public int outlineThickness() {
    return this.outlineThickness;
  }

  public int outlineColor() {
    return this.outlineColor;
  }

  public int gap() {
    return this.gap;
  }

  public CrosshairType type() {
    return this.type;
  }

  public int rotation() {
    return this.rotation;
  }

}
