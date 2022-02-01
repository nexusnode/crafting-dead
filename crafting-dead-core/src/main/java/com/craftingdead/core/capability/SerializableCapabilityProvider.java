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
