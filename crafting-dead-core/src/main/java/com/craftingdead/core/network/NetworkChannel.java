/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.network;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.network.message.play.CancelActionMessage;
import com.craftingdead.core.network.message.play.CrouchMessage;
import com.craftingdead.core.network.message.play.HitMessage;
import com.craftingdead.core.network.message.play.KillFeedMessage;
import com.craftingdead.core.network.message.play.OpenModInventoryMessage;
import com.craftingdead.core.network.message.play.OpenStorageMessage;
import com.craftingdead.core.network.message.play.PerformActionMessage;
import com.craftingdead.core.network.message.play.SetSlotsMessage;
import com.craftingdead.core.network.message.play.SyncGunMessage;
import com.craftingdead.core.network.message.play.SyncPlayerMessage;
import com.craftingdead.core.network.message.play.ToggleFireModeMessage;
import com.craftingdead.core.network.message.play.ToggleRightMouseAbility;
import com.craftingdead.core.network.message.play.TriggerPressedMessage;
import com.craftingdead.core.network.message.play.ValidateLivingHitMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public enum NetworkChannel {

  PLAY(new ResourceLocation(CraftingDead.ID, "play")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel
          .messageBuilder(SyncPlayerMessage.class, 0x00, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncPlayerMessage::encode)
          .decoder(SyncPlayerMessage::decode)
          .consumer(SyncPlayerMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(OpenModInventoryMessage.class, 0x01, NetworkDirection.PLAY_TO_SERVER)
          .encoder(OpenModInventoryMessage::encode)
          .decoder(OpenModInventoryMessage::decode)
          .consumer(OpenModInventoryMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ToggleRightMouseAbility.class, 0x02, NetworkDirection.PLAY_TO_SERVER)
          .encoder(ToggleRightMouseAbility::encode)
          .decoder(ToggleRightMouseAbility::decode)
          .consumer(ToggleRightMouseAbility::handle)
          .add();

      simpleChannel
          .messageBuilder(ToggleFireModeMessage.class, 0x03)
          .encoder(ToggleFireModeMessage::encode)
          .decoder(ToggleFireModeMessage::decode)
          .consumer(ToggleFireModeMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(TriggerPressedMessage.class, 0x04)
          .encoder(TriggerPressedMessage::encode)
          .decoder(TriggerPressedMessage::decode)
          .consumer(TriggerPressedMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SyncGunMessage.class, 0x05, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncGunMessage::encode)
          .decoder(SyncGunMessage::decode)
          .consumer(SyncGunMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SetSlotsMessage.class, 0x06, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SetSlotsMessage::encode)
          .decoder(SetSlotsMessage::decode)
          .consumer(SetSlotsMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(OpenStorageMessage.class, 0x07, NetworkDirection.PLAY_TO_SERVER)
          .encoder(OpenStorageMessage::encode)
          .decoder(OpenStorageMessage::decode)
          .consumer(OpenStorageMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(PerformActionMessage.class, 0x08)
          .encoder(PerformActionMessage::encode)
          .decoder(PerformActionMessage::decode)
          .consumer(PerformActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(CancelActionMessage.class, 0x09)
          .encoder(CancelActionMessage::encode)
          .decoder(CancelActionMessage::decode)
          .consumer(CancelActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ValidateLivingHitMessage.class, 0x0A, NetworkDirection.PLAY_TO_SERVER)
          .encoder(ValidateLivingHitMessage::encode)
          .decoder(ValidateLivingHitMessage::decode)
          .consumer(ValidateLivingHitMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(CrouchMessage.class, 0x0B)
          .encoder(CrouchMessage::encode)
          .decoder(CrouchMessage::decode)
          .consumer(CrouchMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(HitMessage.class, 0x0C, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(HitMessage::encode)
          .decoder(HitMessage::decode)
          .consumer(HitMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(KillFeedMessage.class, 0x0D, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(KillFeedMessage::encode)
          .decoder(KillFeedMessage::decode)
          .consumer(KillFeedMessage::handle)
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
