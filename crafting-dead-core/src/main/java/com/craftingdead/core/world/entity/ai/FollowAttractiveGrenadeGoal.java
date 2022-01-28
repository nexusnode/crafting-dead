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

package com.craftingdead.core.world.entity.ai;

import java.util.EnumSet;
import java.util.List;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.grenade.Grenade;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class FollowAttractiveGrenadeGoal extends Goal {

  private final Mob goalOwner;
  private final double moveSpeedMultiplier;
  private Grenade grenade;
  private int delayCounter;

  public FollowAttractiveGrenadeGoal(Mob goalOwner, double moveSpeedMultiplier) {
    this.goalOwner = goalOwner;
    this.moveSpeedMultiplier = moveSpeedMultiplier;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (this.goalOwner.hasEffect(ModMobEffects.FLASH_BLINDNESS.get())) {
      return false;
    }
    List<Grenade> list = this.goalOwner.level.getEntitiesOfClass(Grenade.class,
        this.goalOwner.getBoundingBox().inflate(20.0D, 5.0D, 20.0D));

    Grenade nearestGrenade = null;
    double lastSqDistance = Double.MAX_VALUE;

    for (Grenade grenade : list) {
      if (grenade.isAttracting()) {
        double sqDistance = this.goalOwner.distanceToSqr(grenade);
        if (sqDistance <= lastSqDistance) {
          lastSqDistance = sqDistance;
          nearestGrenade = grenade;
        }
      }
    }

    if (nearestGrenade == null || !nearestGrenade.isAlive()) {
      return false;
    }

    this.grenade = nearestGrenade;
    return true;
  }

  @Override
  public boolean canContinueToUse() {
    if (!this.grenade.isAlive()) {
      return false;
    }

    if (this.goalOwner.hasEffect(ModMobEffects.FLASH_BLINDNESS.get())) {
      return false;
    }

    if (this.grenade.level != this.goalOwner.level) {
      return false;
    }

    return this.goalOwner.distanceToSqr(this.grenade) <= 256.0D;
  }

  @Override
  public void start() {
    this.delayCounter = 0;
  }

  @Override
  public void stop() {
    this.grenade = null;
  }

  @Override
  public void tick() {
    this.goalOwner.getLookControl().setLookAt(this.grenade.getX(),
        this.grenade.getEyeY(),
        this.grenade.getZ());
    if (--this.delayCounter <= 0) {
      this.delayCounter = 5 + this.goalOwner.getRandom().nextInt(10);
      this.goalOwner.getNavigation().moveTo(this.grenade, this.moveSpeedMultiplier);
    }
  }
}
