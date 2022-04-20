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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.grenade.Grenade;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RemoteDetonatorItem extends Item {

  public RemoteDetonatorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    if (!CraftingDead.serverConfig.explosivesRemoteDetonatorEnabled.get()) {
      return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
    var item = player.getItemInHand(hand);
    player.startUsingItem(hand);

    if (level instanceof ServerLevel serverLevel) {
      serverLevel.playSound(null, player, SoundEvents.UI_BUTTON_CLICK,
          SoundSource.PLAYERS, 0.8F, 1.2F);

      serverLevel.getEntities(player,
          player.getBoundingBox().inflate(CraftingDead.serverConfig.explosivesRemoteDetonatorRange.get()), (entity) -> {
            if (!(entity instanceof Grenade)) {
              return false;
            }
            Grenade grenadeEntity = (Grenade) entity;

            boolean isOwner =
                grenadeEntity.getSource().map(thrower -> thrower == player).orElse(false);

            return isOwner && grenadeEntity.canBeRemotelyActivated();
          }).forEach(entity -> ((Grenade) entity).setActivated(true));
    }
    return InteractionResultHolder.consume(item);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level,
      List<Component> lines, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, level, lines, tooltipFlag);
    lines.add(new TranslatableComponent("remote_detonator.information", CraftingDead.serverConfig.explosivesRemoteDetonatorRange.get())
        .withStyle(ChatFormatting.GRAY));
  }
}
