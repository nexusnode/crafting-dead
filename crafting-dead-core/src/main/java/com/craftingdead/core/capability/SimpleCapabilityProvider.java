/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.capability;

import java.util.Set;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SimpleCapabilityProvider<C> implements ICapabilityProvider {

  protected final C capability;
  protected final Set<Supplier<Capability<? super C>>> capabilityHolder;

  public SimpleCapabilityProvider(C capability, Supplier<Capability<? super C>> capabilityHolder) {
    this(capability, ImmutableSet.of(capabilityHolder));
  }

  public SimpleCapabilityProvider(C capability, Set<Supplier<Capability<? super C>>> capabilityHolder) {
    this.capability = capability;
    this.capabilityHolder = capabilityHolder;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return cap != null ? this.capabilityHolder.stream().map(Supplier::get).anyMatch(cap::equals)
        ? LazyOptional.of(() -> this.capability).cast()
        : LazyOptional.empty() : LazyOptional.empty();
  }
}
