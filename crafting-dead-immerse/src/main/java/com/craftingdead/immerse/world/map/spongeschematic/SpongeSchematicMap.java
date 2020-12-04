package com.craftingdead.immerse.world.map.spongeschematic;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.immerse.world.map.IMap;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.registries.GameData;

public class SpongeSchematicMap implements IMap {

  private static final Set<Biome> DEFAULT_BIOMES = ImmutableSet.of(Biomes.OCEAN, Biomes.PLAINS,
      Biomes.DESERT, Biomes.MOUNTAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.SWAMP, Biomes.RIVER,
      Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_MOUNTAINS,
      Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.BEACH, Biomes.DESERT_HILLS,
      Biomes.WOODED_HILLS, Biomes.TAIGA_HILLS, Biomes.MOUNTAIN_EDGE, Biomes.JUNGLE,
      Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.DEEP_OCEAN, Biomes.STONE_SHORE,
      Biomes.SNOWY_BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST,
      Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA,
      Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.WOODED_MOUNTAINS, Biomes.SAVANNA,
      Biomes.SAVANNA_PLATEAU, Biomes.BADLANDS, Biomes.WOODED_BADLANDS_PLATEAU,
      Biomes.BADLANDS_PLATEAU, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN,
      Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN,
      Biomes.DEEP_FROZEN_OCEAN, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT_LAKES,
      Biomes.GRAVELLY_MOUNTAINS, Biomes.FLOWER_FOREST, Biomes.TAIGA_MOUNTAINS, Biomes.SWAMP_HILLS,
      Biomes.ICE_SPIKES, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE,
      Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.DARK_FOREST_HILLS,
      Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS,
      Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.SHATTERED_SAVANNA,
      Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.ERODED_BADLANDS,
      Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_BADLANDS_PLATEAU);

  private final int width;
  private final int height;
  private final int length;
  @Nullable
  private final Map<Integer, BlockState> palette;
  private final int[] blockData;
  @Nullable
  private final Map<Integer, Biome> biomePalette;
  private final int[] biomeData;
  private final Map<BlockPos, TileEntity> tileEntities;
  private final Metadata metadata;

  private SpongeSchematicMap(Builder builder) {
    this.width = builder.width;
    this.height = builder.height;
    this.length = builder.length;
    this.palette = builder.palette;
    this.blockData = builder.blockData;
    this.biomePalette = builder.biomePalette;
    this.biomeData = builder.biomeData;
    this.tileEntities = builder.tileEntities;
    this.metadata = builder.metadata;
  }

  @Override
  public BlockState getBlockState(BlockPos pos) {
    int index;
    try {
      index = this.blockData[(pos.getY() * length + pos.getZ()) * width + pos.getX()];
    } catch (ArrayIndexOutOfBoundsException e) {
      return Blocks.AIR.getDefaultState();
    }

    if (this.palette == null) {
      return GameData.getBlockStateIDMap().getByValue(index);
    } else {
      return this.palette.get(index);
    }
  }

  @Override
  public TileEntity getTileEntity(BlockPos pos) {
    return this.tileEntities.get(pos);
  }

  @Override
  public Set<TileEntity> getTileEntities() {
    return new HashSet<>(this.tileEntities.values());
  }

  @Override
  public Set<Entity> getEntities() {
    return null;
  }

  @Override
  public Biome getBiome(int x, int y, int z) {
    int index;
    try {
      index = this.biomeData[x + z * this.getWidth()];
    } catch (ArrayIndexOutOfBoundsException e) {
      return Biomes.PLAINS;
    }

    if (this.biomePalette == null) {
      return GameData.getWrapper(Biome.class).getByValue(index);
    } else {
      return this.biomePalette.get(index);
    }
  }

  @Override
  public Set<Biome> getBiomes() {
    return this.biomePalette == null ? DEFAULT_BIOMES : new HashSet<>(this.biomePalette.values());
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public String getAuthor() {
    return this.metadata.author;
  }

  public static class Metadata {
    @Nullable
    private final String name;
    @Nullable
    private final String author;
    @Nullable
    private final Date dateCreated;

    private Metadata(Builder builder) {
      this.name = builder.name;
      this.author = builder.author;
      this.dateCreated = builder.dateCreated;
    }

    public static class Builder {
      private final SpongeSchematicMap.Builder parent;

      @Nullable
      private String name;
      @Nullable
      private String author;
      @Nullable
      private Date dateCreated;

      public Builder(SpongeSchematicMap.Builder parent) {
        this.parent = parent;
      }

      public Builder setName(@Nullable String name) {
        this.name = Strings.emptyToNull(name);
        return this;
      }

      public Builder setAuthor(@Nullable String author) {
        this.author = Strings.emptyToNull(author);
        return this;
      }

      public Builder setDateCreated(@Nullable Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
      }

      public SpongeSchematicMap.Builder and() {
        this.parent.metadata = new Metadata(this);
        return this.parent;
      }
    }
  }

  public static class Builder {
    private int width;
    private int height;
    private int length;
    @Nullable
    private Map<Integer, BlockState> palette;
    private int[] blockData;
    @Nullable
    private Map<Integer, Biome> biomePalette;
    private int[] biomeData;
    private Map<BlockPos, TileEntity> tileEntities;

    private Metadata metadata;

    public Builder setWidth(int width) {
      this.width = width;
      return this;
    }

    public Builder setHeight(int height) {
      this.height = height;
      return this;
    }

    public Builder setLength(int length) {
      this.length = length;
      return this;
    }

    public Builder setPalette(@Nullable Map<Integer, BlockState> palette) {
      this.palette = palette;
      return this;
    }

    public Builder setBlockData(int[] blockData) {
      this.blockData = blockData;
      return this;
    }

    public Builder setBiomePalette(@Nullable Map<Integer, Biome> biomePalette) {
      this.biomePalette = biomePalette;
      return this;
    }

    public Builder setBiomeData(int[] biomeData) {
      this.biomeData = biomeData;
      return this;
    }

    public Builder setTileEntities(Map<BlockPos, TileEntity> tileEntities) {
      this.tileEntities = tileEntities;
      return this;
    }

    public Metadata.Builder metadata() {
      return new Metadata.Builder(this);
    }

    public SpongeSchematicMap build() {
      return new SpongeSchematicMap(this);
    }
  }
}
