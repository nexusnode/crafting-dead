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

package com.craftingdead.core.action.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.action.TimedAction;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class UseItemAction extends TimedAction {

  protected static final Random random = new Random();

  private final List<IActionEntry> entries;
  private final boolean freezeMovement;
  private final int totalDurationTicks;
  private final Predicate<Item> heldItemPredicate;

  private IActionEntry selectedEntry;

  private UseItemAction(ActionType<?> actionType, ILiving<?, ?> performer, ILiving<?, ?> target,
      List<IActionEntry> entries, boolean freezeMovement, int totalDurationTicks,
      Predicate<Item> heldItemPredicate) {
    super(actionType, performer, target);
    this.entries = entries;
    this.freezeMovement = freezeMovement;
    this.totalDurationTicks = totalDurationTicks;
    this.heldItemPredicate = heldItemPredicate;
  }

  public static Builder builder(ActionType<?> actionType, ILiving<?, ?> performer,
      ILiving<?, ?> target) {
    return new Builder(actionType, performer, target);
  }

  @Override
  public boolean start() {
    final ItemStack heldStack = this.performer.getEntity().getHeldItemMainhand();
    if (this.heldItemPredicate.test(heldStack.getItem())) {
      for (IActionEntry entry : this.entries) {
        if (entry.canPerform(this.performer, this.target, heldStack)) {
          this.selectedEntry = entry;
          return super.start();
        }
      }
    }
    return false;
  }

  @Override
  protected final void finish() {
    final ItemStack heldStack = this.performer.getEntity().getHeldItemMainhand();
    if (this.selectedEntry.finish(this.performer, this.target, heldStack)) {
      final boolean shrinkStack = this.selectedEntry.shouldShrinkStack(this.performer);
      final ItemStack resultStack = new ItemStack(this.selectedEntry.getReturnItem(this.performer));

      @Nullable
      final PlayerEntity playerEntity = this.performer.getEntity() instanceof PlayerEntity
          ? (PlayerEntity) this.performer.getEntity()
          : null;
      if (shrinkStack && !(playerEntity != null && playerEntity.isCreative())) {
        heldStack.shrink(1);
      }

      if (!resultStack.isEmpty()) {
        if (heldStack.isEmpty()) {
          this.performer.getEntity().setHeldItem(Hand.MAIN_HAND, resultStack);
        } else if (playerEntity != null
            && playerEntity.inventory.addItemStackToInventory(resultStack)) {
          this.performer.getEntity().entityDropItem(resultStack);
        }
      }

      if (this.selectedEntry.getFinishSound() != null) {
        this.performer.getEntity().playSound(this.selectedEntry.getFinishSound(), 1.0F, 1.0F);
      }
    }
  }

  @Override
  public boolean tick() {
    boolean finished = super.tick();
    final ItemStack heldStack = this.performer.getEntity().getHeldItemMainhand();

    final boolean usingItem;
    if (this.performer.getEntity().getEntityWorld().isRemote()) {
      ClientDist clientDist = CraftingDead.getInstance().getClientDist();
      usingItem =
          clientDist.isLocalPlayer(this.performer.getEntity()) && clientDist.isRightMouseDown();
    } else {
      usingItem = true;
    }

    if (!this.selectedEntry.canPerform(this.performer, this.target, heldStack) || !usingItem) {
      this.performer.cancelAction(true);
    }

    if (this.freezeMovement) {
      if (this.target != null) {
        this.target.setMovementBlocked(true);
      }
      this.performer.setMovementBlocked(true);
    }

    return finished;
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.totalDurationTicks;
  }

  public static class Builder {

    private final ActionType<?> actionType;
    private final ILiving<?, ?> performer;
    private final ILiving<?, ?> target;
    private final List<IActionEntry> entries = new ArrayList<>();
    private boolean freezeMovement;
    private int totalDurationTicks = 32;
    private Predicate<Item> heldItemPredicate;

    public Builder(ActionType<?> actionType, ILiving<?, ?> performer, ILiving<?, ?> target) {
      this.actionType = actionType;
      this.performer = performer;
      this.target = target;
    }

    public Builder addEntry(IActionEntry entry) {
      if (!this.entries.contains(entry)) {
        this.entries.add(entry);
      }
      return this;
    }

    public Builder setFreezeMovement(boolean freezeMovement) {
      this.freezeMovement = freezeMovement;
      return this;
    }

    public Builder setTotalDurationTicks(int totalDurationTicks) {
      this.totalDurationTicks = totalDurationTicks;
      return this;
    }

    public Builder setHeldItemPredicate(Predicate<Item> heldItemPredicate) {
      this.heldItemPredicate = heldItemPredicate;
      return this;
    }

    public UseItemAction build() {
      return new UseItemAction(this.actionType, this.performer, this.target, this.entries,
          this.freezeMovement, this.totalDurationTicks, this.heldItemPredicate);
    }
  }
}
