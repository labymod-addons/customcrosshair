package net.labymod.addons.customcrosshair.v1_17.mixins;

import net.labymod.addons.customcrosshair.client.world.item.BowItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ItemStack.class})
@Implements({@Interface(
    iface = BowItemStack.class,
    prefix = "bowitemStack$",
    remap = Remap.NONE
)})
public abstract class MixinItemStack implements BowItemStack {

  @Shadow
  public abstract Item getItem();

  @Override
  public boolean isBow() {
    return this.getItem() == Items.BOW || this.getItem() == Items.CROSSBOW;
  }
}
