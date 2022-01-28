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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.EntityDamageSource;

public class KillFeedDamageSource extends EntityDamageSource implements KillFeedProvider {

  private final ItemStack itemStack;
  private final KillFeedEntry.Type killFeedType;

  public KillFeedDamageSource(String damageTypeIn, LivingEntity source,
      ItemStack itemStack, KillFeedEntry.Type killFeedType) {
    super(damageTypeIn, source);
    this.itemStack = itemStack;
    this.killFeedType = killFeedType;
  }

  @Override
  public KillFeedEntry createKillFeedEntry(Player killedEntity) {
    return new KillFeedEntry(this.entity.getId(), this.entity.getDisplayName(),
        killedEntity.getDisplayName(), this.itemStack, this.killFeedType);
  }
}
