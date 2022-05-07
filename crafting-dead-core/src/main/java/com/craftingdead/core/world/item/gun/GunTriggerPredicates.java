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
import com.craftingdead.core.util.PredicateRegistryEntry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunTriggerPredicates {

  public static final ResourceKey<Registry<PredicateRegistryEntry<Gun>>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, "gun_trigger_predicate"));

  public static final DeferredRegister<PredicateRegistryEntry<Gun>> deferredRegister =
      DeferredRegister.create(REGISTRY_KEY, CraftingDead.ID);

  @SuppressWarnings("unchecked")
  public static final Supplier<IForgeRegistry<PredicateRegistryEntry<Gun>>> registry =
      deferredRegister.makeRegistry(
          (Class<PredicateRegistryEntry<Gun>>) (Class<?>) PredicateRegistryEntry.class,
          RegistryBuilder::new);

  public static final Codec<PredicateRegistryEntry<Gun>> CODEC =
      ExtraCodecs.lazyInitializedCodec(() -> registry.get().getCodec());

  public static final RegistryObject<PredicateRegistryEntry<Gun>> PERFORMING_SECONDARY_ACTION =
      deferredRegister.register("performing_secondary_actions",
          () -> PredicateRegistryEntry.of(Gun::isPerformingSecondaryAction));

  public static final RegistryObject<PredicateRegistryEntry<Gun>> NONE =
      deferredRegister.register("none", () -> PredicateRegistryEntry.of(gun -> true));
}
