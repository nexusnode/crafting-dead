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
