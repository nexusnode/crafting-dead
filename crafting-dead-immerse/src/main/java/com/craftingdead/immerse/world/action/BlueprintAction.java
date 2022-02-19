package com.craftingdead.immerse.world.action;

import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.action.ProgressBar;
import com.craftingdead.core.world.action.item.ItemAction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class BlueprintAction extends ItemAction {

  private final LivingExtension<?, ?> performer;
  private final BlockPlaceContext context;

  protected BlueprintAction(LivingExtension<?, ?> performer, BlockPlaceContext context) {
    super(context.getHand());
    this.performer = performer;
    this.context = context;
  }

  public BlockPlaceContext getContext() {
    return this.context;
  }

  protected boolean canPlace(BlockPos blockPos) {
    return this.performer.getLevel().getBlockState(blockPos).getMaterial().isReplaceable()
        && this.getType().getPlacementPredicate().test(this.performer, blockPos);
  }

  @Override
  public boolean tick() {
    if (!this.performer.getLevel().isClientSide() && this.performer.getEntity()
        .distanceToSqr(Vec3.atCenterOf(this.context.getClickedPos())) > 64.0D) {
      this.performer.cancelAction(true);
      return false;
    }
    return super.tick();
  }

  public void addBuildEffects(BlockPos blockPos, BlockState blockState) {
    this.getPerformer().getLevel().addDestroyBlockEffect(blockPos, blockState);
  }

  public boolean placeBlock(BlockPos blockPos, BlockState blockState) {
    var level = this.getPerformer().getLevel();

    if (level.isClientSide()
        || !this.canPlace(blockPos)
        || !level.setBlockAndUpdate(blockPos, blockState)) {
      return false;
    }

    blockState.getBlock().setPlacedBy(level, blockPos, blockState,
        this.getPerformer().getEntity(), this.getItemStack());
    if (this.performer.getEntity() instanceof ServerPlayer player) {
      CriteriaTriggers.PLACED_BLOCK.trigger(player, blockPos, this.getItemStack());
    }

    return true;
  }

  @Override
  public ActionObserver createPerformerObserver() {
    return ActionObserver.create(this, ProgressBar.create(this.getType(), null, this::getProgress));
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public abstract BlueprintActionType getType();
}
