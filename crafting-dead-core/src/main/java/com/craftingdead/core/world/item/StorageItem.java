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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.inventory.GenericMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.inventory.storage.ItemStackHandlerStorage;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
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
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(LazyOptional.of(this.storageContainer),
        ImmutableSet.of(
            () -> Capabilities.STORAGE,
            () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY),
        CompoundNBT::new);
  }

  @Override
  public void appendHoverText(ItemStack backpackStack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.appendHoverText(backpackStack, world, lines, tooltipFlag);

    backpackStack.getCapability(Capabilities.STORAGE).ifPresent(storage -> {
      if (!storage.isEmpty()) {
        lines.add(new StringTextComponent(" "));
        lines.add(new TranslationTextComponent("container.inventory")
            .withStyle(TextFormatting.RED, TextFormatting.BOLD));

        int rowsBeyondLimit = 0;

        for (int i = 0; i < storage.getSlots(); i++) {
          ItemStack stack = storage.getStackInSlot(i);
          if (!stack.isEmpty()) {
            if (i >= MAX_ROWS_TO_SHOW) {
              ++rowsBeyondLimit;
            } else {
              IFormattableTextComponent amountText =
                  new StringTextComponent(stack.getCount() + "x ")
                      .withStyle(TextFormatting.DARK_GRAY);
              ITextComponent itemText =
                  stack.getHoverName().plainCopy().withStyle(TextFormatting.GRAY);
              lines.add(amountText.append(itemText));
            }
          }
        }

        if (rowsBeyondLimit > 0) {
          lines.add(new StringTextComponent(". . . +" + rowsBeyondLimit)
              .withStyle(TextFormatting.RED));
        }
      }
    });
  }

  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT shareTag = stack.getTag();
    if (shareTag == null) {
      shareTag = new CompoundNBT();
    }
    CompoundNBT storageTag = stack.getCapability(Capabilities.STORAGE)
        .map(Storage::serializeNBT)
        .orElse(null);
    if (storageTag != null && !storageTag.isEmpty()) {
      shareTag.put("storage", storageTag);
    }
    return shareTag;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    if (nbt != null && nbt.contains("storage", Constants.NBT.TAG_COMPOUND)) {
      stack.getCapability(Capabilities.STORAGE)
          .ifPresent(gun -> gun.deserializeNBT(nbt.getCompound("storage")));
    }
    super.readShareTag(stack, nbt);
  }

}
