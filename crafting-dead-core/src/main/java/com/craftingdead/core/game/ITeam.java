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
