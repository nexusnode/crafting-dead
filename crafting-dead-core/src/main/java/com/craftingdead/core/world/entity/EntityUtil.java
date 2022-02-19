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
