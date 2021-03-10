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

package com.craftingdead.core.item;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.paint.DefaultPaint;
import com.craftingdead.core.capability.paint.IPaint;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;

public class PaintItem extends Item {

  private final Optional<Supplier<Integer>> colour;
  private final boolean hasSkin;

  public PaintItem(Properties properties) {
    super(properties);
    this.colour = Optional.ofNullable(properties.colour);
    this.hasSkin = properties.hasSkin;
  }

  public boolean hasSkin() {
    return this.hasSkin;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    stack
        .getCapability(ModCapabilities.PAINT)
        .map(IPaint::getColour)
        .orElse(Optional.empty())
        .ifPresent(colour -> lines
            .add(Text.of("#" + Integer.toHexString(colour)).withStyle(TextFormatting.GRAY)));
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(
        new DefaultPaint(this.hasSkin ? this.getRegistryName() : null,
            this.colour.map(Supplier::get).orElse(null)),
        () -> ModCapabilities.PAINT);
  }

  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = super.getShareTag(stack);
    if (nbt == null) {
      nbt = new CompoundNBT();
    }
    nbt.put("paint",
        stack.getCapability(ModCapabilities.PAINT).map(IPaint::serializeNBT).orElse(null));
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    super.readShareTag(stack, nbt);
    if (nbt != null && nbt.contains("paint", Constants.NBT.TAG_COMPOUND)) {
      stack
          .getCapability(ModCapabilities.PAINT)
          .ifPresent(paint -> paint.deserializeNBT(nbt.getCompound("paint")));
    }
  }

  public static class Properties extends Item.Properties {

    private Supplier<Integer> colour;
    private boolean hasSkin = true;

    public Properties setColour() {
      this.colour = () -> new Random().nextInt(0xFFFFFF + 1);
      return this;
    }

    public Properties setColour(Supplier<Integer> colour) {
      this.colour = colour;
      return this;
    }

    public Properties setHasSkin(boolean hasSkin) {
      this.hasSkin = hasSkin;
      return this;
    }
  }
}
