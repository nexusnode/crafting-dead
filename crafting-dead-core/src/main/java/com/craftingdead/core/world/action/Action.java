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

package com.craftingdead.core.world.action;

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.LivingExtension;

public interface Action {

  /**
   * A prerequisite check to verify if this action is able to start.
   * 
   * @return if the action can start
   */
  boolean start();

  /**
   * Ticks the {@link Action} and determines if it should continue running.
   * 
   * @return if the action is finished
   */
  boolean tick();

  void cancel();

  LivingExtension<?, ?> getPerformer();

  LivingExtension.ProgressMonitor getPerformerProgress();

  Optional<LivingExtension<?, ?>> getTarget();

  LivingExtension.ProgressMonitor getTargetProgress();

  ActionType getType();
}
