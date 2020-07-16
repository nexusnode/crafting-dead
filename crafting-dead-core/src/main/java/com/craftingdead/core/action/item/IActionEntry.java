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
