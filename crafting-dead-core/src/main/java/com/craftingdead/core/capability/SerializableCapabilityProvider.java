/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class SerializableCapabilityProvider<C extends INBTSerializable<S>, S extends INBT>
    extends SimpleCapabilityProvider<C> implements INBTSerializable<S> {

  public SerializableCapabilityProvider(C capability, Supplier<Capability<? super C>> capabilityHolder) {
    super(capability, capabilityHolder);
  }

  public SerializableCapabilityProvider(C capability, Set<Supplier<Capability<? super C>>> capabilityHolder) {
    super(capability, capabilityHolder);
  }

  @Override
  public S serializeNBT() {
    return this.capability.serializeNBT();
  }

  @Override
  public void deserializeNBT(S nbt) {
    this.capability.deserializeNBT(nbt);
  }
}
