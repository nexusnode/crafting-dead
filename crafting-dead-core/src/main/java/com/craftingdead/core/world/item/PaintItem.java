/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
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
    return CapabilityUtil.provider(() -> Paint.of(this.skin, this.multipaint
        ? OptionalInt.of(
            DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]
                .getTextColor())
        : OptionalInt.empty()),
        Paint.CAPABILITY);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag flag) {
    lines.add(new TranslatableComponent("paint.accepted_guns")
        .withStyle(ChatFormatting.GRAY));
    Skins.REGISTRY.get(this.skin).getAcceptedGuns().stream()
        .map(ForgeRegistries.ITEMS::getValue)
        .map(Item::getDescription)
        .map(text -> text.copy().withStyle(ChatFormatting.RED))
        .forEach(lines::add);
  }
}
