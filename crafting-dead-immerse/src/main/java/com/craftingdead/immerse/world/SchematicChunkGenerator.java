package com.craftingdead.immerse.world;

import com.craftingdead.immerse.world.SchematicChunkGenerator.SchematicGenerationSettings;
import com.craftingdead.immerse.world.map.IMap;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;

public class SchematicChunkGenerator extends ChunkGenerator<SchematicGenerationSettings> {

  public SchematicChunkGenerator(IWorld world, SchematicGenerationSettings generationSettings) {
    super(world, new SchematicBiomeProvider(generationSettings.schematic), generationSettings);
  }

  @Override
  public void generateSurface(WorldGenRegion worldGenRegion, IChunk chunk) {}

  @Override
  public void generateCarvers(BiomeManager biomeManagerIn, IChunk chunkIn,
      GenerationStage.Carving carvingStage) {}

  @Override
  public int getGroundHeight() {
    IChunk chunk = this.world.getChunk(0, 0);
    return chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, 8, 8);
  }

  @Override
  public void makeBase(IWorld world, IChunk chunk) {
    ChunkPos chunkPos = chunk.getPos();
    BlockPos.Mutable relativeBlockPos = new BlockPos.Mutable();
    BlockPos.Mutable blockPos = new BlockPos.Mutable();

    IMap schematic = this.settings.schematic;

    Heightmap oceanFloorHeightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
    Heightmap worldSurfaceHeightMap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
    for (int x = 0; x < 16; ++x) {
      for (int z = 0; z < 16; ++z) {
        final int worldX = (chunkPos.x << 4) + x;
        final int worldZ = (chunkPos.z << 4) + z;
        if (worldX >= 0 && worldX < schematic.getWidth() && worldZ >= 0
            && worldZ < schematic.getLength()) {
          for (int y = 0; y < schematic.getHeight(); ++y) {
            relativeBlockPos.setPos(x, y, z);
            blockPos.setPos(worldX, y, worldZ);

            BlockState blockState = this.settings.schematic.getBlockState(blockPos);

            chunk.setBlockState(relativeBlockPos, blockState, false);
            oceanFloorHeightmap.update(x, y, z, blockState);
            worldSurfaceHeightMap.update(x, y, z, blockState);

            TileEntity tileEntity = this.settings.schematic.getTileEntity(blockPos);
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
    for (int y = 0; y < this.settings.schematic.getHeight(); y++) {
      final BlockState blockState = this.settings.schematic.getBlockState(new BlockPos(x, y, z));
      if (heightmapType.getHeightLimitPredicate().test(blockState)) {
        return y + 1;
      }
    }
    return 0;
  }

  private static class SchematicBiomeProvider extends BiomeProvider {

    private final IMap schematic;

    protected SchematicBiomeProvider(IMap schematic) {
      super(schematic.getBiomes());
      this.schematic = schematic;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
      return this.schematic.getBiome(x, y, z);
    }
  }

  public static class SchematicGenerationSettings extends GenerationSettings {

    private final IMap schematic;

    public SchematicGenerationSettings(IMap schematic) {
      this.schematic = schematic;
    }
  }
}
