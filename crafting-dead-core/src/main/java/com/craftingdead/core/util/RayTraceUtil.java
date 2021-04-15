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

package com.craftingdead.core.util;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class RayTraceUtil {

  public static List<Entity> filterEntities(Entity fromEntity, Vector3d scaledLook) {
    return fromEntity
        .getCommandSenderWorld()
        .getEntities(fromEntity,
            fromEntity.getBoundingBox().expandTowards(scaledLook).inflate(1.0D, 1.0D, 1.0D),
            (entityTest) -> {
              // Ignores entities that cannot be collided
              if (entityTest.isSpectator() || !entityTest.isPickable()) {
                return false;
              }
              if (entityTest instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entityTest;

                // Ignores dead entities
                if (livingEntity.isDeadOrDying()) {
                  return false;
                }
              }
              return true;
            });
  }

  public static Optional<EntityRayTraceResult> rayTraceEntities(Entity fromEntity) {
    ModifiableAttributeInstance reachDistanceAttribute =
        fromEntity instanceof PlayerEntity
            ? ((PlayerEntity) fromEntity).getAttribute(ForgeMod.REACH_DISTANCE.get())
            : null;
    double distance = reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue();
    Vector3d start = fromEntity.getEyePosition(1.0F);
    Vector3d look = fromEntity.getViewVector(1.0F);
    Vector3d scaledLook = look.scale(distance);
    Vector3d end = start.add(scaledLook);
    return RayTraceUtil.rayTraceEntities(fromEntity, start, end,
        filterEntities(fromEntity, scaledLook), distance * distance);
  }

  /**
   * Ray trace the specified entities from the parsed entity.
   *
   * @param fromEntity - the entity performing the ray trace
   * @param start - the start {@link Vector3d}
   * @param end - the end {@link Vector3d}
   * @param entities - the entities to ray trace
   * @param sqrDistance - the square distance
   * @return the {@link EntityRayTraceResult} as an {@link Optional}
   */
  public static Optional<EntityRayTraceResult> rayTraceEntities(Entity fromEntity, Vector3d start,
      Vector3d end, Iterable<Entity> entities, double sqrDistance) {
    Entity finalHitEntity = null;
    Vector3d finalHitVec = null;

    double sqrDistanceToLastHit = sqrDistance;
    for (Entity otherEntity : entities) {
      AxisAlignedBB otherEntityCollisionBox =
          otherEntity.getBoundingBox().inflate(otherEntity.getPickRadius());
      Optional<Vector3d> potentialHit = otherEntityCollisionBox.clip(start, end);

      if (otherEntityCollisionBox.contains(start) && sqrDistanceToLastHit >= 0.0D) {
        finalHitEntity = otherEntity;
        finalHitVec = potentialHit.orElse(start);
        sqrDistanceToLastHit = 0.0D;
      } else if (potentialHit.isPresent()) {
        Vector3d hitVec = potentialHit.get();
        double sqrDistanceToHit = start.distanceToSqr(hitVec);
        if (sqrDistanceToHit < sqrDistanceToLastHit || sqrDistanceToLastHit == 0.0D) {
          if (otherEntity.getRootVehicle() == fromEntity.getRootVehicle()) {
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
    Vector3d start = fromEntity.getEyePosition(partialTicks);
    Vector3d look = fromEntity.getViewVector(partialTicks);
    Vector3d scaledLook = look.scale(distance);
    Vector3d end = start.add(scaledLook);
    return Optional.ofNullable(fromEntity.level.clip(new RayTraceContext(start, end,
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
      final double distance, final float accuracy, int shotCount, final Random random) {
    return rayTrace(fromEntity, distance, 1.0F, getAccuracyOffset(accuracy, shotCount, random),
        getAccuracyOffset(accuracy, shotCount, random));
  }

  public static float getAccuracyOffset(float accuracy, int shotCount, final Random random) {
    return (1.0F - (accuracy * accuracy)) * (Math.min(20, shotCount + 1) / 2.0F)
        * ((1.0F - accuracy) * (random.nextInt(9) + 1))
        * (random.nextInt(5) % 2 == 0 ? -1.0F : 1.0F);
  }

  /**
   * Perform a full ray trace from the parsed entity, ignoring blocks that are
   * {@link #isBlockPierceable}
   *
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @param partialTicks - the partialTicks
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> rayTrace(final Entity fromEntity,
      final double distance, final float partialTicks, float pitchOffset, float yawOffset) {
    Vector3d start = fromEntity.getEyePosition(partialTicks);

    Vector3d look = getVectorForRotation(fromEntity.getViewXRot(partialTicks) + pitchOffset,
        fromEntity.getViewYRot(partialTicks) + yawOffset);

    Vector3d scaledLook = look.scale(distance);
    Vector3d end = start.add(scaledLook);

    Optional<BlockRayTraceResult> blockRayTraceResult =
        rayTraceBlocksPiercing(start, distance, look, fromEntity.level);

    final double sqrDistance = blockRayTraceResult.isPresent()
        ? blockRayTraceResult.get().getLocation().distanceToSqr(start)
        : distance * distance;

    Optional<EntityRayTraceResult> entityRayTraceResult = rayTraceEntities(fromEntity, start, end,
        filterEntities(fromEntity, scaledLook), sqrDistance);

    return entityRayTraceResult.isPresent() ? entityRayTraceResult : blockRayTraceResult;
  }

  /**
   * Perform a ray trace looking for blocks, ignoring blocks that are {@link #isBlockPierceable}
   */
  public static Optional<BlockRayTraceResult> rayTraceBlocksPiercing(Vector3d start,
      double distance,
      Vector3d look,
      World world) {
    return rayTraceBlocksPiercing(start, distance, look, RayTraceContext.BlockMode.COLLIDER,
        RayTraceContext.FluidMode.NONE, world);
  }

  /**
   * Perform a ray trace looking for blocks, ignoring blocks that are {@link #isBlockPierceable}
   */
  public static Optional<BlockRayTraceResult> rayTraceBlocksPiercing(Vector3d start,
      double distance,
      Vector3d look,
      RayTraceContext.BlockMode blockMode,
      RayTraceContext.FluidMode fluidMode,
      World world) {
    Vector3d newStart = start;
    Vector3d end = start.add(look.scale(distance));
    boolean pierceableBlock = false;
    BlockRayTraceResult blockRayTraceResult = null;
    BlockPos lastBlockPos = null;
    do {
      if (newStart.distanceTo(start) >= distance) {
        break;
      }

      RayTraceContext context = new RayTraceContext(newStart, end, blockMode, fluidMode, null);
      blockRayTraceResult = world.clip(context);

      if (blockRayTraceResult != null) {
        // Not sure about this one, but I have a concern about inaccuracy of Double which could lead
        // to an endless loop
        BlockPos blockPos = blockRayTraceResult.getBlockPos();
        if (lastBlockPos != null && lastBlockPos.equals(blockPos)) {
          break;
        }
        lastBlockPos = blockPos;

        BlockState blockState = world.getBlockState(blockPos);
        pierceableBlock = !blockState.canOcclude();
        if (pierceableBlock) {
          Vector3d hitVec = blockRayTraceResult.getLocation();
          VoxelShape shape = context.getBlockShape(blockState, world, blockPos);
          if (!shape.isEmpty()) {
            AxisAlignedBB bb = shape.bounds();
            double xDist = look.x() < 0d ? hitVec.x() - bb.minX - blockPos.getX()
                : blockPos.getX() - hitVec.x() + bb.maxX;
            double yDist = look.y() < 0d ? hitVec.y() - bb.minY - blockPos.getY()
                : blockPos.getY() - hitVec.y() + bb.maxY;
            double zDist = look.z() < 0d ? hitVec.z() - bb.minZ - blockPos.getZ()
                : blockPos.getZ() - hitVec.z() + bb.maxZ;
            double xRayDist =
                Math.abs(look.x()) != 0d ? xDist / Math.abs(look.x()) : Double.MAX_VALUE;
            double yRayDist =
                Math.abs(look.y()) != 0d ? yDist / Math.abs(look.y()) : Double.MAX_VALUE;
            double zRayDist =
                Math.abs(look.z()) != 0d ? zDist / Math.abs(look.z()) : Double.MAX_VALUE;

            double rayDist = Math.min(xRayDist, Math.min(zRayDist, yRayDist));
            newStart = hitVec.add(look.scale(rayDist));
          }
        }
      }
    } while (pierceableBlock);

    return Optional.ofNullable(blockRayTraceResult);
  }

  public static Vector3d getVectorForRotation(float pitch, float yaw) {
    float pitchRad = pitch * ((float) Math.PI / 180F);
    float yawRad = -yaw * ((float) Math.PI / 180F);
    float yawCos = MathHelper.cos(yawRad);
    float yawSin = MathHelper.sin(yawRad);
    float pitchCos = MathHelper.cos(pitchRad);
    float pitchSin = MathHelper.sin(pitchRad);
    return new Vector3d((yawSin * pitchCos), (-pitchSin), (yawCos * pitchCos));
  }
}
