package net.labymod.addons.customcrosshair;

import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.listener.IngameOverlayElementRenderListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;

@AddonListener
@Singleton
public class CustomCrosshair extends LabyAddon<DefaultCustomCrosshairConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerListener(IngameOverlayElementRenderListener.class);
  }

  @Override
  protected Class<DefaultCustomCrosshairConfiguration> configurationClass() {
    return DefaultCustomCrosshairConfiguration.class;
  }
}
