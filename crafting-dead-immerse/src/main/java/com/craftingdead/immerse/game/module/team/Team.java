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

package com.craftingdead.immerse.game.module.team;

import java.util.Optional;
import com.craftingdead.core.network.SynchedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Style;

public interface Team {

  void registerDataParameters(SynchedData dataManager);

  void save(TeamInstance<?> teamInstance, CompoundTag nbt);

  void load(TeamInstance<?> teamInstance, CompoundTag nbt);

  int getColour();

  default Optional<ResourceLocation> getSkin() {
    return Optional.empty();
  }

  default Style getColourStyle() {
    return Style.EMPTY.withColor(TextColor.fromRgb(this.getColour()));
  }

  String getName();

  default Component getDisplayName() {
    return new TextComponent(this.getName())
        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(this.getColour())));
  }
}
