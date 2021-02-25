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

package com.craftingdead.immerse.world;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.base.Splitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldUtil {

  private static final Splitter COMMA_SPLITTER = Splitter.on(',');
  private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

  @Nullable
  public static BlockState getBlockStateFromString(String str) {
    int index = str.indexOf("["); // [f=b]
    String blockIdString = index != -1 ? str.substring(0, index) : str;
    ResourceLocation blockId = new ResourceLocation(blockIdString);

    if (ForgeRegistries.BLOCKS.containsKey(blockId)) {
      Block block = ForgeRegistries.BLOCKS.getValue(blockId);
      BlockState blockState = block.getDefaultState();

      if (index != -1 && str.length() > (index + 4) && str.charAt(str.length() - 1) == ']') {
        StateContainer<Block, BlockState> stateContainer = block.getStateContainer();
        String propertyString = str.substring(index + 1, str.length() - 1);
        Iterator<String> propertyIterator = COMMA_SPLITTER.split(propertyString).iterator();

        while (propertyIterator.hasNext()) {
          String propAndVal = propertyIterator.next();
          Iterator<String> valueIterator = EQUAL_SPLITTER.split(propAndVal).iterator();

          if (valueIterator.hasNext() == false) {
            continue;
          }

          Property<?> property = stateContainer.getProperty(valueIterator.next());

          if (property == null || valueIterator.hasNext() == false) {
            continue;
          }

          Comparable<?> value = property.parseValue(valueIterator.next()).orElse(null);
          if (value != null) {
            blockState = getBlockStateWithProperty(blockState, property, value);
          }
        }
      }

      return blockState;
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public static <T extends Comparable<T>> BlockState getBlockStateWithProperty(BlockState state,
      Property<T> prop, Comparable<?> value) {
    return state.with(prop, (T) value);
  }
}
