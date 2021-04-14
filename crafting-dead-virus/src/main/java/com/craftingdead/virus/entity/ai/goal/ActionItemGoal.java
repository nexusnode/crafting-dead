package com.craftingdead.virus.entity.ai.goal;

import java.util.function.Supplier;
import com.craftingdead.core.item.ActionItem;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

public class ActionItemGoal extends Goal {

  private final MobEntity entity;

  private final Supplier<Boolean> condition;
  private final Runnable callback;

  public ActionItemGoal(MobEntity entity, Supplier<Boolean> condition, Runnable callback) {
    this.entity = entity;
    this.condition = condition;
    this.callback = callback;
  }

  @Override
  public boolean canUse() {
    return this.condition.get() && this.entity.getMainHandItem().getItem() instanceof ActionItem
        && this.entity.getTarget() != null;
  }

  @Override
  public boolean isInterruptable() {
    return false;
  }

  @Override
  public boolean canContinueToUse() {
    return false;
  }

  @Override
  public void start() {
    ActionItem item = (ActionItem) this.entity.getMainHandItem().getItem();
    item.performAction(this.entity, this.entity.getTarget());
    this.callback.run();
  }

  @Override
  public void stop() {}
}
