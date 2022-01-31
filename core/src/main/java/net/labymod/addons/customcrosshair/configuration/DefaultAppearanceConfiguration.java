package net.labymod.addons.customcrosshair.configuration;

import net.labymod.addons.customcrosshair.CrosshairType;
import net.labymod.api.client.gui.screen.widget.widgets.input.DropdownWidget.DropdownSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.configuration.loader.ConfigLayer;
import net.labymod.api.util.ColorUtil;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultAppearanceConfiguration implements AppearanceConfiguration, ConfigLayer {

  @DropdownSetting
  private CrosshairType type = CrosshairType.DEFAULT;

  @ColorPickerWidget.ColorPickerSetting
  private int color = ColorUtil.toValue(0, 0, 0);

  @SliderSetting(min = 1, max = 50)
  private int width = 1;

  @SliderSetting(min = 1, max = 50)
  private int height = 1;

  @SliderSetting(min = 1, max = 20)
  private int gap = 1;

  @SliderSetting(min = 1, max = 10)
  private int thickness = 1;

  @SliderSetting(min = 0, max = 360)
  private int rotation = 0;

  @SliderSetting(min = 0, max = 10)
  private int outlineThickness = 0;

  @ColorPickerWidget.ColorPickerSetting
  private int outlineColor = ColorUtil.toValue(0, 0, 0);

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public int getThickness() {
    return this.thickness;
  }

  public int getColor() {
    return this.color;
  }

  public int getOutlineThickness() {
    return this.outlineThickness;
  }

  public int getOutlineColor() {
    return this.outlineColor;
  }

  public int getGap() {
    return this.gap;
  }

  public CrosshairType getType() {
    return this.type;
  }

  public int getRotation() {
    return this.rotation;
  }

}
