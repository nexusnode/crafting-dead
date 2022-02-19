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

import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ActionItem extends Item {

  @Nullable
  private final BiFunction<LivingExtension<?, ?>, BlockPos, Action> blockActionFactory;
  @Nullable
  private final BiFunction<LivingExtension<?, ?>, LivingExtension<?, ?>, Action> entityActionFactory;

  public ActionItem(Properties properties) {
    super(properties);
    this.blockActionFactory = properties.blockActionFactory;
    this.entityActionFactory = properties.entityActionFactory;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player playerEntity,
      LivingEntity targetEntity, InteractionHand hand) {
    if (!playerEntity.getCommandSenderWorld().isClientSide()) {
      this.performAction(playerEntity, targetEntity);
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
    if (!playerEntity.level.isClientSide()) {
      this.performAction(playerEntity, null);
    }
    return new InteractionResultHolder<>(InteractionResult.PASS, playerEntity.getItemInHand(hand));
  }

  public void performAction(LivingEntity performerEntity, LivingEntity targetEntity) {
    if (this.entityActionFactory != null) {
      performerEntity.getCapability(LivingExtension.CAPABILITY)
          .ifPresent(performer -> performer.performAction(
              this.entityActionFactory.apply(performer, targetEntity == null
                  ? null
                  : targetEntity.getCapability(LivingExtension.CAPABILITY).orElse(null)),
              false, true));
    }
  }

  public static class Properties extends Item.Properties {

    @Nullable
    private BiFunction<LivingExtension<?, ?>, BlockPos, Action> blockActionFactory;
    @Nullable
    private BiFunction<LivingExtension<?, ?>, LivingExtension<?, ?>, Action> entityActionFactory;

    public Properties action(Supplier<? extends ActionType> actionType) {
      // Can't use method reference because don't want to resolve the supplier too early.
      this.entityActionFactory =
          (performer, target) -> actionType.get().createAction(performer, target);
      return this;
    }

    public Properties setBlockFactory(
        BiFunction<LivingExtension<?, ?>, BlockPos, Action> blockFactory) {
      this.blockActionFactory = blockFactory;
      return this;
    }

    public Properties setEntityFactory(
        BiFunction<LivingExtension<?, ?>, LivingExtension<?, ?>, Action> entityFactory) {
      this.entityActionFactory = entityFactory;
      return this;
    }
  }
}
