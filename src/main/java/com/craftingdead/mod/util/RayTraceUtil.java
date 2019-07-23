package com.craftingdead.mod.util;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

@UtilityClass
public class RayTraceUtil {

  /**
   * Ray trace the specified entities from the parsed entity.
   * 
   * @param fromEntity - the entity performing the ray trace
   * @param start - the start {@link Vec3d}
   * @param end - the end {@link Vec3d}
   * @param entities - the entities to ray trace
   * @param sqrDistance - the square distance
   * @return the {@link EntityRayTraceResult} as an {@link Optional}
   */
  public static Optional<EntityRayTraceResult> rayTraceEntities(Entity fromEntity, Vec3d start,
      Vec3d end, Iterable<Entity> entities, double sqrDistance) {
    Entity finalHitEntity = null;
    Vec3d finalHitVec = null;

    double sqrDistanceToLastHit = sqrDistance;
    for (Entity otherEntity : entities) {
      AxisAlignedBB otherEntityCollisionBox =
          otherEntity.getBoundingBox().grow(otherEntity.getCollisionBorderSize());
      Optional<Vec3d> potentialHit = otherEntityCollisionBox.rayTrace(start, end);

      if (otherEntityCollisionBox.contains(start) && sqrDistanceToLastHit >= 0.0D) {
        finalHitEntity = otherEntity;
        finalHitVec = potentialHit.orElse(start);
        sqrDistanceToLastHit = 0.0D;
      } else if (potentialHit.isPresent()) {
        Vec3d hitVec = potentialHit.get();
        double sqrDistanceToHit = start.squareDistanceTo(hitVec);
        if (sqrDistanceToHit < sqrDistanceToLastHit || sqrDistanceToLastHit == 0.0D) {
          if (otherEntity.getLowestRidingEntity() == fromEntity.getLowestRidingEntity()) {
            if (sqrDistanceToHit == 0.0D) {
              finalHitEntity = otherEntity;
              finalHitVec = hitVec;
            }
          } else {
            finalHitEntity = otherEntity;
            finalHitVec = hitVec;
            sqrDistanceToLastHit = sqrDistanceToHit;
          }
        }
      }
    }

    return Optional.ofNullable(
        finalHitEntity != null ? new EntityRayTraceResult(finalHitEntity, finalHitVec) : null);
  }

  /**
   * Perform a full ray trace from the parsed entity.
   * 
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> rayTrace(final Entity fromEntity,
      final double distance) {
    return traceAllObjects(fromEntity, distance, 1.0F);
  }

  /**
   * Perform a full ray trace from the parsed entity.
   * 
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @param partialTicks - the partialTicks
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> traceAllObjects(final Entity fromEntity,
      final double distance, final float partialTicks) {
    Vec3d start = fromEntity.getEyePosition(partialTicks);
    Vec3d look = fromEntity.getLook(partialTicks);
    Vec3d scaledLook = look.scale(distance);
    Vec3d end = start.add(scaledLook);

    Optional<BlockRayTraceResult> blockRayTraceResult =
        Optional.ofNullable(fromEntity.world.rayTraceBlocks(new RayTraceContext(start, end,
            RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, fromEntity)));

    AxisAlignedBB boundingBox =
        fromEntity.getBoundingBox().expand(scaledLook).grow(1.0D, 1.0D, 1.0D);
    double sqrDistance = blockRayTraceResult.isPresent()
        ? blockRayTraceResult.get().getHitVec().squareDistanceTo(start)
        : distance * distance;

    Optional<EntityRayTraceResult> entityRayTraceResult = rayTraceEntities(fromEntity, start, end,
        fromEntity.getEntityWorld().getEntitiesInAABBexcluding(fromEntity, boundingBox,
            (entityTest) -> !entityTest.isSpectator() && entityTest.canBeCollidedWith()),
        sqrDistance);

    return entityRayTraceResult.isPresent() ? entityRayTraceResult : blockRayTraceResult;
  }
}
