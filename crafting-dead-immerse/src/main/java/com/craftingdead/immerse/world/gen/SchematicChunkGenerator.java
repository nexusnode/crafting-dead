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

package com.craftingdead.immerse.world.gen;

import com.craftingdead.immerse.world.SchematicBlockReader;
import com.craftingdead.immerse.world.schematic.ISchematic;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

public class SchematicChunkGenerator extends ChunkGenerator {

  private final ISchematic schematic;

  public SchematicChunkGenerator(ISchematic schematic,
      DimensionStructuresSettings dimensionStructureSettings) {
    super(new SchematicBiomeProvider(schematic), dimensionStructureSettings);
    this.schematic = schematic;
  }

  @Override
  public void generateSurface(WorldGenRegion worldGenRegion, IChunk chunk) {}

  @Override
  public void func_230350_a_(long seed, BiomeManager biomeManagerIn, IChunk chunkIn,
      GenerationStage.Carving carvingStage) {}

  @Override
  public int getGroundHeight() {
    for (int y = 0; y < this.schematic.getHeight(); y++) {
      final BlockState blockState = this.schematic.getBlockState(new BlockPos(0, y, 0));
      if (!Heightmap.Type.MOTION_BLOCKING.getHeightLimitPredicate().test(blockState)) {
        return y - 1;
      }
    }
    return this.schematic.getHeight();
  }

  @Override
  public void func_230352_b_(IWorld world, StructureManager structureManager, IChunk chunk) {
    ChunkPos chunkPos = chunk.getPos();
    BlockPos.Mutable relativeBlockPos = new BlockPos.Mutable();
    BlockPos.Mutable blockPos = new BlockPos.Mutable();

    Heightmap oceanFloorHeightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
    Heightmap worldSurfaceHeightMap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
    for (int x = 0; x < 16; ++x) {
      for (int z = 0; z < 16; ++z) {
        final int worldX = (chunkPos.x << 4) + x;
        final int worldZ = (chunkPos.z << 4) + z;
        if (worldX >= 0 && worldX < this.schematic.getWidth() && worldZ >= 0
            && worldZ < this.schematic.getLength()) {
          for (int y = 0; y < this.schematic.getHeight(); ++y) {
            relativeBlockPos.setPos(x, y, z);
            blockPos.setPos(worldX, y, worldZ);

            BlockState blockState = this.schematic.getBlockState(blockPos);

            chunk.setBlockState(relativeBlockPos, blockState, false);
            oceanFloorHeightmap.update(x, y, z, blockState);
            worldSurfaceHeightMap.update(x, y, z, blockState);

            TileEntity tileEntity = this.schematic.getTileEntity(blockPos);
            if (tileEntity != null) {
              chunk.addTileEntity(relativeBlockPos, tileEntity);
            }
          }
        }
      }
    }
  }

  @Override
  public int getHeight(int x, int z, Heightmap.Type heightmapType) {
    for (int y = 0; y < this.schematic.getHeight(); y++) {
      final BlockState blockState = this.schematic.getBlockState(new BlockPos(x, y, z));
      if (heightmapType.getHeightLimitPredicate().test(blockState)) {
        return y + 1;
      }
    }
    return 0;
  }

  @Override
  protected Codec<? extends ChunkGenerator> func_230347_a_() {
    return Codec.unit(this);
  }

  @Override
  public ChunkGenerator func_230349_a_(long seed) {
    return this;
  }

  @Override
  public IBlockReader func_230348_a_(int x, int z) {
    return new SchematicBlockReader(this.schematic);
  }

  private static class SchematicBiomeProvider extends BiomeProvider {

    private final ISchematic schematic;

    protected SchematicBiomeProvider(ISchematic schematic) {
      super(schematic.getBiomes());
      this.schematic = schematic;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
      return this.schematic.getBiome(x, y, z);
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
      return Codec.unit(this);
    }

    @Override
    public BiomeProvider getBiomeProvider(long seed) {
      return this;
    }
  }
}
