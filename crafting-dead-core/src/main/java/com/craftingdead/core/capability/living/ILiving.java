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

package com.craftingdead.core.capability.living;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nonnull;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ILiving<T extends LivingEntity, E extends ILivingExtension>
    extends ILivingExtension {

  /**
   * @see {@link net.minecraftforge.event.AttachCapabilitiesEvent}
   */
  static final ResourceLocation CAPABILITY_KEY = new ResourceLocation(CraftingDead.ID, "living");

  /**
   * Load this {@link ILiving} - called upon capability attachment.
   */
  void load();

  /**
   * Get an extension by its ID.
   * 
   * @param id - the extension's ID
   * @return an {@link Optional} extension
   */
  Optional<E> getExtension(ResourceLocation id);

  /**
   * Get an expected extension.
   * 
   * @param id - the extension's ID
   * @throws IllegalStateException if not present
   * @return the extension
   */
  @Nonnull
  E getExpectedExtension(ResourceLocation id) throws IllegalStateException;

  /**
   * Register an extension in which events from this {@link ILiving} will be passed to.
   * 
   * @param id - the extension's ID
   * @param extension - the extension to attach
   */
  void registerExtension(ResourceLocation id, E extension);

  /**
   * Perform an unforced action.
   * 
   * @param action - the {@link IAction} to perform
   * @param sendUpdate - alert the other side (client/server) of this change
   * @return whether the {@link IAction} is being performed
   */
  default boolean performAction(IAction action, boolean sendUpdate) {
    return this.performAction(action, false, sendUpdate);
  }

  /**
   * Perform the specified {@link IAction}.
   * 
   * @param action - the {@link IAction} to perform
   * @param force - whether this {@link IAction} will be forced (will override an existing action)
   * @param sendUpdate - alert the other side (client/server) of this change
   * @return whether the {@link IAction} is being performed
   */
  boolean performAction(IAction action, boolean force, boolean sendUpdate);

  /**
   * Cancel the current {@link IAction} being performed.
   * 
   * @param sendUpdate- alert the other side (client/server) of this change
   */
  void cancelAction(boolean sendUpdate);

  /**
   * Attach a progress monitor to this {@link ILiving} (will show a progress bar on the client).
   * 
   * @param actionProgress - the {@link IProgressMonitor} to attach
   */
  void setActionProgress(IProgressMonitor actionProgress);

  /**
   * Get the currently attached {@link IProgressMonitor} monitor.
   * 
   * @return an {@link Optional} progress monitor
   * @see #setActionProgress(IProgressMonitor)
   */
  Optional<IProgressMonitor> getProgressMonitor();

  /**
   * Whether this {@link ILiving} is currently monitoring an action.
   * 
   * @return true if it is monitoring an action.
   */
  default boolean isMonitoringAction() {
    return this.getProgressMonitor().isPresent();
  }

  /**
   * Prevent this {@link ILiving} from moving (for the current tick).
   * 
   * @param movementBlocked - if the movement should be blocked
   */
  void setMovementBlocked(boolean movementBlocked);

  /**
   * Whether this {@link ILiving} is currently moving.
   * 
   * @return true if moving
   */
  boolean isMoving();

  /**
   * Gets the {@link IItemHandler} associated with this {@link ILiving} (used for the mod's
   * equipment storage)
   * 
   * @return the {@link IItemHandler}
   * @see {@link com.craftingdead.core.inventory.InventorySlotType}
   */
  IItemHandlerModifiable getItemHandler();

  /**
   * Get an {@link EntitySnapshot} for the specified tick time.
   * 
   * @param tick - the tick in which to retrieve the {@link EntitySnapshot} for
   * @return the snapshot
   * @throws IndexOutOfBoundsException if the tick is too small
   */
  EntitySnapshot getSnapshot(int tick) throws IndexOutOfBoundsException;

  /**
   * Whether this {@link ILiving} is crouching.
   * 
   * @return true if crouching
   */
  boolean isCrouching();

  /**
   * Makes this {@link ILiving} crouch.
   * 
   * @param crouching - crouching state
   * @param sendUpdate - alert the other side (client/server) of this change
   */
  void setCrouching(boolean crouching, boolean sendUpdate);

  /**
   * Get a modified gun accuracy for this {@link ILiving}. E.g. walking will reduce accuracy.
   * 
   * @param accuracy - the accuracy to modify
   * @param random - a {@link Random} instance
   * @return the modified accuracy
   */
  default float getModifiedAccuracy(float accuracy, Random random) {
    if (this.isMoving()) {
      accuracy -= 0.15F;
    } else if (this.isCrouching()) {
      accuracy += 0.15F;
    }

    // Apply some random accuracy for non-players (bots)
    if (!(this.getEntity() instanceof PlayerEntity)) {
      accuracy -= random.nextFloat();
    }

    return accuracy;
  }

  /**
   * Get the {@link LivingEntity} associated with this {@link ILiving}.
   * 
   * @return the {@link LivingEntity}
   */
  T getEntity();

  /**
   * Get an expected {@link ILiving} instance.
   * 
   * @param <E> - the specific type of {@link LivingEntity}
   * @param livingEntity - the {@link LivingEntity} instance
   * @throws IllegalStateException if not present
   * @return the {@link ILiving} instance
   * @see #getOptional(LivingEntity)
   */
  @Nonnull
  public static <E extends LivingEntity> ILiving<E, ?> getExpected(E livingEntity)
      throws IllegalStateException {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .<ILiving<E, ?>>cast()
        .orElseThrow(() -> new IllegalStateException("Missing living capability " + livingEntity));
  }

  /**
   * Get an optional {@link ILiving} instance.
   * 
   * @param <E> - the specific type of {@link LivingEntity}
   * @param livingEntity - the {@link LivingEntity} instance
   * @return an {@link Optional} instance
   * @see #getExpected(LivingEntity)
   */
  public static <R extends ILiving<E, ?>, E extends LivingEntity> Optional<R> getOptional(
      E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .<R>cast()
        .map(Optional::of)
        .orElse(Optional.empty());
  }

  /**
   * Progress monitor used to track the progress of actions.
   */
  public static interface IProgressMonitor {

    /**
     * The message to display to a {@link ILiving} that is monitoring the action.
     * 
     * @return a {@link ITextComponent} instance
     */
    ITextComponent getMessage();

    /**
     * Get an optional sub-message.
     * 
     * @return an {@link Optional} sub-message.
     * @see #getMessage()
     */
    Optional<ITextComponent> getSubMessage();

    /**
     * Get the progress percentage (0 - 1) of the action.
     * 
     * @param partialTicks
     * @return
     */
    float getProgress(float partialTicks);

    /**
     * Stop the underlying action being performed.
     */
    void stop();
  }
}
