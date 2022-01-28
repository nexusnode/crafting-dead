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
import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.world.item.gun.skin.Paint;
import com.craftingdead.core.world.item.gun.skin.Skin;
import com.craftingdead.core.world.item.gun.skin.Skins;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintItem extends Item {

  private final boolean multipaint;
  private final ResourceKey<Skin> skin;

  public PaintItem(Properties properties) {
    super(properties);
    this.multipaint = true;
    this.skin = null;
  }

  public PaintItem(ResourceKey<Skin> skin, Properties properties) {
    super(properties);
    this.multipaint = false;
    this.skin = skin;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> Paint.of(this.skin, this.multipaint
            ? OptionalInt.of(
                DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]
                    .getTextColor())
            : OptionalInt.empty())),
        () -> Paint.CAPABILITY);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag flag) {
    lines.add(new TranslatableComponent("item_lore.paint.accepted_guns")
        .withStyle(ChatFormatting.GRAY));
    Skins.REGISTRY.get(this.skin).getAcceptedGuns().stream()
        .map(ForgeRegistries.ITEMS::getValue)
        .map(Item::getDescription)
        .map(text -> text.copy().withStyle(ChatFormatting.RED))
        .forEach(lines::add);
  }
}
