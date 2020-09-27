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
