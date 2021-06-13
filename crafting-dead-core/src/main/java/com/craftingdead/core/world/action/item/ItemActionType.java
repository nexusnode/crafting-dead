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
