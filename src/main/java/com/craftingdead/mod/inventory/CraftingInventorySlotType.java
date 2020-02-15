package com.craftingdead.mod.inventory;

import java.util.Set;
import com.google.common.collect.ImmutableSet;

public enum CraftingInventorySlotType {
  GUN(0), UNDERBARREL_ATTACHMENT(1), OVERBARREL_ATTACHMENT(2), MUZZLE_ATTACHMENT(3), PAINT(4);

  public static final Set<CraftingInventorySlotType> ADDON_SLOTS =
      ImmutableSet.of(UNDERBARREL_ATTACHMENT, OVERBARREL_ATTACHMENT, MUZZLE_ATTACHMENT, PAINT);

  private final int index;

  private CraftingInventorySlotType(int index) {
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }
}
