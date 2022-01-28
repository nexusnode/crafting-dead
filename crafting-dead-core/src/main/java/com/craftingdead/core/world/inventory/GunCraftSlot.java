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
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunItem;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import com.craftingdead.core.world.item.gun.skin.Paint;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GunCraftSlot extends Slot {

  private final SimpleContainer craftingInventory;

  public GunCraftSlot(ResultContainer inventory, int slotIndex, int x, int y,
      SimpleContainer craftingInventory) {
    super(inventory, slotIndex, x, y);
    this.craftingInventory = craftingInventory;
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    return itemStack.getItem() instanceof GunItem;
  }

  @Override
  public void set(ItemStack itemStack) {
    itemStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {
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
  public void onTake(Player playerEntity, ItemStack gunStack) {
    gunStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {
      gun.setPaintStack(ItemStack.EMPTY);
      Set<Attachment> attachments = new HashSet<>();
      for (int i = 0; i < this.craftingInventory.getContainerSize(); i++) {
        ItemStack itemStack = this.craftingInventory.getItem(i);
        if (gun.isAcceptedAttachment(itemStack) && itemStack.getItem() instanceof AttachmentLike) {
          attachments.add(((AttachmentLike) itemStack.getItem()).asAttachment());
          this.craftingInventory.setItem(i, ItemStack.EMPTY);
        } else if (Paint.isValid(gunStack, itemStack)) {
          gun.setPaintStack(itemStack);
          this.craftingInventory.setItem(i, ItemStack.EMPTY);
        }
      }
      gun.setAttachments(attachments);
    });
  }
}
