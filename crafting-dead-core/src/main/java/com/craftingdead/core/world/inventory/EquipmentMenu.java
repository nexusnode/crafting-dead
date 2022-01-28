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

import java.util.function.BiPredicate;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.craftingdead.core.world.item.HatItem;
import com.craftingdead.core.world.item.MeleeWeaponItem;
import com.craftingdead.core.world.item.clothing.Clothing;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import com.craftingdead.core.world.item.gun.skin.Paint;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EquipmentMenu extends AbstractContainerMenu {

  private final IItemHandler equipment;

  private final ResultContainer outputInventory = new ResultContainer();
  private final SimpleContainer craftingInventory = new SimpleContainer(4);

  public EquipmentMenu(int id, Inventory playerInventory) {
    this(id, playerInventory, new ItemStackHandler(ModEquipmentSlotType.values().length));
  }

  public EquipmentMenu(int id, Inventory playerInventory, IItemHandler equipment) {
    super(ModMenuTypes.EQUIPMENT.get(), id);
    this.equipment = equipment;
    this.craftingInventory.addListener(this::slotsChanged);

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

    this.addSlot(new PredicateItemHandlerSlot(this.equipment, ModEquipmentSlotType.GUN.getIndex(),
        equipmentColumnX, equipmentColumnY,
        (slot, itemStack) -> itemStack.getCapability(Gun.CAPABILITY).isPresent()));

    this.addSlot(
        new PredicateItemHandlerSlot(this.equipment, ModEquipmentSlotType.MELEE.getIndex(),
            equipmentColumnX, equipmentColumnY += slotSize,
            (slot, itemStack) -> itemStack.getItem() instanceof MeleeWeaponItem));

    this.addSlot(new PredicateItemHandlerSlot(this.equipment, ModEquipmentSlotType.HAT.getIndex(),
        equipmentColumnX, equipmentColumnY += slotSize,
        (slot, itemStack) -> itemStack.getItem() instanceof HatItem));

    this.addSlot(new PredicateItemHandlerSlot(this.equipment,
        ModEquipmentSlotType.CLOTHING.getIndex(), equipmentColumnX, equipmentColumnY += slotSize,
        (slot, itemStack) -> itemStack.getCapability(Clothing.CAPABILITY).isPresent()));

    this.addSlot(
        new PredicateItemHandlerSlot(this.equipment, ModEquipmentSlotType.VEST.getIndex(),
            equipmentColumnX + slotSize, equipmentColumnY, (slot, itemStack) -> itemStack
                .getCapability(Storage.CAPABILITY)
                .map(storage -> storage.isValidForSlot(ModEquipmentSlotType.VEST))
                .orElse(false)));

    final int gunCraftSlotGap = 3;
    final int gunCraftY = 8;

    this.addSlot(new GunCraftSlot(this.outputInventory, 0, 125, gunCraftY + slotSize + 3,
        this.craftingInventory));

    final BiPredicate<PredicateSlot, ItemStack> attachmentOrPaintPredicate =
        (slot, itemStack) -> this.getGunStack().getCapability(Gun.CAPABILITY)
            .map(gun -> gun.isAcceptedAttachment(itemStack)).orElse(false);

    final BiPredicate<PredicateSlot, ItemStack> attachmentPredicate =
        (slot, itemStack) -> itemStack.getItem() instanceof AttachmentLike
            && ((AttachmentLike) itemStack.getItem())
                .asAttachment().getInventorySlot().getIndex() == slot.getSlotIndex();

    this.addSlot(new PredicateSlot(this.craftingInventory,
        GunCraftSlotType.MUZZLE_ATTACHMENT.getIndex(), 104,
        gunCraftY + slotSize + gunCraftSlotGap,
        attachmentPredicate.and(attachmentOrPaintPredicate)));
    this.addSlot(new PredicateSlot(this.craftingInventory,
        GunCraftSlotType.UNDERBARREL_ATTACHMENT.getIndex(), 125,
        gunCraftY + slotSize * 2 + gunCraftSlotGap * 2,
        attachmentPredicate.and(attachmentOrPaintPredicate)));
    this.addSlot(new PredicateSlot(this.craftingInventory,
        GunCraftSlotType.OVERBARREL_ATTACHMENT.getIndex(), 125, gunCraftY,
        attachmentPredicate.and(attachmentOrPaintPredicate)));

    this.addSlot(new PredicateSlot(this.craftingInventory,
        GunCraftSlotType.PAINT.getIndex(), 146, gunCraftY + slotSize + gunCraftSlotGap,
        (slot, itemStack) -> Paint.isValid(this.getGunStack(), itemStack)));
  }

  @Override
  public boolean stillValid(Player playerEntity) {
    return true;
  }

  @Override
  public void removed(Player playerEntity) {
    super.removed(playerEntity);
    if (!playerEntity.level.isClientSide()) {
      this.clearContainer(playerEntity, this.craftingInventory);
      this.clearContainer(playerEntity, this.outputInventory);
    }
  }

  public IItemHandler getItemHandler() {
    return this.equipment;
  }

  public ItemStack getGunStack() {
    return this.outputInventory.getItem(0);
  }

  public boolean isCraftingInventoryEmpty() {
    return this.craftingInventory.isEmpty();
  }

  public boolean isCraftable() {
    return this.getGunStack().getCapability(Gun.CAPABILITY)
        .map(gun -> {
          for (int i = 0; i < this.craftingInventory.getContainerSize(); i++) {
            ItemStack itemStack = this.craftingInventory.getItem(i);
            if (!itemStack.isEmpty()
                && !gun.isAcceptedAttachment(itemStack)
                && !Paint.isValid(this.getGunStack(), itemStack)) {
              return false;
            }
          }
          return true;
        })
        .orElse(false);
  }

  @Override
  public ItemStack quickMoveStack(Player playerEntity, int clickedIndex) {
    Slot clickedSlot = this.slots.get(clickedIndex);
    if (clickedSlot != null && clickedSlot.hasItem()) {
      // Shift-clicking gun crafting slots is currently unavailable due to
      // the nature of how itemstacks are merged when shift-clicking.
      // In other words, attachments do not get attached.
      if (clickedSlot instanceof GunCraftSlot) {
        return ItemStack.EMPTY;
      }

      ItemStack clickedStack = clickedSlot.getItem();

      if (clickedIndex < 36) {
        // Pushes the clicked stack to the higher slots in a "negative direction"
        if (!this.moveItemStackTo(clickedStack, 36, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else {
        // Pushes the clicked stack to the lower slots in a "positive direction"
        if (!this.moveItemStackTo(clickedStack, 0, 36, false)) {
          return ItemStack.EMPTY;
        }
      }

      // From vanilla code. Seems to be mandatory.
      if (clickedStack.isEmpty()) {
        clickedSlot.set(ItemStack.EMPTY);
      } else {
        clickedSlot.setChanged();
      }
    }

    return ItemStack.EMPTY;
  }
}
