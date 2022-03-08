/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

import java.util.function.Supplier;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ActionItem extends Item {

  private final Supplier<? extends ItemActionType<?>> itemActionType;

  public ActionItem(Supplier<? extends ItemActionType<?>> itemActionType, Properties properties) {
    super(properties);
    this.itemActionType = itemActionType;
  }

  public ItemActionType<?> getActionType() {
    return this.itemActionType.get();
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    if (!context.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(context.getPlayer());
      if (this.getActionType().createBlockAction(performer, context)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResult.CONSUME;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity targetEntity, InteractionHand hand) {
    if (!player.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(player);
      var target = LivingExtension.getOrThrow(targetEntity);
      if (this.getActionType().createEntityAction(performer, target, hand)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResult.CONSUME;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    if (!player.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(player);
      var hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
      if (hitResult.getType() == HitResult.Type.BLOCK
          && this.getActionType()
              .createBlockAction(performer, new UseOnContext(player, hand, hitResult))
              .map(action -> performer.performAction(action, true))
              .orElse(false)) {
        return InteractionResultHolder.consume(player.getItemInHand(hand));
      }

      if (this.getActionType().createAction(performer, hand)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResultHolder.consume(player.getItemInHand(hand));
      }
    }

    return InteractionResultHolder.pass(player.getItemInHand(hand));
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    return this.getActionType().getTotalDurationTicks();
  }
}
