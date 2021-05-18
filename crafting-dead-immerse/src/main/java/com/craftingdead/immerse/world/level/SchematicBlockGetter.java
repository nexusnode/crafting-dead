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

package com.craftingdead.immerse.world.level;

import com.craftingdead.immerse.world.level.schematic.Schematic;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SchematicBlockGetter implements IBlockReader {

  private final Schematic schematic;

  public SchematicBlockGetter(Schematic schematic) {
    this.schematic = schematic;
  }

  @Override
  public TileEntity getBlockEntity(BlockPos pos) {
    return this.schematic.getTileEntity(pos);
  }

  @Override
  public BlockState getBlockState(BlockPos pos) {
    return this.schematic.getBlockState(pos);
  }

  @Override
  public FluidState getFluidState(BlockPos pos) {
    return this.getBlockState(pos).getFluidState();
  }
}
