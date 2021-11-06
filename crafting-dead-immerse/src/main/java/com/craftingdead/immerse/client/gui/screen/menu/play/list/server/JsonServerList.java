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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import com.google.common.collect.Streams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonServerList implements ServerList {

  private static final Gson gson = new Gson();

  private final Path serverListFile;

  public JsonServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public CompletableFuture<Stream<ServerEntry>> load() {
    CompletableFuture<Stream<ServerEntry>> future = new CompletableFuture<>();

    AsynchronousFileChannel fileChannel;
    try {
      fileChannel = AsynchronousFileChannel.open(
          this.serverListFile, StandardOpenOption.READ);
    } catch (IOException e) {
      future.completeExceptionally(e);
      return future;
    }

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Void>() {

      @Override
      public void completed(Integer result, Void attachment) {
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        String jsonString = new String(bytes, StandardCharsets.UTF_8).trim();
        future.complete(Streams.stream(gson.fromJson(jsonString, JsonArray.class))
            .map(JsonElement::getAsJsonObject)
            .map(json -> new ServerEntry(json.get("map").getAsString(),
                json.get("hostName").getAsString(), json.get("port").getAsInt())));
      }

      @Override
      public void failed(Throwable exc, Void attachment) {
        future.completeExceptionally(exc);
      }
    });

    return future;
  }
}
