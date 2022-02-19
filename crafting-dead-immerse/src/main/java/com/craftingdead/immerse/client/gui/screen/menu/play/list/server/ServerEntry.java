/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

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

  public ServerAddress toServerAddress() {
    return new ServerAddress(this.hostName, this.port);
  }
}
