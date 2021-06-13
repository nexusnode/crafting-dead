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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.gun.magazine.Magazine;
import com.craftingdead.core.world.gun.magazine.MagazineImpl;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class MagazineItem extends Item {

  private final float armorPenetration;
  private final int size;
  private final boolean customTexture;

  public MagazineItem(Properties properties) {
    super(properties);
    this.size = properties.size;
    this.armorPenetration = properties.armorPenetration;
    this.customTexture = properties.customTexture;
  }

  public float getArmorPenetration() {
    return this.armorPenetration;
  }

  public int getSize() {
    return this.size;
  }

  public boolean hasCustomTexture() {
    return this.customTexture;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(LazyOptional.of(() -> new MagazineImpl(this)),
        () -> Capabilities.MAGAZINE, CompoundNBT::new);
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
        .getCapability(Capabilities.MAGAZINE)
        .map(Magazine::getSize)
        .orElse(this.size);
  }

  @Override
  public void setDamage(ItemStack itemStack, int damage) {
    itemStack
        .getCapability(Capabilities.MAGAZINE)
        .ifPresent(magazine -> magazine.setSize(Math.max(0, this.size - damage)));
  }

  @Override
  public boolean canBeDepleted() {
    return true;
  }

  @Override
  public void appendHoverText(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);

    // Shows the current amount if the maximum size is higher than 1
    if (this.getSize() > 1) {
      int currentAmount =
          stack.getCapability(Capabilities.MAGAZINE).map(Magazine::getSize).orElse(0);

      ITextComponent amountText = new StringTextComponent(currentAmount + "/" + this.getSize())
          .withStyle(TextFormatting.RED);

      lines.add(new TranslationTextComponent("item_lore.magazine_item.amount")
          .withStyle(TextFormatting.GRAY)
          .append(amountText));
    }

    if (this.armorPenetration > 0) {
      lines.add(new TranslationTextComponent("item_lore.magazine_item.armor_penetration")
          .withStyle(TextFormatting.GRAY)
          .append(new StringTextComponent(String.format("%.0f%%", this.armorPenetration))
              .withStyle(TextFormatting.RED)));
    }
  }

  @Override
  public CompoundNBT getShareTag(ItemStack itemStack) {
    CompoundNBT nbt = super.getShareTag(itemStack);
    if (nbt == null) {
      nbt = new CompoundNBT();
    }
    CompoundNBT magazineNbt = itemStack.getCapability(Capabilities.MAGAZINE)
        .map(INBTSerializable::serializeNBT)
        .orElse(null);
    if (magazineNbt != null && !magazineNbt.isEmpty()) {
      nbt.put("magazine", magazineNbt);
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    super.readShareTag(itemStack, nbt);
    if (nbt != null && nbt.contains("magazine", Constants.NBT.TAG_COMPOUND)) {
      itemStack.getCapability(Capabilities.MAGAZINE)
          .ifPresent(magazine -> magazine.deserializeNBT(nbt.getCompound("magazine")));
    }
  }

  public static class Properties extends Item.Properties {

    private float armorPenetration;
    private int size;
    private boolean customTexture;

    public Properties setArmorPenetration(float armorPenetration) {
      this.armorPenetration = armorPenetration;
      return this;
    }

    public Properties setSize(int size) {
      this.size = size;
      return this;
    }

    public Properties setCustomTexture(boolean customTexture) {
      this.customTexture = customTexture;
      return this;
    }
  }
}
