package com.craftingdead.mod.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class TankZombieEntity extends AdvancedZombieEntity {

  public TankZombieEntity(EntityType<? extends TankZombieEntity> type, World world) {
    super(type, world);
  }

  public TankZombieEntity(World world) {
    super(world);
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.19D);
    this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(15.0D);
  }
}
