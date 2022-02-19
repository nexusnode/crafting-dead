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

package com.craftingdead.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class Util {

  public static <T> Optional<T> optional(@Nullable Supplier<T> supplier) {
    return Optional.ofNullable(supplier).map(Supplier::get);
  }

  public static <T> Supplier<T> supply(T value) {
    return () -> value;
  }

  public static <T> Codec<Set<T>> setOf(Codec<T> codec) {
    return codec.listOf().xmap(HashSet::new, ArrayList::new);
  }

  public static <T> ResourceKey<T> createKey(
      ResourceKey<? extends Registry<T>> registryKey, String name) {
    return ResourceKey.create(registryKey, new ResourceLocation(CraftingDead.ID, name));
  }

  public static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
    return ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, name));
  }
}
