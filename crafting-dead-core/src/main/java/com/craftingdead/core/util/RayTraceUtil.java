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
                return !(livingEntity.getHealth() <= 0F);
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

  /**
   * Perform a full ray trace from the parsed entity, excluding blocks from {@link RayTraceUtil#isBlockPierceable}.
   *
   * @param fromEntity - the entity performing the ray trace
   * @param distance - the distance
   * @param partialTicks - the partialTicks
   * @return the {@link RayTraceResult} as an {@link Optional}
   */
  public static Optional<? extends RayTraceResult> rayTracePiercing(final Entity fromEntity,
                                                            final double distance, final float partialTicks,
                                                            final float accuracy, final Random random) {

    Vec3d look = fromEntity.getLook(partialTicks);
    if (accuracy < 1.0F) {
      look = look
              .add((1.0F - accuracy) / 7.5F * random.nextFloat() * (random.nextBoolean() ? -1.0F : 1.0F),
                      0,
                      (1.0F - accuracy) / 7.5F * random.nextFloat() * (random.nextBoolean() ? -1.0F : 1.0F));
    }

    Vec3d scaledLook = look.scale(distance);
    Vec3d originalStart = fromEntity.getEyePosition(partialTicks);
    Vec3d end = originalStart.add(scaledLook);
    Vec3d newStart = originalStart;
    boolean pierceableBlock;
    BlockRayTraceResult blockRayTraceResult = null;
    Optional<EntityRayTraceResult> entityRayTraceResult = Optional.empty();
    BlockPos lastBlockPos = null;
    do {
      if (newStart.distanceTo(originalStart) >= distance) {
        break;
      }

      pierceableBlock = false;
      RayTraceContext context = new RayTraceContext(newStart, end, RayTraceContext.BlockMode.COLLIDER,
              RayTraceContext.FluidMode.NONE, fromEntity);
      blockRayTraceResult = fromEntity.world.rayTraceBlocks(context);

      final double sqrDistance = blockRayTraceResult != null
              ? blockRayTraceResult.getHitVec().squareDistanceTo(newStart)
              : distance * distance;
      entityRayTraceResult = rayTraceEntities(fromEntity, newStart, end,
              filterEntities(fromEntity, scaledLook), sqrDistance);

      if (!entityRayTraceResult.isPresent() && blockRayTraceResult != null) {
        //Not sure about this one, but I have a concern about inaccuracy of Double which could lead to an endless loop
        BlockPos blockPos = blockRayTraceResult.getPos();
        if (lastBlockPos != null && lastBlockPos.equals(blockPos)) {
          break;
        }
        lastBlockPos = blockPos;

        BlockState blockState = fromEntity.getEntityWorld().getBlockState(blockPos);
        Block block = blockState.getBlock();
        pierceableBlock = isBlockPierceable(block);
        if (pierceableBlock) {
          Vec3d hitVec = blockRayTraceResult.getHitVec();
          AxisAlignedBB bb = context.getBlockShape(blockState, fromEntity.getEntityWorld(), blockPos).getBoundingBox();
          double xDist = look.getX() < 0d ? hitVec.getX() - bb.minX - blockPos.getX()
                  : blockPos.getX() - hitVec.getX() + bb.maxX;
          double yDist = look.getY() < 0d ? hitVec.getY() - bb.minY - blockPos.getY()
                  : blockPos.getY() - hitVec.getY() + bb.maxY;
          double zDist = look.getZ() < 0d ? hitVec.getZ() - bb.minZ- blockPos.getZ()
                  : blockPos.getZ() - hitVec.getZ() + bb.maxZ;
          double xRayDist =  Math.abs(look.getX()) != 0d ? xDist /  Math.abs(look.getX()) : Double.MAX_VALUE;
          double yRayDist = Math.abs(look.getY()) != 0d ? yDist / Math.abs(look.getY()) : Double.MAX_VALUE;
          double zRayDist = Math.abs(look.getZ()) != 0d ? zDist / Math.abs(look.getZ()) : Double.MAX_VALUE;

          double rayDist = Math.min(xRayDist, Math.min(zRayDist, yRayDist));
          newStart = hitVec.add(look.scale(rayDist));
        }
      }
    } while (!entityRayTraceResult.isPresent() && pierceableBlock);

    return entityRayTraceResult.isPresent() ? entityRayTraceResult : Optional.ofNullable(blockRayTraceResult);
  }


  public static boolean isBlockPierceable(Block block) {
    return block instanceof FenceBlock
            || block instanceof DoorBlock
            || block instanceof AbstractGlassBlock
            || block instanceof LeavesBlock
            || block instanceof TrapDoorBlock;
  }
}
