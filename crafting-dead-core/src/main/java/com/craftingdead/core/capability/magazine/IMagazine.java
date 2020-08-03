package com.craftingdead.core.capability.magazine;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMagazine extends INBTSerializable<CompoundNBT> {

  float getArmorPenetration();

  int getSize();

  void setSize(int size);

  void refill();

  default boolean isEmpty() {
    return this.getSize() == 0;
  }

  void decrementSize();

  Item getNextTier();

  boolean hasCustomTexture();
}
