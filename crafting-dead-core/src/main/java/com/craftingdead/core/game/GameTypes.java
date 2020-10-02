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
package com.craftingdead.core.game;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.game.survival.SurvivalClient;
import com.craftingdead.core.game.survival.SurvivalGame;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GameTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<GameType> GAME_TYPES =
      DeferredRegister.create((Class<GameType>) (Class<?>) GameType.class, CraftingDead.ID);

  public static final RegistryObject<GameType> SURVIVAL = GAME_TYPES
      .register("vanilla",
          () -> new GameType(logicalServer -> new SurvivalGame(), SurvivalClient::new));
}
