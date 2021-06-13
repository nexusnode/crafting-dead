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

package com.craftingdead.core.world.inventory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.AttachmentLike;
import com.craftingdead.core.world.item.GunItem;
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
    itemStack.getCapability(Capabilities.GUN).ifPresent(gun -> {
      gun.getAttachments().forEach(
          attachment -> this.craftingInventory.setItem(attachment.getInventorySlot().getIndex(),
              new ItemStack(attachment)));
      gun.setAttachments(Collections.emptySet());

      this.craftingInventory.setItem(GunCraftSlotType.PAINT.getIndex(),
          gun.getPaintStack());
      gun.setPaintStack(ItemStack.EMPTY);
    });
    super.set(itemStack);
  }

  @Override
  public ItemStack onTake(PlayerEntity playerEntity, ItemStack gunStack) {
    gunStack.getCapability(Capabilities.GUN).ifPresent(gun -> {
      gun.setPaintStack(ItemStack.EMPTY);
      Set<Attachment> attachments = new HashSet<>();
      for (int i = 0; i < this.craftingInventory.getContainerSize(); i++) {
        ItemStack itemStack = this.craftingInventory.getItem(i);
        if (gun.isAcceptedPaintOrAttachment(itemStack)) {
          if (itemStack.getItem() instanceof AttachmentLike) {
            attachments.add(((AttachmentLike) itemStack.getItem()).asAttachment());
          } else {
            gun.setPaintStack(itemStack);
          }
          this.craftingInventory.setItem(i, ItemStack.EMPTY);
        }
      }
      gun.setAttachments(attachments);
    });
    return super.onTake(playerEntity, gunStack);
  }
}
