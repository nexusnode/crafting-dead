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

package com.craftingdead.immerse.network;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.network.login.AcknowledgeGameMessage;
import com.craftingdead.immerse.network.login.LoginIndexedMessage;
import com.craftingdead.immerse.network.login.SetupGameMessage;
import com.craftingdead.immerse.network.play.ChangeGameMessage;
import com.craftingdead.immerse.network.play.DisplayKilledMessage;
import com.craftingdead.immerse.network.play.SyncGameMessage;
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
