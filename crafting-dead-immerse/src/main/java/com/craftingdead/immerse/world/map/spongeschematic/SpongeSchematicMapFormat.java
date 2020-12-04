package com.craftingdead.immerse.world.map.spongeschematic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.world.WorldUtil;
import com.craftingdead.immerse.world.map.NBTMapFormat;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.ShortNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @see <a href="https://github.com/SpongePowered/Schematic-Specification">Sponge Schematic
 *      Specification</a>
 */
public class SpongeSchematicMapFormat extends NBTMapFormat<SpongeSchematicMap> {

  private static final Logger logger = LogManager.getLogger();

  private static final Map<Integer, IVersionReader> versionReaders =
      ImmutableMap.of(2, new Version2());

  @Override
  protected SpongeSchematicMap read(CompoundNBT nbt) throws IOException {
    final int version = nbt.getInt("Version");
    if (version == 0) {
      throw new IOException("Version tag required.");
    }

    final int dataVersion = nbt.getInt("DataVersion");
    if (dataVersion != SharedConstants.getVersion().getWorldVersion()) {
      logger.warn(
          "Schematic was made in a different version of Minecraft (schematic_version={}, game_version={}). Data may be incompatible.",
          dataVersion, SharedConstants.getVersion().getWorldVersion());
    }

    IVersionReader versionReader = versionReaders.get(version);
    if (versionReader != null) {
      return versionReader.read(nbt);
    } else {
      throw new IOException("Unsupported schematic version");
    }
  }

  private static interface IVersionReader {
    SpongeSchematicMap read(CompoundNBT nbt) throws IOException;
  }

  private static class Version2 implements IVersionReader {

    @Override
    public SpongeSchematicMap read(CompoundNBT nbt) throws IOException {
      final int width = getExpectedTag(nbt, "Width", ShortNBT.class).getInt();
      final int height = getExpectedTag(nbt, "Height", ShortNBT.class).getInt();
      final int length = getExpectedTag(nbt, "Length", ShortNBT.class).getInt();

      final CompoundNBT metadataNbt = nbt.getCompound("Metadata");

      return new SpongeSchematicMap.Builder()
          .setWidth(width)
          .setHeight(height)
          .setLength(length)
          .setPalette(this.readPalette(nbt.getCompound("Palette")).orElse(null))
          .setBlockData(
              convertVarIntArray(
                  getExpectedTag(nbt, "BlockData", ByteArrayNBT.class).getByteArray()))
          .setBiomePalette(
              this.readBiomePalette(nbt.getCompound("BiomePalette")).orElse(null))
          .setBiomeData(convertVarIntArray(nbt.getByteArray("BiomeData")))
          .setTileEntities(this
              .readTileEntities(nbt.getList("BlockEntities", Constants.NBT.TAG_COMPOUND)))
          .metadata()
          .setName(metadataNbt.getString("Name"))
          .setAuthor(metadataNbt.getString("Author"))
          .setDateCreated(metadataNbt.contains("Date", Constants.NBT.TAG_LONG)
              ? new Date(metadataNbt.getLong("Date"))
              : null)
          .and()
          .build();
    }

    protected static int[] convertVarIntArray(byte[] array) {
      List<Integer> list = new ArrayList<>();

      int i = 0;
      int value = 0;
      int length = 0;
      while (i < array.length) {
        value = 0;
        length = 0;

        while (true) {
          value |= (array[i] & 127) << (length++ * 7);
          if (length > 5) {
            throw new RuntimeException("VarInt too big (probably corrupted data)");
          }
          if ((array[i] & 128) != 128) {
            i++;
            break;
          }
          i++;
        }

        list.add(value);
      }

      return Ints.toArray(list);
    }

    protected Optional<Map<Integer, BlockState>> readPalette(CompoundNBT nbt) throws IOException {
      final int size = nbt.keySet().size();
      Map<Integer, BlockState> palette = new Int2ObjectArrayMap<>(size);

      for (String key : nbt.keySet()) {
        final int id = nbt.getInt(key);
        BlockState blockState = WorldUtil.getBlockStateFromString(key);

        if (blockState == null) {
          logger.warn("Unknown block state \"{}\"", key);
          blockState = Blocks.AIR.getDefaultState();
        }

        if (id < 0 || id >= size) {
          logger.warn("Block state id out of range \"{}\"", id);
          return Optional.empty();
        }

        palette.put(id, blockState);
      }

      return Optional.of(palette);
    }

    protected Optional<Map<Integer, Biome>> readBiomePalette(CompoundNBT nbt) throws IOException {
      final int size = nbt.keySet().size();
      Map<Integer, Biome> palette = new Int2ObjectArrayMap<>(size);

      for (String key : nbt.keySet()) {
        final int id = nbt.getInt(key);
        Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(key));

        if (biome == null) {
          logger.warn("Unknown biome \"{}\"", key);
          biome = Biomes.DEFAULT;
        }

        if (id < 0 || id >= size) {
          logger.warn("Biome id out of range \"{}\"", id);
          return Optional.empty();
        }

        palette.put(id, biome);
      }

      return Optional.of(palette);
    }

    protected Map<BlockPos, TileEntity> readTileEntities(ListNBT nbt) throws IOException {
      Map<BlockPos, TileEntity> tileEntities = new HashMap<>();
      for (int i = 0; i < nbt.size(); i++) {
        CompoundNBT entry = nbt.getCompound(i);
        ResourceLocation id =
            new ResourceLocation(getExpectedTag(entry, "Id", StringNBT.class).getString());

        CompoundNBT tileEntityNbt = entry.copy();

        // Rename 'Id' key to 'id'
        tileEntityNbt.remove("Id");
        tileEntityNbt.putString("id", id.toString());

        // Convert 'Pos' to x, y, z fields
        int[] blockPos = tileEntityNbt.getIntArray("Pos");
        tileEntityNbt.putInt("x", blockPos[0]);
        tileEntityNbt.putInt("y", blockPos[1]);
        tileEntityNbt.putInt("z", blockPos[2]);
        tileEntityNbt.remove("Pos");

        TileEntityType<?> tileEntityType = ForgeRegistries.TILE_ENTITIES.getValue(id);
        if (tileEntityType == null) {
          logger.warn("Unknown tile entity \"{}\"", id.toString());
          continue;
        }

        TileEntity tileEntity = tileEntityType.create();
        if (tileEntity == null) {
          logger.warn("Cannot create tile entity \"{}\"", id.toString());
          continue;
        }

        tileEntity.read(tileEntityNbt);

        tileEntities.put(tileEntity.getPos(), tileEntity);
      }
      return tileEntities;
    }
  }
}
