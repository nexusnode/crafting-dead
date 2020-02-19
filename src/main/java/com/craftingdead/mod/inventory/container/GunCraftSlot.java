package com.craftingdead.mod.inventory.container;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.PaintItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class GunCraftSlot extends GunSlot {

  private final Inventory craftingInventory;

  public GunCraftSlot(CraftResultInventory inventory, int slotIndex, int x, int y,
      Inventory craftingInventory) {
    super(inventory, slotIndex, x, y);
    this.craftingInventory = craftingInventory;
  }

  @Override
  public void putStack(ItemStack itemStack) {
    itemStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
      gunController.getAttachments().forEach(attachment -> {
        this.craftingInventory
            .setInventorySlotContents(attachment.getInventorySlot().getIndex(),
                new ItemStack(attachment));
      });
      gunController.getPaint().ifPresent(paint -> {
        this.craftingInventory
            .setInventorySlotContents(CraftingInventorySlotType.PAINT.getIndex(),
                new ItemStack(paint));
      });
    });
    super.putStack(itemStack);
  }

  @Override
  public ItemStack onTake(PlayerEntity playerEntity, ItemStack gunStack) {
    gunStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
      gunController.setPaint(Optional.empty());
      Set<AttachmentItem> attachments = new HashSet<>();
      for (int i = 0; i < this.craftingInventory.getSizeInventory(); i++) {
        ItemStack itemStack = this.craftingInventory.getStackInSlot(i);
        if (gunController.isAcceptedPaintOrAttachment(itemStack)) {
          if (itemStack.getItem() instanceof AttachmentItem) {
            attachments.add((AttachmentItem) itemStack.getItem());
          } else if (itemStack.getItem() instanceof PaintItem) {
            gunController.setPaint(Optional.of((PaintItem) itemStack.getItem()));
          }
          this.craftingInventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }
      }
      gunController.setAttachments(attachments);
    });
    return super.onTake(playerEntity, gunStack);
  }
}
