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
    this.serverList.loadServerList();
    for (int i = 0; i < this.serverList.countServers(); i++) {
      ServerData serverData = this.serverList.getServerData(i);
      ServerAddress address = ServerAddress.fromString(serverData.serverIP);
      entryConsumer.accept(new ServerEntry(null, address.getIP(), address.getPort()));
    }
  }

  @Override
  public void write(ServerEntry entry) {
    ServerList.saveSingleServer(new ServerData(createId(entry),
        entry.getHostName() + ":" + entry.getPort(), false));
    this.serverList.loadServerList();
  }

  @Override
  public void delete(ServerEntry entry) {
    String id = createId(entry);
    for (int i = 0; i < this.serverList.countServers(); i++) {
      ServerData serverData = this.serverList.getServerData(i);
      if (serverData.serverName.equals(id)) {
        this.serverList.func_217506_a(serverData);
        this.serverList.saveServerList();
        return;
      }
    }
  }

  private static String createId(ServerEntry entry) {
    return entry.getHostName() + entry.getPort();
  }
}
