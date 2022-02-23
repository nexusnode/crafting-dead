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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

public class NbtServerList implements MutableServerList {

  private final Path serverListFile;

  public NbtServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public CompletableFuture<Stream<ServerEntry>> load() {
    return this.read()
        .thenApply(tag -> tag.getList("servers", Tag.TAG_COMPOUND).stream()
            .filter(CompoundTag.class::isInstance)
            .map(CompoundTag.class::cast)
            .map(serverTag -> new ServerEntry(
                serverTag.contains("map") ? serverTag.getString("map") : null,
                serverTag.getString("host"), serverTag.getInt("port"))));
  }

  @Override
  public CompletableFuture<Void> save(Stream<ServerEntry> servers) {
    CompoundTag tag = new CompoundTag();
    tag.put("servers", servers
        .map(server -> {
          CompoundTag serverTag = new CompoundTag();
          server.getMap().ifPresent(map -> serverTag.putString("map", map));
          serverTag.putString("host", server.getHostName());
          serverTag.putInt("port", server.getPort());
          return serverTag;
        })
        .collect(Collectors.toCollection(ListTag::new)));
    return this.save(tag);
  }

  private CompletableFuture<CompoundTag> read() {
    CompletableFuture<CompoundTag> future = new CompletableFuture<>();
    Util.backgroundExecutor().execute(() -> {
      try {
        CompoundTag tag = NbtIo.read(this.serverListFile.toFile());
        future.complete(tag == null ? new CompoundTag() : tag);
      } catch (IOException e) {
        future.completeExceptionally(e);
      }
    });
    return future;
  }

  private CompletableFuture<Void> save(CompoundTag tag) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    Util.backgroundExecutor().execute(() -> {
      try {
        File tempFile =
            File.createTempFile("servers", ".dat", this.serverListFile.getParent().toFile());
        NbtIo.write(tag, tempFile);
        File oldFile = new File(this.serverListFile.getParent().toString(), "servers.dat_old");
        Util.safeReplaceFile(this.serverListFile.toFile(), tempFile, oldFile);
        future.complete(null);
      } catch (IOException e) {
        future.completeExceptionally(e);
      }
    });
    return future;
  }
}
