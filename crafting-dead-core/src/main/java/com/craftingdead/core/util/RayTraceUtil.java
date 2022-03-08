/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.core.util;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class RayTraceUtil {

  public static Optional<EntityHitResult> rayTraceEntities(Entity fromEntity) {
    var reachDistanceAttribute = fromEntity instanceof Player player
        ? player.getAttribute(ForgeMod.REACH_DISTANCE.get())
        : null;
    var distance = reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue();
    var startPos = fromEntity.getEyePosition(1.0F);
    var look = fromEntity.getViewVector(1.0F);
    var scaledLook = look.scale(distance);
    var endPos = startPos.add(scaledLook);
    return Optional.ofNullable(ProjectileUtil.getEntityHitResult(fromEntity, startPos, endPos,
        fromEntity.getBoundingBox().expandTowards(scaledLook).inflate(1.0D, 1.0D, 1.0D),
        EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR),
        distance * distance));
  }

  /**
   * Ray traces blocks and entities.
   *
   * @param fromEntity - the entity performing the ray trace
   * @param distance - maximum distance
   * @param partialTick - partialTick for frame interpolation
   * @return an optional hit result
   */
  public static Optional<? extends HitResult> rayTrace(Entity fromEntity,
      double distance, float partialTick, float pitchOffset, float yawOffset) {
    var startPos = fromEntity.getEyePosition(partialTick);

    var look = calculateViewVector(fromEntity.getViewXRot(partialTick) + pitchOffset,
        fromEntity.getViewYRot(partialTick) + yawOffset);

    var scaledLook = look.scale(distance);
    var endPos = startPos.add(scaledLook);

    var blockHitResult = rayTraceBlocks(startPos, distance, look, fromEntity.getLevel());

    var sqrDistance = blockHitResult.isPresent()
        ? blockHitResult.get().getLocation().distanceToSqr(startPos)
        : distance * distance;

    var entityHitResult = ProjectileUtil.getEntityHitResult(fromEntity, startPos, endPos,
        fromEntity.getBoundingBox().expandTowards(scaledLook).inflate(1.0D, 1.0D, 1.0D),
        EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR),
        sqrDistance);

    return entityHitResult == null ? blockHitResult : Optional.of(entityHitResult);
  }

  /**
   * Ray traces blocks, ignoring fluids.
   * 
   * @param startPos- start position
   * @param distance- maximum distance
   * @param look- look/rotation vector
   * @param level- the level
   * @return an optional hit result
   */
  public static Optional<BlockHitResult> rayTraceBlocks(Vec3 startPos, double distance,
      Vec3 look, Level level) {
    return rayTraceBlocks(startPos, distance, look,
        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, level);
  }

  /**
   * Ray traces blocks.
   * 
   * @param startPos - start position
   * @param distance - maximum distance
   * @param look - look/rotation vector
   * @param blockMode - block clipping mode
   * @param fluidMode - fluid clipping mode
   * @param level - the level
   * @return an optional hit result
   */
  public static Optional<BlockHitResult> rayTraceBlocks(Vec3 startPos, double distance,
      Vec3 look, ClipContext.Block blockMode, ClipContext.Fluid fluidMode, Level level) {
    var currentPos = startPos;
    final var endPos = startPos.add(look.scale(distance));

    BlockHitResult hitResult = null;
    BlockPos lastBlockPos = null;
    while (true) {
      if (currentPos.distanceTo(startPos) >= distance) {
        break;
      }

      var context = new ClipContext(currentPos, endPos, blockMode, fluidMode, null);
      hitResult = level.clip(context);

      if (hitResult != null) {
        // Not sure about this one, but I have a concern about inaccuracy of Double which could lead
        // to an endless loop
        var blockPos = hitResult.getBlockPos();
        if (lastBlockPos != null && lastBlockPos.equals(blockPos)) {
          break;
        }
        lastBlockPos = blockPos;

        var blockState = level.getBlockState(blockPos);
        if (blockState.canOcclude()) {
          break;
        }

        var hitPos = hitResult.getLocation();
        var shape = context.getBlockShape(blockState, level, blockPos);
        if (!shape.isEmpty()) {
          var bounds = shape.bounds();
          var xDist = look.x() < 0.0D
              ? hitPos.x() - bounds.minX - blockPos.getX()
              : blockPos.getX() - hitPos.x() + bounds.maxX;
          var yDist = look.y() < 0.0D
              ? hitPos.y() - bounds.minY - blockPos.getY()
              : blockPos.getY() - hitPos.y() + bounds.maxY;
          var zDist = look.z() < 0.0D
              ? hitPos.z() - bounds.minZ - blockPos.getZ()
              : blockPos.getZ() - hitPos.z() + bounds.maxZ;
          var xRayDist =
              Math.abs(look.x()) != 0.0D ? xDist / Math.abs(look.x()) : Double.MAX_VALUE;
          var yRayDist =
              Math.abs(look.y()) != 0.0D ? yDist / Math.abs(look.y()) : Double.MAX_VALUE;
          var zRayDist =
              Math.abs(look.z()) != 0.0D ? zDist / Math.abs(look.z()) : Double.MAX_VALUE;

          var rayDist = Math.min(xRayDist, Math.min(zRayDist, yRayDist));
          currentPos = hitPos.add(look.scale(rayDist));
        }
      }
    }

    return Optional.ofNullable(hitResult);
  }

  public static Vec3 calculateViewVector(float pitch, float yaw) {
    float pitchRad = pitch * ((float) Math.PI / 180F);
    float yawRad = -yaw * ((float) Math.PI / 180F);
    float yawCos = Mth.cos(yawRad);
    float yawSin = Mth.sin(yawRad);
    float pitchCos = Mth.cos(pitchRad);
    float pitchSin = Mth.sin(pitchRad);
    return new Vec3((yawSin * pitchCos), (-pitchSin), (yawCos * pitchCos));
  }
}
