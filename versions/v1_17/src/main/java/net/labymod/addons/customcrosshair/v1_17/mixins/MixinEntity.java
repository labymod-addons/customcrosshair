package net.labymod.addons.customcrosshair.v1_17.mixins;

import net.labymod.addons.customcrosshair.client.entity.TargetEntity;
import net.labymod.addons.customcrosshair.client.world.item.BowItemStack;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({net.minecraft.world.entity.Entity.class})
@Implements({@Interface(
    iface = BowItemStack.class,
    prefix = "targetEntity$",
    remap = Remap.NONE
)})
public abstract class MixinEntity implements TargetEntity {

  @Override
  public boolean isHostile() {
    return !Bat.class.isInstance(this) && !Animal.class.isInstance(this);
  }

}
