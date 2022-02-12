package com.craftingdead.immerse.world.action;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class BuildWallAction extends BlueprintAction {

  private final BuildWallActionType type;

  private final BlockState blockState;

  public BuildWallAction(LivingExtension<?, ?> performer, BlockPlaceContext context,
      BuildWallActionType type) {
    super(performer, context);
    this.type = type;
    this.blockState = type.getBlock().defaultBlockState();
  }

  private BlockPos getMinPos() {
    return this.getContext().getClickedPos().offset(-1, 0, 0);
  }

  private BlockPos getMaxPos() {
    return this.getContext().getClickedPos().offset(1, 2, 0);
  }

  @Override
  public boolean start() {
    return super.start() && BlockPos.betweenClosedStream(this.getMinPos(), this.getMaxPos())
        .allMatch(pos -> this.canPlace(pos, this.blockState));
  }

  @Override
  public boolean tick() {
    if (!super.tick()) {
      return false;
    }

    if (this.getTicksUsingItem() % 10 == 0) {
      BlockPos.betweenClosed(this.getMinPos(), this.getMaxPos())
          .forEach(pos -> this.addBuildEffects(pos, this.blockState));
    }

    return true;
  }

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);
    if (!reason.isCompleted()) {
      return;
    }

    BlockPos.betweenClosed(this.getMinPos(), this.getMaxPos())
        .forEach(pos -> this.placeBlock(pos, this.blockState));
  }

  @Override
  public BlueprintActionType getType() {
    return this.type;
  }
}
