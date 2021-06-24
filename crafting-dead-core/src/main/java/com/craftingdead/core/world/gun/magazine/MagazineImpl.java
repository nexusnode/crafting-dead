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

package com.craftingdead.core.world.gun.magazine;

import java.util.concurrent.atomic.AtomicInteger;
import com.craftingdead.core.world.item.MagazineItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class MagazineImpl implements Magazine {

  private final MagazineItem magazineItem;
  private final AtomicInteger size;
  private boolean dirty;

  public MagazineImpl(MagazineItem magazineItem) {
    this.magazineItem = magazineItem;
    this.size = new AtomicInteger(magazineItem.getSize());
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("size", this.size.get());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.size.set(nbt.getInt("size"));
  }

  @Override
  public float getArmorPenetration() {
    return this.magazineItem.getArmorPenetration();
  }

  @Override
  public int getSize() {
    return this.size.get();
  }

  @Override
  public void setSize(int size) {
    this.dirty = true;
    this.size.set(size);
  }

  @Override
  public void refill() {
    this.dirty = true;
    this.size.set(this.magazineItem.getSize());
  }

  @Override
  public int decrementSize() {
    this.dirty = true;
    return this.size.decrementAndGet();
  }

  @Override
  public boolean hasCustomTexture() {
    return this.magazineItem.hasCustomTexture();
  }

  @Override
  public int getMaxSize() {
    return this.magazineItem.getSize();
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    out.writeVarInt(this.size.get());
    this.dirty = false;
  }

  @Override
  public void decode(PacketBuffer in) {
    this.size.set(in.readVarInt());
  }

  @Override
  public boolean requiresSync() {
    return this.dirty;
  }
}
