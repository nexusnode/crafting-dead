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
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonServerList implements ServerProvider {

  private static final Logger logger = LogManager.getLogger();


  private static final Gson gson = new Gson();

  private final Path serverListFile;

  public JsonServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public void read(Consumer<ServerEntry> entryConsumer) {
    AsynchronousFileChannel fileChannel;
    try {
      fileChannel = AsynchronousFileChannel.open(
          this.serverListFile, StandardOpenOption.READ);
    } catch (IOException e) {
      logger.warn("Failed to read server list file", e);
      return;
    }

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Void>() {

      @Override
      public void completed(Integer result, Void attachment) {
        buffer.flip();
        JsonArray serverListJson = gson.fromJson(new String(buffer.array()), JsonArray.class);
        for (JsonElement jsonElement : serverListJson) {
          JsonObject serverJson = jsonElement.getAsJsonObject();
          entryConsumer.accept(new ServerEntry(serverJson.get("map").getAsString(),
              serverJson.get("hostName").getAsString(), serverJson.get("port").getAsInt()));
        }
      }

      @Override
      public void failed(Throwable exc, Void attachment) {
        logger.warn("Failed to read server list file", exc);
      }
    });
  }
}
