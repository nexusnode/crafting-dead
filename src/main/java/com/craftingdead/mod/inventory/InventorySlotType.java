package com.craftingdead.mod.inventory;

public enum InventorySlotType {

  MELEE(0), GUN(1), BACKPACK(2), HAT(3), CLOTHING(4), VEST(5);

  private final int index;

  private InventorySlotType(int index) {
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }
}
