package com.craftingdead.immerse.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class Commands {

  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    GameCommand.register(dispatcher);
  }
}
