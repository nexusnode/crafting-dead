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

package com.craftingdead.immerse.world.level.block;

import com.craftingdead.immerse.Permissions;
import javax.annotation.Nullable;
import com.craftingdead.immerse.world.level.block.entity.BaseCenterBlockEntity;
import com.craftingdead.immerse.world.level.block.entity.ImmerseBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.server.permission.PermissionAPI;

public class BaseCenterBlock extends Block implements EntityBlock {

  public BaseCenterBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BaseCenterBlockEntity(pos, state);
  }

  @Override
  public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState,
      @Nullable LivingEntity livingEntity, ItemStack itemStack) {
    super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
    if (level.isClientSide() || livingEntity == null) {
      return;
    }
    level.getBlockEntity(blockPos, ImmerseBlockEntityTypes.BASE_CENTER.get())
        .ifPresent(blockEntity -> blockEntity.placed(livingEntity));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos,
      BlockState newState, boolean moved) {
    if (!level.isClientSide()) {
      level.getBlockEntity(blockPos, ImmerseBlockEntityTypes.BASE_CENTER.get())
          .ifPresent(BaseCenterBlockEntity::removed);
    }
    super.onRemove(blockState, level, blockPos, newState, moved);
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
      boolean willHarvest, FluidState fluid) {
    if (player instanceof ServerPlayer serverPlayer) {
      if (PermissionAPI.getPermission(serverPlayer, Permissions.BASE_DESTROY)) {
        level.getBlockEntity(pos, ImmerseBlockEntityTypes.BASE_CENTER.get())
            .ifPresent(BaseCenterBlockEntity::destroyPlayerBlocks);
      }
    }
    return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
  }
}
