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
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.gun.paint.DefaultPaint;
import com.craftingdead.core.world.gun.paint.IPaint;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

public class PaintItem extends Item {

  @Nullable
  private final Supplier<Integer> colour;
  private final boolean hasSkin;

  public PaintItem(Properties properties) {
    super(properties);
    this.colour = properties.colour;
    this.hasSkin = properties.hasSkin;
  }

  public boolean hasSkin() {
    return this.hasSkin;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    stack.getCapability(ModCapabilities.PAINT)
        .resolve()
        .flatMap(IPaint::getColour)
        .map(colour -> new StringTextComponent("#" + Integer.toHexString(colour))
            .withStyle(TextFormatting.GRAY))
        .ifPresent(lines::add);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(
        LazyOptional.of(() -> new DefaultPaint(this.hasSkin ? this.getRegistryName() : null,
            this.colour == null ? null : this.colour.get())),
        () -> ModCapabilities.PAINT, CompoundNBT::new);
  }

  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = super.getShareTag(stack);
    CompoundNBT paintNbt = stack.getCapability(ModCapabilities.PAINT)
        .map(IPaint::serializeNBT)
        .orElse(null);
    if (paintNbt != null) {
      if (nbt == null) {
        nbt = new CompoundNBT();
      }
      nbt.put("paint", paintNbt);
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    super.readShareTag(stack, nbt);
    if (nbt != null && nbt.contains("paint", Constants.NBT.TAG_COMPOUND)) {
      stack.getCapability(ModCapabilities.PAINT)
          .ifPresent(paint -> paint.deserializeNBT(nbt.getCompound("paint")));
    }
  }

  public static class Properties extends Item.Properties {

    @Nullable
    private Supplier<Integer> colour;
    private boolean hasSkin = true;

    public Properties setColour() {
      return this.setColour(() -> ThreadLocalRandom.current().nextInt(0xFFFFFF + 1));
    }

    public Properties setColour(int colour) {
      return this.setColour(() -> colour);
    }

    public Properties setColour(@Nullable Supplier<Integer> colour) {
      this.colour = colour;
      return this;
    }

    public Properties setHasSkin(boolean hasSkin) {
      this.hasSkin = hasSkin;
      return this;
    }
  }
}
