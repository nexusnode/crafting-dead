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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.delegate.DelegateActionType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemActionType extends ActionType {

  private final List<DelegateActionType> delegateActions;
  private final boolean freezeMovement;
  private final int totalDurationTicks;
  private final Predicate<ItemStack> heldItemPredicate;

  private ItemActionType(boolean triggeredByClient, List<DelegateActionType> delegateActions,
      boolean freezeMovement, int totalDurationTicks, Predicate<ItemStack> heldItemPredicate) {
    super(triggeredByClient, ItemAction::new);
    this.delegateActions = delegateActions;
    this.freezeMovement = freezeMovement;
    this.totalDurationTicks = totalDurationTicks;
    this.heldItemPredicate = heldItemPredicate;
  }

  public List<DelegateActionType> getDelegateActions() {
    return Collections.unmodifiableList(this.delegateActions);
  }

  public boolean isFreezeMovement() {
    return this.freezeMovement;
  }

  public int getTotalDurationTicks() {
    return this.totalDurationTicks;
  }

  public Predicate<ItemStack> getHeldItemPredicate() {
    return this.heldItemPredicate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private boolean triggeredByClient;
    private final List<DelegateActionType> delegateActions = new ArrayList<>();
    private boolean freezeMovement;
    private int totalDurationTicks = 32;
    private Predicate<ItemStack> heldItemPredicate;

    public Builder setTriggeredByClient(boolean triggeredByClient) {
      this.triggeredByClient = triggeredByClient;
      return this;
    }

    public Builder delegate(DelegateActionType entry) {
      return this.addDelegatedAction(() -> true, entry);
    }

    public Builder addDelegatedAction(BooleanSupplier condition, DelegateActionType entry) {
      if (condition.getAsBoolean() && !this.delegateActions.contains(entry)) {
        this.delegateActions.add(entry);
      }
      return this;
    }

    public Builder setFreezeMovement(boolean freezeMovement) {
      this.freezeMovement = freezeMovement;
      return this;
    }

    public Builder duration(int totalDurationTicks) {
      this.totalDurationTicks = totalDurationTicks;
      return this;
    }

    public Builder forItem(Supplier<Item> item) {
      return this.forItem(itemStack -> itemStack.is(item.get()));
    }

    public Builder forItem(Predicate<ItemStack> heldItemPredicate) {
      this.heldItemPredicate = heldItemPredicate;
      return this;
    }

    public ItemActionType build() {
      return new ItemActionType(this.triggeredByClient, this.delegateActions, this.freezeMovement,
          this.totalDurationTicks, this.heldItemPredicate);
    }
  }
}
