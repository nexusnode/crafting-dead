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

package com.craftingdead.core.network;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.network.message.play.AddKillFeedEntryMessage;
import com.craftingdead.core.network.message.play.CancelActionMessage;
import com.craftingdead.core.network.message.play.CrouchMessage;
import com.craftingdead.core.network.message.play.EnableCombatModeMessage;
import com.craftingdead.core.network.message.play.HitMessage;
import com.craftingdead.core.network.message.play.OpenEquipmentMenuMessage;
import com.craftingdead.core.network.message.play.OpenStorageMessage;
import com.craftingdead.core.network.message.play.PerformActionMessage;
import com.craftingdead.core.network.message.play.SecondaryActionMessage;
import com.craftingdead.core.network.message.play.SetFireModeMessage;
import com.craftingdead.core.network.message.play.SyncGunContainerSlotMessage;
import com.craftingdead.core.network.message.play.SyncGunEquipmentSlotMessage;
import com.craftingdead.core.network.message.play.SyncLivingMessage;
import com.craftingdead.core.network.message.play.TriggerPressedMessage;
import com.craftingdead.core.network.message.play.ValidatePendingHitMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public enum NetworkChannel {

  PLAY(new ResourceLocation(CraftingDead.ID, "play")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel
          .messageBuilder(SyncLivingMessage.class, 0x00, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncLivingMessage::encode)
          .decoder(SyncLivingMessage::decode)
          .consumer(SyncLivingMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(OpenEquipmentMenuMessage.class, 0x01, NetworkDirection.PLAY_TO_SERVER)
          .encoder(OpenEquipmentMenuMessage::encode)
          .decoder(OpenEquipmentMenuMessage::decode)
          .consumer(OpenEquipmentMenuMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SecondaryActionMessage.class, 0x02)
          .encoder(SecondaryActionMessage::encode)
          .decoder(SecondaryActionMessage::decode)
          .consumer(SecondaryActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SetFireModeMessage.class, 0x03)
          .encoder(SetFireModeMessage::encode)
          .decoder(SetFireModeMessage::decode)
          .consumer(SetFireModeMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(TriggerPressedMessage.class, 0x04)
          .encoder(TriggerPressedMessage::encode)
          .decoder(TriggerPressedMessage::decode)
          .consumer(TriggerPressedMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SyncGunContainerSlotMessage.class, 0x05, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncGunContainerSlotMessage::encode)
          .decoder(SyncGunContainerSlotMessage::decode)
          .consumer(SyncGunContainerSlotMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(OpenStorageMessage.class, 0x06, NetworkDirection.PLAY_TO_SERVER)
          .encoder(OpenStorageMessage::encode)
          .decoder(OpenStorageMessage::decode)
          .consumer(OpenStorageMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(PerformActionMessage.class, 0x07)
          .encoder(PerformActionMessage::encode)
          .decoder(PerformActionMessage::decode)
          .consumer(PerformActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(CancelActionMessage.class, 0x08)
          .encoder(CancelActionMessage::encode)
          .decoder(CancelActionMessage::decode)
          .consumer(CancelActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ValidatePendingHitMessage.class, 0x09, NetworkDirection.PLAY_TO_SERVER)
          .encoder(ValidatePendingHitMessage::encode)
          .decoder(ValidatePendingHitMessage::decode)
          .consumer(ValidatePendingHitMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(CrouchMessage.class, 0x0A)
          .encoder(CrouchMessage::encode)
          .decoder(CrouchMessage::decode)
          .consumer(CrouchMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(HitMessage.class, 0x0B, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(HitMessage::encode)
          .decoder(HitMessage::decode)
          .consumer(HitMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(AddKillFeedEntryMessage.class, 0x0C, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(AddKillFeedEntryMessage::encode)
          .decoder(AddKillFeedEntryMessage::decode)
          .consumer(AddKillFeedEntryMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SyncGunEquipmentSlotMessage.class, 0x0D, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncGunEquipmentSlotMessage::encode)
          .decoder(SyncGunEquipmentSlotMessage::decode)
          .consumer(SyncGunEquipmentSlotMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(EnableCombatModeMessage.class, 0x0E, NetworkDirection.PLAY_TO_SERVER)
          .encoder(EnableCombatModeMessage::encode)
          .decoder(EnableCombatModeMessage::decode)
          .consumer(EnableCombatModeMessage::handle)
          .add();
    }
  };

  /**
   * Network protocol version.
   */
  private static final String NETWORK_VERSION = "0.0.1.0";
  /**
   * Prevents re-registering messages.
   */
  private static boolean loaded;
  /**
   * Simple channel.
   */
  private final SimpleChannel simpleChannel;

  private NetworkChannel(ResourceLocation channelName) {
    this.simpleChannel = NetworkRegistry.ChannelBuilder
        .named(channelName)
        .clientAcceptedVersions(NETWORK_VERSION::equals)
        .serverAcceptedVersions(NETWORK_VERSION::equals)
        .networkProtocolVersion(() -> NETWORK_VERSION)
        .simpleChannel();
  }

  protected abstract void registerMessages(SimpleChannel simpleChannel);

  public SimpleChannel getSimpleChannel() {
    return this.simpleChannel;
  }

  public static void loadChannels() {
    if (!loaded) {
      for (NetworkChannel channel : NetworkChannel.values()) {
        channel.registerMessages(channel.simpleChannel);
      }
      loaded = true;
    }
  }
}
