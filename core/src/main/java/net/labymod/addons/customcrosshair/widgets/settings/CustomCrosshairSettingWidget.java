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

package net.labymod.addons.customcrosshair.widgets.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.labymod.addons.customcrosshair.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.activity.CustomCrosshairEditActivity;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import net.labymod.addons.customcrosshair.widgets.CrosshairCanvasRendererWidget;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.lss.style.modifier.attribute.AttributeState;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.AbstractWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.util.SettingActivitySupplier;
import net.labymod.api.configuration.settings.widget.WidgetFactory;

@SettingWidget
@AutoWidget
@Link(value = "crosshair-settings.lss")
public class CustomCrosshairSettingWidget extends AbstractWidget<Widget> implements
    SettingActivitySupplier {

  private final CustomCrosshairConfiguration configuration;
  private final CrosshairCanvasRendererWidget canvasWidget;

  private CustomCrosshairSettingWidget(
      final CustomCrosshairConfiguration configuration,
      final CrosshairCanvas canvas
  ) {
    this.configuration = configuration;
    this.canvasWidget = new CrosshairCanvasRendererWidget(canvas);
  }

  @Override
  public void initialize(final Parent parent) {
    super.initialize(parent);
    this.setAttributeState(AttributeState.ENABLED, true);
    this.addChild(this.canvasWidget);
  }

  public void update(final CrosshairCanvas canvas) {
    this.canvasWidget.update(canvas);
  }

  @Override
  public Activity activity(final Setting setting) {
    return new CustomCrosshairEditActivity(this.configuration);
  }

  @SettingElement
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface CanvasSetting {

  }

  @SettingFactory
  public static class Factory implements
      WidgetFactory<CanvasSetting, CustomCrosshairSettingWidget> {

    @Override
    public CustomCrosshairSettingWidget[] create(
        final Setting setting,
        final CanvasSetting annotation,
        final SettingInfo<?> info,
        final SettingAccessor accessor
    ) {
      if (!(info.config() instanceof CustomCrosshairConfiguration)) {
        throw new IllegalStateException(
            "This setting can only be used within the CustomCrosshair addon"
        );
      }

      final CrosshairCanvas canvas = accessor.get();
      final CustomCrosshairConfiguration configuration = (CustomCrosshairConfiguration) info.config();
      final CustomCrosshairSettingWidget canvasWidget = new CustomCrosshairSettingWidget(
          configuration,
          canvas
      );

      configuration.canvas().addChangeListener(
          (property, oldValue, newValue) -> canvasWidget.update(newValue)
      );

      return new CustomCrosshairSettingWidget[]{canvasWidget};
    }

    @Override
    public Class<?>[] types() {
      return new Class[]{CrosshairCanvas.class};
    }
  }
}
