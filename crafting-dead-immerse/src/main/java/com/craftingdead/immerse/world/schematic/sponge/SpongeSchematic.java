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

package com.craftingdead.immerse.world.schematic.sponge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.immerse.world.schematic.ISchematic;
import com.google.common.base.Strings;
import com.mojang.serialization.Lifecycle;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraftforge.registries.GameData;

public class SpongeSchematic implements ISchematic {

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

  private SpongeSchematic(Builder builder) {
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
      return BiomeRegistry.PLAINS;
    }

    if (this.biomePalette == null) {
      return GameData.getWrapper(Registry.BIOME_KEY, Lifecycle.stable()).getByValue(index);
    } else {
      return this.biomePalette.get(index);
    }
  }

  @Override
  public List<Biome> getBiomes() {
    return this.biomePalette == null ? Collections.singletonList(BiomeRegistry.PLAINS)
        : new ArrayList<>(this.biomePalette.values());
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
      private final SpongeSchematic.Builder parent;

      @Nullable
      private String name;
      @Nullable
      private String author;
      @Nullable
      private Date dateCreated;

      public Builder(SpongeSchematic.Builder parent) {
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

      public SpongeSchematic.Builder and() {
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

    public SpongeSchematic build() {
      return new SpongeSchematic(this);
    }
  }
}
