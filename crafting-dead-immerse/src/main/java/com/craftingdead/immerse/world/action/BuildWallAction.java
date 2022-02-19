package com.craftingdead.immerse.world.action;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.immerse.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class BuildWallAction extends BlueprintAction {

  private final BuildWallActionType type;

  private final BlockState blockState;

  private final BlockPos minPos;
  private final BlockPos maxPos;

  public BuildWallAction(LivingExtension<?, ?> performer, BlockPlaceContext context,
      BuildWallActionType type) {
    super(performer, context);
    this.type = type;
    this.blockState = type.getBlock().defaultBlockState();

    var direction = performer.getEntity().getDirection();
    var clickedPos = context.getClickedPos();

    this.minPos = new BlockPos(-1, 0, 0)
        .rotate(BlockUtil.getRotation(direction))
        .offset(clickedPos);
    this.maxPos = new BlockPos(1, 2, 0)
        .rotate(BlockUtil.getRotation(direction))
        .offset(clickedPos);
  }

  @Override
  public boolean start(boolean simulate) {
    return super.start(simulate) && BlockPos.betweenClosedStream(this.minPos, this.maxPos)
        .allMatch(this::canPlace);
  }

  @Override
  public boolean tick() {
    if (super.tick()) {
      return true;
    }

    if (this.getTicksUsingItem() % 10 == 0) {
      this.getPerformer().getEntity().playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.0F);
      BlockPos.betweenClosed(this.minPos, this.maxPos)
          .forEach(pos -> this.addBuildEffects(pos, this.blockState));
    }

    return false;
  }

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);
    if (!reason.isCompleted()) {
      return;
    }

    BlockPos.betweenClosed(this.minPos, this.maxPos)
        .forEach(pos -> this.placeBlock(pos, this.blockState));
  }

  @Override
  public BlueprintActionType getType() {
    return this.type;
  }
}
