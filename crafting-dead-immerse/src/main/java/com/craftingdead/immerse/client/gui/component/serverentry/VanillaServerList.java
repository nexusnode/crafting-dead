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

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public class VanillaServerList implements IServerEntryReader, IServerEntryWriter {

  private final ServerList serverList = new ServerList(Minecraft.getInstance());

  @Override
  public void read(Consumer<ServerEntry> entryConsumer) {
    this.serverList.load();
    for (int i = 0; i < this.serverList.size(); i++) {
      ServerData serverData = this.serverList.get(i);
      ServerAddress address = ServerAddress.parseString(serverData.ip);
      entryConsumer.accept(new ServerEntry(null, address.getHost(), address.getPort()));
    }
  }

  @Override
  public void write(ServerEntry entry) {
    ServerList.saveSingleServer(new ServerData(createId(entry),
        entry.getHostName() + ":" + entry.getPort(), false));
    this.serverList.load();
  }

  @Override
  public void delete(ServerEntry entry) {
    String id = createId(entry);
    for (int i = 0; i < this.serverList.size(); i++) {
      ServerData serverData = this.serverList.get(i);
      if (serverData.name.equals(id)) {
        this.serverList.remove(serverData);
        this.serverList.save();
        return;
      }
    }
  }

  private static String createId(ServerEntry entry) {
    return entry.getHostName() + entry.getPort();
  }
}
