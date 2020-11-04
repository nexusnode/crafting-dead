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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.commands.impl.CommandThirst;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class CommandManager {

  private static final HashMap<String, Class> COMMAND_MAP = new HashMap<>();
  private static final Logger LOGGER = CraftingDead.getInstance().getLogger();

  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    COMMAND_MAP.keySet().stream().forEach(command -> {
      Class source = COMMAND_MAP.get(command);
      try {
        Method register = source.getDeclaredMethod("register", new Class[]{dispatcher.getClass()});
        if (!register.isAccessible()) {
          register.setAccessible(true);
        }
        register.invoke(null, dispatcher);
        LOGGER.info("Command registered: " + command);
      } catch (Exception OK) {
        LOGGER.error("Could not register command: " + command);
      }
    });
  }

  private static void registerCommand(String command, Class source) {
    if (!COMMAND_MAP.containsKey(command) && !COMMAND_MAP.containsValue(source)) {
      COMMAND_MAP.put(command, source);
    }
  }

  static {
    registerCommand("thirst", CommandThirst.class);
  }
}
