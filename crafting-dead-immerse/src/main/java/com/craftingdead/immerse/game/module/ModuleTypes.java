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

package com.craftingdead.immerse.game.module;

import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.module.shop.ShopNetworkProtocol;
import com.craftingdead.immerse.game.network.NetworkProtocol;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ModuleTypes {

  public static final ResourceKey<Registry<ModuleType>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(new ResourceLocation(CraftingDeadImmerse.ID, "module_type"));

  public static final DeferredRegister<ModuleType> deferredRegister =
      DeferredRegister.create(REGISTRY_KEY, CraftingDeadImmerse.ID);

  public static final Supplier<IForgeRegistry<ModuleType>> registry =
      deferredRegister.makeRegistry(ModuleType.class, RegistryBuilder::new);

  public static final RegistryObject<ModuleType> TEAM =
      deferredRegister.register("team", () -> new ModuleType(NetworkProtocol.EMPTY));

  public static final RegistryObject<ModuleType> SHOP =
      deferredRegister.register("shop", () -> new ModuleType(ShopNetworkProtocol.INSTANCE));
}
