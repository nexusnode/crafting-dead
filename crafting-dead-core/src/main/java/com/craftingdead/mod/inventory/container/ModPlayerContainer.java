package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.inventory.InventorySlotType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ModPlayerContainer extends Container {

  private final PlayerInventory inventory;
  private final IInventory modInventory;
  private final CraftResultInventory outputInventory = new CraftResultInventory();
  private final Inventory craftingInventory = new Inventory(4);

  public ModPlayerContainer(int windowId, PlayerInventory playerInventory) {
    super(ModContainerTypes.PLAYER.get(), windowId);
    this.inventory = playerInventory;
    this.modInventory = playerInventory.player
        .getCapability(ModCapabilities.LIVING)
        .orElseThrow(() -> new IllegalStateException("No living capability"))
        .getInventory();
    this.craftingInventory.addListener(this::onCraftMatrixChanged);

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

    this.addSlot(new GunCraftSlot(this.outputInventory, 0, 125, 48, this.craftingInventory));

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
        .addSlot(new PaintSlot(this.craftingInventory, CraftingInventorySlotType.PAINT.getIndex(),
            146, 48));
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
      this.clearContainer(playerEntity, playerEntity.getEntityWorld(), this.outputInventory);
    }
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
