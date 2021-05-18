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
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public interface NetworkProtocol {

  static final NetworkProtocol EMPTY = new NetworkProtocol() {

    @Override
    public <T> PacketBuffer encode(T object) throws IOException {
      throw new UnsupportedOperationException("Empty protocol");
    }

    @Override
    public <T> void process(PacketBuffer buf, Context ctx) throws IOException {
      throw new UnsupportedOperationException("Empty protocol");
    }
  };

  <T> PacketBuffer encode(T object) throws IOException;

  <T> void process(PacketBuffer buf, NetworkEvent.Context ctx) throws IOException;
}
