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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Util;
import net.minecraftforge.common.util.Constants;

public class NbtServerList implements MutableServerList {

  private final Path serverListFile;

  public NbtServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public CompletableFuture<Stream<ServerEntry>> load() {
    return this.read()
        .thenApply(tag -> tag.getList("servers", Constants.NBT.TAG_COMPOUND).stream()
            .filter(CompoundNBT.class::isInstance)
            .map(CompoundNBT.class::cast)
            .map(serverTag -> new ServerEntry(
                serverTag.contains("map") ? serverTag.getString("map") : null,
                serverTag.getString("host"), serverTag.getInt("port"))));
  }

  @Override
  public CompletableFuture<Void> save(Stream<ServerEntry> servers) {
    CompoundNBT tag = new CompoundNBT();
    tag.put("servers", servers
        .map(server -> {
          CompoundNBT serverTag = new CompoundNBT();
          server.getMap().ifPresent(map -> serverTag.putString("map", map));
          serverTag.putString("host", server.getHostName());
          serverTag.putInt("port", server.getPort());
          return serverTag;
        })
        .collect(Collectors.toCollection(ListNBT::new)));
    return this.save(tag);
  }

  private CompletableFuture<CompoundNBT> read() {
    CompletableFuture<CompoundNBT> future = new CompletableFuture<>();
    Util.backgroundExecutor().execute(() -> {
      try {
        CompoundNBT tag = CompressedStreamTools.read(this.serverListFile.toFile());
        future.complete(tag == null ? new CompoundNBT() : tag);
      } catch (IOException e) {
        future.completeExceptionally(e);
      }
    });
    return future;
  }

  private CompletableFuture<Void> save(CompoundNBT tag) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    Util.backgroundExecutor().execute(() -> {
      try {
        File tempFile =
            File.createTempFile("servers", ".dat", this.serverListFile.getParent().toFile());
        CompressedStreamTools.write(tag, tempFile);
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
