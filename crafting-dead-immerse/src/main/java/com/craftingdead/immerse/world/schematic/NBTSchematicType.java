package com.craftingdead.immerse.world.schematic;

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;

public abstract class NBTSchematicType<T extends ISchematic> extends SchematicType<T> {

  @Override
  public T read(InputStream inputStream) throws IOException {
    return this.read(CompressedStreamTools.readCompressed(inputStream));
  }

  protected abstract T read(CompoundNBT nbt) throws IOException;

  @SuppressWarnings("unchecked")
  public static <N extends INBT> N getExpectedTag(CompoundNBT compoundNbt, String key,
      Class<N> expectedType) throws IOException {
    INBT nbt = compoundNbt.get(key);
    if (expectedType.isAssignableFrom(nbt.getClass())) {
      return (N) nbt;
    } else {
      throw new IOException(
          "Tag of type '" + expectedType.getSimpleName() + "' with key '" + key + "' is missing.");
    }
  }
}
