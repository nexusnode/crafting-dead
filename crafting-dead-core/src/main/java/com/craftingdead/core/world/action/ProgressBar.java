package com.craftingdead.core.world.action;

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.chat.Component;

public interface ProgressBar {

  /**
   * The message to display to a {@link LivingExtension} that is monitoring the action.
   * 
   * @return a {@link ITextComponent} instance
   */
  Component getMessage();

  /**
   * Get an optional sub-message.
   * 
   * @return an {@link Optional} sub-message.
   * @see #getMessage()
   */
  Optional<Component> getSubMessage();

  /**
   * Get the progress percentage (0.0F - 1.0F) of the action.
   * 
   * @param partialTick
   * @return
   */
  float getProgress(float partialTick);
}
