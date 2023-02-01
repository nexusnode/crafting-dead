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

package com.craftingdead.core.world.item;


import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StorageItem extends EquipmentItem {

  public static final int MAX_ROWS_TO_SHOW = 6;

  public static final UUID ARMOR_MODIFIER_ID =
      UUID.fromString("5900b64d-0e0b-4872-804b-0522bb87c33f");

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;
  private final Equipment.Slot slot;
  private final int itemRows;
  private final ItemHandlerMenuConstructor menuConstructor;

  public StorageItem(Properties properties) {
    super(properties);
    this.attributeModifiers = properties.attributeModifiers.build();
    this.slot = properties.slot;
    this.itemRows = properties.itemRows;
    this.menuConstructor = properties.menuConstructor;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return new ICapabilitySerializable<CompoundTag>() {

      private final LazyOptional<Storage> storage = LazyOptional.of(Storage::new);

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
          return this.storage.lazyMap(Storage::itemHandler).cast();
        }

        if (cap == Equipment.CAPABILITY) {
          return this.storage.cast();
        }

        return LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
        return this.storage
            .lazyMap(Storage::itemHandler)
            .lazyMap(ItemStackHandler::serializeNBT)
            .orElseGet(CompoundTag::new);
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
        this.storage
            .lazyMap(Storage::itemHandler)
            .ifPresent(itemHandler -> itemHandler.deserializeNBT(tag));
      }
    };
  }

  @Override
  public void appendHoverText(ItemStack itemStack, Level world, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, world, lines, tooltipFlag);

    itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        .ifPresent(itemHandler -> {
          int itemsBeyondLimit = 0;
          int itemsDisplayed = 0;

          for (int i = 0; i < itemHandler.getSlots(); i++) {
            var stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
              if (itemsDisplayed++ >= MAX_ROWS_TO_SHOW) {
                itemsBeyondLimit++;
                continue;
              }
              var amountText = new TextComponent(stack.getCount() + "x ")
                  .withStyle(ChatFormatting.DARK_GRAY);
              var itemText = stack.getHoverName().plainCopy().withStyle(ChatFormatting.GRAY);
              // First item
              if (itemsDisplayed == 1) {
                lines.add(new TextComponent(" "));
                lines.add(new TranslatableComponent("storage_item.contents")
                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
              }
              lines.add(amountText.append(itemText));
            }
          }

          if (itemsBeyondLimit > 0) {
            lines.add(new TextComponent(". . . + " + itemsBeyondLimit)
                .withStyle(ChatFormatting.RED));
          }
        });
  }

  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    var shareTag = stack.getTag() == null ? new CompoundTag() : stack.getTag();
    stack.getCapability(Equipment.CAPABILITY)
        .<Storage>cast()
        .lazyMap(Storage::itemHandler)
        .map(ItemStackHandler::serializeNBT)
        .filter(tag -> !tag.isEmpty())
        .ifPresent(tag -> shareTag.put("storage", tag));
    return shareTag;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
    if (tag != null && tag.contains("storage", Tag.TAG_COMPOUND)) {
      stack.getCapability(Equipment.CAPABILITY)
          .<Storage>cast()
          .ifPresent(storage -> storage.itemHandler().deserializeNBT(tag.getCompound("storage")));
    }
    super.readShareTag(stack, tag);
  }

  public static class Properties extends Item.Properties {

    private ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeModifiers =
        ImmutableMultimap.builder();
    private Equipment.Slot slot;
    private int itemRows;
    private ItemHandlerMenuConstructor menuConstructor;

    public Properties attributeModifier(Attribute attribute, AttributeModifier modifier) {
      this.attributeModifiers.put(attribute, modifier);
      return this;
    }

    public Properties slot(Equipment.Slot slot) {
      this.slot = slot;
      return this;
    }

    public Properties itemRows(int itemRows) {
      this.itemRows = itemRows;
      return this;
    }

    public Properties menuConstructor(ItemHandlerMenuConstructor menuConstructor) {
      this.menuConstructor = menuConstructor;
      return this;
    }
  }

  @FunctionalInterface
  public interface ItemHandlerMenuConstructor {

    @Nullable
    AbstractContainerMenu createMenu(int windowId, Inventory inventory, IItemHandler itemHandler);
  }

  private class Storage implements Equipment, MenuConstructor {

    private final ItemStackHandler itemHandler;

    public Storage() {
      final var size = StorageItem.this.itemRows * 9;
      this.itemHandler = new ItemStackHandler(size) {
        @Override
        protected void onLoad() {
          if (this.getSlots() != size) {
            this.setSize(size);
          }
        }
      };
    }

    private ItemStackHandler itemHandler() {
      return this.itemHandler;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
      return StorageItem.this.menuConstructor.createMenu(windowId, inventory, this.itemHandler);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> attributeModifiers() {
      return StorageItem.this.attributeModifiers;
    }

    @Override
    public boolean isValidForSlot(Slot slot) {
      return slot == StorageItem.this.slot;
    }
  }
}
