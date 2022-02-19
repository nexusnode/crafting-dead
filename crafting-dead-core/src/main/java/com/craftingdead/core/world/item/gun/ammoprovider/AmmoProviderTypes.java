/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun.ammoprovider;

import com.craftingdead.core.CraftingDead;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class AmmoProviderTypes {

  public static final DeferredRegister<AmmoProviderType> AMMO_PROVIDER_TYPES =
      DeferredRegister.create(AmmoProviderType.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<AmmoProviderType>> registry =
      Lazy.of(AmmoProviderTypes.AMMO_PROVIDER_TYPES.makeRegistry("ammo_provider_type",
          RegistryBuilder::new));

  public static final RegistryObject<AmmoProviderType> MAGAZINE =
      AMMO_PROVIDER_TYPES.register("magazine",
          () -> new AmmoProviderType(MagazineAmmoProvider::new));

  public static final RegistryObject<AmmoProviderType> REFILLABLE =
      AMMO_PROVIDER_TYPES.register("refillable",
          () -> new AmmoProviderType(RefillableAmmoProvider::new));
}
