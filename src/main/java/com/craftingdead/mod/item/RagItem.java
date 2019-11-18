package com.craftingdead.mod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class RagItem extends MedicalItem {

  public RagItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    RayTraceResult raytraceresult =
        rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
    BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos();
    ItemStack itemstack = playerIn.getHeldItem(handIn);

    if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)
        && (itemstack.getItem() == ModItems.ragBloody
            || itemstack.getItem() == ModItems.ragDirty)) {
      return new ActionResult<>(ActionResultType.SUCCESS,
          this.turnSwapItem(itemstack, playerIn, new ItemStack(ModItems.ragClean)));
    }

    if (playerIn.getHealth() > 20F || itemstack.getItem() == ModItems.ragBloody
        || itemstack.getItem() == ModItems.ragDirty) {
      return new ActionResult<>(ActionResultType.FAIL, itemstack);
    } else {
      playerIn.setActiveHand(handIn);
      return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
  }
}
