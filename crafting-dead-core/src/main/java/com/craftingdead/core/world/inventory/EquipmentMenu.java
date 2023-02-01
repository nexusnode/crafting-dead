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

import java.util.function.BiPredicate;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.equipment.Equipment;
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

public class EquipmentMenu extends AbstractContainerMenu {

  private final ResultContainer outputContainer = new ResultContainer();
  private final SimpleContainer craftingContainer = new SimpleContainer(4);

  private final PlayerExtension<?> player;

  public EquipmentMenu(int id, Inventory inventory) {
    this(id, PlayerExtension.getOrThrow(inventory.player));
  }

  public EquipmentMenu(int id, PlayerExtension<?> player) {
    super(ModMenuTypes.EQUIPMENT.get(), id);

    this.player = player;

    this.craftingContainer.addListener(this::slotsChanged);

    var inventory = player.entity().getInventory();

    final int slotSize = 18;

    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlot(
            new Slot(inventory, x + (y + 1) * 9, 8 + x * slotSize, 84 + y * slotSize));
      }
    }

    for (int x = 0; x < 9; ++x) {
      this.addSlot(new Slot(inventory, x, 8 + x * slotSize, 141));
    }

    int equipmentColumnX = 77;
    int equipmentColumnY = 8;

    this.addSlot(new EquipmentSlot(player,
        Equipment.Slot.HAT, equipmentColumnX, equipmentColumnY));

    this.addSlot(new EquipmentSlot(player,
        Equipment.Slot.CLOTHING,
        equipmentColumnX, equipmentColumnY += slotSize));

    this.addSlot(new EquipmentSlot(player,
        Equipment.Slot.VEST,
        equipmentColumnX, equipmentColumnY += slotSize));

    this.addSlot(new EquipmentSlot(player,
        Equipment.Slot.BACKPACK,
        equipmentColumnX, equipmentColumnY + slotSize));

    int weaponColumnX = 8;
    int weaponColumnY = 8;

    this.addSlot(new EquipmentSlot(player,
        Equipment.Slot.GUN,
        weaponColumnX, weaponColumnY));

    this.addSlot(new EquipmentSlot(player,
        Equipment.Slot.MELEE,
        weaponColumnX, weaponColumnY + slotSize));

    final int gunCraftSlotGap = 2;
    final int gunCraftY = 6;
    final int gunCraftX = 115;

    this.addSlot(new GunCraftSlot(this.outputContainer, 0,
        gunCraftX + slotSize + gunCraftSlotGap,
        gunCraftY + slotSize + gunCraftSlotGap,
        this.craftingContainer));

    final BiPredicate<PredicateSlot, ItemStack> attachmentOrPaintPredicate =
        (slot, itemStack) -> this.getGunStack().getCapability(Gun.CAPABILITY)
            .map(gun -> gun.isAcceptedAttachment(itemStack)).orElse(false);

    final BiPredicate<PredicateSlot, ItemStack> attachmentPredicate =
        (slot, itemStack) -> itemStack.getItem() instanceof AttachmentLike
            && ((AttachmentLike) itemStack.getItem())
                .asAttachment().getInventorySlot().getIndex() == slot.getSlotIndex();

    this.addSlot(new PredicateSlot(this.craftingContainer,
        GunCraftSlotType.MUZZLE_ATTACHMENT.getIndex(),
        gunCraftX,
        gunCraftY + slotSize + gunCraftSlotGap,
        attachmentPredicate.and(attachmentOrPaintPredicate)));
    this.addSlot(new PredicateSlot(this.craftingContainer,
        GunCraftSlotType.UNDERBARREL_ATTACHMENT.getIndex(),
        gunCraftX + slotSize + gunCraftSlotGap,
        gunCraftY + slotSize * 2 + gunCraftSlotGap * 2,
        attachmentPredicate.and(attachmentOrPaintPredicate)));
    this.addSlot(new PredicateSlot(this.craftingContainer,
        GunCraftSlotType.OVERBARREL_ATTACHMENT.getIndex(),
        gunCraftX + slotSize + gunCraftSlotGap,
        gunCraftY,
        attachmentPredicate.and(attachmentOrPaintPredicate)));

    this.addSlot(new PredicateSlot(this.craftingContainer,
        GunCraftSlotType.PAINT.getIndex(),
        gunCraftX + slotSize * 2 + gunCraftSlotGap * 2,
        gunCraftY + slotSize + gunCraftSlotGap,
        (slot, itemStack) -> Paint.isValid(this.getGunStack(), itemStack)));
  }

  public PlayerExtension<?> getPlayer() {
    return this.player;
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    if (!player.getLevel().isClientSide()) {
      this.clearContainer(player, this.craftingContainer);
      this.clearContainer(player, this.outputContainer);
    }
  }

  public ItemStack getGunStack() {
    return this.outputContainer.getItem(0);
  }

  public boolean isCraftingInventoryEmpty() {
    return this.craftingContainer.isEmpty();
  }

  public boolean isCraftable() {
    return this.getGunStack().getCapability(Gun.CAPABILITY)
        .map(gun -> {
          for (int i = 0; i < this.craftingContainer.getContainerSize(); i++) {
            ItemStack itemStack = this.craftingContainer.getItem(i);
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
