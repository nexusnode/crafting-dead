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
package com.craftingdead.immerse.game;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.network.util.GenericDataManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractGame<T extends ITeam> implements IGame<T> {

  private final Supplier<GameType> gameType;
  protected final Map<ResourceLocation, T> teams;
  private final Map<Player<?>, T> playerTeams;
  protected final GenericDataManager dataManager;

  private final DataParameter<String> displayName;

  public AbstractGame(Supplier<GameType> gameType, Set<T> teams) {
    this(gameType, teams, "Untitled");
  }

  public AbstractGame(Supplier<GameType> gameType, Set<T> teams, String displayName) {
    this.gameType = gameType;
    this.teams = Object2ObjectMaps.unmodifiable(
        teams.stream().collect(Collectors.toMap(ITeam::getId, Function.identity(), (u, v) -> {
          throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, Object2ObjectArrayMap::new)));
    this.playerTeams = new Object2ObjectOpenHashMap<>();
    this.dataManager = new GenericDataManager();

    this.displayName = this.dataManager.register(DataSerializers.STRING, displayName);
  }

  @Override
  public void write(PacketBuffer packetBuffer, boolean writeAll) throws IOException {
    GenericDataManager.writeEntries(
        writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), packetBuffer);
  }

  @Override
  public void read(PacketBuffer packetBuffer) throws IOException {
    this.dataManager.setEntryValues(GenericDataManager.readEntries(packetBuffer));
  }

  @Override
  public boolean isDirty() {
    return this.dataManager.isDirty();
  }

  @Override
  public T getTeam(Player<?> player) {
    return this.playerTeams.get(player);
  }

  @Override
  public void setTeam(Player<?> player, T team) {
    this.playerTeams.put(player, team);
  }

  @Override
  public String getDisplayName() {
    return this.dataManager.get(this.displayName);
  }

  @Override
  public GameType getGameType() {
    return this.gameType.get();
  }
}
