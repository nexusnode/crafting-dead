package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class MagazineItem extends Item {

  private final Magazine magazine;

  public MagazineItem(Properties properties, Magazine magazine) {
    super(properties);
    this.magazine = magazine;
  }

  public Magazine getMagazine() {
    return new Magazine(this.magazine);
  }
}
