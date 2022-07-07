package net.labymod.addons.customcrosshair.listener;

import com.google.inject.Inject;
import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.configuration.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.configuration.VisibilityConfiguration;
import net.labymod.addons.customcrosshair.renderer.CustomCrosshairRenderer;
import net.labymod.api.client.options.Perspective;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent;
import net.labymod.api.event.client.render.overlay.IngameOverlayElementRenderEvent.OverlayElementType;

public class IngameOverlayElementRenderListener {

  private final CustomCrosshair addon;
  private final CustomCrosshairRenderer customCrosshairRenderer;

  @Inject
  private IngameOverlayElementRenderListener(CustomCrosshair addon,
      CustomCrosshairRenderer customCrosshairRenderer) {
    this.addon = addon;
    this.customCrosshairRenderer = customCrosshairRenderer;
  }

  @Subscribe(Priority.LATE)
  public void onRender(IngameOverlayElementRenderEvent event) {
    CustomCrosshairConfiguration configuration = this.addon.configuration();

    if ((!event.isCancelled() && !event.getElementType().equals(OverlayElementType.CROSSHAIR)
        && event.getPhase().equals(Phase.POST)) || !configuration.enabled()) {
      return;
    }

    VisibilityConfiguration visibilityConfiguration = configuration.visibility();
    Perspective perspective = this.addon.labyAPI().getMinecraft().getOptions().getPerspective();
    if (perspective.equals(Perspective.FIRST_PERSON)) {
      if (!visibilityConfiguration.displayInFirstPerson()) {
        return;
      }

    } else {
      if (!visibilityConfiguration.displayInThirdPerson()) {
        return;
      }
    }

    event.setCancelled(true);
    this.customCrosshairRenderer.tick();
    this.customCrosshairRenderer.renderCrosshair(event.getStack());
  }
}
