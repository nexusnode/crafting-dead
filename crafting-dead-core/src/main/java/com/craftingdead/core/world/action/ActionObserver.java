package com.craftingdead.core.world.action;

import java.util.Optional;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@FunctionalInterface
public interface ActionObserver {

  Action getAction();

  default Optional<ProgressBar> getProgressBar() {
    return Optional.empty();
  }

  public static ActionObserver create(Action action, @Nullable Component subMessage,
      FloatUnaryOperator progress) {

    var progressBar = new ProgressBar() {
      @Override
      public Component getMessage() {
        return new TranslatableComponent(action.getType().makeDescriptionId() + ".message");
      }

      @Override
      public Optional<Component> getSubMessage() {
        return Optional.ofNullable(subMessage);
      }

      @Override
      public float getProgress(float partialTick) {
        return progress.apply(partialTick);
      }
    };

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
