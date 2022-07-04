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

import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.survival.SurvivalPlayerHandler;
import com.craftingdead.immerse.world.item.hydration.Hydration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class DrinkItem extends Item {

  private static final int DRINK_DURATION = 32;

  private final int water;
  private final Supplier<Item> emptyItem;

  public DrinkItem(Properties properties, int water, Supplier<Item> emptyItem) {
    super(properties);
    this.water = water;
    this.emptyItem = emptyItem;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag tag) {
    return CapabilityUtil.provider(() -> Hydration.fixed(this.water), Hydration.CAPABILITY);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
    var player = livingEntity instanceof Player ? (Player) livingEntity : null;
    if (player instanceof ServerPlayer serverPlayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
    }

    if (player != null) {
      player.awardStat(Stats.ITEM_USED.get(this));
      if (!player.getAbilities().instabuild) {
        itemStack.shrink(1);
      }
    }

    if (player == null || !player.getAbilities().instabuild) {
      if (itemStack.isEmpty()) {
        return new ItemStack(this.emptyItem.get());
      }

      if (player != null) {
        player.getInventory().add(new ItemStack(this.emptyItem.get()));
      }
    }

    level.gameEvent(livingEntity, GameEvent.DRINKING_FINISH, livingEntity.eyeBlockPosition());
    return itemStack;
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    return DRINK_DURATION;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack itemStack) {
    return UseAnim.DRINK;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    var handler =
        PlayerExtension.getOrThrow(player).getHandlerOrThrow(SurvivalPlayerHandler.TYPE);
    return handler.getWater() < handler.getMaxWater()
        ? ItemUtils.startUsingInstantly(level, player, hand)
        : InteractionResultHolder.pass(player.getItemInHand(hand));
  }
}
