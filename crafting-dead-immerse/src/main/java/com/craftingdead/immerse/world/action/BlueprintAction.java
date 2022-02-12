package com.craftingdead.immerse.world.action;

import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.world.action.item.ItemAction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.immerse.ChunkExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlueprintAction extends ItemAction {

  private final LivingExtension<?, ?> performer;
  private final BlockPlaceContext context;

  private ChunkExtension chunkExtension;

  protected BlueprintAction(LivingExtension<?, ?> performer, BlockPlaceContext context) {
    super(context.getHand());
    this.performer = performer;
    this.context = context;
  }

  public BlockPlaceContext getContext() {
    return this.context;
  }

  protected boolean canPlace(BlockPos blockPos, BlockState blockState) {
    return blockState.getMaterial().isReplaceable()
        && !this.getType().isBaseRequired() || this.chunkExtension.hasBase(blockPos);
  }

  @Override
  public boolean start() {
    this.chunkExtension =
        ChunkExtension.getOrThrow(this.context.getLevel(), this.context.getClickedPos());
    return super.start();
  }

  @Override
  public boolean tick() {
    var result = RayTraceUtil.pick(this.performer.getEntity()).orElse(null);
    if (result == null || !result.getBlockPos().equals(this.context.getClickedPos())) {
      this.performer.cancelAction(true);
      return false;
    }
    return super.tick();
  }

  public void addBuildEffects(BlockPos blockPos, BlockState blockState) {
    this.getPerformer().getLevel().addDestroyBlockEffect(blockPos, blockState);
    this.getPerformer().getEntity().playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.0F);

    // Random rand = new Random();
    // for (int i = 0; i < 5; i++) {
    // final var multiplier = 1.5F;
    // var xOffset = multiplier * (0.5F - rand.nextFloat());
    // var yOffset = multiplier * (0.5F - rand.nextFloat());
    // var zOffset = multiplier * (0.5F - rand.nextFloat());
    // if (!blockState.isAir()) {
    // level.addParticle(
    // new BlockParticleOption(ParticleTypes.BLOCK, blockState).setPos(blockPos),
    // blockPos.getX() + 0.5 + xOffset,
    // blockPos.getY() + 0.5 + yOffset,
    // blockPos.getZ() + 0.5 + zOffset,
    // 0, 0, 0);
    // }
    // }
  }

  public boolean placeBlock(BlockPos blockPos, BlockState blockState) {
    var level = this.getPerformer().getLevel();
    return !level.isClientSide()
        && !this.getPerformer().getLevel().isClientSide()
        && this.canPlace(blockPos, blockState) && level.setBlockAndUpdate(blockPos, blockState);
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public abstract BlueprintActionType getType();
}
