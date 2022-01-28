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

import java.util.Collection;
import com.craftingdead.core.network.Synched;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public interface LivingHandler extends INBTSerializable<CompoundTag>, Synched {

  default void tick() {}

  /**
   * When this entity is damaged; with potions and armour taken into account.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return the new damage amount
   */
  default float handleDamaged(DamageSource source, float amount) {
    return amount;
  }

  /**
   * When this entity is hurt.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return if the event should be cancelled
   */
  default boolean handleHurt(DamageSource source, float amount) {
    return false;
  }

  /**
   * When this entity kills another entity.
   *
   * @param target - the {@link Entity} killed
   * @return if the event should be cancelled
   */
  default boolean handleKill(Entity target) {
    return false;
  }

  /**
   * When this entity's health reaches 0.
   *
   * @param cause - the cause of death
   * @return if the event should be cancelled
   */
  default boolean handleDeath(DamageSource cause) {
    return false;
  }

  /**
   * When this entity's death causes dropped items to appear.
   *
   * @param cause - the DamageSource that caused the drop to occur
   * @param loot - a collections of {@link ItemEntity}s that will be dropped
   * @return if the event should be cancelled
   */
  default boolean handleDeathLoot(DamageSource cause, Collection<ItemEntity> loot) {
    return false;
  }

  /**
   * When this entity has started to be "tracked" by the specified {@link ServerPlayerEntity} (the
   * player receives updates about this entity, e.g. motion).
   * 
   * @param playerEntity - the player tracking us
   */
  default void handleStartTracking(ServerPlayer playerEntity) {}

  /**
   * Whether the {@link LivingExtension} is allowed to move or not.
   * 
   * @return true if movement is blocked
   */
  default boolean isMovementBlocked() {
    return false;
  }

  default Visibility getVisibility() {
    return Visibility.VISIBLE;
  }

  @Override
  default CompoundTag serializeNBT() {
    return new CompoundTag();
  }

  @Override
  default void deserializeNBT(CompoundTag nbt) {}
}
