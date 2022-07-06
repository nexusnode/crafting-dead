/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.renderer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.util.BlockUtil;
import com.craftingdead.immerse.world.action.BuildAction;
import com.craftingdead.immerse.world.action.BuildDoorWallAction;
import com.craftingdead.immerse.world.item.BlueprintItem;
import com.craftingdead.immerse.world.item.ImmerseItems;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

public class BlueprintOutlineRenderer {

  private static final Set<BlockPos> WALL = BlockPos.betweenClosedStream(-1, 0, 0, 1, 2, 0)
      .map(BlockPos::immutable)
      .collect(Collectors.toUnmodifiableSet());
  private static final Set<BlockPos> DOOR_WALL = BuildDoorWallAction.WALL_OFFSETS;
  private static final Set<BlockPos> BLOCK = Set.of(BlockPos.ZERO);
  private static final Set<BlockPos> PLATFORM = BlockPos.betweenClosedStream(-1, 0, 0, 1, 0, 2)
      .map(BlockPos::immutable)
      .collect(Collectors.toUnmodifiableSet());

  private Map<Item, Set<BlockPos>> blueprintOutlines;

  private BlueprintItem lastItem;
  private BlockPos lastPos;

  private BlockPos blockPos;
  private boolean valid;

  public void register() {
    var builder = ImmutableMap.<Item, Set<BlockPos>>builder();

    builder.put(ImmerseItems.BASE_CENTER_BLUEPRINT.get(), BLOCK);
    builder.put(ImmerseItems.CAMPFIRE_BLUEPRINT.get(), BLOCK);
    builder.put(ImmerseItems.CHEST_BLUEPRINT.get(), BLOCK);

    builder.put(ImmerseItems.OAK_PLANK_WALL_BLUEPRINT.get(), WALL);
    builder.put(ImmerseItems.SPRUCE_PLANK_WALL_BLUEPRINT.get(), WALL);
    builder.put(ImmerseItems.BIRCH_PLANK_WALL_BLUEPRINT.get(), WALL);
    builder.put(ImmerseItems.JUNGLE_PLANK_WALL_BLUEPRINT.get(), WALL);
    builder.put(ImmerseItems.ACACIA_PLANK_WALL_BLUEPRINT.get(), WALL);
    builder.put(ImmerseItems.DARK_OAK_PLANK_WALL_BLUEPRINT.get(), WALL);

    builder.put(ImmerseItems.OAK_DOOR_BLUEPRINT.get(), DOOR_WALL);
    builder.put(ImmerseItems.SPRUCE_DOOR_BLUEPRINT.get(), DOOR_WALL);
    builder.put(ImmerseItems.BIRCH_DOOR_BLUEPRINT.get(), DOOR_WALL);
    builder.put(ImmerseItems.JUNGLE_DOOR_BLUEPRINT.get(), DOOR_WALL);
    builder.put(ImmerseItems.ACACIA_DOOR_BLUEPRINT.get(), DOOR_WALL);
    builder.put(ImmerseItems.DARK_OAK_DOOR_BLUEPRINT.get(), DOOR_WALL);

    builder.put(ImmerseItems.OAK_PLANK_PLATFORM_BLUEPRINT.get(), PLATFORM);
    builder.put(ImmerseItems.SPRUCE_PLANK_PLATFORM_BLUEPRINT.get(), PLATFORM);
    builder.put(ImmerseItems.BIRCH_PLANK_PLATFORM_BLUEPRINT.get(), PLATFORM);
    builder.put(ImmerseItems.JUNGLE_PLANK_PLATFORM_BLUEPRINT.get(), PLATFORM);
    builder.put(ImmerseItems.ACACIA_PLANK_PLATFORM_BLUEPRINT.get(), PLATFORM);
    builder.put(ImmerseItems.DARK_OAK_PLANK_PLATFORM_BLUEPRINT.get(), PLATFORM);

    this.blueprintOutlines = builder.buildOrThrow();
  }

  private boolean validate(PlayerExtension<AbstractClientPlayer> player,
      BlueprintItem item, BlockHitResult hitResult) {
    return item.getActionType()
        .createBlockAction(player,
            new UseOnContext(player.entity(), InteractionHand.MAIN_HAND, hitResult))
        .map(action -> action.start(true))
        .orElse(false);
  }

  public void render(PlayerExtension<AbstractClientPlayer> player, BlueprintItem item,
      BlockHitResult hitResult, Camera camera, PoseStack poseStack,
      MultiBufferSource bufferSource) {

    var blockPos = this.blockPos;
    var valid = this.valid;
    if (player.getAction().orElse(null) instanceof BuildAction action) {
      blockPos = action.getContext().getClickedPos();
      valid = true;
    } else if (item != this.lastItem || !hitResult.getBlockPos().equals(this.lastPos)) {
      this.lastItem = item;
      this.lastPos = hitResult.getBlockPos();
      valid = this.valid = this.validate(player, item, hitResult);

      var context = new BlockPlaceContext(player.entity(), InteractionHand.MAIN_HAND,
          player.mainHandItem(), hitResult);
      blockPos = this.blockPos = context.getClickedPos();
    }

    var rotation = BlockUtil.getRotation(player.entity().getDirection());

    var position = camera.getPosition();
    var outlines = this.blueprintOutlines.get(item);
    if (outlines == null) {
      return;
    }

    var vertexConsumer = bufferSource.getBuffer(RenderType.lines());
    for (var shape : outlines) {
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
