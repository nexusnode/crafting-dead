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

package com.craftingdead.immerse.game.deathmatch.message;

import javax.annotation.Nullable;
import com.craftingdead.core.living.IPlayer;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.deathmatch.DeathmatchServer;
import com.craftingdead.immerse.game.deathmatch.DeathmatchTeam;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RequestJoinTeamMessage {

  @Nullable
  private final DeathmatchTeam team;

  public RequestJoinTeamMessage(@Nullable DeathmatchTeam team) {
    this.team = team;
  }

  public static void encode(RequestJoinTeamMessage message, PacketBuffer out) {
    out.writeBoolean(message.team == null);
    if (message.team != null) {
      out.writeEnum(message.team);
    }
  }

  public static RequestJoinTeamMessage decode(PacketBuffer in) {
    return new RequestJoinTeamMessage(
        in.readBoolean() ? null : in.readEnum(DeathmatchTeam.class));
  }

  public static void handle(RequestJoinTeamMessage message, NetworkEvent.Context ctx) {
    DeathmatchServer gameServer =
        (DeathmatchServer) CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer();
    gameServer.setPlayerTeam(IPlayer.getExpected(ctx.getSender()),
        message.team == null ? null : gameServer.getTeamInstance(message.team));
    ctx.setPacketHandled(true);
  }
}
