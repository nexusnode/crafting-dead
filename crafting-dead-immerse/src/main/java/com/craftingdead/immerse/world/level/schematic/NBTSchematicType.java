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

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;

public abstract class NBTSchematicType<T extends Schematic> extends SchematicType<T> {

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
