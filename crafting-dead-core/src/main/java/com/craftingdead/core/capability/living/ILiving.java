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
package com.craftingdead.core.capability.living;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ILiving<E extends LivingEntity, L extends ILivingHandler> extends ILivingHandler {

  public static final ResourceLocation ID = new ResourceLocation(CraftingDead.ID, "living");

  Optional<L> getExtension(ResourceLocation id);

  void registerExtension(ResourceLocation id, L extension);

  default boolean performAction(IAction action, boolean sendUpdate) {
    return this.performAction(action, false, sendUpdate);
  }

  boolean performAction(IAction action, boolean force, boolean sendUpdate);

  void cancelAction(boolean sendUpdate);

  void setActionProgress(IActionProgress actionProgress);

  Optional<IActionProgress> getActionProgress();

  void setMovementBlocked(boolean movementFrozen);

  boolean isMovementBlocked();

  boolean isMoving();

  IItemHandlerModifiable getItemHandler();

  Optional<EntitySnapshot> getSnapshot(long tick);

  boolean isCrouching();

  void setCrouching(boolean crouching, boolean sendUpdate);

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

  E getEntity();

  public static <E extends LivingEntity> ILiving<E, ?> getExpected(E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .<ILiving<E, ?>>cast()
        .orElseThrow(() -> new IllegalStateException("Missing living capability " + livingEntity));
  }

  public static <R extends ILiving<E, ?>, E extends LivingEntity> Optional<R> getOptional(
      E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .<R>cast()
        .map(Optional::of)
        .orElse(Optional.empty());
  }

  public static interface IActionProgress {

    ITextComponent getMessage();

    @Nullable
    ITextComponent getSubMessage();

    float getProgress(float partialTicks);

    void stop();
  }
}
