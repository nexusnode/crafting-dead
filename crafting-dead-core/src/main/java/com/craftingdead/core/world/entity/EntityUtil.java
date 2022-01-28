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

package com.craftingdead.core.world.entity;

import com.craftingdead.core.util.MutableVector2f;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityUtil {

  private static final MutableVector2f ROTATIONS_TO_TARGET = new MutableVector2f();

  /**
   * Determines whether the viewer entity can see the target entity. Blocks in front of viewer are
   * ignored.
   *
   * @param viewer
   * @param target
   * @param fov - in degrees
   */
  public static boolean canSee(LivingEntity viewer, Entity target, float fov) {
    getRotationsToTarget(viewer, target, ROTATIONS_TO_TARGET);
    float yawToTarget = ROTATIONS_TO_TARGET.getX();
    float pitchToTarget = ROTATIONS_TO_TARGET.getY();

    float currentYaw = Mth.wrapDegrees(viewer.yHeadRot);
    float currentPitch = Mth.wrapDegrees(viewer.getXRot());

    float yawDifference = Math.abs(Mth.wrapDegrees(currentYaw - yawToTarget));
    float pitchDifference = Math.abs(Mth.wrapDegrees(currentPitch - pitchToTarget));

    boolean insideYaw = yawDifference <= fov;
    boolean insidePitch = pitchDifference <= fov;
    return insideYaw && insidePitch;
  }

  /**
   * Gets how much yaw and pitch, respectively, the target's eye is from the viewer's eye.
   * 
   * @return float array with yaw and pitch, respectively.
   */
  public static void getRotationsToTarget(LivingEntity viewer, Entity target,
      MutableVector2f result) {
    var entityVec = target.position();
    var livingVec = viewer.position();

    var xDiff = livingVec.x - entityVec.x;
    var zDiff = entityVec.z - livingVec.z;
    var yDiff = (entityVec.y + target.getEyeHeight()) - (livingVec.y + viewer.getEyeHeight());

    var distance2D = Mth.sqrt((float) (xDiff * xDiff + zDiff * zDiff));
    float yawToTarget = (float) (Math.atan2(xDiff, zDiff) * 180.0D / Math.PI);
    float pitchToTarget = (float) (-(Math.atan2(yDiff, distance2D) * 180.0D / Math.PI));

    result.set(Mth.wrapDegrees(yawToTarget), Mth.wrapDegrees(pitchToTarget));
  }
}
