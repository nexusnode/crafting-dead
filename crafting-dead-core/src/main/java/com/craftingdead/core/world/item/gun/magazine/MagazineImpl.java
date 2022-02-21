/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun.magazine;

import java.util.concurrent.atomic.AtomicInteger;
import com.craftingdead.core.world.item.MagazineItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class MagazineImpl implements Magazine {

  private final MagazineItem magazineItem;
  private final AtomicInteger size;
  private boolean dirty;

  public MagazineImpl(MagazineItem magazineItem) {
    this.magazineItem = magazineItem;
    this.size = new AtomicInteger(magazineItem.getSize());
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putInt("size", this.size.get());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
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
  public int getMaxSize() {
    return this.magazineItem.getSize();
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    out.writeVarInt(this.size.get());
    this.dirty = false;
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.size.set(in.readVarInt());
  }

  @Override
  public boolean requiresSync() {
    return this.dirty;
  }
}
