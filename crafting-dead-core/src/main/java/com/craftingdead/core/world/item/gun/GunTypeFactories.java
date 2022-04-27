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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.gun.aimable.AimableGunType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunTypeFactories {

  public static final DeferredRegister<GunTypeFactory> gunTypeFactories =
      DeferredRegister.create(GunTypeFactory.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<GunTypeFactory>> registry =
      Lazy.of(gunTypeFactories.makeRegistry("gun_type_factory", RegistryBuilder::new));

  public static final RegistryObject<GunTypeFactory> SIMPLE = gunTypeFactories.register("simple",
      () -> new GunTypeFactory(GunType.DIRECT_CODEC));

  public static final RegistryObject<GunTypeFactory> AIMABLE = gunTypeFactories.register("aimable",
      () -> new GunTypeFactory(AimableGunType.DIRECT_CODEC));
}
