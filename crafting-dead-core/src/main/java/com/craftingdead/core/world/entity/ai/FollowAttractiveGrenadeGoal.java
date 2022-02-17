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
