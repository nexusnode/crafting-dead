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

package com.craftingdead.immerse.game.network;

import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface NetworkProtocol {

  static final NetworkProtocol EMPTY = new NetworkProtocol() {

    @Override
    public <T> void encode(FriendlyByteBuf buf, T payload) throws IOException {
      throw new UnsupportedOperationException("Empty protocol");
    }

    @Override
    public <T> T decode(FriendlyByteBuf buf, NetworkEvent.Context context) throws IOException {
      throw new UnsupportedOperationException("Empty protocol");
    }
  };

  <T> void encode(FriendlyByteBuf buf, T payload) throws IOException;

  <T> T decode(FriendlyByteBuf buf, NetworkEvent.Context context) throws IOException;
}
