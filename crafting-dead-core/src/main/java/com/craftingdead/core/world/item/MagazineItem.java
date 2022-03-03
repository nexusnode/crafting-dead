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

import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import com.craftingdead.core.world.item.gun.magazine.MagazineImpl;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class MagazineItem extends Item {

  private final float armorPenetration;
  private final int size;

  public MagazineItem(Properties properties) {
    super(properties);
    this.size = properties.size;
    this.armorPenetration = properties.armorPenetration;
  }

  public float getArmorPenetration() {
    return this.armorPenetration;
  }

  public int getSize() {
    return this.size;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(() -> new MagazineImpl(this), Magazine.CAPABILITY);
  }

  @Override
  public boolean isValidRepairItem(ItemStack itemStack, ItemStack materialStack) {
    return materialStack.is(Tags.Items.GUNPOWDER)
        || super.isValidRepairItem(itemStack, materialStack);
  }

  @Override
  public int getMaxDamage(ItemStack itemStack) {
    return this.size;
  }

  @Override
  public int getDamage(ItemStack itemStack) {
    return this.size - itemStack
        .getCapability(Magazine.CAPABILITY)
        .map(Magazine::getSize)
        .orElse(this.size);
  }

  @Override
  public void setDamage(ItemStack itemStack, int damage) {
    itemStack
        .getCapability(Magazine.CAPABILITY)
        .ifPresent(magazine -> magazine.setSize(Math.max(0, this.size - damage)));
  }

  @Override
  public boolean canBeDepleted() {
    return true;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, level, lines, tooltipFlag);

    // Shows the current amount if the maximum size is higher than 1
    if (this.getSize() > 1) {
      int currentAmount =
          stack.getCapability(Magazine.CAPABILITY).map(Magazine::getSize).orElse(0);

      Component amountText = new TextComponent(currentAmount + "/" + this.getSize())
          .withStyle(ChatFormatting.RED);

      lines.add(new TranslatableComponent("magazine.amount")
          .withStyle(ChatFormatting.GRAY)
          .append(amountText));
    }

    if (this.armorPenetration > 0) {
      lines.add(new TranslatableComponent("magazine.armor_penetration")
          .withStyle(ChatFormatting.GRAY)
          .append(new TextComponent(String.format("%.0f%%", this.armorPenetration))
              .withStyle(ChatFormatting.RED)));
    }
  }

  @Override
  public CompoundTag getShareTag(ItemStack itemStack) {
    CompoundTag nbt = super.getShareTag(itemStack);
    if (nbt == null) {
      nbt = new CompoundTag();
    }
    CompoundTag magazineNbt = itemStack.getCapability(Magazine.CAPABILITY)
        .map(INBTSerializable::serializeNBT)
        .orElse(null);
    if (magazineNbt != null && !magazineNbt.isEmpty()) {
      nbt.put("magazine", magazineNbt);
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack itemStack, @Nullable CompoundTag nbt) {
    super.readShareTag(itemStack, nbt);
    if (nbt != null && nbt.contains("magazine", Tag.TAG_COMPOUND)) {
      itemStack.getCapability(Magazine.CAPABILITY)
          .ifPresent(magazine -> magazine.deserializeNBT(nbt.getCompound("magazine")));
    }
  }

  public static class Properties extends Item.Properties {

    private float armorPenetration;
    private int size;

    public Properties setArmorPenetration(float armorPenetration) {
      this.armorPenetration = armorPenetration;
      return this;
    }

    public Properties setSize(int size) {
      this.size = size;
      this.durability(size);
      return this;
    }
  }
}
