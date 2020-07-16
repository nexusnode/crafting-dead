package com.craftingdead.core.action;

import java.util.Optional;
import com.craftingdead.core.capability.living.ILiving;

public interface IAction {

  /**
   * A prerequisite check to verify if this action is able to start.
   * 
   * @return if the action can start
   */
  boolean start();

  /**
   * Ticks the {@link IAction} and determines if it should continue running.
   * 
   * @return if the action is finished
   */
  boolean tick();

  void cancel();

  ILiving<?> getPerformer();

  ILiving.IActionProgress getPerformerProgress();

  Optional<ILiving<?>> getTarget();

  ILiving.IActionProgress getTargetProgress();

  ActionType<?> getActionType();
}
