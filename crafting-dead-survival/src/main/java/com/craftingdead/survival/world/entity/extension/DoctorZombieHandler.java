/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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
