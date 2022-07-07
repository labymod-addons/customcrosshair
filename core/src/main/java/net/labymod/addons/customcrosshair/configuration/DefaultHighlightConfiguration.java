package net.labymod.addons.customcrosshair.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.util.ColorUtil;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class DefaultHighlightConfiguration implements HighlightConfiguration {

  @ColorPickerWidget.ColorPickerSetting
  private int highlightHostile = ColorUtil.toValue(0, 0, 0);

  @ColorPickerWidget.ColorPickerSetting
  private int highlightPassive = ColorUtil.toValue(0, 0, 0);
  @ColorPickerWidget.ColorPickerSetting
  private int highlightPlayer = ColorUtil.toValue(0, 0, 0);

  public int highlightingHostile() {
    return this.highlightHostile;
  }

  public int highlightingPassive() {
    return this.highlightPassive;
  }

  public int highlightingPlayer() {
    return this.highlightPlayer;
  }

}
