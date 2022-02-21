package com.craftingdead.core.world.action;

import java.util.Optional;

@FunctionalInterface
public interface ActionObserver {

  Action getAction();

  default Optional<ProgressBar> getProgressBar() {
    return Optional.empty();
  }

  static ActionObserver create(Action action, ProgressBar progressBar) {
    return new ActionObserver() {

      @Override
      public Action getAction() {
        return action;
      }

      @Override
      public Optional<ProgressBar> getProgressBar() {
        return Optional.of(progressBar);
      }
    };
  }
}
