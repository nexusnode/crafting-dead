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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.Util;

public class NBTMutableServerList implements MutableServerProvider {

  private static final Logger logger = LogManager.getLogger();

  private static final Executor executor = Executors.newFixedThreadPool(5,
      new ThreadFactoryBuilder()
          .setNameFormat("NBT Server List #%d")
          .setDaemon(true)
          .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger))
          .build());

  private final Path serverListFile;

  public NBTMutableServerList(Path serverListFile) {
    this.serverListFile = serverListFile;
  }

  @Override
  public void read(Consumer<ServerEntry> entryConsumer) {
    executor.execute(() -> {
      CompoundNBT compoundNbt;
      try {
        compoundNbt = CompressedStreamTools.read(this.serverListFile.toFile());
      } catch (IOException e) {
        logger.warn("Failed to read server list", e);
        return;
      }

      if (compoundNbt == null) {
        return;
      }

      ListNBT serversNbt = compoundNbt.getList("servers", 10);
      for (int i = 0; i < serversNbt.size(); ++i) {
        CompoundNBT serverEntry = serversNbt.getCompound(i);
        entryConsumer.accept(
            new ServerEntry(serverEntry.contains("map") ? serverEntry.getString("map") : null,
                serverEntry.getString("host"), serverEntry.getInt("port")));
      }
    });
  }

  @Override
  public void write(ServerEntry entry) {
    executor.execute(() -> {
      try {
        CompoundNBT compoundNbt = CompressedStreamTools.read(this.serverListFile.toFile());

        ListNBT serversNbt;
        if (compoundNbt == null) {
          compoundNbt = new CompoundNBT();
          compoundNbt.put("servers", serversNbt = new ListNBT());
        } else {
          serversNbt = compoundNbt.getList("servers", 10);
          for (int i = 0; i < serversNbt.size(); ++i) {
            CompoundNBT serverEntry = serversNbt.getCompound(i);
            if (serverEntry.getString("host").equals(entry.getHostName())
                && serverEntry.getInt("port") == entry.getPort()) {
              return;
            }
          }
        }

        CompoundNBT serverEntry = new CompoundNBT();
        entry.getMap().ifPresent(map -> serverEntry.putString("map", map));
        serverEntry.putString("host", entry.getHostName());
        serverEntry.putInt("port", entry.getPort());

        serversNbt.add(serverEntry);

        this.save(compoundNbt);
      } catch (IOException e) {
        logger.warn("Failed to write server entry", e);
      }
    });
  }

  @Override
  public void delete(ServerEntry entry) {
    executor.execute(() -> {
      try {
        CompoundNBT compoundNbt = CompressedStreamTools.read(this.serverListFile.toFile());
        ListNBT serversNbt = compoundNbt.getList("servers", 10);

        for (int i = 0; i < serversNbt.size(); ++i) {
          CompoundNBT serverEntry = serversNbt.getCompound(i);
          if (serverEntry.getString("host").equals(entry.getHostName())
              && serverEntry.getInt("port") == entry.getPort()) {
            serversNbt.remove(i);
            break;
          }
        }

        this.save(compoundNbt);
      } catch (IOException e) {
        logger.warn("Failed to delete server entry", e);
      }
    });
  }

  private void save(CompoundNBT compoundNbt) throws IOException {
    File tempFile =
        File.createTempFile("servers", ".dat", this.serverListFile.getParent().toFile());
    CompressedStreamTools.write(compoundNbt, tempFile);
    File oldFile = new File(this.serverListFile.getParent().toString(), "servers.dat_old");
    Util.safeReplaceFile(this.serverListFile.toFile(), tempFile, oldFile);
  }
}
