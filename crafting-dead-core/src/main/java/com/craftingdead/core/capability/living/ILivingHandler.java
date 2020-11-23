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

import java.util.Collection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public interface ILivingHandler extends INBTSerializable<CompoundNBT> {

  void tick();

  /**
   * When this entity is damaged; with potions and armour taken into account.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return the new damage amount
   */
  default float onDamaged(DamageSource source, float amount) {
    return amount;
  }

  /**
   * When this entity is attacked.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return if the event should be cancelled
   */
  default boolean onAttacked(DamageSource source, float amount) {
    return false;
  }

  /**
   * When this entity kills another entity.
   *
   * @param target - the {@link Entity} killed
   * @return if the event should be cancelled
   */
  default boolean onKill(Entity target) {
    return false;
  }

  /**
   * When this entity's health reaches 0.
   *
   * @param cause - the cause of death
   * @return if the event should be cancelled
   */
  default boolean onDeath(DamageSource cause) {
    return false;
  }

  /**
   * When this entity's death causes dropped items to appear.
   *
   * @param cause - the DamageSource that caused the drop to occur
   * @param drops - a collections of EntityItems that will be dropped
   * @return if the event should be cancelled
   */
  default boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    return false;
  }

  /**
   * When this entity has started to be "tracked" by the specified {@link ServerPlayerEntity} (the
   * player receives updates about this entity, e.g. motion).
   * 
   * @param playerEntity - the player tracking us
   */
  default void onStartTracking(ServerPlayerEntity playerEntity) {}
}
