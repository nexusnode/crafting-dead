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
