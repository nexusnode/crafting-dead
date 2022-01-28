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

package com.craftingdead.immerse.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SpawnPoint {

  public static final Codec<SpawnPoint> CODEC = RecordCodecBuilder
      .create(instance -> instance
          .group(
              Level.RESOURCE_KEY_CODEC.optionalFieldOf("dimension", Level.OVERWORLD)
                  .forGetter(SpawnPoint::getDimension),
              BlockPos.CODEC.fieldOf("blockPos").forGetter(SpawnPoint::getBlockPos))
          .apply(instance, SpawnPoint::new));

  private final ResourceKey<Level> dimension;
  private final BlockPos blockPos;

  public SpawnPoint(ResourceKey<Level> dimension, BlockPos blockPos) {
    this.dimension = dimension;
    this.blockPos = blockPos;
  }

  public ResourceKey<Level> getDimension() {
    return dimension;
  }

  public BlockPos getBlockPos() {
    return blockPos;
  }
}
