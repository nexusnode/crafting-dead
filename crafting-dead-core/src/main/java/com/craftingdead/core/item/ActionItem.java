package com.craftingdead.core.item;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ActionItem extends Item {

  private final IBlockAction blockAction;
  private final IEntityAction entityAction;

  public ActionItem(Properties properties) {
    super(properties);
    this.blockAction = properties.blockAction;
    this.entityAction = properties.entityAction;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn,
      LivingEntity target, Hand hand) {
    if (this.entityAction != null) {
      playerIn.addStat(Stats.ITEM_USED.get(this));
      ActionResult<ItemStack> result =
          this.entityAction.performAction(stack, playerIn, target, random);
      playerIn.setHeldItem(hand, result.getResult());
      return result.getType().isAccepted();
    }
    return false;
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);
    String instructionsTranslationKey = this.getRegistryName().toString() + "/instructions";
    if (I18n.hasKey(instructionsTranslationKey)) {
      lines.add(new TranslationTextComponent(instructionsTranslationKey));
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    BlockRayTraceResult rayTraceResult =
        (BlockRayTraceResult) rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
    BlockState blockState = worldIn.getBlockState(rayTraceResult.getPos());
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    if (this.blockAction != null) {
      playerIn.addStat(Stats.ITEM_USED.get(this));
      return this.blockAction
          .performAction(itemStack, playerIn, rayTraceResult.getPos(), blockState, random);
    }
    return new ActionResult<>(ActionResultType.PASS, itemStack);
  }

  public static ItemStack useReturn(ItemStack itemStack, ItemStack returnStack,
      PlayerEntity player) {
    itemStack.shrink(1);
    if (itemStack.isEmpty()) {
      return returnStack;
    } else {
      if (!player.inventory.addItemStackToInventory(returnStack)) {
        player.dropItem(returnStack, false);
      }
      return itemStack;
    }
  }

  @FunctionalInterface
  public static interface IBlockAction {
    @Nonnull
    ActionResult<ItemStack> performAction(ItemStack itemStack, PlayerEntity playerEntity,
        BlockPos blockPos, BlockState blockState, Random random);
  }

  @FunctionalInterface
  public static interface IEntityAction {
    @Nonnull
    ActionResult<ItemStack> performAction(ItemStack itemStack, PlayerEntity playerEntity,
        Entity entity, Random random);
  }

  public static class Properties extends Item.Properties {

    private IBlockAction blockAction;
    private IEntityAction entityAction;

    public Properties setBlockAction(IBlockAction blockAction) {
      this.blockAction = blockAction;
      return this;
    }

    public Properties setEntityAction(IEntityAction entityAction) {
      this.entityAction = entityAction;
      return this;
    }
  }
}
