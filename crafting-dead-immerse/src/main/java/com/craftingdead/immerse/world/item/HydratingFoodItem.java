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

package com.craftingdead.immerse.world.item;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.survival.SurvivalPlayerHandler;
import com.craftingdead.immerse.world.item.hydration.Hydration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class HydratingFoodItem extends Item {

  private final int water;

  public HydratingFoodItem(Properties properties, int water) {
    super(properties);
    this.water = water;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag tag) {
    return CapabilityUtil.provider(() -> Hydration.fixed(this.water), Hydration.CAPABILITY);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);
    var handler = PlayerExtension.getOrThrow(player).getHandlerOrThrow(SurvivalPlayerHandler.TYPE);
    if (itemstack.isEdible()) {
      if (player.canEat(itemstack.getFoodProperties(player).canAlwaysEat())
          || handler.needsWater()) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
      } else {
        return InteractionResultHolder.fail(itemstack);
      }
    } else {
      return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
  }
}
