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
import javax.annotation.Nullable;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import com.craftingdead.core.world.item.gun.magazine.MagazineImpl;
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
import net.minecraftforge.common.util.LazyOptional;

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
    return new SerializableCapabilityProvider<>(LazyOptional.of(() -> new MagazineImpl(this)),
        () -> Magazine.CAPABILITY, CompoundTag::new);
  }

  @Override
  public boolean isValidRepairItem(ItemStack itemStack, ItemStack materialStack) {
    return Tags.Items.GUNPOWDER.contains(materialStack.getItem())
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
  public void appendHoverText(ItemStack stack, Level world, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);

    // Shows the current amount if the maximum size is higher than 1
    if (this.getSize() > 1) {
      int currentAmount =
          stack.getCapability(Magazine.CAPABILITY).map(Magazine::getSize).orElse(0);

      Component amountText = new TextComponent(currentAmount + "/" + this.getSize())
          .withStyle(ChatFormatting.RED);

      lines.add(new TranslatableComponent("item_lore.magazine_item.amount")
          .withStyle(ChatFormatting.GRAY)
          .append(amountText));
    }

    if (this.armorPenetration > 0) {
      lines.add(new TranslatableComponent("item_lore.magazine_item.armor_penetration")
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
      return this;
    }
  }
}
