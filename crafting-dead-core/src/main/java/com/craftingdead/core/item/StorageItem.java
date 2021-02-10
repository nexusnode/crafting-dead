/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.item;


import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.storage.DefaultStorage;
import com.craftingdead.core.capability.storage.IStorage;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.GenericContainer;
import com.craftingdead.core.util.Text;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageItem extends Item {

  public static final int MAX_ROWS_TO_SHOW = 6;

  public static final Supplier<IStorage> VEST =
      () -> new DefaultStorage(2 * 9, InventorySlotType.VEST, GenericContainer::createVest);

  private final Supplier<IStorage> storageContainer;

  public StorageItem(Supplier<IStorage> storageContainer, Properties properties) {
    super(properties);
    this.storageContainer = storageContainer;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(this.storageContainer.get(), ImmutableSet
        .of(() -> ModCapabilities.STORAGE, () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY));
  }

  @Override
  public void addInformation(ItemStack backpackStack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.addInformation(backpackStack, world, lines, tooltipFlag);

    backpackStack.getCapability(ModCapabilities.STORAGE).ifPresent(storage -> {
      if (!storage.isEmpty()) {
        lines.add(Text.of(" "));
        lines.add(Text.translate("container.inventory")
            .mergeStyle(TextFormatting.RED, TextFormatting.BOLD));

        int rowsBeyondLimit = 0;

        for (int i = 0; i < storage.getSlots(); i++) {
          ItemStack stack = storage.getStackInSlot(i);
          if (!stack.isEmpty()) {
            if (i >= MAX_ROWS_TO_SHOW) {
              ++rowsBeyondLimit;
            } else {
              IFormattableTextComponent amountText =
                  Text.of(stack.getCount() + "x ").mergeStyle(TextFormatting.DARK_GRAY);
              ITextComponent itemText =
                  stack.getDisplayName().copyRaw().mergeStyle(TextFormatting.GRAY);
              lines.add(amountText.append(itemText));
            }
          }
        }

        if (rowsBeyondLimit > 0) {
          lines.add(Text.of(". . . +" + rowsBeyondLimit).mergeStyle(TextFormatting.RED));
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
    CompoundNBT storageTag = stack.getCapability(ModCapabilities.STORAGE)
        .map(IStorage::serializeNBT)
        .orElse(null);
    if (storageTag != null && !storageTag.isEmpty()) {
      shareTag.put("storage", storageTag);
    }
    return shareTag;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    if (nbt != null && nbt.contains("storage", Constants.NBT.TAG_COMPOUND)) {
      stack.getCapability(ModCapabilities.STORAGE)
          .ifPresent(gun -> gun.deserializeNBT(nbt.getCompound("storage")));
    }
    super.readShareTag(stack, nbt);
  }

}
