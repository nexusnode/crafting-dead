/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item;

import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.CapabilityUtil;
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
    return CapabilityUtil.provider(
        () -> new SimpleScope(14, SCOPE_OVERLAY_TEXTURE, 2048, 512, itemStack),
        Scope.CAPABILITY);
  }
}
