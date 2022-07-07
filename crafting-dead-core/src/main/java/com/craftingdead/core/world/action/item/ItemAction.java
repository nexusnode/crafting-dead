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
  private ItemStack originalStack;

  public ItemAction(InteractionHand hand) {
    this.hand = hand;
  }

  public InteractionHand getHand() {
    return this.hand;
  }

  public ItemStack getItemStack() {
    return this.performer().entity().getItemInHand(this.hand);
  }

  public int getTicksUsingItem() {
    return this.performer().entity().getTicksUsingItem();
  }

  private boolean checkHeldItem() {
    return this.type().getHeldItemPredicate().test(this.getItemStack());
  }

  @Override
  public boolean start(boolean simulate) {
    if (this.checkHeldItem()) {
      if (!simulate) {
        this.originalStack = getItemStack();
        if (!this.performer().level().isClientSide()) {
          this.performer().entity().startUsingItem(this.hand);
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public void stop(StopReason reason) {
    this.performer().entity().stopUsingItem();
    if (!reason.isCompleted()) {
      return;
    }

    final var heldStack = this.getItemStack();

    final var resultStack = this.getResultItem(this.performer())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);

    if (this.shouldConsumeItem(this.performer())) {
      heldStack.shrink(1);
    }

    if (!resultStack.isEmpty()) {
      if (heldStack.isEmpty()) {
        this.performer().entity().setItemInHand(this.hand, resultStack);
      } else if (this.performer().entity() instanceof Player player
          && player.getInventory().add(resultStack)) {
        this.performer().entity().spawnAtLocation(resultStack);
      }
    }

    this.type().getFinishSound().ifPresent(
        sound -> this.performer().entity().playSound(sound, 1.0F, 1.0F));
  }

  protected boolean shouldConsumeItem(LivingExtension<?, ?> performer) {
    return this.type().shouldConsumeItem() &&
        !(performer.entity() instanceof Player player && player.isCreative())
        || this.type().shouldConsumeItemInCreative();
  }

  protected Optional<Item> getResultItem(LivingExtension<?, ?> performer) {
    return (!this.type().useResultItemInCreative()
        && performer.entity() instanceof Player player
        && player.isCreative()) ? Optional.empty() : this.type().getResultItem();
  }

  @Override
  public boolean tick() {
    if (!this.performer().entity().isUsingItem()
        || this.originalStack != this.getItemStack()) {
      this.performer().cancelAction(true);
      return false;
    }

    if (this.type().isFreezeMovement()) {
      this.performer().setMovementBlocked(true);
    }

    return this.performer().entity().getUseItemRemainingTicks() == 1;
  }

  public float getProgress(float partialTicks) {
    if (!this.performer().entity().isUsingItem()) {
      return 0.0F;
    } else {
      return (float) (this.performer().entity().getTicksUsingItem() + partialTicks)
          / this.type().getDurationTicks();
    }
  }

  @Override
  public abstract ItemActionType<?> type();
}
