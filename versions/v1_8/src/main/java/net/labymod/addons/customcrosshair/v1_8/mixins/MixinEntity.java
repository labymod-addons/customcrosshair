package net.labymod.addons.customcrosshair.v1_8.mixins;

import net.labymod.addons.customcrosshair.client.entity.TargetEntity;
import net.labymod.addons.customcrosshair.client.world.item.BowItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({net.minecraft.entity.Entity.class})
@Implements({@Interface(
    iface = BowItemStack.class,
    prefix = "targetEntity$",
    remap = Remap.NONE
)})
public abstract class MixinEntity implements TargetEntity {

  @Override
  public boolean isHostile() {
    return false; /** !(this instanceof EntityBat) && !(this instanceof EntityChicken) && !(this instanceof EntityCow) && !(this instanceof EntityHorse)
     && !(this instanceof EntityOcelot) && !(this instanceof EntityPig) && !(this instanceof EntityRabbit) && !(this instanceof EntitySheep)
     && !(this instanceof EntitySquid) && !(this instanceof EntityVillager) && !(this instanceof EntityWolf);**/
  }
}
