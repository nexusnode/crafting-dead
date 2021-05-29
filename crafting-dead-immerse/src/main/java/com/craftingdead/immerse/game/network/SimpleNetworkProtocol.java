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
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SimpleNetworkProtocol implements NetworkProtocol {

  private final Map<Byte, Codec<?>> codecIndicies = new Byte2ObjectArrayMap<>();
  private final Map<Class<?>, Codec<?>> codecTypes = new Object2ObjectArrayMap<>();

  public <T> CodecBuilder<T> codecBuilder(int index, Class<T> type) {
    return new CodecBuilder<>(index, type);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void encode(PacketBuffer buf, T payload) throws IOException {
    Codec<T> entry = (Codec<T>) this.codecTypes.get(payload.getClass());
    if (entry == null) {
      throw new IOException("Unknown payload type " + payload.getClass().getName());
    }
    buf.writeByte(entry.index);
    entry.encoder.accept(payload, buf);
  }

  @Override
  public <T> T decode(PacketBuffer buf, NetworkEvent.Context ctx) throws IOException {
    byte index = buf.readByte();
    @SuppressWarnings("unchecked")
    Codec<T> entry = (Codec<T>) this.codecIndicies.get(index);
    if (entry != null) {
      return entry.decoder.apply(buf);
    } else {
      throw new IOException("Unknown payload index " + index);
    }
  }

  private static class Codec<T> {

    private final byte index;
    private final BiConsumer<T, PacketBuffer> encoder;
    private final Function<PacketBuffer, T> decoder;

    private Codec(byte index, BiConsumer<T, PacketBuffer> encoder,
        Function<PacketBuffer, T> decoder) {
      this.index = index;
      this.encoder = encoder;
      this.decoder = decoder;
    }
  }

  public class CodecBuilder<T> {

    private final byte index;
    private final Class<T> type;
    private BiConsumer<T, PacketBuffer> encoder;
    private Function<PacketBuffer, T> decoder;

    private CodecBuilder(int index, Class<T> type) {
      if (index >= 256) {
        throw new IllegalArgumentException("Index larger than unsigned byte, must be below 256");
      }
      this.index = (byte) (index & 0xFF);
      this.type = type;
    }

    public CodecBuilder<T> emptyEncoder() {
      return this.encoder((x, y) -> {
      });
    }

    public CodecBuilder<T> encoder(BiConsumer<T, PacketBuffer> encoder) {
      this.encoder = encoder;
      return this;
    }

    public CodecBuilder<T> unitDecoder(Supplier<T> value) {
      return this.decoder(in -> value.get());
    }

    public CodecBuilder<T> decoder(Function<PacketBuffer, T> decoder) {
      this.decoder = decoder;
      return this;
    }

    public SimpleNetworkProtocol register() {
      Codec<T> entry =
          new Codec<>(this.index, this.encoder, this.decoder);

      if (SimpleNetworkProtocol.this.codecIndicies.containsKey(this.index)) {
        throw new IllegalArgumentException(
            "Codec with index " + this.index + " already registered");
      }
      SimpleNetworkProtocol.this.codecIndicies.put(this.index, entry);

      if (SimpleNetworkProtocol.this.codecTypes.containsKey(this.type)) {
        throw new IllegalArgumentException(
            "Codec with type " + this.type.getName() + " already registered");
      }
      SimpleNetworkProtocol.this.codecTypes.put(this.type, entry);

      return SimpleNetworkProtocol.this;
    }
  }
}
