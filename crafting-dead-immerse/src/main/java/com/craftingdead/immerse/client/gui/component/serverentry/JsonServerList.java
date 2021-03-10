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

package com.craftingdead.immerse.client.gui.component.serverentry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonServerList implements IServerEntryReader {

  private static final Gson gson = new Gson();

  private final Path serverListFile;

  public JsonServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public void read(Consumer<ServerEntry> entryConsumer) {
    try (BufferedReader reader = Files.newBufferedReader(this.serverListFile)) {
      JsonArray serverListJson = gson.fromJson(reader, JsonArray.class);
      for (JsonElement jsonElement : serverListJson) {
        JsonObject serverJson = jsonElement.getAsJsonObject();
        entryConsumer.accept(new ServerEntry(serverJson.get("map").getAsString(),
            serverJson.get("hostName").getAsString(), serverJson.get("port").getAsInt()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
