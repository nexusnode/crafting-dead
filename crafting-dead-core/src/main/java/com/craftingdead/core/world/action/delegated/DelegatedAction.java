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

package com.craftingdead.core.world.action.delegated;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public interface DelegatedAction {

  /**
   * Checks if this {@link DelegatedAction} can be performed and if so, is continued to be called every
   * tick.
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
  boolean shouldShrinkStack(LivingExtension<?, ?> performer);

  /**
   * Get the item that's returned upon completion.
   * 
   * @param performer - the {@link LivingExtension} performing the {@link Action}
   * @return the optional {@link Item}
   */
  Optional<Item> getReturnItem(LivingExtension<?, ?> performer);

  /**
   * Get the sound to be played upon completion.
   * 
   * @return the {@link SoundEvent}
   */
  Optional<SoundEvent> getFinishSound();
}
