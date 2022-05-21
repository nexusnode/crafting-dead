/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.inventory;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractMenu extends AbstractContainerMenu {

  public static final int SLOT_SIZE = 18;

  @Nullable
  protected IItemHandler contents;
  protected Container playerInventory;

  public AbstractMenu(MenuType<?> type, int id, Container playerInventory,
      IItemHandler contents) {
    super(type, id);
    this.playerInventory = playerInventory;
    this.contents = contents;
  }

  protected void addPlayerInventorySlots() {
    if (this.playerInventory != null) {
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 9; j++) {
          this.addSlot(new Slot(this.playerInventory, j + i * 9 + 9, 8 + j * SLOT_SIZE,
              84 + i * SLOT_SIZE + this.getInventoryOffset()));
        }
      }

      // Hot bar
      for (int k = 0; k < 9; k++) {
        this.addSlot(
            new Slot(this.playerInventory, k, 8 + k * SLOT_SIZE, 142 + this.getInventoryOffset()));
      }
    }
  }

  public int getInventoryOffset() {
    return 0;
  }

  public int getContentsSize() {
    return this.contents == null ? 0 : this.contents.getSlots();
  }

  @Nullable
  public Container getPlayerInventory() {
    return this.playerInventory;
  }

  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);

    if (slot != null && slot.hasItem()) {
      ItemStack stack = slot.getItem();
      itemstack = stack.copy();

      if (index < this.getContentsSize()) {
        if (!this.moveItemStackTo(stack, this.getContentsSize(), this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(stack, 0, this.getContentsSize(), false)) {
        return ItemStack.EMPTY;
      }

      if (stack.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return itemstack;
  }
}
