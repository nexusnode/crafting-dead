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
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.item.scope.Scope;
import com.craftingdead.core.world.item.scope.SimpleScope;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
  public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity,
      InteractionHand hand) {
    ItemStack itemstack = playerEntity.getItemInHand(hand);
    playerEntity.startUsingItem(hand);
    playerEntity.playSound(ModSoundEvents.SCOPE_ZOOM.get(), 0.75F, 1.0F);
    if (world.isClientSide()) {
      Minecraft mc = Minecraft.getInstance();
      mc.options.setCameraType(CameraType.FIRST_PERSON);
    }
    return InteractionResultHolder.consume(itemstack);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> new SimpleScope(14, SCOPE_OVERLAY_TEXTURE, 2048, 512, itemStack)),
        () -> Scope.CAPABILITY);
  }
}
