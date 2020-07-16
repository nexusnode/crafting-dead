package com.craftingdead.core.capability.magazine;

import com.craftingdead.core.item.MagazineItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class DefaultMagazine implements IMagazine {

  private final MagazineItem magazineItem;
  private int size;

  public DefaultMagazine() {
    throw new UnsupportedOperationException("Specify magazine item");
  }

  public DefaultMagazine(MagazineItem magazineItem) {
    this.magazineItem = magazineItem;
    this.size = magazineItem.getSize();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("size", this.size);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.size = nbt.getInt("size");
  }

  @Override
  public float getArmorPenetration() {
    return this.magazineItem.getArmorPenetration();
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public void refill() {
    this.size = this.magazineItem.getSize();
  }

  @Override
  public void decrementSize() {
    this.size--;
  }

  @Override
  public ItemStack createIcon() {
    return new ItemStack(this.magazineItem);
  }

  @Override
  public Item getNextTier() {
    return this.magazineItem.getNextTier().get();
  }
}
