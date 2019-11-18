package com.craftingdead.mod.item;

import java.util.Random;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ClothingItem extends Item {

  /**
   * Used to determine if this peice of clothing actually protects a player or not (0, 1, 2)
   */
  private int armorValue = 0;

  /**
   * The amount of armor that will be placed on the player: 0 = none, 1 = low, 2 = medium, 3 =
   * strong
   */
  private int armorLevel = 0;

  /**
   * Can reduce fire damage from a flame thrower?
   */
  private boolean fireResistance = false;

  /**
   * Controls whether the clothing gives you slowness or not
   */
  private boolean isSlowness = false;

  public ClothingItem(Properties properties) {
    super(properties.setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_WEARABLE));
    this.armorLevel = properties.armorLevel;
    this.armorValue = properties.armorValue;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {

    if (!worldIn.isRemote) {
      Random rand = new Random();
      int var1 = rand.nextInt(3) + 3;

      for (int i = 0; i < var1; i++) {
        if (rand.nextBoolean()) {
          entityLiving.entityDropItem(new ItemStack(ModItems.ragClean));
        } else {
          entityLiving.entityDropItem(new ItemStack(ModItems.ragDirty));
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

  // TODO Fix Animation
  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK;
  }

  public static class Properties extends Item.Properties {

    private int armorValue;
    private int armorLevel;

    private boolean isSlowness;
    private boolean fireResistance;

    public Properties setMaxStackSize(int maxStackSize) {
      this.maxStackSize(maxStackSize);
      return this;
    }

    public Properties setGroup(ItemGroup groupIn) {
      this.group(groupIn);
      return this;
    }

    public Properties setArmorValue(int armorValue) {
      this.armorValue = armorValue;
      return this;
    }

    public Properties setArmorLevel(int armorLevel) {
      this.armorLevel = armorLevel;
      return this;
    }

    public Properties setSlowness() {
      isSlowness = true;
      return this;
    }

    public Properties setFireResistance() {
      this.fireResistance = true;
      return this;
    }
  }
}
