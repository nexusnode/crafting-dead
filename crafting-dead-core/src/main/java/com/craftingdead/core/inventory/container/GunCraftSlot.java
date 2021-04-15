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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.item.PaintItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class GunCraftSlot extends Slot {

  private final Inventory craftingInventory;

  public GunCraftSlot(CraftResultInventory inventory, int slotIndex, int x, int y,
      Inventory craftingInventory) {
    super(inventory, slotIndex, x, y);
    this.craftingInventory = craftingInventory;
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    return itemStack.getItem() instanceof GunItem;
  }

  @Override
  public void set(ItemStack itemStack) {
    itemStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
      gun.getAttachments().forEach(
          attachment -> this.craftingInventory.setItem(attachment.getInventorySlot().getIndex(),
              new ItemStack(attachment)));
      gun.setAttachments(Collections.emptySet());

      this.craftingInventory.setItem(CraftingInventorySlotType.PAINT.getIndex(),
          gun.getPaintStack());
      gun.setPaintStack(ItemStack.EMPTY);
    });
    super.set(itemStack);
  }

  @Override
  public ItemStack onTake(PlayerEntity playerEntity, ItemStack gunStack) {
    gunStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
      gunController.setPaintStack(ItemStack.EMPTY);
      Set<AttachmentItem> attachments = new HashSet<>();
      for (int i = 0; i < this.craftingInventory.getContainerSize(); i++) {
        ItemStack itemStack = this.craftingInventory.getItem(i);
        if (gunController.isAcceptedPaintOrAttachment(itemStack)) {
          if (itemStack.getItem() instanceof AttachmentItem) {
            attachments.add((AttachmentItem) itemStack.getItem());
          } else if (itemStack.getItem() instanceof PaintItem) {
            gunController.setPaintStack(itemStack);
          }
          this.craftingInventory.setItem(i, ItemStack.EMPTY);
        }
      }
      gunController.setAttachments(attachments);
    });
    return super.onTake(playerEntity, gunStack);
  }
}
