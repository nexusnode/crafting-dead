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

import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.action.ProgressBar;
import com.craftingdead.core.world.action.item.ItemAction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class BuildAction extends ItemAction {

  private final LivingExtension<?, ?> performer;
  private final BlockPlaceContext context;

  @Nullable
  private List<Placement> placements;

  public BuildAction(LivingExtension<?, ?> performer, BlockPlaceContext context) {
    super(context.getHand());
    this.performer = performer;
    this.context = context;
  }

  public BlockPlaceContext getContext() {
    return this.context;
  }

  @Nullable
  protected abstract List<Placement> createPlacements();

  @Nullable
  protected Placement createPlacement(BlockPos blockPos, Block block) {
    var context = BlockPlaceContext.at(this.getContext(), blockPos,
        this.getContext().getClickedFace());
    var blockState = block.getStateForPlacement(context);
    var existingBlockState = this.getPerformer().getLevel().getBlockState(blockPos);
    return existingBlockState.canBeReplaced(context)
        && this.getType().getPlacementPredicate().test(this.performer, blockPos)
            ? new Placement(blockPos.immutable(), blockState, existingBlockState)
            : null;
  }

  @Override
  public boolean start(boolean simulate) {
    this.placements = this.createPlacements();
    return super.start(simulate) && this.placements != null;
  }

  @Override
  public boolean tick() {
    if (super.tick()) {
      return true;
    }

    if (!this.performer.getLevel().isClientSide() && this.performer.getEntity()
        .distanceToSqr(Vec3.atCenterOf(this.context.getClickedPos())) > 64.0D) {
      this.performer.cancelAction(true);
      return false;
    }

    if (this.getTicksUsingItem() % 10 == 0) {
      this.getPerformer().getEntity().playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.0F);
      this.placements.forEach(placement -> {
        var currentStae = this.getPerformer().getLevel().getBlockState(placement.blockPos());
        if (placement.existingBlockState() != currentStae) {
          this.getPerformer().cancelAction(true);
        }
        this.addBuildEffects(placement.blockPos(), placement.blockState());
      });
    }

    return false;
  }

  public void addBuildEffects(BlockPos blockPos, BlockState blockState) {
    this.getPerformer().getLevel().addDestroyBlockEffect(blockPos, blockState);
  }

  public boolean placeBlock(BlockPos blockPos, BlockState blockState) {
    var level = this.getPerformer().getLevel();

    if (level.isClientSide() || !level.setBlockAndUpdate(blockPos, blockState)) {
      return false;
    }

    blockState.getBlock().setPlacedBy(level, blockPos, blockState,
        this.getPerformer().getEntity(), this.getItemStack());
    this.getType().getBlockPlacementHandler().accept(this.performer, blockPos);
    if (this.performer.getEntity() instanceof ServerPlayer player) {
      CriteriaTriggers.PLACED_BLOCK.trigger(player, blockPos, this.getItemStack());
    }

    return true;
  }

  @Override
  public ActionObserver createPerformerObserver() {
    return ActionObserver.create(this, ProgressBar.create(this.getType(), null, this::getProgress));
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public abstract BuildActionType getType();

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);
    if (!reason.isCompleted()) {
      return;
    }

    this.placements.forEach(
        placement -> this.placeBlock(placement.blockPos(), placement.blockState()));
  }

  protected record Placement(BlockPos blockPos, BlockState blockState,
      BlockState existingBlockState) {}
}
