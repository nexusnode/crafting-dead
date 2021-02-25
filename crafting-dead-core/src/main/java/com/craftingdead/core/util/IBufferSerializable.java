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

package com.craftingdead.core.util;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public interface IBufferSerializable {

  void encode(PacketBuffer out, boolean writeAll);

  void decode(PacketBuffer in);

  /**
   * Whether this capability's data is 'dirty' and needs to be sent to the client.
   */
  boolean requiresSync();

  default void encodeNbt(CompoundNBT nbt, boolean writeAll) {
    PacketBuffer capabilityData = new PacketBuffer(Unpooled.buffer());
    this.encode(capabilityData, writeAll);
    byte[] capabilityDataBytes = new byte[capabilityData.readableBytes()];
    capabilityData.readBytes(capabilityDataBytes);
    nbt.put("__FORGE_CAPABILITY__", new ByteArrayNBT(capabilityDataBytes));
  }

  default void decodeNbt(CompoundNBT nbt) {
    this.decode(new PacketBuffer(Unpooled.wrappedBuffer(nbt.getByteArray("__FORGE_CAPABILITY__"))));
  }
}
