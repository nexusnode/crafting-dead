package com.craftingdead.immerse;

import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

public class Permissions {

  public static final PermissionNode<Boolean> GAME_OP =
      new PermissionNode<Boolean>(CraftingDeadImmerse.ID, "game.op", PermissionTypes.BOOLEAN,
          (player, playerUUID, context) -> false);
}
