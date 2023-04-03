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

package com.craftingdead.immerse.mixin;

import com.google.common.collect.Maps;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.HandshakeMessages;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static net.minecraftforge.registries.ForgeRegistry.REGISTRIES;

@Mixin(HandshakeHandler.class)
public class MixinFMLHandshakeHandler {

  @Shadow(remap = false)
  @Final
  public static Marker FMLHSMARKER;
  @Shadow(remap = false)
  @Final
  public static Logger LOGGER;

  @Shadow(remap = false)
  private Set<ResourceLocation> registriesToReceive;
  @Shadow(remap = false)
  private Map<ResourceLocation, ForgeRegistry.Snapshot> registrySnapshots;

  /**
   * @author
   * @reason
   */
  @Overwrite(remap = false)
  void handleServerModListOnClient(HandshakeMessages.S2CModList serverModList,
      Supplier<NetworkEvent.Context> c) {
    LOGGER.debug(FMLHSMARKER, "Logging into server with mod list [{}]",
        String.join(", ", serverModList.getModList()));
    c.get().setPacketHandled(true);
    NetworkConstants.handshakeChannel.reply(new HandshakeMessages.C2SModListReply(), c.get());

    LOGGER.debug(FMLHSMARKER, "Accepted server connection");
    // Set the modded marker on the channel so we know we got packets
    c.get().getNetworkManager().channel().attr(NetworkConstants.FML_NETVERSION)
        .set(NetworkConstants.NETVERSION);
    c.get().getNetworkManager().channel().attr(NetworkConstants.FML_CONNECTION_DATA)
        .set(new ConnectionData(serverModList.getModList().stream().collect(
            Collectors.toMap(Function.identity(), s -> Pair.of("", ""))),
            serverModList.getChannels()));

    this.registriesToReceive = new HashSet<>(serverModList.getRegistries());
    this.registrySnapshots = Maps.newHashMap();
    LOGGER.debug(REGISTRIES, "Expecting {} registries: {}", () -> this.registriesToReceive.size(),
        () -> this.registriesToReceive);
  }

  /**
   * @author
   * @reason
   */
  @Overwrite(remap = false)
  void handleClientModListOnServer(HandshakeMessages.C2SModListReply clientModList,
      Supplier<NetworkEvent.Context> c) {
    LOGGER.debug(FMLHSMARKER, "Received client connection with modlist [{}]",
        String.join(", ", clientModList.getModList()));
    boolean accepted =
        NetworkRegistry.validateServerChannels(clientModList.getChannels()).isEmpty();
    ((NetworkEvent.Context) c.get()).getNetworkManager().channel()
        .attr(NetworkConstants.FML_CONNECTION_DATA)
        .set(new ConnectionData(
            clientModList.getModList().stream()
                .collect(Collectors.toMap(Function.identity(), s -> Pair.of("", ""))),
            clientModList.getChannels()));
    ((NetworkEvent.Context) c.get()).setPacketHandled(true);
    if (!accepted) {
      LOGGER.error(FMLHSMARKER, "Terminating connection with client, mismatched mod list");
      c.get().getNetworkManager().send(new ClientboundDisconnectPacket(
          new TextComponent("Connection closed - mismatched mod channel list")));
      ((NetworkEvent.Context) c.get()).getNetworkManager()
          .disconnect(new TextComponent("Connection closed - mismatched mod channel list"));
    } else {
      LOGGER.debug(FMLHSMARKER, "Accepted client connection mod list");
    }
  }
}
