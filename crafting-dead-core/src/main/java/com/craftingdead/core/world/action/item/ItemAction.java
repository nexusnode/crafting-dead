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

package com.craftingdead.core.world.action.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.world.action.TimedAction;
import com.craftingdead.core.world.action.delegate.DelegateAction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemAction extends TimedAction<ItemActionType> {

  private DelegateAction delegatedAction;

  ItemAction(ItemActionType type, LivingExtension<?, ?> performer, LivingExtension<?, ?> target) {
    super(type, performer, target);
  }

  @Override
  public boolean start() {
    final var heldStack = this.getPerformer().getMainHandItem();
    if (this.getType().getHeldItemPredicate().test(heldStack)) {
      for (var delegatedActionType : this.getType().getDelegateActions()) {
        var action = delegatedActionType.create(this).orElse(null);
        if (action != null) {
          this.delegatedAction = action;
          return super.start();
        }
      }
    }
    return false;
  }

  @Override
  protected final void finish() {
    final var heldStack = this.getPerformer().getMainHandItem();
    if (this.delegatedAction.finish(
        this.getPerformer(), this.getTarget().orElse(null), heldStack)) {
      final var consumeItem = this.delegatedAction.shouldConsumeItem(this.getPerformer());
      final var resultStack = this.delegatedAction.getResultItem(this.getPerformer())
          .map(Item::getDefaultInstance)
          .orElse(ItemStack.EMPTY);

      if (consumeItem
          && !(this.getPerformer().getEntity() instanceof Player player && player.isCreative())) {
        heldStack.shrink(1);
      }

      if (!resultStack.isEmpty()) {
        if (heldStack.isEmpty()) {
          this.getPerformer().getEntity().setItemInHand(InteractionHand.MAIN_HAND, resultStack);
        } else if (this.getPerformer().getEntity() instanceof Player player
            && player.getInventory().add(resultStack)) {
          this.getPerformer().getEntity().spawnAtLocation(resultStack);
        }
      }

      this.delegatedAction.getFinishSound().ifPresent(
          sound -> this.getPerformer().getEntity().playSound(sound, 1.0F, 1.0F));
    }
  }

  @Override
  public boolean tick() {
    final var finished = super.tick();
    final ItemStack heldStack = this.getPerformer().getMainHandItem();

    boolean usingItem = true;
    if (this.getPerformer().getLevel().isClientSide()) {
      ClientDist clientDist = CraftingDead.getInstance().getClientDist();
      usingItem =
          !clientDist.isLocalPlayer(this.getPerformer().getEntity())
              || clientDist.isRightMouseDown();
    }

    if (!this.delegatedAction.canPerform(
        this.getPerformer(), this.getTarget().orElse(null), heldStack) || !usingItem) {
      this.getPerformer().cancelAction(true);
    }

    if (this.getType().isFreezeMovement()) {
      this.getTarget().ifPresent(target -> target.setMovementBlocked(true));
      this.getPerformer().setMovementBlocked(true);
    }

    return finished;
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.getType().getTotalDurationTicks();
  }
}
