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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SimpleNetworkProtocol implements NetworkProtocol {

  private final Map<Byte, ObjectEntry<?>> objectIndicies = new Byte2ObjectArrayMap<>();
  private final Map<Class<?>, ObjectEntry<?>> objectTypes = new Object2ObjectArrayMap<>();

  public <T> ObjectEntryBuilder<T> messageBuilder(int index, Class<T> type) {
    return new ObjectEntryBuilder<>(index, type);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> PacketBuffer encode(T object) throws IOException {
    ObjectEntry<T> entry = (ObjectEntry<T>) this.objectTypes.get(object.getClass());
    if (entry == null) {
      throw new IOException("Unknown message type " + object.getClass().getName());
    }
    PacketBuffer out = new PacketBuffer(Unpooled.buffer());
    out.writeByte(entry.index);
    entry.encoder.accept(object, out);
    return out;
  }

  @Override
  public <T> void process(PacketBuffer buf, NetworkEvent.Context ctx) throws IOException {
    byte index = buf.readByte();
    @SuppressWarnings("unchecked")
    ObjectEntry<T> entry = (ObjectEntry<T>) this.objectIndicies.get(index);
    if (entry != null) {
      entry.handler.accept(entry.decoder.apply(buf), ctx);
    } else {
      throw new IOException("Unknown message index " + index);
    }
  }

  private class ObjectEntry<T> {

    private final byte index;
    private final BiConsumer<T, PacketBuffer> encoder;
    private final Function<PacketBuffer, T> decoder;
    private final BiConsumer<T, NetworkEvent.Context> handler;

    public ObjectEntry(byte index, BiConsumer<T, PacketBuffer> encoder,
        Function<PacketBuffer, T> decoder, BiConsumer<T, NetworkEvent.Context> handler) {
      this.index = index;
      this.encoder = encoder;
      this.decoder = decoder;
      this.handler = handler;
    }
  }

  public class ObjectEntryBuilder<T> {

    private final byte index;
    private final Class<T> type;
    private BiConsumer<T, PacketBuffer> encoder;
    private Function<PacketBuffer, T> decoder;
    private BiConsumer<T, NetworkEvent.Context> handler;

    private ObjectEntryBuilder(int index, Class<T> type) {
      if (index >= 256) {
        throw new IllegalArgumentException("Index larger than unsigned byte, must be below 256");
      }
      this.index = (byte) (index & 0xFF);
      this.type = type;
    }

    public ObjectEntryBuilder<T> encoder(BiConsumer<T, PacketBuffer> encoder) {
      this.encoder = encoder;
      return this;
    }

    public ObjectEntryBuilder<T> decoder(Function<PacketBuffer, T> decoder) {
      this.decoder = decoder;
      return this;
    }

    public ObjectEntryBuilder<T> handler(BiConsumer<T, NetworkEvent.Context> handler) {
      this.handler = handler;
      return this;
    }

    public SimpleNetworkProtocol register() {
      ObjectEntry<T> entry =
          new ObjectEntry<>(this.index, this.encoder, this.decoder, this.handler);

      if (SimpleNetworkProtocol.this.objectIndicies.containsKey(this.index)) {
        throw new IllegalArgumentException(
            "Object with index " + this.index + " already registered");
      }
      SimpleNetworkProtocol.this.objectIndicies.put(this.index, entry);

      if (SimpleNetworkProtocol.this.objectTypes.containsKey(this.type)) {
        throw new IllegalArgumentException(
            "Object of type " + this.type.getName() + " already registered");
      }
      SimpleNetworkProtocol.this.objectTypes.put(this.type, entry);

      if (this.handler == null) {
        throw new IllegalArgumentException("No handler specified");
      }

      return SimpleNetworkProtocol.this;
    }
  }

  public static <T> BiConsumer<T, ByteBuf> emptyEncoder() {
    return (x, y) -> {
    };
  }

  public static <T> Function<ByteBuf, T> supplierDecoder(Supplier<T> value) {
    return in -> value.get();
  }
}
