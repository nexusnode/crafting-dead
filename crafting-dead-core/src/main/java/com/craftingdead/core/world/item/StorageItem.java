/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item;


import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.inventory.GenericMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.inventory.storage.ItemStackHandlerStorage;
import com.craftingdead.core.world.inventory.storage.Storage;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageItem extends Item {

  public static final int MAX_ROWS_TO_SHOW = 6;

  public static final NonNullSupplier<Storage> VEST =
      () -> new ItemStackHandlerStorage(2 * 9, ModEquipmentSlot.VEST, GenericMenu::createVest);

  public static final NonNullSupplier<Storage> SMALL_BACKPACK =
      () -> new ItemStackHandlerStorage(2 * 9, ModEquipmentSlot.BACKPACK,
          GenericMenu::createSmallBackpack);

  public static final NonNullSupplier<Storage> MEDIUM_BACKPACK =
      () -> new ItemStackHandlerStorage(4 * 9, ModEquipmentSlot.BACKPACK,
          GenericMenu::createMediumBackpack);

  public static final NonNullSupplier<Storage> LARGE_BACKPACK =
      () -> new ItemStackHandlerStorage(6 * 9, ModEquipmentSlot.BACKPACK,
          GenericMenu::createLargeBackpack);

  public static final NonNullSupplier<Storage> GUN_BAG =
      () -> new ItemStackHandlerStorage(4 * 9, ModEquipmentSlot.BACKPACK,
          GenericMenu::createGunBag);

  private final NonNullSupplier<Storage> storageContainer;

  public StorageItem(NonNullSupplier<Storage> storageContainer, Properties properties) {
    super(properties);
    this.storageContainer = storageContainer;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(this.storageContainer,
        Storage.CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Override
  public void appendHoverText(ItemStack backpackStack, Level world, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(backpackStack, world, lines, tooltipFlag);

    backpackStack.getCapability(Storage.CAPABILITY).ifPresent(storage -> {
      if (!storage.isEmpty()) {
        lines.add(new TextComponent(" "));
        lines.add(new TranslatableComponent("storage_item.contents")
            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));

        int itemsBeyondLimit = 0;
        int itemsDisplayed = 0;

        for (int i = 0; i < storage.getSlots(); i++) {
          var stack = storage.getStackInSlot(i);
          if (!stack.isEmpty()) {
            if (itemsDisplayed++ >= MAX_ROWS_TO_SHOW) {
              itemsBeyondLimit++;
              continue;
            }
            var amountText = new TextComponent(stack.getCount() + "x ")
                .withStyle(ChatFormatting.DARK_GRAY);
            var itemText = stack.getHoverName().plainCopy().withStyle(ChatFormatting.GRAY);
            lines.add(amountText.append(itemText));
          }
        }

        if (itemsBeyondLimit > 0) {
          lines.add(new TextComponent(". . . +" + itemsBeyondLimit)
              .withStyle(ChatFormatting.RED));
        }
      }
    });
  }

  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    var shareTag = stack.getTag();
    if (shareTag == null) {
      shareTag = new CompoundTag();
    }
    var storageTag = stack.getCapability(Storage.CAPABILITY)
        .map(Storage::serializeNBT)
        .orElse(null);
    if (storageTag != null && !storageTag.isEmpty()) {
      shareTag.put("storage", storageTag);
    }
    return shareTag;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
    if (tag != null && tag.contains("storage", Tag.TAG_COMPOUND)) {
      stack.getCapability(Storage.CAPABILITY)
          .ifPresent(gun -> gun.deserializeNBT(tag.getCompound("storage")));
    }
    super.readShareTag(stack, tag);
  }

}
