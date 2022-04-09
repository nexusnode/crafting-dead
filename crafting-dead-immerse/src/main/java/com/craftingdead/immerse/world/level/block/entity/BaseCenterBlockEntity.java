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

package com.craftingdead.immerse.world.level.block.entity;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.survival.SurvivalPlayerHandler;
import com.craftingdead.immerse.world.level.extension.LandClaim;
import com.craftingdead.immerse.world.level.extension.LegacyBase;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class BaseCenterBlockEntity extends BlockEntity {

  private static final int RADIUS = 35;
  private static final int BUILD_RADIUS = 7;

  @Nullable
  private UUID baseId;
  private final Set<BlockPos> playerBlocks = new HashSet<>();

  public BaseCenterBlockEntity(BlockPos pos, BlockState state) {
    super(ImmerseBlockEntityTypes.BASE_CENTER.get(), pos, state);
  }

  public void placed(LivingEntity livingEntity) {
    final SurvivalPlayerHandler playerHandler;
    if (livingEntity instanceof Player player) {
      player.displayClientMessage(new TranslatableComponent("message.creating_base"), false);

      playerHandler =
          PlayerExtension.getOrThrow(player).getHandlerOrThrow(SurvivalPlayerHandler.TYPE);
      var existingBase = playerHandler.getBase().orElse(null);
      if (existingBase != null) {
        player.displayClientMessage(
            new TranslatableComponent("message.already_own_base",
                existingBase.getBlockPos().toShortString())
                    .withStyle(ChatFormatting.RED),
            true);
        return;
      }
    } else {
      playerHandler = null;
    }

    var landManager = LevelExtension.getOrThrow(this.level).getLandManager();

    var base = new LegacyBase(livingEntity.getUUID(), this.getBlockPos(), BUILD_RADIUS);
    this.baseId = base.getId();
    landManager.registerLandOwner(base);

    landManager
        .registerLandClaim(
            new LandClaim(new BoundingBox(this.getBlockPos()).inflatedBy(RADIUS), this.baseId))
        .whenCompleteAsync((result, exception) -> {
          if (livingEntity instanceof Player player) {
            player.displayClientMessage(switch (result) {
              case ALREADY_CLAIMED -> new TranslatableComponent("message.land_already_claimed")
                  .withStyle(ChatFormatting.RED);
              case OUT_OF_BOUNDS -> new TranslatableComponent("message.land_out_of_bounds")
                  .withStyle(ChatFormatting.RED);
              case SUCCESS -> {
                if (playerHandler != null) {
                  playerHandler.setBaseId(this.baseId);
                }
                yield new TranslatableComponent("message.base_creation_succeeded")
                    .withStyle(ChatFormatting.GREEN);
              }
            }, true);
          }
        }, this.level.getServer());
  }

  public void removed() {
    if (this.baseId != null) {
      var landManager = LevelExtension.getOrThrow(this.level).getLandManager();
      landManager.removeLandOwner(this.baseId);
    }
  }

  public void playerPlacedBlock(LivingExtension<?, ?> player, BlockPos... posArray) {
    for (BlockPos pos : posArray) {
      this.playerBlocks.add(pos.immutable());
    }
  }

  public void playerRemovedBlock(LivingExtension<?, ?> player, BlockPos... posArray) {
    for (BlockPos pos : posArray) {
      this.playerBlocks.remove(pos);
    }
  }

  public void destroyPlayerBlocks() {
    var bedrock = Blocks.BEDROCK.defaultBlockState();
    var air = Blocks.AIR.defaultBlockState();

    // The UPDATE_KNOWN_SHAPE prevent block neighbors from being updated
    // we need it to avoid certain blocks from dropping
    for (BlockPos pos : playerBlocks) {
      Objects.requireNonNull(level)
          .setBlock(pos, bedrock,
              Block.UPDATE_SUPPRESS_DROPS
                  | Block.UPDATE_KNOWN_SHAPE
                  | Block.UPDATE_SUPPRESS_LIGHT);
    }

    // Now run it again to apply a proper block update around the neighbors
    for (BlockPos pos : playerBlocks) {
      level.setBlock(pos, air, Block.UPDATE_CLIENTS);
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    if (this.baseId != null) {
      tag.putUUID("baseId", this.baseId);
    }

    var blockList = new ListTag();
    for (BlockPos pos : playerBlocks) {
      blockList.add(new IntArrayTag(new int[]{ pos.getX(), pos.getY(), pos.getZ() }));
    }
    tag.put("blocks", blockList);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    if (tag.hasUUID("baseId")) {
      this.baseId = tag.getUUID("baseId");
    }

    if (tag.contains("blocks")) {
      var blockList = tag.getList("blocks", Tag.TAG_INT_ARRAY);
      for (int i = 0; i < blockList.size(); i++) {
        var pos = blockList.getIntArray(i);
        if (pos.length == 3) {
          this.playerBlocks.add(new BlockPos(pos[0], pos[1], pos[2]));
        }
      }
    }
  }
}
