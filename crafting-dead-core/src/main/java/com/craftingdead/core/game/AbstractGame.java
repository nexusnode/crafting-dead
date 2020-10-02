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

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.craftingdead.core.capability.living.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractGame<T extends ITeam, P extends Player<? extends E>, E extends PlayerEntity>
    implements IGame<T, P, E> {

  private final Supplier<GameType> gameType;
  protected final Map<ResourceLocation, T> teams;
  private final Map<P, T> playerTeams;

  public AbstractGame(Supplier<GameType> gameType, Set<T> teams) {
    this.gameType = gameType;
    this.teams = Object2ObjectMaps.unmodifiable(
        teams.stream().collect(Collectors.toMap(ITeam::getId, Function.identity(), (u, v) -> {
          throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, Object2ObjectArrayMap::new)));
    this.playerTeams = new Object2ObjectOpenHashMap<>();
  }

  @Override
  public T getTeam(P player) {
    return this.playerTeams.get(player);
  }

  @Override
  public void setTeam(P player, T team) {
    this.playerTeams.put(player, team);
  }

  @Override
  public GameType getGameType() {
    return this.gameType.get();
  }
}
