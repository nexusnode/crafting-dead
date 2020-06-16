package com.craftingdead.core.item;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.craftingdead.core.util.Text;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ClothingItem extends Item {

  private final int armorLevel;
  @Nullable
  private final Integer slownessLevel;
  private final boolean fireImmunity;

  public ClothingItem(Properties properties) {
    super(properties);

    this.armorLevel = properties.armorLevel;
    this.slownessLevel = properties.slownessLevel;
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

  @Nullable
  public Integer getSlownessLevel() {
    return this.slownessLevel;
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

    if (this.slownessLevel != null) {
      String potionNameAndLevel = I18n.format(Effects.SLOWNESS.getName()) + " "
          + I18n.format("enchantment.level." + (this.slownessLevel + 1));

      lines.add(Text.of(potionNameAndLevel).applyTextStyle(TextFormatting.GRAY));
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
    private Integer slownessLevel = null;
    private boolean fireImmunity = false;

    public Properties setArmorLevel(int armorLevel) {
      this.armorLevel = armorLevel;
      return this;
    }

    /**
     * Potion levels starts at 0.
     */
    public Properties setSlownessLevel(int slownessLevel) {
      this.slownessLevel = slownessLevel;
      return this;
    }

    public Properties setFireImmunity(boolean fireImmunity) {
      this.fireImmunity = fireImmunity;
      return this;
    }
  }
}
