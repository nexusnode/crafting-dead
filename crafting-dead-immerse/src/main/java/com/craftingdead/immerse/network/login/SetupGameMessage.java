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

package com.craftingdead.immerse.network.login;

import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.network.NetworkChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SetupGameMessage extends LoginIndexedMessage {

  private final GameType gameType;

  public SetupGameMessage(GameType gameType) {
    this.gameType = gameType;
  }

  public static void encode(SetupGameMessage msg, FriendlyByteBuf out) {
    out.writeRegistryId(msg.gameType);
  }

  public static SetupGameMessage decode(FriendlyByteBuf in) {
    return new SetupGameMessage(in.readRegistryId());
  }

  public static void handle(SetupGameMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> CraftingDeadImmerse.getInstance().getClientDist().loadGame(msg.gameType));
    ctx.get().setPacketHandled(true);
    NetworkChannel.LOGIN.getSimpleChannel().reply(new AcknowledgeGameMessage(), ctx.get());
  }
}
