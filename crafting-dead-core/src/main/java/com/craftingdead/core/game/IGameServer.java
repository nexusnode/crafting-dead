package com.craftingdead.core.game;

import java.util.Optional;
import com.craftingdead.core.capability.living.Player;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGameServer<T extends ITeam, P extends Player<? extends ServerPlayerEntity>>
    extends IGame<T, P, ServerPlayerEntity>, INBTSerializable<CompoundNBT> {

  Optional<T> getForcedTeam();
}
