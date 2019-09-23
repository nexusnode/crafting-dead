package com.craftingdead.mod.item;

import com.craftingdead.mod.capability.ModCapabilities;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class DrinkItem extends Item {

  private final int water;

  private final IItemProvider containerItem;


  public DrinkItem(int water, IItemProvider containerItem, Properties properties) {
    super(properties);
    this.water = water;
    this.containerItem = containerItem;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
      player.setWater(player.getWater() + this.water);
    });
    if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
      ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
    }
    if (this.isFood()) {
      entityLiving.onFoodEaten(worldIn, stack);
    } else {
      stack.shrink(1);
    }
    return stack;
  }

  @Override
  public ItemStack getContainerItem(ItemStack itemStack) {
    return new ItemStack(this.containerItem);
  }

  @Override
  public boolean hasContainerItem(ItemStack itemStack) {
    return this.containerItem != null;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.DRINK;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    if (this.isFood() && !playerIn.canEat(this.getFood().canEatWhenFull())) {
      return new ActionResult<>(ActionResultType.FAIL, itemstack);
    } else {
      playerIn.setActiveHand(handIn);
      return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    if (stack.getItem().isFood()) {
      return this.getFood().isFastEating() ? 16 : 32;
    } else {
      return 32;
    }
  }

  //TODO is using TranslationTextComponent prudent?
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
      super.addInformation(stack, worldIn, tooltip, flagIn);

    if (this.water != 0) {
      tooltip.add(new TranslationTextComponent("Water " + TextFormatting.RED + this.water));
    }

  }
}
