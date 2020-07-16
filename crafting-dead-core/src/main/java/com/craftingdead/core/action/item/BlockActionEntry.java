package com.craftingdead.core.action.item;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

public class BlockActionEntry extends AbstractActionEntry<BlockActionEntry.Properties> {

  private final Predicate<BlockState> predicate;

  private BlockPos blockPosTarget;
  private BlockState blockStateTarget;

  public BlockActionEntry(Properties properties) {
    super(properties);
    this.predicate = properties.predicate;
  }

  @Override
  public boolean canPerform(ILiving<?> performer, @Nullable ILiving<?> target,
      ItemStack heldStack) {
    IAttributeInstance reachDistanceAttribute =
        performer.getEntity().getAttribute(PlayerEntity.REACH_DISTANCE);
    RayTraceResult result = performer.getEntity().pick(
        reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue(), 1.0F, true);
    if (result instanceof BlockRayTraceResult) {
      BlockPos blockPos = ((BlockRayTraceResult) result).getPos();
      BlockState blockState = performer.getEntity().getEntityWorld().getBlockState(blockPos);
      if (this.blockPosTarget == null || this.blockStateTarget == null) {
        this.blockPosTarget = blockPos;
        this.blockStateTarget = blockState;
        if (!this.predicate.test(this.blockStateTarget)) {
          return false;
        }
      }

      return this.blockPosTarget.equals(blockPos) && this.blockStateTarget.equals(blockState);
    }
    return false;
  }

  @Override
  public boolean finish(ILiving<?> performer, ILiving<?> target, ItemStack heldStack) {
    return true;
  }

  public static class Properties extends AbstractActionEntry.Properties<Properties> {

    private Predicate<BlockState> predicate;

    public Properties setPredicate(Predicate<BlockState> predicate) {
      this.predicate = predicate;
      return this;
    }
  }
}
