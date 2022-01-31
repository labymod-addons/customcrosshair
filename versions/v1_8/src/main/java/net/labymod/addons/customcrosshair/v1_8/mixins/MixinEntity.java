package net.labymod.addons.customcrosshair.v1_8.mixins;

import net.labymod.addons.customcrosshair.client.entity.TargetEntity;
import net.labymod.addons.customcrosshair.client.world.item.BowItemStack;
import net.labymod.api.models.Implement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
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
