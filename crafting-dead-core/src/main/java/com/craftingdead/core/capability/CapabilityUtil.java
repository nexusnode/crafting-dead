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

package com.craftingdead.core.capability;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;

public class CapabilityUtil {

  public static <T extends CapabilityProvider<T>> Predicate<T> capabilityPresent(
      Capability<?> capability) {
    return provider -> provider.getCapability(capability).isPresent();
  }

  public static <T, R extends T> R getOrThrow(Capability<T> capability,
      ICapabilityProvider provider, Class<R> clazz) {
    final var value = get(capability, provider, clazz);
    if (value == null) {
      throw new IllegalStateException("Expecting capability: " + capability.getName());
    }
    return value;
  }

  @Nullable
  public static <T, R extends T> R get(Capability<T> capability,
      ICapabilityProvider provider, Class<R> clazz) {
    final var value = provider.getCapability(capability).orElse(null);
    return clazz.isInstance(value) ? clazz.cast(value) : null;
  }

  @SafeVarargs
  public static <C extends INBTSerializable<CompoundTag>> ICapabilityProvider serializableProvider(
      NonNullSupplier<C> instanceSupplier, Capability<? super C>... capabilities) {
    return serializableProvider(CompoundTag::new, instanceSupplier, capabilities);
  }

  @SafeVarargs
  public static <C extends INBTSerializable<T>, T extends Tag> ICapabilityProvider serializableProvider(
      Supplier<T> emptyTag, NonNullSupplier<C> instanceSupplier,
      Capability<? super C>... capabilities) {
    return serializableProvider(emptyTag, LazyOptional.of(instanceSupplier),
        ReferenceSet.of(capabilities), null);
  }

  @SafeVarargs
  public static <C extends INBTSerializable<T>, T extends Tag> ICapabilityProvider serializableProvider(
      Supplier<T> emptyTag, LazyOptional<C> instance, Capability<? super C>... capabilities) {
    return serializableProvider(emptyTag, instance, ReferenceSet.of(capabilities), null);
  }

  public static <C extends INBTSerializable<T>, T extends Tag> ICapabilityProvider serializableProvider(
      Supplier<T> emptyTag, LazyOptional<C> instance, Set<Capability<? super C>> capabilities,
      @Nullable NonNullFunction<C, ICapabilityProvider> instanceMapper) {
    return new SerializableCapabilityProvider<>(emptyTag, instance, capabilities, instanceMapper);
  }

  @SafeVarargs
  public static <C> ICapabilityProvider provider(NonNullSupplier<C> instanceSupplier,
      Capability<? super C>... capabilities) {
    return provider(LazyOptional.of(instanceSupplier), ReferenceSet.of(capabilities), null);
  }

  @SafeVarargs
  public static <C> ICapabilityProvider provider(LazyOptional<C> instance,
      Capability<? super C>... capabilities) {
    return provider(instance, ReferenceSet.of(capabilities), null);
  }

  public static <C> ICapabilityProvider provider(LazyOptional<C> instance,
      Set<Capability<? super C>> capabilities,
      @Nullable NonNullFunction<C, ICapabilityProvider> instanceMapper) {
    return new SimpleCapabilityProvider<>(instance, capabilities, instanceMapper);
  }
}
