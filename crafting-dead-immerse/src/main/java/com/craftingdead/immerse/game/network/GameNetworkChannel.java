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

package com.craftingdead.immerse.game.network;

import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.IGame;
import io.netty.handler.codec.EncoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

public class GameNetworkChannel {

  public static final ResourceLocation CHANNEL_NAME =
      new ResourceLocation(CraftingDeadImmerse.ID, "game");

  private static final EventNetworkChannel channel =
      NetworkRegistry.newEventChannel(CHANNEL_NAME,
          () -> CraftingDeadImmerse.VERSION, CraftingDeadImmerse.VERSION::equals,
          CraftingDeadImmerse.VERSION::equals);

  private static final Logger logger = LogManager.getLogger();

  private static boolean loaded = false;

  public static void load() {
    if (loaded) {
      throw new IllegalStateException("Game network channel already loaded");
    }
    loaded = true;
    channel.addListener(GameNetworkChannel::handleClientPayload);
    channel.addListener(GameNetworkChannel::handleServerPayload);
  }

  private static void handleClientPayload(NetworkEvent.ClientCustomPayloadEvent event) {
    IGame game = CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer();
    try {
      game.getNetworkProtocol().process(event.getPayload(), event.getSource().get());
    } catch (IOException e) {
      logger.error("Failed to process server packet for '{}'",
          game.getGameType().getRegistryName().toString());
    }
  }

  private static void handleServerPayload(NetworkEvent.ServerCustomPayloadEvent event) {
    IGame game = CraftingDeadImmerse.getInstance().getClientDist().getGameClient();
    try {
      game.getNetworkProtocol().process(event.getPayload(), event.getSource().get());
    } catch (IOException e) {
      logger.error("Failed to process game packet for '{}'",
          game.getGameType().getRegistryName().toString());
    }
  }

  public static <MSG> void sendToServer(MSG message) {
    sendTo(message, Minecraft.getInstance().getConnection().getNetworkManager(),
        NetworkDirection.PLAY_TO_SERVER);
  }

  public static <MSG> void sendTo(MSG message, NetworkManager manager, NetworkDirection direction) {
    manager.sendPacket(toVanillaPacket(message, direction));
  }

  public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
    target.send(toVanillaPacket(message, target.getDirection()));
  }

  public static <MSG> IPacket<?> toVanillaPacket(MSG message, NetworkDirection direction) {
    IGame game = CraftingDeadImmerse.getInstance().getGame(direction.getOriginationSide());
    try {
      return direction
          .buildPacket(Pair.of(game.getNetworkProtocol().encode(message), Integer.MIN_VALUE),
              GameNetworkChannel.CHANNEL_NAME)
          .getThis();
    } catch (IOException e) {
      throw new EncoderException("Failed to encode game packet for '"
          + game.getGameType().getRegistryName().toString() + "'");
    }
  }
}
