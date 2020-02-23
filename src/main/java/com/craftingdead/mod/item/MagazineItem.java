package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class MagazineItem extends Item {

  private final int size;

  public MagazineItem(Properties properties, int size) {
    super(properties);
    this.size = size;
  }

  public int getSize() {
    return this.size;
  }
}
