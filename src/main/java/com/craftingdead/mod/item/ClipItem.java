package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class ClipItem extends Item {

  private final int size;

  public ClipItem(Properties properties, int size) {
    super(properties);
    this.size = size;
  }

  public int getSize() {
    return this.size;
  }
}
