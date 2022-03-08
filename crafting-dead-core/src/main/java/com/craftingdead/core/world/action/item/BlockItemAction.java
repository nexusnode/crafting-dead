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

package com.craftingdead.core.world.action.item;

import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.action.ProgressBar;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockItemAction extends ItemAction {

  private final BlockItemActionType type;
  private final LivingExtension<?, ?> performer;
  private final UseOnContext context;

  private BlockState blockState;

  protected BlockItemAction(BlockItemActionType type,
      LivingExtension<?, ?> performer, UseOnContext context) {
    super(context.getHand());
    this.type = type;
    this.performer = performer;
    this.context = context;
  }

  protected BlockItemAction(InteractionHand hand, BlockItemActionType type,
      LivingExtension<?, ?> performer, BlockHitResult hitResult) {
    super(hand);
    this.type = type;
    this.performer = performer;
    this.context = new UseOnContext(performer.getLevel(),
        performer.getEntity() instanceof Player player ? player : null, hand,
        performer.getEntity().getItemInHand(hand), hitResult);
  }

  public UseOnContext getContext() {
    return this.context;
  }

  public BlockState getBlockState() {
    return this.blockState;
  }

  @Override
  public ActionObserver createPerformerObserver() {
    return ActionObserver.create(this, ProgressBar.create(this.getType(), null, this::getProgress));
  }

  @Override
  public boolean start(boolean simulate) {
    this.blockState = this.performer.getLevel().getBlockState(this.context.getClickedPos());
    return super.start(simulate) && this.type.getPredicate().test(this.blockState);
  }

  @Override
  public boolean tick() {
    if (!this.performer.getLevel().isClientSide() && this.performer.getEntity()
        .distanceToSqr(Vec3.atCenterOf(this.context.getClickedPos())) > 64.0D) {
      this.performer.cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public ItemActionType<?> getType() {
    return this.type;
  }
}
