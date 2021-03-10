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
