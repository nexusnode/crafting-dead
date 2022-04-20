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
import java.util.function.Supplier;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;

class SerializableCapabilityProvider<C extends INBTSerializable<T>, T extends Tag>
    extends SimpleCapabilityProvider<C> implements INBTSerializable<T> {

  private final Supplier<T> emptyTag;

  public SerializableCapabilityProvider(
      Supplier<T> emptyTag, LazyOptional<C> instance,
      Set<Capability<? super C>> capabilities,
      NonNullFunction<C, ICapabilityProvider> instanceMapper) {
    super(instance, capabilities, instanceMapper);
    this.emptyTag = emptyTag;
  }

  @Override
  public T serializeNBT() {
    return this.instance.map(C::serializeNBT).orElseGet(this.emptyTag);
  }

  @Override
  public void deserializeNBT(T tag) {
    this.instance.ifPresent(i -> i.deserializeNBT(tag));
  }
}
