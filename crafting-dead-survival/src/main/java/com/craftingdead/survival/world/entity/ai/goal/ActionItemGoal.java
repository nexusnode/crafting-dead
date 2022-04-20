/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.world.entity.ai.goal;

import java.util.function.Supplier;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.ActionItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class ActionItemGoal extends Goal {

  private final Mob entity;

  private final Supplier<Boolean> condition;
  private final Runnable callback;

  public ActionItemGoal(Mob entity, Supplier<Boolean> condition, Runnable callback) {
    this.entity = entity;
    this.condition = condition;
    this.callback = callback;
  }

  @Override
  public boolean canUse() {
    return this.condition.get()
        && this.entity.getMainHandItem().getItem() instanceof ActionItem
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
    var item = (ActionItem) this.entity.getMainHandItem().getItem();
    item.getActionType().createEntityAction(LivingExtension.getOrThrow(this.entity),
        LivingExtension.getOrThrow(this.entity.getTarget()), InteractionHand.MAIN_HAND);
    this.callback.run();
  }

  @Override
  public void stop() {}
}
