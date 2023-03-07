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

package net.labymod.addons.customcrosshair.activity;

import java.util.concurrent.TimeUnit;
import net.labymod.addons.customcrosshair.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasPreset;
import net.labymod.addons.customcrosshair.widgets.CrosshairCanvasRendererWidget;
import net.labymod.addons.customcrosshair.widgets.edit.CrosshairPresetListWidget;
import net.labymod.addons.customcrosshair.widgets.edit.EditCrosshairCanvasWidget;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.lss.style.modifier.attribute.AttributeState;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.concurrent.task.Task;
import org.jetbrains.annotations.Nullable;

@Link("edit.lss")
public class CustomCrosshairEditActivity extends Activity {

  private final CustomCrosshairConfiguration configuration;
  private EditCrosshairCanvasWidget editCanvas;
  private ButtonWidget importButton;
  private Task clipboardRefreshTask;

  public CustomCrosshairEditActivity(final CustomCrosshairConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void initialize(final Parent parent) {
    super.initialize(parent);

    final VerticalListWidget<Widget> container = new VerticalListWidget<>();
    container.addId("container");

    final ComponentWidget presetTitle = ComponentWidget.i18n(
        "customcrosshair.activity.presets.title"
    );
    presetTitle.addId("title");
    container.addChild(presetTitle);

    final CrosshairPresetListWidget presetContainer = new CrosshairPresetListWidget();
    presetContainer.addId("preset-container");

    for (final CrosshairCanvasPreset preset : CrosshairCanvasPreset.getValues()) {
      final CrosshairCanvas canvas = preset.getCanvas();
      final CrosshairCanvasRendererWidget widget = new CrosshairCanvasRendererWidget(canvas);
      widget.setPressable(() -> this.editCanvas.update(canvas));
      widget.setAttributeState(AttributeState.ENABLED, true);
      presetContainer.addChild(widget);
    }

    container.addChild(presetContainer);

    final ComponentWidget editTitle = ComponentWidget.i18n(
        "customcrosshair.activity.edit.title"
    );
    editTitle.addId("title");
    container.addChild(editTitle);

    final FlexibleContentWidget editWrapper = new FlexibleContentWidget();
    editWrapper.addId("edit-wrapper");

    this.editCanvas = new EditCrosshairCanvasWidget(this.configuration.canvas().get());
    this.editCanvas.addId("edit-canvas");
    editWrapper.addContent(this.editCanvas);

    final FlexibleContentWidget inputWrapper = new FlexibleContentWidget();
    inputWrapper.addId("input-wrapper");

    final ButtonWidget shareButton = ButtonWidget.i18n(
        "customcrosshair.activity.edit.share.name"
    );
    shareButton.addId("share-button");
    shareButton.setHoverComponent(
        Component.translatable("customcrosshair.activity.edit.share.description")
    );
    shareButton.setPressable(() -> {
      try {
        final String encode = CrosshairCanvas.encode(this.editCanvas.canvas());
        this.labyAPI.minecraft().setClipboard(encode);
        this.pushNotification("customcrosshair.notification.share.copied");
      } catch (final Exception e) {
        e.printStackTrace();
        this.pushNotification("customcrosshair.notification.share.error");
      }
    });

    inputWrapper.addContent(shareButton);

    this.importButton = ButtonWidget.i18n(
        "customcrosshair.activity.edit.import.name"
    );
    this.importButton.addId("import-button");
    this.importButton.setHoverComponent(
        Component.translatable("customcrosshair.activity.edit.import.description")
    );
    this.importButton.setEnabled(this.isClipboardValid());
    this.importButton.setPressable(() -> {
      if (!this.importButton.isAttributeStateEnabled(AttributeState.ENABLED)) {
        return;
      }

      final String clipboard = this.labyAPI.minecraft().getClipboard();
      if (!this.isValid(clipboard)) {
        this.importButton.setEnabled(false);
        this.pushNotification("customcrosshair.notification.import.invalid");
        return;
      }

      final CrosshairCanvas canvas = CrosshairCanvas.decode(clipboard);
      if (canvas == null) {
        this.importButton.setEnabled(false);
        this.pushNotification("customcrosshair.notification.import.error");
        return;
      }

      this.pushNotification("customcrosshair.notification.import.success");
      this.editCanvas.update(canvas);
    });

    inputWrapper.addContent(this.importButton);
    editWrapper.addFlexibleContent(inputWrapper);
    container.addChild(editWrapper);

    this.document.addChild(new ScrollWidget(container));
  }

  @Override
  public void onCloseScreen() {
    if (this.clipboardRefreshTask != null) {
      this.clipboardRefreshTask.cancel();
      this.clipboardRefreshTask = null;
    }

    this.configuration.canvas().set(this.editCanvas.saveDraft());
    super.onCloseScreen();
  }

  @Override
  public void onOpenScreen() {
    super.onOpenScreen();
    if (this.clipboardRefreshTask != null) {
      this.clipboardRefreshTask.cancel();
    }

    this.clipboardRefreshTask = Task.builder(() -> {
      if (this.importButton == null) {
        return;
      }

      this.importButton.setEnabled(this.isClipboardValid());
    }).repeat(1, TimeUnit.SECONDS).build();

    this.clipboardRefreshTask.executeOnRenderThread();
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return new CustomCrosshairEditActivity(this.configuration).generic();
  }

  private boolean isValid(final String clipboard) {
    return clipboard != null && (clipboard.startsWith(CrosshairCanvas.ENCODED_PREFIX)
        || clipboard.startsWith(CrosshairCanvas.ENCODED_LUNAR_PREFIX));
  }

  private boolean isClipboardValid() {
    return this.isValid(this.labyAPI.minecraft().getClipboard());
  }

  private void pushNotification(final String translationKey) {
    final Notification notification = Notification.builder()
        .title(Component.translatable("customcrosshair.settings.name"))
        .text(Component.translatable(translationKey))
        .build();
    this.labyAPI.notificationController().push(notification);
  }
}
