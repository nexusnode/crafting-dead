package com.craftingdead.mod.entity.monster;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;

public class GiantZombieEntity extends AdvancedZombieEntity {

  public GiantZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world,
      IItemProvider heldItem, IItemProvider clothingItem, IItemProvider hatItem) {
    super(type, world, heldItem, clothingItem, hatItem);
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
