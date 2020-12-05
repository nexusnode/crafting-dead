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
package com.craftingdead.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class EntityUtil {

  /**
   * Whether the target is inside the field of view of the viewer entity. Blocks in front of
   * view are ignored.
   *
   * @param viewer
   * @param target
   * @param fov - In degrees (e.g. 90F)
   */
  public static boolean isInsideFOV(LivingEntity viewer, Entity target, float fov) {
    float[] rotationsToTarget = getRotationsToTarget(viewer, target);
    float yawToTarget = rotationsToTarget[0];
    float pitchToTarget = rotationsToTarget[1];

    float currentYaw = MathHelper.wrapDegrees(viewer.rotationYawHead);
    float currentPitch = MathHelper.wrapDegrees(viewer.rotationPitch);

    float yawDifference = Math.abs(MathHelper.wrapDegrees(currentYaw - yawToTarget));
    float pitchDifference = Math.abs(MathHelper.wrapDegrees(currentPitch - pitchToTarget));

    boolean isInsideYaw = yawDifference <= fov;
    boolean isInsidePitch = pitchDifference <= fov;
    return isInsideYaw && isInsidePitch;
  }

  /**
   * Gets how much yaw and pitch, respectively, the target's eye is from the viewer's eye.
   * @return float array with yaw and pitch, respectively.
   */
  private static float[] getRotationsToTarget(LivingEntity viewer, Entity target) {
    Vector3d entityVec = target.getPositionVec();
    Vector3d livingVec = viewer.getPositionVec();

    double xDiff = livingVec.x - entityVec.x;
    double zDiff = entityVec.z - livingVec.z;
    double yDiff = (entityVec.y + target.getEyeHeight()) - (livingVec.y + viewer.getEyeHeight());

    double distance2D = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
    float yawToTarget = (float) (Math.atan2(xDiff, zDiff) * 180.0D / Math.PI);
    float pitchToTarget = (float) (-(Math.atan2(yDiff, distance2D) * 180.0D / Math.PI));

    return new float[] {
        MathHelper.wrapDegrees(yawToTarget),
        MathHelper.wrapDegrees(pitchToTarget)
    };
  }
}
