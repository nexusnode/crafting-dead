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

package com.craftingdead.core.world.damagesource;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class KillFeedEntry {

  private final LivingEntity killerEntity;
  private final LivingEntity deadEntity;
  private final ItemStack weaponStack;
  private final Type type;

  public KillFeedEntry(LivingEntity killerEntity, LivingEntity deadEntity, ItemStack weaponStack,
      Type type) {
    this.killerEntity = killerEntity;
    this.deadEntity = deadEntity;
    this.weaponStack = weaponStack;
    this.type = type;
  }

  public LivingEntity getKillerEntity() {
    return this.killerEntity;
  }

  public LivingEntity getDeadEntity() {
    return this.deadEntity;
  }

  public ItemStack getWeaponStack() {
    return this.weaponStack;
  }

  public Type getType() {
    return this.type;
  }

  public static enum Type {
    NONE, HEADSHOT, WALLBANG, WALLBANG_HEADSHOT;
  }
}
