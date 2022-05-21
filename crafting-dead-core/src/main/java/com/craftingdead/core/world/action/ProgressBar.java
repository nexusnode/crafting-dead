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

package com.craftingdead.core.world.action;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

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

  static ProgressBar create(ActionType<?> actionType, @Nullable Component subMessage,
      FloatUnaryOperator progress) {
    return new ProgressBar() {
      @Override
      public Component getMessage() {
        return new TranslatableComponent(actionType.makeDescriptionId() + ".message");
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
  }
}
