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

package com.craftingdead.core.world.inventory.storage;

import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerStorage extends ItemStackHandler implements Storage {

  private final int size;
  private final ModEquipmentSlot slotType;
  private final ItemHandlerMenuConstructor containerProvider;

  public ItemStackHandlerStorage() {
    this(1, null, (windowId, inventory, itemHandler) -> null);
  }

  public ItemStackHandlerStorage(int size, ModEquipmentSlot slotType,
      ItemHandlerMenuConstructor constructor) {
    super(size);
    this.size = size;
    this.slotType = slotType;
    this.containerProvider = constructor;
  }

  @Override
  protected void onLoad() {
    if (this.getSlots() != this.size) {
      this.setSize(this.size);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
    return this.containerProvider.createMenu(windowId, inventory, this);
  }

  @Override
  public boolean isValidForSlot(ModEquipmentSlot slotType) {
    return slotType == this.slotType;
  }

  @FunctionalInterface
  public interface ItemHandlerMenuConstructor {

    @Nullable
    AbstractContainerMenu createMenu(int windowId, Inventory player, IItemHandler itemHandler);
  }
}
