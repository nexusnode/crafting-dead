package com.craftingdead.survival.world.entity.extension;

import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.entity.extension.BasicLivingExtension;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DoctorZombieHandler extends ZombieHandler {

  public DoctorZombieHandler(BasicLivingExtension<Zombie> extension) {
    super(extension);
  }

  @SuppressWarnings("deprecation")
  @Override
  protected ItemStack createHeldItem() {
    return Registry.ITEM.getTag(ModItemTags.SYRINGES)
        .flatMap(set -> set.getRandomElement(this.extension.random()))
        .map(Holder::value)
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  @Override
  protected ItemStack createClothingItem() {
    return ModItems.DOCTOR_CLOTHING.get().getDefaultInstance();
  }

  @Override
  protected ItemStack getHatStack() {
    return ModItems.DOCTOR_MASK.get().getDefaultInstance();
  }
}
