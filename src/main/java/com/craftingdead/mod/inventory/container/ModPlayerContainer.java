package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.inventory.InventorySlotType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

public class ModPlayerContainer extends Container {

  private final IInventory inventory;
  private final IInventory modInventory;
  private final IInventory craftingInventory = new Inventory(5);

  public ModPlayerContainer(int windowId, PlayerInventory playerInventory) {
    super(ModContainerTypes.PLAYER.get(), windowId);
    this.inventory = playerInventory;
    this.modInventory = playerInventory.player
        .getCapability(ModCapabilities.PLAYER)
        .orElseThrow(() -> new IllegalStateException("No player capability"))
        .getInventory();

    final int deltaY = 20;

    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18 + deltaY));
      }
    }

    for (int i = 0; i < 9; ++i) {
      this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142 + deltaY));
    }

    this.addSlot(new MeleeSlot(this.modInventory, InventorySlotType.MELEE.getIndex(), 26, 9));
    this.addSlot(new GunSlot(this.modInventory, InventorySlotType.GUN.getIndex(), 44, 9));
    this.addSlot(new ClothingSlot(this.modInventory, InventorySlotType.CLOTHING.getIndex(), 65, 9));

    this.addSlot(new HatSlot(this.modInventory, InventorySlotType.HAT.getIndex(), 65, 30));
    this
        .addSlot(
            new BackpackSlot(this.modInventory, InventorySlotType.BACKPACK.getIndex(), 65, 48));
    this.addSlot(new VestSlot(this.modInventory, InventorySlotType.VEST.getIndex(), 65, 66));

    this.addSlot(new GunSlot(this.craftingInventory, CraftingInventorySlotType.GUN.getIndex(), 125, 48));
    this
        .addSlot(new AttachmentSlot(this.craftingInventory,
            CraftingInventorySlotType.MUZZLE_ATTACHMENT.getIndex(), 104, 48));
    this
        .addSlot(new AttachmentSlot(this.craftingInventory,
            CraftingInventorySlotType.UNDERBARREL_ATTACHMENT.getIndex(), 125, 69));
    this
        .addSlot(new AttachmentSlot(this.craftingInventory,
            CraftingInventorySlotType.OVERBARREL_ATTACHMENT.getIndex(), 125, 27));
    this
        .addSlot(
            new PaintSlot(this.modInventory, CraftingInventorySlotType.PAINT.getIndex(), 146, 48));
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerEntity) {
    return this.inventory.isUsableByPlayer(playerEntity)
        && this.modInventory.isUsableByPlayer(playerEntity);
  }

  @Override
  public void onContainerClosed(PlayerEntity playerEntity) {
    super.onContainerClosed(playerEntity);
    if (!playerEntity.getEntityWorld().isRemote()) {
      this.clearContainer(playerEntity, playerEntity.getEntityWorld(), this.craftingInventory);
    }
  }

  public IInventory getCraftingInventory() {
    return this.craftingInventory;
  }
}
