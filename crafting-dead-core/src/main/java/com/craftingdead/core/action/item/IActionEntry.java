/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.action.item;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;

public interface IActionEntry {

  /**
   * Checks if this {@link IActionEntry} can be performed and if so, is continued to be called every
   * tick.
   * 
   * @param performer - the {@link ILiving} performing the {@link IAction}
   * @param target - the {@link ILiving} being targeted
   * @param heldStack - the current held stack
   * @return if the action should continue running
   */
  boolean canPerform(ILiving<?> performer, @Nullable ILiving<?> target, ItemStack heldStack);

  /**
   * Complete the action.
   * 
   * @param performer - the {@link ILiving} performing the {@link IAction}
   * @param target - the {@link ILiving} being targeted
   * @param heldStack - the current held stack
   * @return if the action completed successfully
   */
  boolean finish(ILiving<?> performer, @Nullable ILiving<?> target, ItemStack heldStack);

  /**
   * Determines if the held stack should be consumed upon completion.
   * 
   * @param performer - the {@link ILiving} performing the {@link IAction}
   * @return if the stack should be consumed
   */
  boolean shouldShrinkStack(ILiving<?> performer);

  /**
   * Get the item that's returned upon completion.
   * 
   * @param performer - the {@link ILiving} performing the {@link IAction}
   * @return an {@link IItemProvider} or null if no return item
   */
  @Nullable
  IItemProvider getReturnItem(ILiving<?> performer);

  /**
   * Get the sound to be played upon completion.
   * 
   * @return the {@link SoundEvent}
   */
  SoundEvent getFinishSound();
}
