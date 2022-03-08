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

import java.util.Optional;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ItemAction implements Action {

  private final InteractionHand hand;

  public ItemAction(InteractionHand hand) {
    this.hand = hand;
  }

  public InteractionHand getHand() {
    return this.hand;
  }

  public ItemStack getItemStack() {
    return this.getPerformer().getEntity().getItemInHand(this.hand);
  }

  public int getTicksUsingItem() {
    return this.getPerformer().getEntity().getTicksUsingItem();
  }

  private boolean checkHeldItem() {
    return this.getType().getHeldItemPredicate().test(this.getItemStack());
  }

  @Override
  public boolean start(boolean simulate) {
    if (this.checkHeldItem()) {
      if (!this.getPerformer().getLevel().isClientSide() && !simulate) {
        this.getPerformer().getEntity().startUsingItem(this.hand);
      }
      return true;
    }
    return false;
  }

  @Override
  public void stop(StopReason reason) {
    this.getPerformer().getEntity().stopUsingItem();
    if (!reason.isCompleted()) {
      return;
    }

    final var heldStack = this.getItemStack();

    final var resultStack = this.getResultItem(this.getPerformer())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);

    if (this.shouldConsumeItem(this.getPerformer())) {
      heldStack.shrink(1);
    }

    if (!resultStack.isEmpty()) {
      if (heldStack.isEmpty()) {
        this.getPerformer().getEntity().setItemInHand(this.hand, resultStack);
      } else if (this.getPerformer().getEntity() instanceof Player player
          && player.getInventory().add(resultStack)) {
        this.getPerformer().getEntity().spawnAtLocation(resultStack);
      }
    }

    this.getType().getFinishSound().ifPresent(
        sound -> this.getPerformer().getEntity().playSound(sound, 1.0F, 1.0F));
  }

  protected boolean shouldConsumeItem(LivingExtension<?, ?> performer) {
    return this.getType().shouldConsumeItem() &&
        !(performer.getEntity() instanceof Player player && player.isCreative())
        || this.getType().shouldConsumeItemInCreative();
  }

  protected Optional<Item> getResultItem(LivingExtension<?, ?> performer) {
    return (!this.getType().useResultItemInCreative()
        && performer.getEntity() instanceof Player player
        && player.isCreative()) ? Optional.empty() : this.getType().getReturnItem();
  }

  @Override
  public boolean tick() {
    if (!this.getPerformer().getLevel().isClientSide()
        && !this.getPerformer().getEntity().isUsingItem()) {
      this.getPerformer().cancelAction(true);
      return false;
    }

    if (this.getType().isFreezeMovement()) {
      this.getPerformer().setMovementBlocked(true);
    }

    return this.getPerformer().getEntity().getUseItemRemainingTicks() == 1;
  }

  public float getProgress(float partialTicks) {
    if (!this.getPerformer().getEntity().isUsingItem()) {
      return 0.0F;
    } else {
      return (float) (this.getPerformer().getEntity().getTicksUsingItem() + partialTicks)
          / this.getType().getTotalDurationTicks();
    }
  }

  @Override
  public abstract ItemActionType<?> getType();
}
