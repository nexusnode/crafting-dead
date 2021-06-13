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

import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.item.scope.SimpleScope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class BinocularsItem extends Item {

  private static final ResourceLocation SCOPE_OVERLAY_TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/scope/binoculars.png");

  public BinocularsItem(Properties properties) {
    super(properties);
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    return 72000;
  }

  @Override
  public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity,
      Hand hand) {
    ItemStack itemstack = playerEntity.getItemInHand(hand);
    playerEntity.startUsingItem(hand);
    playerEntity.playSound(ModSoundEvents.SCOPE_ZOOM.get(), 0.75F, 1.0F);
    if (world.isClientSide()) {
      Minecraft mc = Minecraft.getInstance();
      mc.options.setCameraType(PointOfView.FIRST_PERSON);
    }
    return ActionResult.consume(itemstack);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> new SimpleScope(14, SCOPE_OVERLAY_TEXTURE, 2048, 512, itemStack)),
        () -> Capabilities.SCOPE);
  }
}
