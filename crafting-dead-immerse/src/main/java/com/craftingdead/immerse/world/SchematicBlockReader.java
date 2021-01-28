package com.craftingdead.immerse.world;

import com.craftingdead.immerse.world.schematic.ISchematic;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SchematicBlockReader implements IBlockReader {

  private final ISchematic schematic;

  public SchematicBlockReader(ISchematic schematic) {
    this.schematic = schematic;
  }

  @Override
  public TileEntity getTileEntity(BlockPos pos) {
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
