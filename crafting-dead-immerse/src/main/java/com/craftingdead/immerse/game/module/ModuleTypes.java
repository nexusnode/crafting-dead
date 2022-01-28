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

package com.craftingdead.immerse.game.module;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.module.shop.ShopNetworkProtocol;
import com.craftingdead.immerse.game.network.NetworkProtocol;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ModuleTypes {

  public static final DeferredRegister<ModuleType> MODULE_TYPES =
      DeferredRegister.create(ModuleType.class, CraftingDeadImmerse.ID);

  public static final Lazy<IForgeRegistry<ModuleType>> REGISTRY =
      Lazy.of(MODULE_TYPES.makeRegistry("module_types", RegistryBuilder::new));

  public static final RegistryObject<ModuleType> TEAM =
      MODULE_TYPES.register("team", () -> new ModuleType(NetworkProtocol.EMPTY));

  public static final RegistryObject<ModuleType> SHOP =
      MODULE_TYPES.register("shop", () -> new ModuleType(ShopNetworkProtocol.INSTANCE));
}
