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

package com.craftingdead.core.capability.gun;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.util.IBufferSerializable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;

public class AmmoReserve implements INBTSerializable<CompoundNBT>, IBufferSerializable {

  private ItemStack magazineStack;
  private int size;
  private boolean dirty;

  public AmmoReserve(ItemStack magazineStack) {
    this.magazineStack = magazineStack;
  }

  public boolean tryAdd(ItemStack magazineStack) {
    if (this.magazineStack.getItem() == magazineStack.getItem()
        && magazineStack.getCapability(ModCapabilities.MAGAZINE)
            .map(magazine -> this.size += magazine.getSize())
            .isPresent()) {
      return this.dirty = true;
    }
    return false;
  }

  public ItemStack remove() {
    int size = this.size -= Math.min(this.size, getMagazine(this.magazineStack).getMaxSize());
    this.dirty = true;
    if (size == 0) {
      return ItemStack.EMPTY;
    }
    ItemStack newStack = this.magazineStack.copy();
    getMagazine(newStack).setSize(size);
    return newStack;
  }

  private static IMagazine getMagazine(ItemStack magazineStack) {
    return magazineStack.getCapability(ModCapabilities.MAGAZINE)
        .orElseThrow(() -> new IllegalStateException("No magazine capability"));
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    out.writeVarInt(this.size);
    this.dirty = false;
  }

  @Override
  public void decode(PacketBuffer in) {
    this.size = in.readVarInt();
  }

  @Override
  public boolean requiresSync() {
    return this.dirty;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("size", this.size);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    
  }
}
