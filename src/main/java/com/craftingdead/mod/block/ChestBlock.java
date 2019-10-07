package com.craftingdead.mod.block;


import com.craftingdead.mod.tileentity.TileEntityLoot;
import com.craftingdead.mod.type.ChestTileEntity;
import com.craftingdead.mod.type.IronChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;


public class ChestBlock  extends Block {
    public ChestBlock(Properties properties) {
        super(properties);
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return true;
        }
        INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
        if (inamedcontainerprovider == null) return true;
        player.openContainer(inamedcontainerprovider);
        player.addStat(this.getOpenStat());
        return true;
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (!(tileentity instanceof INamedContainerProvider)){
            return null;
        }
        INamedContainerProvider iNamedContainerProvider = (INamedContainerProvider)tileentity;
        return iNamedContainerProvider;
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ChestTileEntity();
    }


}
