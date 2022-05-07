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

package com.craftingdead.core.world.item.gun;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.gun.aimable.AimableGunType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunTypeFactories {

  public static final ResourceKey<Registry<GunTypeFactory>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, "gun_type_factory"));

  public static final DeferredRegister<GunTypeFactory> deferredRegister =
      DeferredRegister.create(REGISTRY_KEY, CraftingDead.ID);

  public static final Supplier<IForgeRegistry<GunTypeFactory>> registry =
      deferredRegister.makeRegistry(GunTypeFactory.class, RegistryBuilder::new);

  public static final RegistryObject<GunTypeFactory> SIMPLE = deferredRegister.register("simple",
      () -> new GunTypeFactory(GunType.DIRECT_CODEC));

  public static final RegistryObject<GunTypeFactory> AIMABLE = deferredRegister.register("aimable",
      () -> new GunTypeFactory(AimableGunType.DIRECT_CODEC));
}
