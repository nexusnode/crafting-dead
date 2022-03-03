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

package com.craftingdead.immerse.game.network;

import java.io.IOException;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameWrapper;
import com.craftingdead.immerse.game.module.ModuleType;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.event.EventNetworkChannel;

public class GameNetworkChannel {

  public static final ResourceLocation CHANNEL_NAME =
      new ResourceLocation(CraftingDeadImmerse.ID, "game");

  private static final EventNetworkChannel channel =
      NetworkRegistry.newEventChannel(CHANNEL_NAME,
          () -> CraftingDeadImmerse.VERSION, CraftingDeadImmerse.VERSION::equals,
          CraftingDeadImmerse.VERSION::equals);

  private static final Logger logger = LogUtils.getLogger();

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
    var gameWrapper = CraftingDeadImmerse.getInstance().getLogicalServer().getGameWrapper();
    processInboundPayload(gameWrapper, event.getPayload(), event.getSource().get());
  }

  private static void handleServerPayload(NetworkEvent.ServerCustomPayloadEvent event) {
    var gameWrapper = CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
    processInboundPayload(gameWrapper, event.getPayload(), event.getSource().get());
  }

  private static <MSG> void processInboundPayload(GameWrapper<?, ?> gameWrapper,
      FriendlyByteBuf buf, NetworkEvent.Context context) {
    try {
      if (buf.readBoolean()) {
        var moduleType = buf.readRegistryIdSafe(ModuleType.class);
        var module = gameWrapper.getModule(moduleType);
        var message = moduleType.getNetworkProtocol().decode(buf, context);
        module.handleMessage(message, context);
      } else {
        var message = gameWrapper.getGame().getType().getNetworkProtocol().decode(buf, context);
        gameWrapper.getGame().handleMessage(message, context);
      }
      context.setPacketHandled(true);
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
      Connection manager, NetworkDirection direction) {
    manager.send(toVanillaPacket(moduleType, message, direction));
  }

  public static <MSG> void send(@Nullable ModuleType moduleType, MSG message,
      PacketDistributor.PacketTarget target) {
    target.send(toVanillaPacket(moduleType, message, target.getDirection()));
  }

  public static <MSG> Packet<?> toVanillaPacket(@Nullable ModuleType moduleType, MSG message,
      NetworkDirection direction) {
    var game = CraftingDeadImmerse.getInstance().getGame(direction.getOriginationSide());
    try {
      var buf = new FriendlyByteBuf(Unpooled.buffer());
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
