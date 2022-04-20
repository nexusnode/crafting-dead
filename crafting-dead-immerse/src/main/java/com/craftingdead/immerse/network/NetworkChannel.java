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

package com.craftingdead.immerse.network;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.network.login.AcknowledgeGameMessage;
import com.craftingdead.immerse.network.login.LoginIndexedMessage;
import com.craftingdead.immerse.network.login.SetupGameMessage;
import com.craftingdead.immerse.network.play.ChangeGameMessage;
import com.craftingdead.immerse.network.play.DisplayKilledMessage;
import com.craftingdead.immerse.network.play.RegisterLandOwnerMessage;
import com.craftingdead.immerse.network.play.RemoveLandOwnerMessage;
import com.craftingdead.immerse.network.play.SyncGameMessage;
import com.craftingdead.immerse.network.play.SyncLandChunkMessage;
import com.craftingdead.immerse.network.play.SyncLandManagerMessage;
import com.craftingdead.immerse.network.play.SyncLandOwnerMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public enum NetworkChannel {

  LOGIN(new ResourceLocation(CraftingDeadImmerse.ID, "login")) {
    @Override
    protected void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel.messageBuilder(SetupGameMessage.class, 0x00, NetworkDirection.LOGIN_TO_CLIENT)
          .loginIndex(LoginIndexedMessage::getLoginIndex, LoginIndexedMessage::setLoginIndex)
          .encoder(SetupGameMessage::encode).decoder(SetupGameMessage::decode)
          .consumer(HandshakeHandler
              .biConsumerFor((handler, msg, ctx) -> SetupGameMessage.handle(msg, ctx)))
          .buildLoginPacketList(isLocal -> CraftingDeadImmerse.getInstance().getLogicalServer()
              .generateSetupGameMessage(isLocal))
          .add();

      simpleChannel
          .messageBuilder(AcknowledgeGameMessage.class, 0x01, NetworkDirection.LOGIN_TO_SERVER)
          .loginIndex(LoginIndexedMessage::getLoginIndex, LoginIndexedMessage::setLoginIndex)
          .encoder(AcknowledgeGameMessage::encode).decoder(AcknowledgeGameMessage::decode)
          .consumer(HandshakeHandler
              .indexFirst((handler, msg, ctx) -> AcknowledgeGameMessage.handle(msg, ctx)))
          .add();
    }
  },
  PLAY(new ResourceLocation(CraftingDeadImmerse.ID, "play")) {
    @Override
    protected void registerMessages(SimpleChannel simpleChannel) {
      simpleChannel
          .messageBuilder(ChangeGameMessage.class, 0x00, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(ChangeGameMessage::encode)
          .decoder(ChangeGameMessage::decode)
          .consumer(ChangeGameMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(DisplayKilledMessage.class, 0x01, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(DisplayKilledMessage::encode)
          .decoder(DisplayKilledMessage::decode)
          .consumer(DisplayKilledMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SyncGameMessage.class, 0x02, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncGameMessage::encode)
          .decoder(SyncGameMessage::decode)
          .consumer(SyncGameMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(RegisterLandOwnerMessage.class, 0x03, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(RegisterLandOwnerMessage::encode)
          .decoder(RegisterLandOwnerMessage::decode)
          .consumer(RegisterLandOwnerMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(RemoveLandOwnerMessage.class, 0x04, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(RemoveLandOwnerMessage::encode)
          .decoder(RemoveLandOwnerMessage::decode)
          .consumer(RemoveLandOwnerMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SyncLandChunkMessage.class, 0x05, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncLandChunkMessage::encode)
          .decoder(SyncLandChunkMessage::decode)
          .consumer(SyncLandChunkMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SyncLandOwnerMessage.class, 0x06, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncLandOwnerMessage::encode)
          .decoder(SyncLandOwnerMessage::decode)
          .consumer(SyncLandOwnerMessage::handle)
          .add();
      simpleChannel
          .messageBuilder(SyncLandManagerMessage.class, 0x07, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncLandManagerMessage::encode)
          .decoder(SyncLandManagerMessage::decode)
          .consumer(SyncLandManagerMessage::handle)
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
