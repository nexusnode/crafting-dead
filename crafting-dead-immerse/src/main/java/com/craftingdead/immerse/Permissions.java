package com.craftingdead.immerse;

import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class Permissions {

  public static final String GAME_OP = "craftingdeadimmerse.game.op";

  public static void register() {
    PermissionAPI.registerNode(GAME_OP, DefaultPermissionLevel.OP,
        "Enables a player to manage games");
  }
}
