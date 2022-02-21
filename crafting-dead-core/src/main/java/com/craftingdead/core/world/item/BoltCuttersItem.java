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

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BoltCuttersItem extends MeleeWeaponItem {

  private final int damageToHandcuffs;

  public BoltCuttersItem(int damageToHandcuffs, int attackDamage, double attackSpeed,
      Properties properties) {
    super(attackDamage, attackSpeed, properties);
    this.damageToHandcuffs = damageToHandcuffs;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity livingEntity, InteractionHand hand) {
    if (livingEntity instanceof ServerPlayer playerHit) {
      var extension = PlayerExtension.getOrThrow(playerHit);
      if (extension.isHandcuffed()) {
        if (extension.damageHandcuffs(this.damageToHandcuffs)) {
          playerHit.displayClientMessage(
              new TranslatableComponent("bolt_cutters.free", player.getDisplayName())
                  .withStyle(ChatFormatting.GREEN),
              true);
        } else {
          playerHit.getLevel().playLocalSound(playerHit.getX(), playerHit.getY(),
              playerHit.getZ(), SoundEvents.ITEM_BREAK,
              playerHit.getSoundSource(), 0.2F, 0.5F, false);
        }

        itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      }
    }
    return InteractionResult.sidedSuccess(player.getLevel().isClientSide());
  }
}
