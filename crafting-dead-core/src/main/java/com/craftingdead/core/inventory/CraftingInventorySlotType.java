package com.craftingdead.core.inventory;

public enum CraftingInventorySlotType {
  UNDERBARREL_ATTACHMENT(0), OVERBARREL_ATTACHMENT(1), MUZZLE_ATTACHMENT(2), PAINT(3);

  private final int index;

  private CraftingInventorySlotType(int index) {
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }
}
