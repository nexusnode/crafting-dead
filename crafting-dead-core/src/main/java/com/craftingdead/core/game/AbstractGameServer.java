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
  public Optional<T> getForcedTeam() {
    return Optional.empty();
  }
}
