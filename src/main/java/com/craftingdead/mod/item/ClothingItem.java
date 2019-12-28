package com.craftingdead.mod.item;

import java.util.Random;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ClothingItem extends Item {

  public ClothingItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {

    if (!worldIn.isRemote) {
      Random rand = new Random();
      int var1 = rand.nextInt(3) + 3;

      for (int i = 0; i < var1; i++) {
        if (rand.nextBoolean()) {
          entityLiving.entityDropItem(new ItemStack(ModItems.cleanRag));
        } else {
          entityLiving.entityDropItem(new ItemStack(ModItems.dirtyRag));
        }
      }
    }

    if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
      ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
    }

    stack.shrink(1);
    return stack;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);

    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 32;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK;
  }

  public static class Properties extends Item.Properties {

    public Properties setArmorValue(int armorValue) {
      return this;
    }

    public Properties setArmorLevel(int armorLevel) {
      return this;
    }

    public Properties setSlowness() {
      return this;
    }

    public Properties setFireResistance() {
      return this;
    }
  }
}
