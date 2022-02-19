package com.craftingdead.immerse.world.level.block;

import javax.annotation.Nullable;
import com.craftingdead.immerse.world.level.block.entity.BaseCenterBlockEntity;
import com.craftingdead.immerse.world.level.block.entity.ImmerseBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
}
