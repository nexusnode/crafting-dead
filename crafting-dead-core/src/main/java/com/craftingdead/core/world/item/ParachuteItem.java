/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
