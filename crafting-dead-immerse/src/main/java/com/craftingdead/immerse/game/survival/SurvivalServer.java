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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.entity.player.ServerPlayerEntity;

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
  public boolean save() {
    return true;
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayerEntity> player) {}

  @Override
  public void removePlayer(PlayerExtension<ServerPlayerEntity> player) {}
}
