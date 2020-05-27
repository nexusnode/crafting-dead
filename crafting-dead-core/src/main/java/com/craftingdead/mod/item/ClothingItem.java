package com.craftingdead.mod.item;

import java.util.List;
import java.util.Random;
import com.craftingdead.mod.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ClothingItem extends Item {

  private final int armorLevel;
  private final double movementSpeedModifier;
  private final boolean fireImmunity;

  public ClothingItem(Properties properties) {
    super(properties);

    this.armorLevel = properties.armorLevel;
    this.movementSpeedModifier = properties.movementSpeedModifier;
    this.fireImmunity = properties.fireImmunity;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (!worldIn.isRemote) {
      Random rand = new Random();
      int randomRagAmount = rand.nextInt(3) + 3;

      for (int i = 0; i < randomRagAmount; i++) {
        if (rand.nextBoolean()) {
          entityLiving.entityDropItem(new ItemStack(ModItems.CLEAN_RAG::get));
        } else {
          entityLiving.entityDropItem(new ItemStack(ModItems.DIRTY_RAG::get));
        }
      }
    }

    if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
      ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
    }

    stack.shrink(1);
    return stack;
  }

  public int getArmorLevel() {
    return this.armorLevel;
  }

  public double getMovementSpeedModifier() {
    return this.movementSpeedModifier;
  }

  public boolean hasFireImmunity() {
    return this.fireImmunity;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);

    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);
    ITextComponent armorLevelText = Text.of(this.armorLevel).applyTextStyle(TextFormatting.RED);

    lines.add(Text.translate("item_lore.clothing.protection_level")
        .applyTextStyle(TextFormatting.GRAY).appendSibling(armorLevelText));

    if (this.movementSpeedModifier != 1D) {
      ITextComponent movemendSpeedText = Text.of((int) (this.movementSpeedModifier * 100D) + "%")
          .applyTextStyle(TextFormatting.RED);

      lines.add(Text.translate("item_lore.clothing.movement_speed")
          .applyTextStyle(TextFormatting.GRAY).appendSibling(movemendSpeedText));
    }

    if (this.fireImmunity) {
      lines.add(
          Text.translate("item_lore.clothing.immune_to_fire").applyTextStyle(TextFormatting.GRAY));
    }
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 32;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK;
  }

  public ResourceLocation getClothingSkin(String skinType) {
    return new ResourceLocation(this.getRegistryName().getNamespace(),
        "textures/models/clothing/" + this.getRegistryName().getPath() + "_" + skinType + ".png");
  }

  public static class Properties extends Item.Properties {

    private int armorLevel = 0;
    private double movementSpeedModifier = 1D;
    private boolean fireImmunity = false;

    public Properties setArmorLevel(int armorLevel) {
      this.armorLevel = armorLevel;
      return this;
    }

    public Properties setMovementSpeedModifier(double movementSpeed) {
      this.movementSpeedModifier = movementSpeed;
      return this;
    }

    public Properties setFireImmunity(boolean fireImmunity) {
      this.fireImmunity = fireImmunity;
      return this;
    }
  }
}
