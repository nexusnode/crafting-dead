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
