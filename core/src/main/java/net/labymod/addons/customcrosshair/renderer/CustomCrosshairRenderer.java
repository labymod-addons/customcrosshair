package net.labymod.addons.customcrosshair.renderer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labymod.addons.customcrosshair.client.entity.TargetEntity;
import net.labymod.addons.customcrosshair.client.world.item.BowItemStack;
import net.labymod.addons.customcrosshair.configuration.DefaultCustomCrosshairConfiguration;
import net.labymod.addons.customcrosshair.renderer.form.CrosshairDotFormRenderer;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.ColorUtil;

@Singleton
public class CustomCrosshairRenderer {

  private final LabyAPI labyAPI;
  private final Minecraft mc;

  private final DefaultCustomCrosshairConfiguration config;

  private int rainbowTicks = 0;

  @Inject
  public CustomCrosshairRenderer(LabyAPI labyAPI, Minecraft mc,
      DefaultCustomCrosshairConfiguration config) {
    this.labyAPI = labyAPI;
    this.mc = mc;
    this.config = config;
  }

  public void renderCrosshair(Stack stack) {
    float x = (mc.getMinecraftWindow().getBounds().getWidth() / 2);
    float y = (mc.getMinecraftWindow().getBounds().getHeight() / 2) - 0.5f;

    CrosshairFormRenderer formRenderer = this.labyAPI.getInjected(this.config.getAppearanceConfiguration().getType().getRendererClass());

    int color = this.config.getAppearanceConfiguration().getColor();

    if(this.mc.getTargetEntity() != null)
    {
      color = this.getTargetEntityColor();
    }else if(this.config.getSpecialConfiguration().isRainbow()) {
      color = this.calculateRainbowColor();
    }

    int outlineColor = this.config.getAppearanceConfiguration().getOutlineColor();
    float gap = this.calculateGap();


    if(this.config.getDotConfiguration().isEnabled()) {
      this.labyAPI.getInjected(CrosshairDotFormRenderer.class).render(stack,x, y, gap, color, outlineColor);
    }

    stack.push();
    stack.translate(x, y, 0);
    stack.rotate(this.config.getAppearanceConfiguration().getRotation(), 0, 0, 1);
    stack.translate(x * -1, y * -1, 0);

    formRenderer.render(stack, x, y, gap, color, outlineColor);
    stack.pop();
  }

  private int getTargetEntityColor() {
    Entity entity = this.mc.getTargetEntity();
    if(entity instanceof Player) {
      return this.config.getHighlightConfiguration().getHighlightPlayer();
    }

    TargetEntity targetEntity = (TargetEntity) entity;
    if(targetEntity.isHostile()) {
      return this.config.getHighlightConfiguration().getHighlightHostile();
    }

    return this.config.getHighlightConfiguration().getHighlightPassive();
  }

  private float calculateGap() {
    float gap = this.config.getAppearanceConfiguration().getGap();

    if(this.config.getSpecialConfiguration().isDynamicBow() && this.mc.getClientPlayer() != null && !mc.getClientPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
      ClientPlayer player = this.mc.getClientPlayer();
      ItemStack bowStack = null;

      if(player.getMainHandItemStack() != null && ((BowItemStack)player.getMainHandItemStack()).isBow()) {
        bowStack = player.getMainHandItemStack();
      }else if(player.getOffHandItemStack() != null && ((BowItemStack)player.getOffHandItemStack()).isBow()) {
        bowStack = player.getOffHandItemStack();
      }

      if(bowStack != null && player.getItemUseDurationTicks() > 0) {
        float dragging = (bowStack.getUseDuration() - (float)player.getItemUseDurationTicks()) / 10;
        if(dragging > 2 ) {
          dragging = 2;
        }

        gap += (1 + dragging) * 2.0F;
      }
    }
    return gap;
  }

  private int calculateRainbowColor() {
    float speed = this.config.getSpecialConfiguration().getRainbowSpeed() / 100.0F * this.rainbowTicks;

    int red = (int) (Math.sin(speed + 0.0F) * 127.0D + 128.0D);
    int green = (int) (Math.sin(speed + 2.0F) * 127.0D + 128.0D);
    int blue = (int) (Math.sin(speed + 4.0F) * 127.0D + 128.0D);

    return ColorUtil.toValue(red, green, blue);
  }

  public void tick() {
    if(this.rainbowTicks >= Integer.MAX_VALUE) {
      this.rainbowTicks = 0;
    }else{
      ++this.rainbowTicks;
    }
  }
}
