package com.craftingdead.mod.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class WeakZombieEntity extends AdvancedZombieEntity {

  public WeakZombieEntity(EntityType<? extends WeakZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(25.0D);
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
    this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
  }
}
