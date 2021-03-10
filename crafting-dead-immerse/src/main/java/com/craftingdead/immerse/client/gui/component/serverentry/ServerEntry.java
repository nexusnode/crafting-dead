package com.craftingdead.immerse.client.gui.component.serverentry;

import java.util.Optional;
import javax.annotation.Nullable;

public class ServerEntry {

  @Nullable
  private final String map;
  private final String hostName;
  private final int port;

  public ServerEntry(String map, String hostName, int port) {
    this.map = map;
    this.hostName = hostName;
    this.port = port;
  }

  public Optional<String> getMap() {
    return Optional.ofNullable(this.map);
  }

  public String getHostName() {
    return this.hostName;
  }

  public int getPort() {
    return this.port;
  }
}
