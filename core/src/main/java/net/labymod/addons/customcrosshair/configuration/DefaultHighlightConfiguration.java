package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.configuration.loader.ConfigLayer;
import net.labymod.api.util.ColorUtil;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultHighlightConfiguration implements HighlightConfiguration, ConfigLayer {

  @ColorPickerWidget.ColorPickerSetting
  private int highlightHostile = ColorUtil.toValue(0, 0, 0);

  @ColorPickerWidget.ColorPickerSetting
  private int highlightPassive = ColorUtil.toValue(0, 0, 0);
  @ColorPickerWidget.ColorPickerSetting
  private int highlightPlayer = ColorUtil.toValue(0, 0, 0);

  public int getHighlightHostile() {
    return this.highlightHostile;
  }

  public int getHighlightPassive() {
    return this.highlightPassive;
  }

  public int getHighlightPlayer() {
    return this.highlightPlayer;
  }

}
