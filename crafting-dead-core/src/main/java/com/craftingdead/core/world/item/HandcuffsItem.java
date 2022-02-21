/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class HandcuffsItem extends Item {

  public HandcuffsItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity livingEntity, InteractionHand hand) {
    if (livingEntity instanceof ServerPlayer playerHit) {
      if (playerHit.getAbilities().invulnerable) {
        return InteractionResult.PASS;
      }

      var extension = PlayerExtension.getOrThrow(playerHit);
      if (!extension.isHandcuffed()) {
        extension.setHandcuffs(itemStack.copy());
        playerHit.displayClientMessage(
            new TranslatableComponent("handcuffs.handcuffed", player.getDisplayName())
                .withStyle(ChatFormatting.RED),
            true);
        itemStack.setCount(0);
      }
    }
    return InteractionResult.sidedSuccess(player.getLevel().isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack item, @Nullable Level level,
      List<Component> lines, TooltipFlag tooltipFlag) {
    lines.add(new TranslatableComponent("handcuffs.information").withStyle(ChatFormatting.GRAY));
  }
}
