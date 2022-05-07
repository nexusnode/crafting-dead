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

package com.craftingdead.core.util;

import java.util.function.Function;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class FunctionRegistryEntry<T, R> extends ForgeRegistryEntry<FunctionRegistryEntry<T, R>>
    implements Function<T, R> {

  private final Function<T, R> fun;

  public FunctionRegistryEntry(Function<T, R> fun) {
    this.fun = fun;
  }

  @Override
  public R apply(T type) {
    return fun.apply(type);
  }

  public static <T, R> FunctionRegistryEntry<T, R> of(Function<T, R> fun) {
    return new FunctionRegistryEntry<>(fun);
  }

  // Work around type erasure
  @SuppressWarnings("unchecked")
  public static <T, R> Class<FunctionRegistryEntry<T, R>> asClass() {
    return (Class<FunctionRegistryEntry<T, R>>) (Class<?>) FunctionRegistryEntry.class;
  }
}
