/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.network.message.login;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.game.GameType;
import com.craftingdead.core.network.NetworkChannel;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetupGameMessage extends LoginIndexedMessage {

  private final GameType gameType;

  public SetupGameMessage(GameType gameType) {
    this.gameType = gameType;
    System.out.println("test0");

  }

  public static void encode(SetupGameMessage msg, PacketBuffer out) {
    System.out.println("test1");

    out.writeRegistryId(msg.gameType);
  }

  public static SetupGameMessage decode(PacketBuffer in) {
    System.out.println("test2");

    return new SetupGameMessage(in.readRegistryId());
  }

  public static void handle(SetupGameMessage msg, Supplier<NetworkEvent.Context> ctx) {
    System.out.println("test3");
    ctx.get().enqueueWork(
        () -> ((ClientDist) CraftingDead.getInstance().getModDist()).loadGame(msg.gameType));
    ctx.get().setPacketHandled(true);
    NetworkChannel.LOGIN.getSimpleChannel().reply(new AcknowledgeGameMessage(), ctx.get());
  }
}
