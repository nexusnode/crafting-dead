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

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class EmptyStorage<C> implements Capability.IStorage<C> {

  private static final EmptyStorage<? extends Object> INSTANCE = new EmptyStorage<>();

  @SuppressWarnings("unchecked")
  public static <C> EmptyStorage<C> getInstance() {
    return (EmptyStorage<C>) INSTANCE;
  }

  @Override
  public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
    return null;
  }

  @Override
  public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {}
}
