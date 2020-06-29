package com.craftingdead.core.entity.monster;

import com.craftingdead.core.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class PoliceZombieEntity extends AdvancedZombieEntity {

  public PoliceZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected Item getMelee() {
    return ModItems.G18.get();
  }

  @Override
  protected Item getClothing() {
    return ModItems.POLICE_CLOTHING.get();
  }

  @Override
  protected Item getHat() {
    return null;
  }
}
