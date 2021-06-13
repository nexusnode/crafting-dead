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

import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.world.action.TimedAction;
import com.craftingdead.core.world.action.delegated.DelegatedAction;
import com.craftingdead.core.world.action.delegated.DelegatedActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemAction extends TimedAction<ItemActionType> {

  private DelegatedAction delegatedAction;

  ItemAction(ItemActionType type, LivingExtension<?, ?> performer, LivingExtension<?, ?> target) {
    super(type, performer, target);
  }

  @Override
  public boolean start() {
    final ItemStack heldStack = this.getPerformer().getEntity().getMainHandItem();
    if (this.getType().getHeldItemPredicate().test(heldStack)) {
      for (DelegatedActionType delegatedActionType : this.getType().getDelegatedActionTypes()) {
        DelegatedAction action = delegatedActionType.create(this).orElse(null);
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
    final ItemStack heldStack = this.getPerformer().getEntity().getMainHandItem();
    if (this.delegatedAction.finish(this.getPerformer(), this.getTarget().orElse(null),
        heldStack)) {
      final boolean shrinkStack = this.delegatedAction.shouldShrinkStack(this.getPerformer());
      final ItemStack resultStack = this.delegatedAction.getReturnItem(this.getPerformer())
          .map(Item::getDefaultInstance)
          .orElse(ItemStack.EMPTY);

      @Nullable
      final PlayerEntity playerEntity = this.getPerformer().getEntity() instanceof PlayerEntity
          ? (PlayerEntity) this.getPerformer().getEntity()
          : null;
      if (shrinkStack && !(playerEntity != null && playerEntity.isCreative())) {
        heldStack.shrink(1);
      }

      if (!resultStack.isEmpty()) {
        if (heldStack.isEmpty()) {
          this.getPerformer().getEntity().setItemInHand(Hand.MAIN_HAND, resultStack);
        } else if (playerEntity != null
            && playerEntity.inventory.add(resultStack)) {
          this.getPerformer().getEntity().spawnAtLocation(resultStack);
        }
      }

      this.delegatedAction.getFinishSound().ifPresent(
          sound -> this.getPerformer().getEntity().playSound(sound, 1.0F, 1.0F));
    }
  }

  @Override
  public boolean tick() {
    boolean finished = super.tick();
    final ItemStack heldStack = this.getPerformer().getEntity().getMainHandItem();

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
