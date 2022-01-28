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
import com.craftingdead.core.world.effect.ModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class LookAtEntityGoal<T extends Entity> extends Goal {

  private final Mob goalOwner;
  private final float maxDistance;
  private final float chance;
  private final Class<T> watchedClass;
  private int lookDuration;
  private Entity watchedEntity;

  public LookAtEntityGoal(Mob goalowner, Class<T> watchedClass, float maxDistance) {
    this(goalowner, watchedClass, maxDistance, 0.02F);
  }

  public LookAtEntityGoal(Mob goalOwner, Class<T> watchedClass, float maxDistance,
      float chance) {
    this.goalOwner = goalOwner;
    this.watchedClass = watchedClass;
    this.maxDistance = maxDistance;
    this.chance = chance;
    this.setFlags(EnumSet.of(Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (this.goalOwner.hasEffect(ModMobEffects.FLASH_BLINDNESS.get())) {
      return false;
    }

    if (this.goalOwner.getRandom().nextFloat() >= this.chance) {
      return false;
    }

    var possibleEntities =
        this.goalOwner.level.getEntitiesOfClass(this.watchedClass, this.goalOwner
            .getBoundingBox().inflate((double) this.maxDistance, 4.0D, (double) this.maxDistance));

    double lastSqDistance = Double.MAX_VALUE;

    for (var grenade : possibleEntities) {
      double sqDistance = this.goalOwner.distanceToSqr(grenade);
      if (sqDistance <= lastSqDistance) {
        lastSqDistance = sqDistance;
        this.watchedEntity = grenade;
      }
    }

    return this.watchedEntity != null;
  }

  @Override
  public boolean canContinueToUse() {
    if (!this.watchedEntity.isAlive()) {
      return false;
    }

    if (this.goalOwner.hasEffect(ModMobEffects.FLASH_BLINDNESS.get())) {
      return false;
    }

    if (this.goalOwner
        .distanceToSqr(this.watchedEntity) > (double) (this.maxDistance * this.maxDistance)) {
      return false;
    }

    return this.lookDuration > 0;
  }

  @Override
  public void start() {
    this.lookDuration = 40 + this.goalOwner.getRandom().nextInt(40);
  }

  @Override
  public void stop() {
    this.watchedEntity = null;
  }

  @Override
  public void tick() {
    this.goalOwner.getLookControl().setLookAt(this.watchedEntity.getX(),
        this.watchedEntity.getEyeY(), this.watchedEntity.getZ());
    --this.lookDuration;
  }
}
