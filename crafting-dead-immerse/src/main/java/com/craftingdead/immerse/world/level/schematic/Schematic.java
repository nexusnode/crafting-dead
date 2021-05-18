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

package com.craftingdead.immerse.world.level.schematic;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface Schematic {
  /**
   * Gets a {@link BlockState} at a given location within the schematic. Requesting a block state
   * outside of those bounds returns the default block state for air.
   *
   * @param pos - {@link BlockPos} of the {@link BlockState}
   * @return the {@link BlockState}
   */

  BlockState getBlockState(BlockPos pos);

  /**
   * Gets the {@link TileEntity} at the specified location. If no tile entity exists at that
   * location, null will be returned.
   *
   * @param pos - {@link BlockPos} of the {@link TilEntity}
   * @return the {@link TileEntity}
   */
  TileEntity getTileEntity(BlockPos pos);

  /**
   * Returns a list of all tile entities in the schematic.
   *
   * @return all tile entities
   */
  Set<TileEntity> getTileEntities();

  /**
   * Returns a list of all entities in the schematic.
   *
   * @return all entities
   */
  Set<Entity> getEntities();

  /**
   * Returns the {@link Biome} at the specified block position.
   * 
   * @param x - x coordinate
   * @param y - y coordinate
   * @param z - z coordinate
   * @return the {@link Biome}
   */
  Biome getBiome(int x, int y, int z);

  /**
   * Returns a list of all biomes in the schematic.
   * 
   * @return the biomes
   */
  List<Biome> getBiomes();

  /**
   * Get the width of the schematic.
   *
   * @return the width
   */
  int getWidth();

  /**
   * Get the height of the schematic.
   *
   * @return the height
   */
  int getHeight();

  /**
   * Get the length of the schematic.
   *
   * @return the length
   */
  int getLength();

  /**
   * Gets the author of the schematic, or an empty {@link String} if unknown.
   *
   * @return the author
   */
  @Nonnull
  String getAuthor();
}
