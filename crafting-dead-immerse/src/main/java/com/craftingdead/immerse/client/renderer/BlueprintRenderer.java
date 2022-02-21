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

package com.craftingdead.immerse.client.renderer;

import java.util.Map;
import java.util.Set;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.util.BlockUtil;
import com.craftingdead.immerse.world.action.BlueprintAction;
import com.craftingdead.immerse.world.action.BuildBlockActionType;
import com.craftingdead.immerse.world.action.BuildDoorWallAction;
import com.craftingdead.immerse.world.action.BuildDoorWallActionType;
import com.craftingdead.immerse.world.action.BuildWallActionType;
import com.craftingdead.immerse.world.item.BlueprintItem;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

public class BlueprintRenderer {

  private static Map<Class<? extends ActionType<?>>, Iterable<BlockPos>> blueprintShapes;

  private static final Set<BlockPos> SINGLE = Set.of(BlockPos.ZERO);

  public static void register() {
    var builder = ImmutableMap.<Class<? extends ActionType<?>>, Iterable<BlockPos>>builder();

    builder.put(BuildBlockActionType.class, SINGLE);
    builder.put(BuildWallActionType.class, BlockPos.betweenClosed(-1, 0, 0, 1, 2, 0));
    builder.put(BuildDoorWallActionType.class, BuildDoorWallAction.wallOffsets);

    blueprintShapes = builder.buildOrThrow();
  }

  private static BlueprintItem lastItem;
  private static BlockPos lastPos;

  private static boolean valid;

  private static boolean validate(PlayerExtension<AbstractClientPlayer> player,
      BlueprintItem item, BlockHitResult hitResult) {
    return item.getActionType()
        .createBlockAction(player,
            new UseOnContext(player.getEntity(), InteractionHand.MAIN_HAND, hitResult))
        .map(action -> action.start(true))
        .orElse(false);
  }

  public static void render(PlayerExtension<AbstractClientPlayer> player, BlueprintItem item,
      BlockHitResult hitResult, Camera camera, PoseStack poseStack,
      MultiBufferSource bufferSource) {

    var blockPos = hitResult.getBlockPos();
    if (!player.getLevel().getBlockState(blockPos).getMaterial().isReplaceable()) {
      blockPos = blockPos.relative(hitResult.getDirection());
    }

    if (player.getAction().orElse(null) instanceof BlueprintAction action) {
      blockPos = action.getContext().getClickedPos();
    }

    if (item != lastItem || !blockPos.equals(lastPos)) {
      lastItem = item;
      lastPos = blockPos;
      valid = validate(player, item, hitResult);
    }

    var rotation = BlockUtil.getRotation(player.getEntity().getDirection());

    var position = camera.getPosition();
    var shapes = blueprintShapes.get(item.getActionType().getClass());
    var vertexConsumer = bufferSource.getBuffer(RenderType.lines());
    if (shapes != null) {
      for (var shape : shapes) {
        shape = shape.rotate(rotation);
        var minX = blockPos.getX() + shape.getX() - position.x();
        var minY = blockPos.getY() + shape.getY() - position.y();
        var minZ = blockPos.getZ() + shape.getZ() - position.z();


        var maxX = minX + 1;
        var maxY = minY + 1;
        var maxZ = minZ + 1;

        LevelRenderer.renderLineBox(poseStack, vertexConsumer, minX, minY, minZ, maxX, maxY, maxZ,
            1.0F, valid ? 1.0F : 0.0F, valid ? 1.0F : 0.0F, 0.5F);
      }
    }
  }
}
