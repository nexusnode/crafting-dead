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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class NbtServerList implements MutableServerList {

  private final Path serverListFile;

  public NbtServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public Flux<ServerEntry> load() {
    return this.read()
        .flatMapIterable(tag -> tag.getList("servers", Tag.TAG_COMPOUND))
        .cast(CompoundTag.class)
        .map(serverTag -> new ServerEntry(
            serverTag.contains("map") ? serverTag.getString("map") : null,
            serverTag.getString("host"), serverTag.getInt("port")));
  }

  @Override
  public Mono<Void> save(Flux<ServerEntry> servers) {
    return servers
        .map(server -> {
          var serverTag = new CompoundTag();
          if (server.map() != null) {
            serverTag.putString("map", server.map());
          }
          serverTag.putString("host", server.host());
          serverTag.putInt("port", server.port());
          return serverTag;
        })
        .collect(Collectors.toCollection(ListTag::new))
        .map(serversTag -> {
          var tag = new CompoundTag();
          tag.put("servers", serversTag);
          return tag;
        })
        .flatMap(this::save);
  }

  private Mono<CompoundTag> read() {
    return Mono.<CompoundTag>create(sink -> {
      try (var input = new DataInputStream(Files.newInputStream(this.serverListFile))) {
        sink.success(NbtIo.read(input));
      } catch (IOException e) {
        sink.error(e);
      }
    }).subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<Void> save(CompoundTag tag) {
    return Mono.<Void>create(sink -> {
      try {
        var tempFile = Files.createTempFile(this.serverListFile.getParent(), "servers", ".dat");
        try (var output = new DataOutputStream(Files.newOutputStream(tempFile))) {
          NbtIo.write(tag, output);
          var oldFile = this.serverListFile.resolveSibling("servers.dat_old");
          Util.safeReplaceFile(this.serverListFile, tempFile, oldFile);
          sink.success();
        }
      } catch (IOException e) {
        sink.error(e);
      }
    }).subscribeOn(Schedulers.boundedElastic());
  }
}
