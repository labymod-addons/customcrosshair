package org.example.core;

import net.labymod.api.client.event.TickEvent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.models.addon.annotation.AddonInfo;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.jetbrains.annotations.NotNull;

@AddonMain
@AddonInfo(id = "example", name = "Example")
public class ExampleAddon {

  public ExampleAddon() {
  }

  @Subscribe
  public void onTick(@NotNull TickEvent event) {
    System.out.println("Hello, Minecraft Tick (" + event.getPhase().name() + ")");
  }

}
