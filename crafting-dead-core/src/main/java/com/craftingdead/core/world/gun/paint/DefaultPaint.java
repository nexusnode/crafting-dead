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

package com.craftingdead.core.world.gun.paint;

import java.util.Optional;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class DefaultPaint implements Paint {

  private final Optional<ResourceLocation> skin;
  private Optional<Integer> colour;

  public DefaultPaint() {
    this(null, null);
  }

  public DefaultPaint(ResourceLocation skin, Integer colour) {
    this.skin = Optional.ofNullable(skin);
    this.colour = Optional.ofNullable(colour);
  }

  @Override
  public Optional<ResourceLocation> getSkin() {
    return this.skin;
  }

  @Override
  public Optional<Integer> getColour() {
    return this.colour;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    this.colour.ifPresent(c -> nbt.putInt("colour", c));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.colour = nbt.contains("colour", Constants.NBT.TAG_INT) ? Optional.of(nbt.getInt("colour"))
        : Optional.empty();
  }
}
