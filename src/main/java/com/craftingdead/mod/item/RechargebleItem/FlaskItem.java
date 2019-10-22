package com.craftingdead.mod.item.RechargebleItem;

import com.craftingdead.mod.item.EatItem;
import com.craftingdead.mod.item.ModItems;
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

public class FlaskItem extends EatItem {

  public FlaskItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {

    RayTraceResult raytraceresult = rayTrace(worldIn, playerIn,
        RayTraceContext.FluidMode.SOURCE_ONLY);
    BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos();
    ItemStack itemstack = playerIn.getHeldItem(handIn);

    //TODO find out how else you can fill the bottle ,and craft with Tea Bag
    if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)
        && itemstack.getItem() == ModItems.flaskEmpty) {
      return new ActionResult<>(ActionResultType.SUCCESS,
          this.turnSwapItem(itemstack, playerIn, new ItemStack(ModItems.flask)));
    }

    //TODO change to determine the need for water
    if (!(getWater() > 0)) {
      return new ActionResult<>(ActionResultType.FAIL, itemstack);
    } else {
      playerIn.setActiveHand(handIn);
      return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
  }
}
