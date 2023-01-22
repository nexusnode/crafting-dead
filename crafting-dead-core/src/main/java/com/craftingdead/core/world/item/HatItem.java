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
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.hat.DefaultHat;
import com.craftingdead.core.world.item.hat.Hat;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class HatItem extends Item {

  private final float headshotReductionPercentage;

  private final boolean immuneToFlashes;

  private final boolean immuneToGas;

  private final boolean nightVision;

  public HatItem(Properties properties) {
    super(properties);
    this.headshotReductionPercentage = properties.headshotReductionPercentage;
    this.immuneToFlashes = properties.immuneToFlashes;
    this.immuneToGas = properties.immuneToGas;
    this.nightVision = properties.nightVision;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> lore,
      TooltipFlag tooltipFlag) {
    if (this.headshotReductionPercentage > 0.0F) {
      Component percentageText =
          new TextComponent(String.format("%.0f", this.headshotReductionPercentage * 100.0F) + "%")
              .withStyle(ChatFormatting.RED);

      lore.add(new TranslatableComponent("hat.headshot_reduction")
          .withStyle(ChatFormatting.GRAY)
          .append(percentageText));
    }
    if (this.immuneToFlashes) {
      lore.add(new TranslatableComponent("hat.immune_to_flashes")
          .withStyle(ChatFormatting.GRAY));
    }
    if (this.immuneToGas) {
      lore.add(new TranslatableComponent("hat.immune_to_gas")
          .withStyle(ChatFormatting.GRAY));
    }
    if (this.nightVision) {
      lore.add(new TranslatableComponent("hat.has_night_vision")
          .withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return CapabilityUtil.provider(
        () -> new DefaultHat(this.nightVision, this.headshotReductionPercentage,
            this.immuneToFlashes),
        Hat.CAPABILITY);
  }

  public static class Properties extends Item.Properties {

    private float headshotReductionPercentage;
    private boolean immuneToFlashes;
    private boolean immuneToGas;
    private boolean nightVision;

    public Properties setHeadshotReductionPercentage(float headshotReductionPercentage) {
      this.headshotReductionPercentage = headshotReductionPercentage;
      return this;
    }

    public Properties setNightVision(boolean nightVision) {
      this.nightVision = nightVision;
      return this;
    }

    public Properties setImmuneToFlashes(boolean immuneToFlashes) {
      this.immuneToFlashes = immuneToFlashes;
      return this;
    }

    public Properties setImmuneToGas(boolean immuneToGas) {
      this.immuneToGas = immuneToGas;
      return this;
    }
  }
}
