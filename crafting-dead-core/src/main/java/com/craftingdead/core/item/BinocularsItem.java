/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.item;

import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.scope.DefaultScope;
import com.craftingdead.core.util.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity,
      Hand hand) {
    ItemStack itemstack = playerEntity.getHeldItem(hand);
    playerEntity.setActiveHand(hand);
    playerEntity.playSound(ModSoundEvents.SCOPE_ZOOM.get(), 0.75F, 1.0F);
    if (world.isRemote()) {
      Minecraft mc = Minecraft.getInstance();
      mc.gameSettings.thirdPersonView = 0;
    }
    return ActionResult.resultConsume(itemstack);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(
        new DefaultScope(14, SCOPE_OVERLAY_TEXTURE, 2048, 512), () -> ModCapabilities.SCOPE);
  }
}
