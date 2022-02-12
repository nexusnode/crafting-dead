/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
  public boolean start() {
    if (this.checkHeldItem()) {
      if (!this.getPerformer().getLevel().isClientSide()) {
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

    final var consumeItem = this.shouldConsumeItem(this.getPerformer());
    final var resultStack = this.getResultItem(this.getPerformer())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);

    if (consumeItem
        && !(this.getPerformer().getEntity() instanceof Player player && player.isCreative())) {
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
    return this.getType().shouldConsumeItemInCreative()
        && !(performer.getEntity() instanceof Player player && player.isCreative())
        && this.getType().shouldConsumeItem();
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
