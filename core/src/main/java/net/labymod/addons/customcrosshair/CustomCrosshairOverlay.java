package net.labymod.addons.customcrosshair;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.CustomCrosshairRenderer;
import net.labymod.api.client.gui.screen.activity.types.IngameOverlayActivity;
import net.labymod.api.client.render.matrix.Stack;

@Singleton
public class CustomCrosshairOverlay extends IngameOverlayActivity {


  private final DefaultCustomCrosshairConfiguration config;
  private final CustomCrosshairRenderer crosshairRenderer;

  private int rainbowTick = 0;

  @Inject
  public CustomCrosshairOverlay(
      DefaultCustomCrosshairConfiguration config,
      CustomCrosshairRenderer crosshairRenderer) {
    this.config = config;
    this.crosshairRenderer = crosshairRenderer;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.config.getSpecialConfiguration().isRainbow() || rainbowTick++ > 255) {
      rainbowTick = 0;
    }

  }

  @Override
  public void render(Stack stack, int mouseX, int mouseY, float partialTicks) {
    super.render(stack, mouseX, mouseY, partialTicks);

    this.crosshairRenderer.renderCrosshair(stack);
  }

  @Override
  public boolean isVisible() {
    return true;
  }
}
