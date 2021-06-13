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

package com.craftingdead.core.world.entity.extension;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nonnull;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.action.Action;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface LivingExtension<E extends LivingEntity, H extends LivingHandler>
    extends LivingHandler {

  /**
   * @see {@link net.minecraftforge.event.AttachCapabilitiesEvent}
   */
  static final ResourceLocation CAPABILITY_KEY = new ResourceLocation(CraftingDead.ID, "living");

  static <E extends LivingEntity> LivingExtension<E, ?> create(E entity) {
    return new LivingExtensionImpl<>(entity);
  }

  /**
   * Load this {@link LivingExtension} - called upon capability attachment.
   */
  void load();

  /**
   * Get a handler by its ID.
   * 
   * @param id - the handler's ID
   * @return an {@link Optional} handler
   */
  Optional<H> getHandler(ResourceLocation id);

  /**
   * Get a handler or throw an exception if not present.
   * 
   * @param id - the handler's ID
   * @throws IllegalStateException if not present
   * @return the handler
   */
  @Nonnull
  H getHandlerOrThrow(ResourceLocation id) throws IllegalStateException;

  /**
   * Register a handler.
   * 
   * @param id - the handler's ID
   * @param handler - the handler to attach
   */
  void registerHandler(ResourceLocation id, H handler);

  /**
   * Perform an unforced action.
   * 
   * @param action - the {@link Action} to perform
   * @param sendUpdate - alert the other side (client/server) of this change
   * @return whether the {@link Action} is being performed
   */
  default boolean performAction(Action action, boolean sendUpdate) {
    return this.performAction(action, false, sendUpdate);
  }

  /**
   * Perform the specified {@link Action}.
   * 
   * @param action - the {@link Action} to perform
   * @param force - whether this {@link Action} will be forced (will override an existing action)
   * @param sendUpdate - alert the other side (client/server) of this change
   * @return whether the {@link Action} is being performed
   */
  boolean performAction(Action action, boolean force, boolean sendUpdate);

  /**
   * Cancel the current {@link Action} being performed.
   * 
   * @param sendUpdate- alert the other side (client/server) of this change
   */
  void cancelAction(boolean sendUpdate);

  /**
   * Attach a progress monitor to this {@link LivingExtension} (will show a progress bar on the
   * client).
   * 
   * @param actionProgress - the {@link ProgressMonitor} to attach
   */
  void setActionProgress(ProgressMonitor actionProgress);

  /**
   * Get the currently attached {@link ProgressMonitor} monitor.
   * 
   * @return an {@link Optional} progress monitor
   * @see #setActionProgress(ProgressMonitor)
   */
  Optional<ProgressMonitor> getProgressMonitor();

  /**
   * Whether this {@link LivingExtension} is currently monitoring an action.
   * 
   * @return true if it is monitoring an action.
   */
  default boolean isMonitoringAction() {
    return this.getProgressMonitor().isPresent();
  }

  /**
   * Prevent this {@link LivingExtension} from moving (for the current tick).
   * 
   * @param movementBlocked - if the movement should be blocked
   */
  void setMovementBlocked(boolean movementBlocked);

  /**
   * Whether this {@link LivingExtension} is currently moving.
   * 
   * @return true if moving
   */
  boolean isMoving();

  /**
   * Gets the {@link IItemHandler} associated with this {@link LivingExtension} (used for the mod's
   * equipment storage)
   * 
   * @return the {@link IItemHandler}
   * @see {@link com.craftingdead.core.world.inventory.ModEquipmentSlotType}
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
   * Whether this {@link LivingExtension} is crouching.
   * 
   * @return true if crouching
   */
  boolean isCrouching();

  /**
   * Makes this {@link LivingExtension} crouch.
   * 
   * @param crouching - crouching state
   * @param sendUpdate - alert the other side (client/server) of this change
   */
  void setCrouching(boolean crouching, boolean sendUpdate);

  /**
   * Get a modified gun accuracy for this {@link LivingExtension}. E.g. walking will reduce
   * accuracy.
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
   * Get the {@link LivingEntity} associated with this {@link LivingExtension}.
   * 
   * @return the {@link LivingEntity}
   */
  E getEntity();

  /**
   * Shorthand for {@link LivingEntity#level}
   * 
   * @return the {@link World}
   */
  default World getLevel() {
    return this.getEntity().level;
  }

  /**
   * Get an expected {@link LivingExtension} instance.
   * 
   * @param <E> - the specific type of {@link LivingEntity}
   * @param livingEntity - the {@link LivingEntity} instance
   * @throws IllegalStateException if not present
   * @return the {@link LivingExtension} instance
   * @see #getOptional(LivingEntity)
   */
  @Nonnull
  public static <E extends LivingEntity> LivingExtension<E, ?> getExpected(E livingEntity)
      throws IllegalStateException {
    return livingEntity.getCapability(Capabilities.LIVING)
        .<LivingExtension<E, ?>>cast()
        .orElseThrow(() -> new IllegalStateException("Missing living capability " + livingEntity));
  }

  /**
   * Progress monitor used to track the progress of actions.
   */
  public static interface ProgressMonitor {

    /**
     * The message to display to a {@link LivingExtension} that is monitoring the action.
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
