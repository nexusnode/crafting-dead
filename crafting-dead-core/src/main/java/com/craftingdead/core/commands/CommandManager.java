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

import com.craftingdead.core.commands.impl.CommandThirst;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

import java.util.HashMap;

/**
 * This class is responsable to manage the registry and storage of custom-commands and it's instance.
 */
public class CommandManager {

  private static final HashMap<String, CommandBase> command_map = new HashMap<>();

  public CommandManager(CommandDispatcher<CommandSource> dispatcher) {
    register(dispatcher);
  }

  private void register(CommandDispatcher<CommandSource> dispatcher) {
    command_map.values().stream().forEach(commandBase -> {
        commandBase.register(dispatcher);
    });
  }

  public static void registerCommand(CommandBase command) {
    if (!command_map.containsKey(command.getName()) && !command_map.containsValue(command)) {
      command_map.put(command.getName(), command);
    }
  }

  static {
    registerCommand(new CommandThirst());
  }
}
