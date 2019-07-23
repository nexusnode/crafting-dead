package com.craftingdead.mod.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;

public class UsableItem extends Item {

  private final List<Supplier<EffectInstance>> effects;
  private final IItemProvider returnItem;
  private final UseAction useAction;

  public UsableItem(Properties properties) {
    super(properties);
    this.effects = properties.effects;
    this.returnItem = properties.returnItem;
    this.useAction = properties.useAction;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    PlayerEntity playerentity =
        entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
    if (playerentity == null || !playerentity.abilities.isCreativeMode) {
      stack.shrink(1);
    }

    if (!worldIn.isRemote) {
      for (Supplier<EffectInstance> effectinstance : this.effects) {
        if (effectinstance.get().getPotion().isInstant()) {
          effectinstance.get().getPotion().affectEntity(playerentity, playerentity, entityLiving,
              effectinstance.get().getAmplifier(), 1.0D);
        } else {
          entityLiving.addPotionEffect(new EffectInstance(effectinstance.get()));
        }
      }
    }

    if (playerentity != null) {
      playerentity.addStat(Stats.ITEM_USED.get(this));
    }

    if (playerentity != null) {
      playerentity.inventory.addItemStackToInventory(new ItemStack(this.returnItem));
    }
    return stack;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 32;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return this.useAction;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
  }

  public static class Properties extends Item.Properties {

    private List<Supplier<EffectInstance>> effects = new ArrayList<>();
    private IItemProvider returnItem;
    private UseAction useAction;

    public Properties addEffect(Supplier<EffectInstance> effect) {
      this.effects.add(effect);
      return this;
    }

    public Properties setReturnItem(IItemProvider returnItem) {
      this.returnItem = returnItem;
      return this;
    }

    public Properties setUseAction(UseAction useAction) {
      this.useAction = useAction;
      return this;
    }
  }
}
