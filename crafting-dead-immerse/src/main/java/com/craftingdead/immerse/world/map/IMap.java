package com.craftingdead.immerse.world.map;

import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface IMap {
  /**
   * Gets a {@link BlockState} at a given location within the map. Requesting a block state outside
   * of those bounds returns the default block state for air.
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
   * Returns a list of all tile entities in the map.
   *
   * @return all tile entities
   */
  Set<TileEntity> getTileEntities();

  /**
   * Returns a list of all entities in the map.
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
   * Returns a list of all biomes in the map.
   * 
   * @return the biomes
   */
  Set<Biome> getBiomes();

  /**
   * Get the width of the map.
   *
   * @return the width
   */
  int getWidth();

  /**
   * Get the height of the map.
   *
   * @return the height
   */
  int getHeight();

  /**
   * Get the length of the map.
   *
   * @return the length
   */
  int getLength();

  /**
   * Gets the author of the map, or an empty {@link String} if unknown.
   *
   * @return the author
   */
  @Nonnull
  String getAuthor();
}
