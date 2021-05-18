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

package com.craftingdead.immerse.network.play;

import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.game.Game;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncGameMessage {

  private final PacketBuffer gameData;

  public SyncGameMessage(Game game, boolean writeAll) {
    PacketBuffer gameData = new PacketBuffer(Unpooled.buffer());
    game.encode(gameData, writeAll);
    this.gameData = gameData;
  }

  public SyncGameMessage(PacketBuffer gameData) {
    this.gameData = gameData;
  }

  public static void encode(SyncGameMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.gameData.readableBytes());
    out.writeBytes(msg.gameData);
  }

  public static SyncGameMessage decode(PacketBuffer in) {
    byte[] gameData = new byte[in.readVarInt()];
    in.readBytes(gameData);
    return new SyncGameMessage(new PacketBuffer(Unpooled.wrappedBuffer(gameData)));
  }

  public static boolean handle(SyncGameMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> ((ClientDist) CraftingDeadImmerse.getInstance().getModDist()).getGameClient()
            .decode(msg.gameData));
    return true;
  }
}
