package com.craftingdead.core.game;

import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITeam extends INBTSerializable<CompoundNBT> {

  default ITextComponent getName() {
    return new TranslationTextComponent(Util.makeTranslationKey("team", this.getId()));
  }

  ResourceLocation getId();

  default Optional<Pair<DimensionType, BlockPos>> findSpawn() {
    return Optional.empty();
  }

  default GameType getGameType() {
    return GameType.SURVIVAL;
  }

  @Override
  default CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  default void deserializeNBT(CompoundNBT nbt) {}
}
