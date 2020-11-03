/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public abstract class CommandBase {

  private final String command;
  private final String[] suggestions;
  private final int permissionLevel;

  public CommandBase(String command) {
    this(command, null, 0);
  }

  public CommandBase(String command, String[] suggestions) {
    this(command, suggestions, 0);
  }

  public CommandBase(String command, String[] suggestions, int permissionLevel) {
    assert command != null;
    this.command = command.toLowerCase();
    this.suggestions = (suggestions == null ? new String[0] : suggestions);
    this.permissionLevel = (permissionLevel < 0 ? 0 : permissionLevel);
  }

  public String getName() {
    return this.command;
  }

  public boolean hasSuggestions() {
    return this.suggestions != null;
  }

  public String[] getSuggestions() {
    return this.suggestions;
  }

  public boolean hasPermissions() {
    return this.permissionLevel > 0;
  }

  public int getPermissions() {
    return this.permissionLevel;
  }

  public abstract void register(CommandDispatcher<CommandSource> dispatcher);
}
