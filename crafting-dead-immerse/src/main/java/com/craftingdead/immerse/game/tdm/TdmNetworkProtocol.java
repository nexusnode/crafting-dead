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

package com.craftingdead.immerse.game.tdm;

import com.craftingdead.immerse.game.network.SimpleNetworkProtocol;
import com.craftingdead.immerse.game.tdm.message.RequestJoinTeamMessage;

public class TdmNetworkProtocol extends SimpleNetworkProtocol {

  public static final TdmNetworkProtocol INSTANCE = new TdmNetworkProtocol();

  private TdmNetworkProtocol() {
    this.codecBuilder(0x00, RequestJoinTeamMessage.class)
        .encoder(RequestJoinTeamMessage::encode)
        .decoder(RequestJoinTeamMessage::decode)
        .register();
  }
}
