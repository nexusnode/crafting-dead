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

package com.craftingdead.immerse.game.tdm.message;

import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.team.ServerTeamModule;
import com.craftingdead.immerse.game.tdm.TdmServer;
import com.craftingdead.immerse.game.tdm.TdmTeam;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class RequestJoinTeamMessage implements TdmServerMessage {

  @Nullable
  private final TdmTeam team;

  public RequestJoinTeamMessage(@Nullable TdmTeam team) {
    this.team = team;
  }

  public void encode(PacketBuffer out) {
    out.writeBoolean(this.team == null);
    if (this.team != null) {
      out.writeEnum(this.team);
    }
  }

  public static RequestJoinTeamMessage decode(PacketBuffer in) {
    return new RequestJoinTeamMessage(
        in.readBoolean() ? null : in.readEnum(TdmTeam.class));
  }

  @Override
  public void handle(TdmServer gameServer, Context context) {
    ServerTeamModule<TdmTeam> teamModule = gameServer.getTeamModule();
    teamModule.setPlayerTeam(PlayerExtension.getOrThrow(context.getSender()),
        this.team == null ? null : teamModule.getTeamInstance(this.team));
  }
}
