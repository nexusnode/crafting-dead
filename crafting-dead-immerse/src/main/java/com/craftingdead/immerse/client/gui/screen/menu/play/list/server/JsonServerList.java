/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class JsonServerList implements ServerList {

  private static final Gson gson = new Gson();

  private final Path serverListFile;

  public JsonServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public Flux<ServerEntry> load() {
    return Mono.fromCallable(() -> Files.readString(this.serverListFile))
        .flatMapIterable(jsonString -> gson.fromJson(jsonString, JsonArray.class))
        .map(JsonElement::getAsJsonObject)
        .map(json -> new ServerEntry(json.get("map").getAsString(),
            json.get("hostName").getAsString(), json.get("port").getAsInt()))
        .subscribeOn(Schedulers.boundedElastic());
  }
}
