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

package com.craftingdead.immerse.world.item.hydration;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@FunctionalInterface
public interface Hydration {

  Capability<Hydration> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  /**
   * @see {@link net.minecraftforge.event.AttachCapabilitiesEvent}
   */
  ResourceLocation CAPABILITY_KEY = new ResourceLocation(CraftingDeadImmerse.ID, "hydration");

  Hydration POTION = itemStack -> PotionUtils.getPotion(itemStack) == Potions.WATER ? 5 : 0;

  int getWater(ItemStack itemStack);

  static Hydration fixed(int water) {
    return __ -> water;
  }
}
