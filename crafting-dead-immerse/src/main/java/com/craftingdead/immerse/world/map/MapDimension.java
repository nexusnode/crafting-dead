package com.craftingdead.immerse.world.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.craftingdead.immerse.world.IDimensionExtension;
import com.craftingdead.immerse.world.SchematicChunkGenerator;
import com.craftingdead.immerse.world.map.spongeschematic.SpongeSchematicMap;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.ChunkGenerator;

public class MapDimension extends OverworldDimension implements IDimensionExtension {

  public MapDimension(World world, DimensionType dimensionType) {
    super(world, dimensionType);
  }

  @Override
  public ChunkGenerator<?> createChunkGenerator() {
    // TODO test
    try (FileInputStream inputStream = new FileInputStream(new File("dust_ii.schem"))) {
      SpongeSchematicMap schematic = (SpongeSchematicMap) MapFormats.SPONGE_SCHEMATIC.get().read(inputStream);
      return new SchematicChunkGenerator(this.world,
          new SchematicChunkGenerator.SchematicGenerationSettings(schematic));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int getSeaLevel() {
    return 0;
  }

  @Override
  public double getHorizonHeight() {
    return 0.0D;
  }

  @Override
  public double getVoidFogYFactor() {
    return 1.0D;
  }
}
