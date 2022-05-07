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

package com.craftingdead.survival.world.item.enchantment;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalEnchantments {

  public static final DeferredRegister<Enchantment> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CraftingDeadSurvival.ID);

  public static final RegistryObject<InfectionEnchantment> INFECTION =
      deferredRegister.register("infection",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
}
