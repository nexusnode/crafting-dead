package com.craftingdead.immerse.world.action;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.immerse.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class BuildDoorWallAction extends BlueprintAction {

  public static final Set<BlockPos> wallOffsets = Set.of(
      new BlockPos(1, 0, 0),
      new BlockPos(-1, 0, 0),
      new BlockPos(1, 1, 0),
      new BlockPos(-1, 1, 0),
      new BlockPos(0, 2, 0),
      new BlockPos(1, 2, 0),
      new BlockPos(-1, 2, 0));

  private final BuildDoorWallActionType type;
  private final BlockState wallState;
  @Nullable
  private final BlockState doorState;

  private final List<BlockPos> wallPositions;

  protected BuildDoorWallAction(LivingExtension<?, ?> performer, BlockPlaceContext context,
      BuildDoorWallActionType type) {
    super(performer, context);
    this.type = type;
    this.wallState = type.getWallBlock().defaultBlockState();
    this.doorState = type.getDoorBlock().getStateForPlacement(context);
    var direction = performer.getEntity().getDirection();
    this.wallPositions = wallOffsets.stream()
        .map(pos -> pos.rotate(BlockUtil.getRotation(direction)))
        .map(context.getClickedPos()::offset)
        .toList();


  }

  @Override
  public BlueprintActionType getType() {
    return this.type;
  }

  @Override
  public boolean start(boolean simulate) {
    return this.doorState != null
        && super.start(simulate)
        && this.wallPositions.stream().allMatch(this::canPlace);
  }

  @Override
  public boolean tick() {
    if (super.tick()) {
      return true;
    }

    if (this.getTicksUsingItem() % 10 == 0) {
      this.wallPositions
          .forEach(pos -> this.addBuildEffects(pos, this.wallState));
      this.addBuildEffects(this.getContext().getClickedPos(), this.doorState);
    }

    return false;
  }

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);
    if (!reason.isCompleted()) {
      return;
    }
    this.wallPositions.forEach(pos -> this.placeBlock(pos, this.wallState));
    this.placeBlock(this.getContext().getClickedPos(), this.doorState);
  }
}
