package com.craftingdead.mod.inventory.container;

import java.util.HashSet;
import java.util.Set;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.PaintItem;
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
  public boolean isItemValid(ItemStack itemStack) {
    return itemStack.getItem() instanceof GunItem;
  }

  @Override
  public void putStack(ItemStack itemStack) {
    itemStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
      gunController.getAttachments().forEach(attachment -> {
        this.craftingInventory
            .setInventorySlotContents(attachment.getInventorySlot().getIndex(),
                new ItemStack(attachment));
      });
      this.craftingInventory
          .setInventorySlotContents(CraftingInventorySlotType.PAINT.getIndex(),
              gunController.getPaintStack());
    });
    super.putStack(itemStack);
  }

  @Override
  public ItemStack onTake(PlayerEntity playerEntity, ItemStack gunStack) {
    gunStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
      gunController.setPaintStack(ItemStack.EMPTY);
      Set<AttachmentItem> attachments = new HashSet<>();
      for (int i = 0; i < this.craftingInventory.getSizeInventory(); i++) {
        ItemStack itemStack = this.craftingInventory.getStackInSlot(i);
        if (gunController.isAcceptedPaintOrAttachment(itemStack)) {
          if (itemStack.getItem() instanceof AttachmentItem) {
            attachments.add((AttachmentItem) itemStack.getItem());
          } else if (itemStack.getItem() instanceof PaintItem) {
            gunController.setPaintStack(itemStack);
          }
          this.craftingInventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }
      }
      gunController.setAttachments(attachments);
    });
    return super.onTake(playerEntity, gunStack);
  }
}
