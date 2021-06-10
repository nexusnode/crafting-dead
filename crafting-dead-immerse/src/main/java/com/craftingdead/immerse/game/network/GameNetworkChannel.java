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
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.ClientGameWrapper;
import com.craftingdead.immerse.game.Game;
import com.craftingdead.immerse.game.GameWrapper;
import com.craftingdead.immerse.game.ServerGameWrapper;
import com.craftingdead.immerse.game.module.Module;
import com.craftingdead.immerse.game.module.ModuleType;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
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
    ServerGameWrapper gameWrapper =
        CraftingDeadImmerse.getInstance().getLogicalServer().getGameWrapper();
    processInboundPayload(gameWrapper, event.getPayload(), event.getSource().get());
  }

  private static void handleServerPayload(NetworkEvent.ServerCustomPayloadEvent event) {
    ClientGameWrapper gameWrapper =
        CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
    processInboundPayload(gameWrapper, event.getPayload(), event.getSource().get());
  }

  private static <MSG> void processInboundPayload(GameWrapper<?, ?> gameWrapper, PacketBuffer buf,
      NetworkEvent.Context context) {
    try {
      if (buf.readBoolean()) {
        ModuleType moduleType = buf.readRegistryIdSafe(ModuleType.class);
        Module module = gameWrapper.getModule(moduleType);
        MSG message = moduleType.getNetworkProtocol().decode(buf, context);
        module.handleMessage(message, context);
      } else {
        MSG message = gameWrapper.getGame().getType().getNetworkProtocol().decode(buf, context);
        gameWrapper.getGame().handleMessage(message, context);
      }
    } catch (IOException e) {
      logger.error("Failed to process server packet for '{}'",
          gameWrapper.getGame().getType().getRegistryName().toString());
    }
  }

  public static <MSG> void sendToServer(@Nullable ModuleType moduleType, MSG message) {
    sendTo(moduleType, message, Minecraft.getInstance().getConnection().getConnection(),
        NetworkDirection.PLAY_TO_SERVER);
  }

  public static <MSG> void sendTo(@Nullable ModuleType moduleType, MSG message,
      NetworkManager manager, NetworkDirection direction) {
    manager.send(toVanillaPacket(moduleType, message, direction));
  }

  public static <MSG> void send(@Nullable ModuleType moduleType, MSG message,
      PacketDistributor.PacketTarget target) {
    target.send(toVanillaPacket(moduleType, message, target.getDirection()));
  }

  public static <MSG> IPacket<?> toVanillaPacket(@Nullable ModuleType moduleType, MSG message,
      NetworkDirection direction) {
    Game<?> game = CraftingDeadImmerse.getInstance().getGame(direction.getOriginationSide());
    try {
      PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
      if (moduleType == null) {
        buf.writeBoolean(false);
        game.getType().getNetworkProtocol().encode(buf, message);
      } else {
        buf.writeBoolean(true);
        buf.writeRegistryId(moduleType);
        moduleType.getNetworkProtocol().encode(buf, message);
      }

      return direction
          .buildPacket(Pair.of(buf, Integer.MIN_VALUE), GameNetworkChannel.CHANNEL_NAME)
          .getThis();
    } catch (IOException e) {
      throw new EncoderException("Failed to encode game packet for '"
          + game.getType().getRegistryName().toString() + "'");
    }
  }
}
