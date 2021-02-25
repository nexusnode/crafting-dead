/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.inventory.container;

import java.util.function.BiPredicate;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.HatItem;
import com.craftingdead.core.item.MeleeWeaponItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class EquipmentContainer extends Container {

  private final IItemHandler itemHandler;

  private final CraftResultInventory outputInventory = new CraftResultInventory();
  private final Inventory craftingInventory = new Inventory(4);

  public EquipmentContainer(int windowId, PlayerInventory playerInventory) {
    super(ModContainerTypes.EQUIPMENT.get(), windowId);
    this.itemHandler = playerInventory.player.getCapability(ModCapabilities.LIVING)
        .orElseThrow(() -> new IllegalStateException("No living capability")).getItemHandler();
    this.craftingInventory.addListener(this::onCraftMatrixChanged);

    final int slotSize = 18;

    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlot(
            new Slot(playerInventory, x + (y + 1) * 9, 8 + x * slotSize, 84 + y * slotSize));
      }
    }

    for (int x = 0; x < 9; ++x) {
      this.addSlot(new Slot(playerInventory, x, 8 + x * slotSize, 142));
    }

    int equipmentColumnX = 8 + (slotSize * 3);
    int equipmentColumnY = 8;

    this.addSlot(new PredicateItemHandlerSlot(this.itemHandler, InventorySlotType.GUN.getIndex(),
        equipmentColumnX, equipmentColumnY,
        (slot, itemStack) -> itemStack.getCapability(ModCapabilities.GUN).isPresent()));

    this.addSlot(new PredicateItemHandlerSlot(this.itemHandler, InventorySlotType.MELEE.getIndex(),
        equipmentColumnX, equipmentColumnY += slotSize,
        (slot, itemStack) -> itemStack.getItem() instanceof MeleeWeaponItem));

    this.addSlot(new PredicateItemHandlerSlot(this.itemHandler, InventorySlotType.HAT.getIndex(),
        equipmentColumnX, equipmentColumnY += slotSize,
        (slot, itemStack) -> itemStack.getItem() instanceof HatItem));

    this.addSlot(new PredicateItemHandlerSlot(this.itemHandler,
        InventorySlotType.CLOTHING.getIndex(), equipmentColumnX, equipmentColumnY += slotSize,
        (slot, itemStack) -> itemStack.getCapability(ModCapabilities.CLOTHING).isPresent()));

    this.addSlot(new PredicateItemHandlerSlot(this.itemHandler, InventorySlotType.VEST.getIndex(),
        equipmentColumnX + slotSize, equipmentColumnY, (slot, itemStack) -> itemStack
            .getCapability(ModCapabilities.STORAGE)
            .map(storage -> storage.isValidForSlot(InventorySlotType.VEST))
            .orElse(false)));

    final int gunCraftSlotGap = 3;
    final int gunCraftY = 8;

    this.addSlot(new GunCraftSlot(this.outputInventory, 0, 125, gunCraftY + slotSize + 3,
        this.craftingInventory));

    final BiPredicate<PredicateSlot, ItemStack> attachmentAndPaintPredicate =
        (slot, itemStack) -> this.getGunStack().getCapability(ModCapabilities.GUN)
            .map(gun -> gun.isAcceptedPaintOrAttachment(itemStack)).orElse(false);
    final BiPredicate<PredicateSlot, ItemStack> attachmentPredicate =
        (slot, itemStack) -> itemStack.getItem() instanceof AttachmentItem
            && ((AttachmentItem) itemStack.getItem()).getInventorySlot().getIndex() == slot
                .getSlotIndex();

    this.addSlot(new PredicateSlot(this.craftingInventory,
        CraftingInventorySlotType.MUZZLE_ATTACHMENT.getIndex(), 104,
        gunCraftY + slotSize + gunCraftSlotGap,
        attachmentPredicate.and(attachmentAndPaintPredicate)));
    this.addSlot(new PredicateSlot(this.craftingInventory,
        CraftingInventorySlotType.UNDERBARREL_ATTACHMENT.getIndex(), 125,
        gunCraftY + slotSize * 2 + gunCraftSlotGap * 2,
        attachmentPredicate.and(attachmentAndPaintPredicate)));
    this.addSlot(new PredicateSlot(this.craftingInventory,
        CraftingInventorySlotType.OVERBARREL_ATTACHMENT.getIndex(), 125, gunCraftY,
        attachmentPredicate.and(attachmentAndPaintPredicate)));

    final BiPredicate<PredicateSlot, ItemStack> paintPredicate =
        (slot, itemStack) -> itemStack.getCapability(ModCapabilities.PAINT).isPresent();

    this.addSlot(new PredicateSlot(this.craftingInventory,
        CraftingInventorySlotType.PAINT.getIndex(), 146, gunCraftY + slotSize + gunCraftSlotGap,
        paintPredicate.and(attachmentAndPaintPredicate)));
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerEntity) {
    return true;
  }

  @Override
  public void onContainerClosed(PlayerEntity playerEntity) {
    super.onContainerClosed(playerEntity);
    if (!playerEntity.getEntityWorld().isRemote()) {
      this.clearContainer(playerEntity, playerEntity.getEntityWorld(), this.craftingInventory);
      this.clearContainer(playerEntity, playerEntity.getEntityWorld(), this.outputInventory);
    }
  }

  public IItemHandler getItemHandler() {
    return this.itemHandler;
  }

  public ItemStack getGunStack() {
    return this.outputInventory.getStackInSlot(0);
  }

  public boolean isCraftingInventoryEmpty() {
    return this.craftingInventory.isEmpty();
  }

  public boolean isCraftable() {
    return this.getGunStack().getCapability(ModCapabilities.GUN).map(gunController -> {
      for (int i = 0; i < this.craftingInventory.getSizeInventory(); i++) {
        ItemStack itemStack = this.craftingInventory.getStackInSlot(i);
        if (!itemStack.isEmpty() && !gunController.isAcceptedPaintOrAttachment(itemStack)) {
          return false;
        }
      }
      return true;
    }).orElse(false);
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerEntity, int clickedIndex) {
    Slot clickedSlot = this.inventorySlots.get(clickedIndex);
    if (clickedSlot != null && clickedSlot.getHasStack()) {
      // Shift-clicking gun crafting slots is currently unavailable due to
      // the nature of how itemstacks are merged when shift-clicking.
      // In other words, attachments do not get attached.
      if (clickedSlot instanceof GunCraftSlot) {
        return ItemStack.EMPTY;
      }

      ItemStack clickedStack = clickedSlot.getStack();

      if (clickedIndex < 27) {
        // Pushes the clicked stack to the higher slots in a "negative direction"
        if (!this.mergeItemStack(clickedStack, 27, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else {
        // Pushes the clicked stack to the lower slots in a "positive direction"
        if (!this.mergeItemStack(clickedStack, 0, 27, false)) {
          return ItemStack.EMPTY;
        }
      }

      // From vanilla code. Seems to be mandatory.
      if (clickedStack.isEmpty()) {
        clickedSlot.putStack(ItemStack.EMPTY);
      } else {
        clickedSlot.onSlotChanged();
      }
    }

    return ItemStack.EMPTY;
  }
}
