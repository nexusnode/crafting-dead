package com.craftingdead.immerse.world.map;

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;

public abstract class NBTMapFormat<T extends IMap> extends MapFormat<T> {

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
