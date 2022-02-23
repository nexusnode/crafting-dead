/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.world.level.extension;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class LandOwnerTypes {

  public static final DeferredRegister<LandOwnerType> landOwnerTypes =
      DeferredRegister.create(LandOwnerType.class, CraftingDeadImmerse.ID);

  public static final Lazy<IForgeRegistry<LandOwnerType>> registry =
      Lazy.of(landOwnerTypes.makeRegistry("land_owner_type", RegistryBuilder::new));

  public static final RegistryObject<LandOwnerType> LEGACY_BASE =
      landOwnerTypes.register("legacy_base",
          () -> new LandOwnerType(LegacyBase.CODEC, LegacyBase.CODEC));

}
