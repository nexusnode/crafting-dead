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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.GameServer;
import com.craftingdead.immerse.game.PlayerRemovalReason;
import com.craftingdead.immerse.world.effect.ImmerseMobEffects;
import com.craftingdead.immerse.world.item.hydration.Hydration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SurvivalServer extends SurvivalGame implements GameServer {

  public static final Codec<SurvivalServer> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          Codec.BOOL.fieldOf("thirst_enabled").forGetter(SurvivalGame::isThirstEnabled),
          Codec.BOOL.fieldOf("kill_feed_enabled").forGetter(SurvivalServer::killFeedEnabled))
      .apply(instance, SurvivalServer::new));

  private final boolean killFeedEnabled;

  public SurvivalServer(boolean thirstEnabled, boolean killFeedEnabled) {
    super(thirstEnabled);
    this.killFeedEnabled = killFeedEnabled;
  }

  @Override
  public boolean persistPlayerData() {
    return true;
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean persistGameData() {
    return true;
  }

  @Override
  public boolean killFeedEnabled() {
    return this.killFeedEnabled;
  }

  @SubscribeEvent
  public void handleLivingExtensionLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension<?> player
        && !player.level().isClientSide()) {
      player.registerHandler(SurvivalPlayerHandler.TYPE, new SurvivalPlayerHandler(this, player));
    }
  }

  @Override
  public void removePlayer(PlayerExtension<ServerPlayer> player, PlayerRemovalReason reason) {
    if (reason.isGameUnloaded()) {
      player.removeHandler(SurvivalPlayerHandler.TYPE);
    }
  }

  @SubscribeEvent
  public void handleUseItemFinish(LivingEntityUseItemEvent.Finish event) {
    event.getItem().getCapability(Hydration.CAPABILITY)
        .map(hydration -> hydration.getWater(event.getItem()))
        .ifPresent(hydration -> event
            .getEntityLiving()
            .addEffect(new MobEffectInstance(ImmerseMobEffects.HYDRATE.get(), 1, hydration)));
  }

  @SubscribeEvent
  public void handleAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
    var item = event.getObject().getItem();

    if (item == Items.POTION) {
      event.addCapability(Hydration.CAPABILITY_KEY,
          CapabilityUtil.provider(() -> Hydration.POTION, Hydration.CAPABILITY));
      return;
    }

    final int water;
    if (item == Items.APPLE || item == Items.RABBIT_STEW) {
      water = 2;
    } else if (item == Items.CARROT || item == Items.BEETROOT || item == Items.HONEY_BOTTLE) {
      water = 1;
    } else if (item == Items.CHORUS_FRUIT || item == Items.SWEET_BERRIES) {
      water = 3;
    } else if (item == Items.ENCHANTED_GOLDEN_APPLE
        || item == Items.GOLDEN_APPLE
        || item == Items.MUSHROOM_STEW
        || item == Items.SUSPICIOUS_STEW
        || item == Items.BEETROOT_SOUP
        || item == Items.MELON_SLICE) {
      water = 5;
    } else if (item == Items.GOLDEN_CARROT) {
      water = 6;
    } else {
      water = -1;
    }

    if (water != -1) {
      event.addCapability(Hydration.CAPABILITY_KEY,
          CapabilityUtil.provider(() -> Hydration.fixed(water), Hydration.CAPABILITY));
    }
  }
}
