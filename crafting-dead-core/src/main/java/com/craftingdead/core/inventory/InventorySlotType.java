package com.craftingdead.core.inventory;

public enum InventorySlotType {

  MELEE(0), GUN(1), HAT(2), CLOTHING(3), VEST(4);

  private final int index;

  private InventorySlotType(int index) {
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }
}
