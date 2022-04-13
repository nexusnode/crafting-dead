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

package com.craftingdead.immerse.world.action;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.immerse.game.survival.SurvivalPlayerHandler;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public abstract class BuildActionType extends ItemActionType<BuildAction> {

  public static final BiPredicate<LivingExtension<?, ?>, BlockPos> WITHIN_BASE =
      (performer, blockPos) -> LevelExtension.getOrThrow(performer.getLevel()).getLandManager()
          .getLandOwnerAt(blockPos)
          .map(owner -> owner.isAllowedToBuild(performer.getEntity(), blockPos))
          .orElse(false);

  public static final BiPredicate<LivingExtension<?, ?>, BlockPos> NOT_CLAIMED =
      (performer, blockPos) -> !LevelExtension.getOrThrow(performer.getLevel()).getLandManager()
          .isLandClaimed(blockPos);

  public static final BiPredicate<LivingExtension<?, ?>, BlockPos> DONT_OWN_BASE =
      (performer, blockPos) -> performer.getHandler(SurvivalPlayerHandler.TYPE)
          .map(handler -> handler.getBase().isEmpty())
          .orElse(true);

  public static final BiConsumer<LivingExtension<?, ?>, BlockPos> NOTIFY_BASE =
      (performer, blockPos) -> LevelExtension.getOrThrow(performer.getLevel()).getLandManager()
          .getLandOwnerAt(blockPos)
          .ifPresent(base -> base.playerPlacedBlock(performer, blockPos));


  private final BiPredicate<LivingExtension<?, ?>, BlockPos> predicate;
  private final BiConsumer<LivingExtension<?, ?>, BlockPos> placementHandler;

  protected BuildActionType(Builder<?> builder) {
    super(builder);
    this.predicate = builder.predicate;
    this.placementHandler = builder.placementHandler;
  }

  public BiPredicate<LivingExtension<?, ?>, BlockPos> getPlacementPredicate() {
    return this.predicate;
  }

  public BiConsumer<LivingExtension<?, ?>, BlockPos> getBlockPlacementHandler() {
    return this.placementHandler;
  }

  protected abstract BuildAction create(LivingExtension<?, ?> performer,
      BlockPlaceContext context);

  @Override
  public void encode(BuildAction action, FriendlyByteBuf out) {
    out.writeEnum(action.getHand());
    out.writeBlockHitResult(action.getContext().getHitResult());
  }

  @Override
  public BuildAction decode(LivingExtension<?, ?> performer, FriendlyByteBuf in) {
    var hand = in.readEnum(InteractionHand.class);
    var hitResult = in.readBlockHitResult();
    var context = new BlockPlaceContext(performer.getLevel(),
        performer.getEntity() instanceof Player player ? player : null, hand,
        performer.getEntity().getItemInHand(hand), hitResult);
    return this.create(performer, context);
  }

  @Override
  public Optional<Action> createBlockAction(LivingExtension<?, ?> performer, UseOnContext context) {
    return Optional.of(this.create(performer, new BlockPlaceContext(context)));
  }

  public static abstract class Builder<SELF extends Builder<SELF>>
      extends ItemActionType.Builder<SELF> {

    private BiPredicate<LivingExtension<?, ?>, BlockPos> predicate = (performer, blockPos) -> true;
    private BiConsumer<LivingExtension<?, ?>, BlockPos> placementHandler = (performer, blockPos) -> {};

    public SELF withinBase() {
      this.predicate = this.predicate.and(WITHIN_BASE);
      this.placementHandler = placementHandler.andThen(NOTIFY_BASE);
      return this.self();
    }

    public SELF notClaimed() {
      this.predicate = this.predicate.and(NOT_CLAIMED);
      return this.self();
    }

    public SELF dontOwnBase() {
      this.predicate = this.predicate.and(DONT_OWN_BASE);
      return this.self();
    }
  }
}
