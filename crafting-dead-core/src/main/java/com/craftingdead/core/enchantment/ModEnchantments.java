/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.enchantment;

import com.craftingdead.core.CraftingDead;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CraftingDead.ID);

  public static final RegistryObject<InfectionEnchantment> INFECTION = ENCHANTMENTS
      .register("infection",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
  public static final RegistryObject<ArmorPenetrationEnchantment> ARMOR_PENETRATION = ENCHANTMENTS
      .register("armor_penetration",
          () -> new ArmorPenetrationEnchantment(Enchantment.Rarity.COMMON,
              EquipmentSlotType.MAINHAND));
}
