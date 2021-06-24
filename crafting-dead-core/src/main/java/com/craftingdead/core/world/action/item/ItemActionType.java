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
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.delegated.DelegatedActionType;
import net.minecraft.item.ItemStack;

public class ItemActionType extends ActionType {

  private final List<DelegatedActionType> delegatedActions;
  private final boolean freezeMovement;
  private final int totalDurationTicks;
  private final Predicate<ItemStack> heldItemPredicate;

  private ItemActionType(boolean triggeredByClient, List<DelegatedActionType> delegatedActions,
      boolean freezeMovement, int totalDurationTicks, Predicate<ItemStack> heldItemPredicate) {
    super(triggeredByClient, ItemAction::new);
    this.delegatedActions = delegatedActions;
    this.freezeMovement = freezeMovement;
    this.totalDurationTicks = totalDurationTicks;
    this.heldItemPredicate = heldItemPredicate;
  }

  public List<DelegatedActionType> getDelegatedActionTypes() {
    return Collections.unmodifiableList(this.delegatedActions);
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
    private final List<DelegatedActionType> delegatedActions = new ArrayList<>();
    private boolean freezeMovement;
    private int totalDurationTicks = 32;
    private Predicate<ItemStack> heldItemPredicate;

    public Builder setTriggeredByClient(boolean triggeredByClient) {
      this.triggeredByClient = triggeredByClient;
      return this;
    }

    public Builder addDelegatedAction(DelegatedActionType entry) {
      return this.addDelegatedAction(() -> true, entry);
    }

    public Builder addDelegatedAction(BooleanSupplier condition, DelegatedActionType entry) {
      if (condition.getAsBoolean() && !this.delegatedActions.contains(entry)) {
        this.delegatedActions.add(entry);
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

    public Builder setHeldItemPredicate(Predicate<ItemStack> heldItemPredicate) {
      this.heldItemPredicate = heldItemPredicate;
      return this;
    }

    public ItemActionType build() {
      return new ItemActionType(this.triggeredByClient, this.delegatedActions, this.freezeMovement,
          this.totalDurationTicks, this.heldItemPredicate);
    }
  }
}
