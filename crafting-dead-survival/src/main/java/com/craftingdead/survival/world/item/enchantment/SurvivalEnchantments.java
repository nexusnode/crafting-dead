/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.survival.world.item.enchantment;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CraftingDeadSurvival.ID);

  public static final RegistryObject<InfectionEnchantment> INFECTION =
      ENCHANTMENTS.register("infection",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
}
