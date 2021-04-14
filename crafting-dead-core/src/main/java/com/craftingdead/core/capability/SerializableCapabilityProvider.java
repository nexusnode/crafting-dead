/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.capability;

import java.util.Set;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;

public class SerializableCapabilityProvider<C extends INBTSerializable<S>, S extends INBT>
    extends SimpleCapabilityProvider<C> implements INBTSerializable<S> {

  private final Supplier<S> emptyNbt;

  public SerializableCapabilityProvider(LazyOptional<C> capability,
      Supplier<Capability<? super C>> capabilityHolder, Supplier<S> emptyNbt) {
    super(capability, ImmutableSet.of(capabilityHolder));
    this.emptyNbt = emptyNbt;
  }

  public SerializableCapabilityProvider(LazyOptional<C> capability,
      Set<Supplier<Capability<? super C>>> capabilityHolder, Supplier<S> emptyNbt) {
    super(capability, capabilityHolder, null);
    this.emptyNbt = emptyNbt;
  }

  public SerializableCapabilityProvider(LazyOptional<C> instance,
      Set<Supplier<Capability<? super C>>> capabilities,
      NonNullFunction<C, ICapabilityProvider> instanceMapper, Supplier<S> emptyNbt) {
    super(instance, capabilities, instanceMapper);
    this.emptyNbt = emptyNbt;
  }

  @Override
  public S serializeNBT() {
    return this.instance.map(C::serializeNBT).orElseGet(this.emptyNbt);
  }

  @Override
  public void deserializeNBT(S nbt) {
    this.instance.ifPresent(i -> i.deserializeNBT(nbt));
  }
}
