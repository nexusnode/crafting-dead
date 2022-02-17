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

package com.craftingdead.core.world.action.delegate;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvent;

public interface DelegateAction {

  /**
   * Checks if this {@link DelegateAction} can be performed and if so, is continued to be called
   * every tick.
   * 
   * @param performer - the {@link LivingExtension} performing the {@link Action}
   * @param target - the {@link LivingExtension} being targeted
   * @param heldStack - the current held stack
   * @return if the action should continue running
   */
  boolean canPerform(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack);

  /**
   * Complete the action.
   * 
   * @param performer - the {@link LivingExtension} performing the {@link Action}
   * @param target - the {@link LivingExtension} being targeted
   * @param heldStack - the current held stack
   * @return if the action completed successfully
   */
  boolean finish(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack);

  /**
   * Determines if the held stack should be consumed upon completion.
   * 
   * @param performer - the {@link LivingExtension} performing the {@link Action}
   * @return if the stack should be consumed
   */
  boolean shouldConsumeItem(LivingExtension<?, ?> performer);

  /**
   * Get the resultant item.
   * 
   * @param performer - the {@link LivingExtension} performing the {@link Action}
   * @return the optional {@link Item}
   */
  Optional<Item> getResultItem(LivingExtension<?, ?> performer);

  /**
   * Get the sound to be played upon completion.
   * 
   * @return the {@link SoundEvent}
   */
  Optional<SoundEvent> getFinishSound();
}
