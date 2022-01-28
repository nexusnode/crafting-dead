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

package com.craftingdead.core.world.item;


import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.inventory.GenericMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.inventory.storage.ItemStackHandlerStorage;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.google.common.collect.ImmutableSet;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageItem extends Item {

  public static final int MAX_ROWS_TO_SHOW = 6;

  public static final Supplier<Storage> VEST =
      () -> new ItemStackHandlerStorage(2 * 9, ModEquipmentSlotType.VEST, GenericMenu::createVest);

  private final NonNullSupplier<Storage> storageContainer;

  public StorageItem(NonNullSupplier<Storage> storageContainer, Properties properties) {
    super(properties);
    this.storageContainer = storageContainer;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return new SerializableCapabilityProvider<>(LazyOptional.of(this.storageContainer),
        ImmutableSet.of(
            () -> Storage.CAPABILITY,
            () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY),
        CompoundTag::new);
  }

  @Override
  public void appendHoverText(ItemStack backpackStack, Level world, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(backpackStack, world, lines, tooltipFlag);

    backpackStack.getCapability(Storage.CAPABILITY).ifPresent(storage -> {
      if (!storage.isEmpty()) {
        lines.add(new TextComponent(" "));
        lines.add(new TranslatableComponent("container.inventory")
            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));

        int rowsBeyondLimit = 0;

        for (int i = 0; i < storage.getSlots(); i++) {
          ItemStack stack = storage.getStackInSlot(i);
          if (!stack.isEmpty()) {
            if (i >= MAX_ROWS_TO_SHOW) {
              ++rowsBeyondLimit;
            } else {
              MutableComponent amountText =
                  new TextComponent(stack.getCount() + "x ")
                      .withStyle(ChatFormatting.DARK_GRAY);
              Component itemText =
                  stack.getHoverName().plainCopy().withStyle(ChatFormatting.GRAY);
              lines.add(amountText.append(itemText));
            }
          }
        }

        if (rowsBeyondLimit > 0) {
          lines.add(new TextComponent(". . . +" + rowsBeyondLimit)
              .withStyle(ChatFormatting.RED));
        }
      }
    });
  }

  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag shareTag = stack.getTag();
    if (shareTag == null) {
      shareTag = new CompoundTag();
    }
    CompoundTag storageTag = stack.getCapability(Storage.CAPABILITY)
        .map(Storage::serializeNBT)
        .orElse(null);
    if (storageTag != null && !storageTag.isEmpty()) {
      shareTag.put("storage", storageTag);
    }
    return shareTag;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
    if (nbt != null && nbt.contains("storage", Tag.TAG_COMPOUND)) {
      stack.getCapability(Storage.CAPABILITY)
          .ifPresent(gun -> gun.deserializeNBT(nbt.getCompound("storage")));
    }
    super.readShareTag(stack, nbt);
  }

}
