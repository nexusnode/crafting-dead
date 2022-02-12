package com.craftingdead.immerse.world.action;

import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class BuildDoorWallAction extends BlueprintAction {

  private final BuildDoorWallActionType type;
  private final BlockState wallState;
  @Nullable
  private final BlockState doorState;

  private final Set<BlockPos> wallPositions;

  protected BuildDoorWallAction(LivingExtension<?, ?> performer, BlockPlaceContext context,
      BuildDoorWallActionType type) {
    super(performer, context);
    this.type = type;
    this.wallState = type.getWallBlock().defaultBlockState();
    this.doorState = type.getDoorBlock().getStateForPlacement(context);

    var pos = context.getClickedPos();
    this.wallPositions = Set.of(
        pos.offset(1, 1, 0),
        pos.offset(-1, 1, 0),
        pos.offset(1, 2, 0),
        pos.offset(-1, 2, 0),
        pos.offset(0, 3, 0),
        pos.offset(1, 3, 0),
        pos.offset(-1, 3, 0));
  }

  @Override
  public BlueprintActionType getType() {
    return this.type;
  }

  @Override
  public boolean start() {
    return this.doorState != null
        && super.start()
        && this.wallPositions.stream()
            .allMatch(pos -> this.canPlace(pos, this.wallState));
  }

  @Override
  public boolean tick() {
    if (!super.tick()) {
      return false;
    }

    if (this.getTicksUsingItem() % 10 == 0) {
      this.wallPositions
          .forEach(pos -> this.addBuildEffects(pos, this.wallState));
      this.addBuildEffects(this.getContext().getClickedPos(), this.doorState);
    }

    return true;
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
