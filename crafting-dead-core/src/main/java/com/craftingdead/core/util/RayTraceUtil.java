package com.craftingdead.core.util;

import java.util.Optional;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceUtil {

  public static Iterable<Entity> filterEntities(Entity fromEntity, Vec3d scaledLook) {
    return fromEntity
        .getEntityWorld()
        .getEntitiesInAABBexcluding(fromEntity,
            fromEntity.getBoundingBox().expand(scaledLook).grow(1.0D, 1.0D, 1.0D),
            (entityTest) -> {
              // Ignores entities that cannot be collided
              if (entityTest.isSpectator() || !entityTest.canBeCollidedWith()) {
                return false;
              }
              if (entityTest instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entityTest;

                // Ignores dead entities
                if (livingEntity.getHealth() <= 0F) {
                  return false;
                }
              }
              return true;
            });
  }

  public static Optional<EntityRayTraceResult> rayTraceEntities(Entity fromEntity) {
    IAttributeInstance reachDistanceAttribute =
        fromEntity instanceof PlayerEntity
            ? ((PlayerEntity) fromEntity).getAttribute(PlayerEntity.REACH_DISTANCE)
            : null;
    double distance = reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue();
    Vec3d start = fromEntity.getEyePosition(1.0F);
    Vec3d look = fromEntity.getLook(1.0F);
    Vec3d scaledLook = look.scale(distance);
    Vec3d end = start.add(scaledLook);
    return RayTraceUtil.rayTraceEntities(fromEntity, start, end,
        filterEntities(fromEntity, scaledLook), distance * distance);
  }

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

    return Optional
        .ofNullable(
            finalHitEntity != null ? new EntityRayTraceResult(finalHitEntity, finalHitVec) : null);
  }

  public static Optional<BlockRayTraceResult> rayTraceBlocks(LivingEntity fromEntity,
      RayTraceContext.FluidMode fluidMode, double distance, float partialTicks) {
    Vec3d start = fromEntity.getEyePosition(partialTicks);
    Vec3d look = fromEntity.getLook(partialTicks);
    Vec3d scaledLook = look.scale(distance);
    Vec3d end = start.add(scaledLook);
    return Optional.ofNullable(fromEntity.world.rayTraceBlocks(new RayTraceContext(start, end,
        RayTraceContext.BlockMode.COLLIDER, fluidMode, fromEntity)));
  }

  /**
   * Perform a full ray trace from the parsed entity.
   *
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> rayTrace(final Entity fromEntity,
      final double distance, final float accuracy, final Random random) {
    return rayTrace(fromEntity, distance, 1.0F, accuracy, random);
  }

  /**
   * Perform a full ray trace from the parsed entity.
   *
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @param partialTicks - the partialTicks
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> rayTrace(final Entity fromEntity,
      final double distance, final float partialTicks, final float accuracy, final Random random) {
    Vec3d start = fromEntity.getEyePosition(partialTicks);
    Vec3d look = fromEntity.getLook(partialTicks);

    if (accuracy < 1.0F) {
      look = look
          .add(
              (1.0F - accuracy) / 7.5F * random.nextFloat() * (random.nextBoolean() ? -1.0F : 1.0F),
              0, (1.0F - accuracy) / 7.5F * random.nextFloat()
                  * (random.nextBoolean() ? -1.0F : 1.0F));
    }

    Vec3d scaledLook = look.scale(distance);
    Vec3d end = start.add(scaledLook);

    Optional<BlockRayTraceResult> blockRayTraceResult = Optional
        .ofNullable(fromEntity.world
            .rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER,
                RayTraceContext.FluidMode.NONE, fromEntity)));

    final double sqrDistance = blockRayTraceResult.isPresent()
        ? blockRayTraceResult.get().getHitVec().squareDistanceTo(start)
        : distance * distance;

    Optional<EntityRayTraceResult> entityRayTraceResult = rayTraceEntities(fromEntity, start, end,
        filterEntities(fromEntity, scaledLook), sqrDistance);

    return entityRayTraceResult.isPresent() ? entityRayTraceResult : blockRayTraceResult;
  }
}
