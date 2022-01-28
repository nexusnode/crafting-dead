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

import com.craftingdead.immerse.game.tdm.TdmClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncMessage implements TdmClientMessage {

  private final FriendlyByteBuf gameData;

  public SyncMessage(FriendlyByteBuf gameData) {
    this.gameData = gameData;
  }

  @Override
  public void handle(TdmClient gameClient, NetworkEvent.Context context) {
    gameClient.decode(this.gameData);
  }
}
