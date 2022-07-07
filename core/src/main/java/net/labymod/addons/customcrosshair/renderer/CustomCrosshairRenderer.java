package net.labymod.addons.customcrosshair.renderer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.CustomCrosshair;
import net.labymod.addons.customcrosshair.client.entity.TargetEntity;
import net.labymod.addons.customcrosshair.client.world.item.BowItemStack;
import net.labymod.addons.customcrosshair.configuration.AppearanceConfiguration;
import net.labymod.addons.customcrosshair.configuration.CustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.configuration.HighlightConfiguration;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairDotFormRenderer;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.util.ColorUtil;

@Singleton
public class CustomCrosshairRenderer {

  private final CustomCrosshair addon;
  private int rainbowTicks = 0;

  @Inject
  public CustomCrosshairRenderer(CustomCrosshair addon) {
    this.addon = addon;
  }

  public void renderCrosshair(Stack stack) {
    CustomCrosshairConfiguration configuration = this.addon.configuration();
    LabyAPI labyAPI = this.addon.labyAPI();
    Minecraft minecraft = labyAPI.getMinecraft();

    float x = (minecraft.getMinecraftWindow().getBounds().getWidth() / 2);
    float y = (minecraft.getMinecraftWindow().getBounds().getHeight() / 2) - 0.5f;

    AppearanceConfiguration appearance = configuration.appearance();
    CrosshairFormRenderer formRenderer = LabyGuice.getInstance(
        appearance.type().rendererClass());

    int color = appearance.color();

    if (minecraft.getTargetEntity() != null) {
      color = this.getTargetEntityColor(configuration, minecraft);
    } else if (configuration.special().rainbow()) {
      color = this.calculateRainbowColor(configuration);
    }

    int outlineColor = appearance.outlineColor();
    float gap = this.calculateGap(configuration, minecraft);

    if (configuration.dot().enabled()) {
      LabyGuice.getInstance(CrosshairDotFormRenderer.class)
          .render(stack, x, y, gap, color, outlineColor);
    }

    stack.push();
    stack.translate(x, y, 0);
    stack.rotate(appearance.rotation(), 0, 0, 1);
    stack.translate(x * -1, y * -1, 0);

    formRenderer.render(stack, x, y, gap, color, outlineColor);
    stack.pop();
  }

  private int getTargetEntityColor(CustomCrosshairConfiguration configuration,
      Minecraft minecraft) {
    Entity entity = minecraft.getTargetEntity();
    HighlightConfiguration highlight = configuration.highlight();

    if (entity instanceof Player) {
      return highlight.highlightingPlayer();
    }

    TargetEntity targetEntity = (TargetEntity) entity;
    if (targetEntity.isHostile()) {
      return highlight.highlightingHostile();
    }

    return highlight.highlightingPassive();
  }

  private float calculateGap(CustomCrosshairConfiguration configuration, Minecraft minecraft) {
    float gap = configuration.appearance().gap();
    if (configuration.special().dynamicBow()
        && minecraft.getClientPlayer() != null
        && !minecraft.getClientPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
      ClientPlayer player = minecraft.getClientPlayer();
      ItemStack bowStack = null;

      if (player.getMainHandItemStack() != null
          && ((BowItemStack) player.getMainHandItemStack()).isBow()) {
        bowStack = player.getMainHandItemStack();
      } else if (player.getOffHandItemStack() != null
          && ((BowItemStack) player.getOffHandItemStack()).isBow()) {
        bowStack = player.getOffHandItemStack();
      }

      if (bowStack != null && player.getItemUseDurationTicks() > 0) {
        float dragging =
            (bowStack.getUseDuration() - (float) player.getItemUseDurationTicks()) / 10;
        if (dragging > 2) {
          dragging = 2;
        }

        gap += (1 + dragging) * 2.0F;
      }
    }
    return gap;
  }

  private int calculateRainbowColor(CustomCrosshairConfiguration configuration) {
    float speed =
        configuration.special().rainbowSpeed() / 100.0F * this.rainbowTicks;

    int red = (int) (Math.sin(speed + 0.0F) * 127.0D + 128.0D);
    int green = (int) (Math.sin(speed + 2.0F) * 127.0D + 128.0D);
    int blue = (int) (Math.sin(speed + 4.0F) * 127.0D + 128.0D);

    return ColorUtil.toValue(red, green, blue);
  }

  public void tick() {
    if (this.rainbowTicks >= Integer.MAX_VALUE) {
      this.rainbowTicks = 0;
    } else {
      ++this.rainbowTicks;
    }
  }
}
