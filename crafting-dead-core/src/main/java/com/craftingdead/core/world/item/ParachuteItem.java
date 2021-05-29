package com.craftingdead.core.world.item;

import com.craftingdead.core.world.effect.ModMobEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ParachuteItem extends Item {

  public ParachuteItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
    ItemStack itemstack = player.getItemInHand(hand);
    player.addEffect(
        new EffectInstance(ModMobEffects.PARACHUTE.get(), 1200, 0, false, false));
    if (!player.abilities.invulnerable) {
      itemstack.shrink(1);
    }
    return ActionResult.sidedSuccess(itemstack, level.isClientSide());
  }
}
