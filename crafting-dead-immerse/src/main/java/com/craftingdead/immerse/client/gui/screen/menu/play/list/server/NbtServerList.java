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
