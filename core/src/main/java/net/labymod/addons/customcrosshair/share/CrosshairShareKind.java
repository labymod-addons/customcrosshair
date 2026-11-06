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
import net.labymod.api.labyconnect.share.ShareKind;
import net.labymod.api.labyconnect.share.ShareTarget;
import net.labymod.api.labyconnect.share.ShareUndo;
import net.labymod.api.util.Color;
import org.jetbrains.annotations.NotNull;

/**
 * The crosshair as a kind to share in the Laby Chat: the canvas and the settings that change how
 * it looks. A received one is switched on when applied, Undo brings back the crosshair before, and
 * viewing one opens its canvas in the crosshair editor.
 */
public final class CrosshairShareKind {

  public static final String ID = CustomCrosshair.NAMESPACE + ":crosshair";

  private static final String LABEL = "Crosshair";

  private final ChatShareRegistry registry;
  private final Supplier<CustomCrosshairConfiguration> configuration;
  private final ShareKind<CrosshairShare> kind;

  private CrosshairShareKind(
      @NotNull ChatShareRegistry registry,
      @NotNull Supplier<CustomCrosshairConfiguration> configuration
  ) {
    this.registry = registry;
    this.configuration = configuration;
    this.kind = ShareKind.builder(ID, new CrosshairShareCodec(), ShareTarget.sync(this::apply))
        .label(LABEL)
        .displayName(Component.translatable("customcrosshair.share.name"))
        .icon(Icon.svg(CustomCrosshair.withDefaultNamespace("textures/share.svg")))
        .source(this::export)
        .preview(CrosshairShareKind::preview)
        .viewer(this::view)
        .build();
  }

  /**
   * Creates the kind and registers it. Throws a {@link LinkageError} on a client without the share
   * API.
   */
  static @NotNull CrosshairShareKind register(
      @NotNull Supplier<CustomCrosshairConfiguration> configuration
  ) {
    ChatShareRegistry registry = Laby.references().chatShareRegistry();
    CrosshairShareKind shareKind = new CrosshairShareKind(registry, configuration);
    // The registry checks the namespace against this class, which belongs to the addon
    registry.register(shareKind.kind);
    return shareKind;
  }

  /**
   * Lets the player pick a friend and sends them the crosshair. Called on the render thread.
   */
  public void share(@NotNull CrosshairShare value) {
    this.registry.share(this.kind, value);
  }

  /**
   * The configured crosshair, kept inside the ranges of the settings.
   */
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

  /**
   * Takes over a received crosshair. Undo brings back the one before, switch included.
   */
  private @NotNull ShareUndo apply(@NotNull CrosshairShare value) {
    CrosshairShare before = this.export();
    this.write(value);
    return ShareUndo.of(() -> this.write(before));
  }

  private void write(@NotNull CrosshairShare value) {
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

  /**
   * Opens the canvas of a received crosshair in the editor, unsaved. Saving there takes over the
   * canvas and switches the addon on, the colors and other settings stay; sharing sends the
   * received crosshair on with the drawn canvas.
   */
  private void view(@NotNull CrosshairShare value) {
    CustomCrosshairEditActivity.openReceived(this.configuration.get(), value);
  }

  private static @NotNull ChatSharePreview preview(@NotNull CrosshairShare value) {
    return ChatSharePreview.pixelArt(
        CrosshairCanvas.SIZE,
        CrosshairCanvas.SIZE,
        value.previewPixels()
    );
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
