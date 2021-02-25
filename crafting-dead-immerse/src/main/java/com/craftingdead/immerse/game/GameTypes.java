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

package com.craftingdead.immerse.game;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.deathmatch.DeathmatchServer;
import com.craftingdead.immerse.game.deathmatch.client.DeathmatchClient;
import com.craftingdead.immerse.game.survival.SurvivalClient;
import com.craftingdead.immerse.game.survival.SurvivalServer;
import com.mojang.serialization.Codec;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GameTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<GameType> GAME_TYPES =
      DeferredRegister.create((Class<GameType>) (Class<?>) GameType.class, CraftingDeadImmerse.ID);

  public static final RegistryObject<GameType> SURVIVAL = GAME_TYPES.register("survival",
      () -> new GameType(Codec.unit(SurvivalServer::new), () -> SurvivalClient::new));

  public static final RegistryObject<GameType> DEATHMATCH = GAME_TYPES.register("deathmatch",
      () -> new GameType(DeathmatchServer.CODEC, () -> DeathmatchClient::new));
}
