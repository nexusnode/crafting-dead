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

package com.craftingdead.immerse.game.team;

import java.util.Optional;
import com.craftingdead.core.network.util.NetworkDataManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

public interface ITeam {

  void registerDataParameters(NetworkDataManager dataManager);

  void save(TeamInstance<?> teamInstance, CompoundNBT nbt);

  void load(TeamInstance<?> teamInstance, CompoundNBT nbt);

  int getColour();

  default Optional<ResourceLocation> getSkin() {
    return Optional.empty();
  }

  default Style getColourStyle() {
    return Style.EMPTY.setColor(Color.fromInt(this.getColour()));
  }

  String getName();

  default ITextComponent getDisplayName() {
    return new StringTextComponent(this.getName())
        .mergeStyle(Style.EMPTY.setColor(Color.fromInt(this.getColour())));
  }
}
