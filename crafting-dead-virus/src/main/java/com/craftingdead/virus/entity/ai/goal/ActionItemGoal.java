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
