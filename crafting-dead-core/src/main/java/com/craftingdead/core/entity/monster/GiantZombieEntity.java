package com.craftingdead.core.entity.monster;

import com.craftingdead.core.item.ModItems;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GiantZombieEntity extends AdvancedZombieEntity {

  public GiantZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected Item getMelee() {
    return ModItems.M4A1.get();
  }

  @Override
  protected Item getClothing() {
    return ModItems.ARMY_CLOTHING.get();
  }

  @Override
  protected Item getHat() {
    return ModItems.ARMY_HELMET.get();
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
  }

  @Override
  protected float getStandingEyeHeight(Pose pose, EntitySize entitySize) {
    return 10.440001F;
  }
}
