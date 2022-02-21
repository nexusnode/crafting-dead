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

package com.craftingdead.core.world.action;

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.LivingExtension;

public interface Action {

  /**
   * A prerequisite check to verify if this action is able to start.
   * 
   * @return if the action can start
   */
  boolean start(boolean simulate);

  /**
   * Ticks the {@link Action} and determines if it should continue running.
   * 
   * @return if the action is finished
   */
  boolean tick();

  void stop(StopReason reason);

  default ActionObserver createPerformerObserver() {
    return () -> this;
  }

  LivingExtension<?, ?> getPerformer();

  default Optional<LivingExtension<?, ?>> getTarget() {
    return Optional.empty();
  }

  default ActionObserver createTargetObserver() {
    return () -> this;
  }

  ActionType<?> getType();

  enum StopReason {

    CANCELLED,
    COMPLETED;

    public boolean isCompleted() {
      return this == COMPLETED;
    }
  }
}
