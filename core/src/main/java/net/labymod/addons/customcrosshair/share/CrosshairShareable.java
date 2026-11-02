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

package net.labymod.addons.customcrosshair.share;

import com.google.gson.JsonObject;
import java.util.Objects;
import java.util.function.Supplier;
import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.activity.CustomCrosshairEditActivity;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvasPreset;
import net.labymod.addons.customcrosshair.share.CrosshairShare.Settings;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.labyconnect.share.ChatSharePreview;
import net.labymod.api.labyconnect.share.ChatShareRegistry;
import net.labymod.api.labyconnect.share.ChatShareable;
import net.labymod.api.util.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The crosshair as something to share in the Laby Chat: the canvas and the settings that change
 * how it looks. Viewing a received one opens its canvas in the crosshair editor.
 */
public final class CrosshairShareable implements ChatShareable<CrosshairShare> {

  public static final String KIND = CustomCrosshair.NAMESPACE + ":crosshair";

  private static final String LABEL = "Crosshair";

  private final ChatShareRegistry registry;
  private final Supplier<CustomCrosshairConfiguration> configuration;
  private final Icon icon = Icon.svg(CustomCrosshair.withDefaultNamespace("textures/share.svg"));

  private CrosshairShareable(
      @NotNull ChatShareRegistry registry,
      @NotNull Supplier<CustomCrosshairConfiguration> configuration
  ) {
    this.registry = registry;
    this.configuration = configuration;
  }

  /**
   * Creates the kind and registers it. Throws a {@link LinkageError} on a client without the share
   * API.
   */
  static @NotNull CrosshairShareable register(
      @NotNull Supplier<CustomCrosshairConfiguration> configuration
  ) {
    ChatShareRegistry registry = Laby.references().chatShareRegistry();
    CrosshairShareable shareable = new CrosshairShareable(registry, configuration);
    registry.register(shareable);
    return shareable;
  }

  /**
   * Lets the player pick a friend and sends them the crosshair. Called on the render thread.
   */
  public void share(@NotNull CrosshairShare value) {
    this.registry.share(this, value);
  }

  @Override
  public @NotNull String kind() {
    return KIND;
  }

  @Override
  public @NotNull String label() {
    return LABEL;
  }

  @Override
  public @NotNull Component displayName() {
    return Component.translatable("customcrosshair.share.name");
  }

  @Override
  public @NotNull Icon icon() {
    return this.icon;
  }

  @Override
  public @NotNull CrosshairShare export() {
    CustomCrosshairConfiguration config = this.configuration.get();
    CrosshairCanvas canvas = config.canvas().get();
    Settings settings = new Settings(
        bool(config.displayInThirdPerson(), Settings.DEFAULTS.displayInThirdPerson()),
        integer(config.rotation(), Settings.DEFAULTS.rotation()),
        bool(config.vanillaBlending(), Settings.DEFAULTS.vanillaBlending()),
        color(config.color(), Settings.DEFAULTS.color()),
        integer(config.outlineThickness(), Settings.DEFAULTS.outlineThickness()),
        color(config.outlineColor(), Settings.DEFAULTS.outlineColor()),
        bool(config.dynamicColor(), Settings.DEFAULTS.dynamicColor()),
        color(config.neutralColor(), Settings.DEFAULTS.neutralColor()),
        color(config.hostileColor(), Settings.DEFAULTS.hostileColor()),
        color(config.playerColor(), Settings.DEFAULTS.playerColor())
    ).clamped();

    return CrosshairShare.of(
        canvas == null ? CrosshairCanvasPreset.DEFAULT.getCanvas() : canvas,
        settings,
        bool(config.enabled(), true)
    );
  }

  @Override
  public @NotNull JsonObject write(@NotNull CrosshairShare value) {
    return value.write();
  }

  @Override
  public @Nullable CrosshairShare read(@NotNull JsonObject data) {
    return CrosshairShare.read(data);
  }

  @Override
  public @Nullable ChatSharePreview preview(@NotNull CrosshairShare value) {
    return ChatSharePreview.pixelArt(
        CrosshairCanvas.SIZE,
        CrosshairCanvas.SIZE,
        value.previewPixels()
    );
  }

  @Override
  public void apply(@NotNull CrosshairShare value) {
    CustomCrosshairConfiguration config = this.configuration.get();
    Settings settings = value.settings();
    config.canvas().set(value.canvas());
    config.displayInThirdPerson().set(settings.displayInThirdPerson());
    config.rotation().set(settings.rotation());
    config.vanillaBlending().set(settings.vanillaBlending());
    config.color().set(toColor(settings.color()));
    config.outlineThickness().set(settings.outlineThickness());
    config.outlineColor().set(toColor(settings.outlineColor()));
    config.dynamicColor().set(settings.dynamicColor());
    config.neutralColor().set(toColor(settings.neutralColor()));
    config.hostileColor().set(toColor(settings.hostileColor()));
    config.playerColor().set(toColor(settings.playerColor()));
    config.enabled().set(value.isEnabled());
  }

  @Override
  public boolean canView() {
    return true;
  }

  /**
   * Opens the canvas of a received crosshair in the editor, unsaved. Saving there takes over the
   * canvas and switches the addon on, the colors and other settings stay; sharing sends the
   * received crosshair on with the drawn canvas.
   */
  @Override
  public void view(@NotNull CrosshairShare value) {
    CustomCrosshairEditActivity.openReceived(this.configuration.get(), value);
  }

  private static boolean bool(ConfigProperty<Boolean> property, boolean fallback) {
    return Objects.requireNonNullElse(property.get(), fallback);
  }

  private static int integer(ConfigProperty<Integer> property, int fallback) {
    return Objects.requireNonNullElse(property.get(), fallback);
  }

  private static ShareColor color(ConfigProperty<Color> property, ShareColor fallback) {
    Color color = property.get();
    if (color == null) {
      return fallback;
    }

    return new ShareColor(color.withoutChroma().get(), color.isChroma(), color.getChromaSpeed());
  }

  private static Color toColor(ShareColor color) {
    Color base = Color.of(color.argb());
    return color.chroma()
        ? base.withChroma(color.chromaSpeed())
        : base.withoutChroma(color.chromaSpeed());
  }
}
