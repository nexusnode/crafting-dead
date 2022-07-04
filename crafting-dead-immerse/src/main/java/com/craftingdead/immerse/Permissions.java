/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse;

import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionNode.PermissionResolver;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

public class Permissions {

  private static final PermissionResolver<Boolean> FALSE_RESOLVER =
      (player, playerId, context) -> Boolean.FALSE;

  public static final PermissionNode<Boolean> GAME_OP =
      new PermissionNode<Boolean>(CraftingDeadImmerse.ID, "game.op",
          PermissionTypes.BOOLEAN, FALSE_RESOLVER);

  public static final PermissionNode<Boolean> HYDRATE =
      new PermissionNode<Boolean>(CraftingDeadImmerse.ID, "hydrate",
          PermissionTypes.BOOLEAN, FALSE_RESOLVER);

  public static final PermissionNode<Boolean> BASE_DESTROY =
      new PermissionNode<Boolean>(CraftingDeadImmerse.ID, "base.destroy",
          PermissionTypes.BOOLEAN, FALSE_RESOLVER);
}
