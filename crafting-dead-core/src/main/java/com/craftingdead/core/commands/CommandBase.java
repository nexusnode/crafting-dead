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
