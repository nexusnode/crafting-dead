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

import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import com.craftingdead.core.world.item.gun.skin.Paint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
          (type, attachment) -> this.craftingInventory.setItem(type.getIndex(),
              new ItemStack(attachment)));
      gun.setAttachments(Collections.emptyMap());

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
      Map<GunCraftSlotType, Attachment> attachments = new HashMap<>();
      for (int i = 0; i < this.craftingInventory.getContainerSize(); i++) {
        ItemStack itemStack = this.craftingInventory.getItem(i);
        if (gun.isAcceptedAttachment(itemStack) && itemStack.getItem() instanceof AttachmentLike) {
          var attachment = ((AttachmentLike) itemStack.getItem()).asAttachment();
          attachments.put(attachment.getInventorySlot(), attachment);
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
