/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game.network;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SimpleNetworkProtocol implements NetworkProtocol {

  private final Map<Byte, Codec<?>> codecIndicies = new Byte2ObjectArrayMap<>();
  private final Map<Class<?>, Codec<?>> codecTypes = new Object2ObjectArrayMap<>();

  public <T> CodecBuilder<T> codecBuilder(int index, Class<T> type) {
    return new CodecBuilder<>(index, type);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void encode(FriendlyByteBuf buf, T payload) throws IOException {
    Codec<T> entry = (Codec<T>) this.codecTypes.get(payload.getClass());
    if (entry == null) {
      throw new IOException("Unknown payload type " + payload.getClass().getName());
    }
    buf.writeByte(entry.index);
    entry.encoder.accept(payload, buf);
  }

  @Override
  public <T> T decode(FriendlyByteBuf buf, NetworkEvent.Context ctx) throws IOException {
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
    private final BiConsumer<T, FriendlyByteBuf> encoder;
    private final Function<FriendlyByteBuf, T> decoder;

    private Codec(byte index, BiConsumer<T, FriendlyByteBuf> encoder,
        Function<FriendlyByteBuf, T> decoder) {
      this.index = index;
      this.encoder = encoder;
      this.decoder = decoder;
    }
  }

  public class CodecBuilder<T> {

    private final byte index;
    private final Class<T> type;
    private BiConsumer<T, FriendlyByteBuf> encoder;
    private Function<FriendlyByteBuf, T> decoder;

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

    public CodecBuilder<T> encoder(BiConsumer<T, FriendlyByteBuf> encoder) {
      this.encoder = encoder;
      return this;
    }

    public CodecBuilder<T> unitDecoder(Supplier<T> value) {
      return this.decoder(in -> value.get());
    }

    public CodecBuilder<T> decoder(Function<FriendlyByteBuf, T> decoder) {
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
