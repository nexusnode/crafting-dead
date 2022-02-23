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
