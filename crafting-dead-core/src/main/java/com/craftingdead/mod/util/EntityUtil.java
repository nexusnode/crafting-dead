package com.craftingdead.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
    Vec3d entityVec = target.getPositionVec();
    Vec3d livingVec = viewer.getPositionVec();

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
