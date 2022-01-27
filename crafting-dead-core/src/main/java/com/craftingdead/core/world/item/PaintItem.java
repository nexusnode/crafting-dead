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
import javax.annotation.Nullable;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.world.item.gun.skin.SimplePaint;
import com.craftingdead.core.world.item.gun.skin.Skin;
import com.craftingdead.core.world.item.gun.skin.Skins;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintItem extends Item {

  private final boolean multipaint;
  private final RegistryKey<Skin> skin;

  public PaintItem(Properties properties) {
    super(properties);
    this.multipaint = true;
    this.skin = null;
  }

  public PaintItem(RegistryKey<Skin> skin, Properties properties) {
    super(properties);
    this.multipaint = false;
    this.skin = skin;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> new SimplePaint(this.multipaint
            ? DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]
                .getColorValue()
            : null, this.skin)),
        () -> Capabilities.PAINT);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable World level,
      List<ITextComponent> lines, ITooltipFlag flag) {
    lines.add(new TranslationTextComponent("item_lore.paint.accepted_guns")
        .withStyle(TextFormatting.GRAY));
    Skins.REGISTRY.get(this.skin).getAcceptedGuns().stream()
        .map(ForgeRegistries.ITEMS::getValue)
        .map(Item::getDescription)
        .map(text -> text.copy().withStyle(TextFormatting.RED))
        .forEach(lines::add);
  }
}
