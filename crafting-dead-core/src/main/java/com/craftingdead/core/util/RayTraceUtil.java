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

import java.util.Optional;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

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
   * Perform a full ray trace from the parsed entity, ignoring blocks that are
   * {@link #isBlockPierceable}
   *
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @param partialTicks - the partialTicks
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> rayTrace(final Entity fromEntity,
      final double distance, final float partialTicks, final float accuracy, final Random random) {
    Vec3d start = fromEntity.getEyePosition(partialTicks);

    float pitchOffset = 0.0F;
    float yawOffset = 0.0F;
    if (accuracy < 1.0F) {
      pitchOffset += (1.0F + accuracy * (random.nextInt(5) % 2 == 0 ? -1.0F : 1.0F));
      yawOffset += (1.0F + accuracy * (random.nextInt(5) % 2 == 0 ? -1.0F : 1.0F));
    }

    Vec3d look = getVectorForRotation(fromEntity.getPitch(partialTicks) + pitchOffset,
        fromEntity.getYaw(partialTicks) + yawOffset);

    Vec3d scaledLook = look.scale(distance);
    Vec3d end = start.add(scaledLook);

    Optional<BlockRayTraceResult> blockRayTraceResult =
        rayTraceBlocksPiercing(start, distance, look,
            fromEntity.getEntityWorld());

    final double sqrDistance = blockRayTraceResult.isPresent()
        ? blockRayTraceResult.get().getHitVec().squareDistanceTo(start)
        : distance * distance;

    Optional<EntityRayTraceResult> entityRayTraceResult = rayTraceEntities(fromEntity, start, end,
        filterEntities(fromEntity, scaledLook), sqrDistance);

    return entityRayTraceResult.isPresent() ? entityRayTraceResult : blockRayTraceResult;
  }

  /**
   * Perform a ray trace looking for blocks, ignoring blocks that are {@link #isBlockPierceable}
   */
  public static Optional<BlockRayTraceResult> rayTraceBlocksPiercing(Vec3d start, double distance,
      Vec3d look,
      World world) {
    return rayTraceBlocksPiercing(start, distance, look, RayTraceContext.BlockMode.COLLIDER,
        RayTraceContext.FluidMode.NONE, world);
  }

  /**
   * Perform a ray trace looking for blocks, ignoring blocks that are {@link #isBlockPierceable}
   */
  public static Optional<BlockRayTraceResult> rayTraceBlocksPiercing(Vec3d start, double distance,
      Vec3d look,
      RayTraceContext.BlockMode blockMode,
      RayTraceContext.FluidMode fluidMode,
      World world) {
    Vec3d newStart = start;
    Vec3d end = start.add(look.scale(distance));
    boolean pierceableBlock;
    BlockRayTraceResult blockRayTraceResult = null;
    BlockPos lastBlockPos = null;
    do {
      if (newStart.distanceTo(start) >= distance) {
        break;
      }

      pierceableBlock = false;
      RayTraceContext context = new RayTraceContext(newStart, end, blockMode, fluidMode, null);
      blockRayTraceResult = world.rayTraceBlocks(context);

      if (blockRayTraceResult != null) {
        // Not sure about this one, but I have a concern about inaccuracy of Double which could lead
        // to an endless loop
        BlockPos blockPos = blockRayTraceResult.getPos();
        if (lastBlockPos != null && lastBlockPos.equals(blockPos)) {
          break;
        }
        lastBlockPos = blockPos;

        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        pierceableBlock = isBlockPierceable(block);
        if (pierceableBlock) {
          Vec3d hitVec = blockRayTraceResult.getHitVec();
          AxisAlignedBB bb = context.getBlockShape(blockState, world, blockPos).getBoundingBox();
          double xDist = look.getX() < 0d ? hitVec.getX() - bb.minX - blockPos.getX()
              : blockPos.getX() - hitVec.getX() + bb.maxX;
          double yDist = look.getY() < 0d ? hitVec.getY() - bb.minY - blockPos.getY()
              : blockPos.getY() - hitVec.getY() + bb.maxY;
          double zDist = look.getZ() < 0d ? hitVec.getZ() - bb.minZ - blockPos.getZ()
              : blockPos.getZ() - hitVec.getZ() + bb.maxZ;
          double xRayDist =
              Math.abs(look.getX()) != 0d ? xDist / Math.abs(look.getX()) : Double.MAX_VALUE;
          double yRayDist =
              Math.abs(look.getY()) != 0d ? yDist / Math.abs(look.getY()) : Double.MAX_VALUE;
          double zRayDist =
              Math.abs(look.getZ()) != 0d ? zDist / Math.abs(look.getZ()) : Double.MAX_VALUE;

          double rayDist = Math.min(xRayDist, Math.min(zRayDist, yRayDist));
          newStart = hitVec.add(look.scale(rayDist));
        }
      }
    } while (pierceableBlock);

    return Optional.ofNullable(blockRayTraceResult);
  }

  public static boolean isBlockPierceable(Block block) {
    return block instanceof FenceBlock
        || block instanceof DoorBlock
        || block instanceof AbstractGlassBlock
        || block instanceof LeavesBlock
        || block instanceof TrapDoorBlock;
  }

  public static Vec3d getVectorForRotation(float pitch, float yaw) {
    float pitchRad = pitch * ((float) Math.PI / 180F);
    float yawRad = -yaw * ((float) Math.PI / 180F);
    float yawCos = MathHelper.cos(yawRad);
    float yawSin = MathHelper.sin(yawRad);
    float pitchCos = MathHelper.cos(pitchRad);
    float pitchSin = MathHelper.sin(pitchRad);
    return new Vec3d((yawSin * pitchCos), (-pitchSin), (yawCos * pitchCos));
  }
}
