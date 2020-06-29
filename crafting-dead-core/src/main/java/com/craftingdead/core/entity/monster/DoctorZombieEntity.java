package com.craftingdead.core.entity.monster;

import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.tags.ModItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DoctorZombieEntity extends AdvancedZombieEntity {

  public DoctorZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected Item getMelee() {
    return this.getRandomItem(ModItemTags.SYRINGES::contains, 1.0F);
  }

  @Override
  protected Item getClothing() {
    return ModItems.DOCTOR_CLOTHING.get();
  }

  @Override
  protected Item getHat() {
    return ModItems.DOCTOR_MASK.get();
  }
}
