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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.server.level.ServerPlayer;

public class SurvivalServer extends SurvivalGame implements GameServer {

  @Override
  public boolean persistPlayerData() {
    return true;
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean persistGameData() {
    return true;
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayer> player) {}

  @Override
  public void removePlayer(PlayerExtension<ServerPlayer> player) {}
}
