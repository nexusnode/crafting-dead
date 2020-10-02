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

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import com.craftingdead.core.capability.living.Player;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractGameServer<T extends ITeam, P extends Player<? extends ServerPlayerEntity>>
    extends AbstractGame<T, P, ServerPlayerEntity> implements IGameServer<T, P> {

  public AbstractGameServer(Supplier<GameType> gameType, Set<T> teams) {
    super(gameType, teams);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    this.teams.forEach((k, v) -> nbt.put(k.toString(), v.serializeNBT()));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.teams.forEach((k, v) -> {
      CompoundNBT teamNbt = nbt.getCompound(k.toString());
      if (!teamNbt.isEmpty()) {
        v.deserializeNBT(teamNbt);
      }
    });
  }

  @Override
  public Optional<T> getDefaultTeam() {
    return Optional.empty();
  }
}
