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

package com.craftingdead.core.network;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public interface Synched {

  void encode(FriendlyByteBuf out, boolean writeAll);

  void decode(FriendlyByteBuf in);

  boolean requiresSync();

  default void encodeNbt(CompoundTag nbt, boolean writeAll) {
    FriendlyByteBuf capabilityData = new FriendlyByteBuf(Unpooled.buffer());
    this.encode(capabilityData, writeAll);
    byte[] capabilityDataBytes = new byte[capabilityData.readableBytes()];
    capabilityData.readBytes(capabilityDataBytes);
    nbt.put("__FORGE_CAPABILITY__", new ByteArrayTag(capabilityDataBytes));
  }

  default void decodeNbt(CompoundTag nbt) {
    this.decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(nbt.getByteArray("__FORGE_CAPABILITY__"))));
  }
}
