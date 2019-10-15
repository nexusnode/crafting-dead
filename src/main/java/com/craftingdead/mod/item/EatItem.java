package com.craftingdead.mod.item;

import com.craftingdead.mod.capability.ModCapabilities;
import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
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
import java.util.Random;

public class EatItem extends Item {

  @Getter
  private int water;

  public boolean rotten = false;

  private IItemProvider containerItem;

  public EatItem(EatItem.Properties properties) {
    super(properties);
    this.water = properties.water;
    this.containerItem = properties.containerItem;
    this.rotten = properties.rotten;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {

    if(rotten){
      Random rand = new Random();
      if (rand.nextInt(5) == 0) {
        entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
          entityLiving.addPotionEffect(new EffectInstance(Effects.HUNGER, 900, 1));
        });
      }
    }

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

    if (stack.getItem().isFood()) {
      tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Food " + TextFormatting.RED + stack.getItem().getFood().getHealing()));
    }

    if (this.water != 0) {
      tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Water " + TextFormatting.RED + this.water));
    }

    if(rotten){
      tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Chance food poisoning could be induced..."));
    }
  }

  /**
   * Changes one item to another
   */
  protected ItemStack turnSwapItem(ItemStack itemStack, PlayerEntity player, ItemStack stack) {
    itemStack.shrink(1);
    player.addStat(Stats.ITEM_USED.get(this));
    if (itemStack.isEmpty()) {
      return stack;
    } else {
      if (!player.inventory.addItemStackToInventory(stack)) {
        player.dropItem(stack, false);
      }
      return itemStack;
    }
  }

  public static class Properties extends Item.Properties {

    public Food foodIn;

    public int water;

    public boolean rotten = false;

    public IItemProvider containerItem;

    public EatItem.Properties setMaxStackSize(int maxStackSize) {
      this.maxStackSize(maxStackSize);
      return this;
    }

    public EatItem.Properties setFood(Food foodIn) {
      this.food(foodIn);
      return this;
    }

    public EatItem.Properties setGroup(ItemGroup groupIn) {
      this.group(groupIn);
      return this;
    }

    public EatItem.Properties setWater(int water) {
      this.water = water;
      return this;
    }

    public EatItem.Properties setContainer(IItemProvider containerItem) {
      this.containerItem = containerItem;
      return this;
    }

    public EatItem.Properties setRotten() {
      this.rotten = true;
      return this;
    }




  }
}
