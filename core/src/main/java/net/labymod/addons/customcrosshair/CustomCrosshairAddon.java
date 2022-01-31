package net.labymod.addons.customcrosshair;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CustomCrosshairRenderer;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.gui.screen.widget.debug.DebugOverlay;
import net.labymod.api.client.options.Perspective;
import net.labymod.api.configuration.config.category.discord.DiscordIntegrationSetting;
import net.labymod.api.configuration.settings.SettingsRegistry;
import net.labymod.api.configuration.settings.gui.SettingCategoryRegistry;
import net.labymod.api.configuration.settings.setting.SimpleSetting;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameInitializeEvent;
import net.labymod.api.event.client.lifecycle.GameInitializeEvent.Lifecycle;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent.OverlayElementType;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
@Singleton
public class CustomCrosshairAddon {

  private final LabyAPI labyAPI;
  private final SettingCategoryRegistry categoryRegistry;
  private final DebugOverlay debugOverlay;

  private final CustomCrosshairRenderer crosshairRenderer;

  private DefaultCustomCrosshairConfiguration config;

  @Inject
  public CustomCrosshairAddon(LabyAPI labyAPI,
      SettingCategoryRegistry categoryRegistry,
      DebugOverlay debugOverlay,
      CustomCrosshairRenderer crosshairRenderer) {
    this.labyAPI = labyAPI;
    this.categoryRegistry = categoryRegistry;
    this.debugOverlay = debugOverlay;
    this.crosshairRenderer = crosshairRenderer;
  }

  @Subscribe(Priority.NORMAL)
  public void onRender(IngameOverlayElementRenderEvent event) {
    if ((!event.isCancelled() && !event.getElementType().equals(OverlayElementType.CROSSHAIR)
        && event.getPhase().equals(
        Phase.POST)) || !this.config.isEnabled()) {
      return;
    }

    Perspective perspective = this.labyAPI.getMinecraft().getOptions().getPerspective();
    if(perspective.equals(Perspective.FIRST_PERSON)) {
      if(!this.config.getVisibilityConfiguration().isDisplayFirstPerson()) {
        return;
      }

    }else{
      if(!this.config.getVisibilityConfiguration().isDisplayThirdPerson()) {
        return;
      }
    }


    if(debugOverlay.isEnabled() && !this.config.getVisibilityConfiguration().isDisplayDebug()) {
        return;
    }

    event.setCancelled(true);

    this.crosshairRenderer.tick();
    this.crosshairRenderer.renderCrosshair(event.getStack());
  }

  @Subscribe(Priority.LATEST)
  public void onGameInitialize(GameInitializeEvent event) {
    if (event.getLifecycle() != Lifecycle.POST_STARTUP) {
      return;
    }

    //register config
    try {
      this.config = this.labyAPI.getConfigurationLoader().load(DefaultCustomCrosshairConfiguration.class);

      // Create registry
      SettingsRegistry registry = this.config.initializeRegistry();

      // Register configuration categories
      this.categoryRegistry.register(registry.getId(), registry);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
