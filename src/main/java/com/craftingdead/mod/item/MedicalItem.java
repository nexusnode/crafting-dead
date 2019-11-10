package com.craftingdead.mod.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.potion.ModEffects;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
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

public class MedicalItem extends Item {

  private int health;

  private boolean healBrokenLeg;

  private boolean stopBleeding;

  private boolean adrenaline;

  private IItemProvider containerItem;

  public MedicalItem(MedicalItem.Properties properties) {
    super(properties);
    this.health = properties.health;
    this.healBrokenLeg = properties.healBrokenLeg;
    this.containerItem = properties.containerItem;
    this.stopBleeding = properties.stopBleeding;
    this.adrenaline = properties.adrenaline;
  }

  // TODO Fix Animation
  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.CROSSBOW;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    if (playerIn.getHealth() > 20F) {
      return new ActionResult<>(ActionResultType.FAIL, itemStack);
    } else {
      playerIn.setActiveHand(handIn);
      return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (this.health > 0) {
      entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
        entityLiving.heal(this.health);
      });
    }

    if (this.healBrokenLeg) {
      entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
        entityLiving.removePotionEffect(ModEffects.BROKEN_LEG);
      });
    }

    if (this.stopBleeding) {
      entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
        entityLiving.removePotionEffect(ModEffects.BROKEN_LEG);
      });
    }

    if (this.adrenaline) {
      entityLiving.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
        entityLiving.addPotionEffect(new EffectInstance(Effects.SPEED, 2000, 1));
      });
    }

    if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
      ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
    }

    stack.shrink(1);
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
  public int getUseDuration(ItemStack stack) {
    if (stack.getItem().isFood()) {
      return this.getFood().isFastEating() ? 16 : 32;
    } else {
      return 32;
    }
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
      ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    if (this.health > 0) {
      tooltip.add(new TranslationTextComponent(
          TextFormatting.GRAY + "Hearts " + TextFormatting.RED + this.health));
    }

    if (this.healBrokenLeg) {
      tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Fixes Broken Legs"));
    }

    if (this.stopBleeding) {
      tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Stops Bleeding"));
    }

    if (this.adrenaline) {
      tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Induces Adrenaline"));
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

    public int health;

    public boolean adrenaline = false;

    public boolean healBrokenLeg = false;

    public boolean stopBleeding = false; // stop loss of blood

    public boolean infection = false;

    public IItemProvider containerItem;

    public MedicalItem.Properties setMaxStackSize(int maxStackSize) {
      this.maxStackSize(maxStackSize);
      return this;
    }

    public MedicalItem.Properties setHealth(int health) {
      this.health = health;
      return this;
    }

    public MedicalItem.Properties setGroup(ItemGroup groupIn) {
      this.group(groupIn);
      return this;
    }

    public MedicalItem.Properties setAdrenaline() {
      this.adrenaline = true;
      return this;
    }

    public MedicalItem.Properties setHealBrokenLeg() {
      this.healBrokenLeg = true;
      return this;
    }

    public MedicalItem.Properties setStopBleeding() {
      this.stopBleeding = true;
      return this;
    }

    public MedicalItem.Properties setInfection() {
      this.infection = true;
      return this;
    }

    public MedicalItem.Properties setContainer(IItemProvider containerItem) {
      this.containerItem = containerItem;
      return this;
    }
  }
}
