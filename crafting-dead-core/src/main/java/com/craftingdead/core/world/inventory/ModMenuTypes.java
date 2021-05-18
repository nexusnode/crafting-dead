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

package com.craftingdead.core.world.inventory;

import com.craftingdead.core.CraftingDead;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuTypes {

  public static final DeferredRegister<ContainerType<?>> MENUS =
      DeferredRegister.create(ForgeRegistries.CONTAINERS, CraftingDead.ID);

  public static final RegistryObject<ContainerType<EquipmentMenu>> EQUIPMENT =
      MENUS.register("equipment", () -> new ContainerType<>(EquipmentMenu::new));

  public static final RegistryObject<ContainerType<GenericMenu>> VEST =
      MENUS.register("vest", () -> new ContainerType<>(GenericMenu::createVest));
}
