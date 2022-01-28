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
