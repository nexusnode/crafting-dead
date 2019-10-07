package com.craftingdead.mod.tileentity;

import com.craftingdead.mod.block.ModBlocks;
import com.craftingdead.mod.type.ChestType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.craftingdead.mod.tileentity.ModTileEntityTypes.ironchest;

@OnlyIn(value= Dist.CLIENT, _interface= IChestLid.class)
public class IronChestTileEntity extends LockableLootTileEntity {

    private NonNullList<ItemStack> chestContents;
    private ChestType chestType;
    private Block blockToUse;

    public IronChestTileEntity() {
        this(ironchest, ChestType.IRON, ModBlocks.chestblock);
    }

    public IronChestTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    protected IronChestTileEntity(TileEntityType<?> typeIn, ChestType chestTypeIn, Block blockToUseIn) {
        super(typeIn);
        this.chestContents = NonNullList.withSize(chestTypeIn.size, ItemStack.EMPTY);
        this.chestType = chestTypeIn;
        this.blockToUse = blockToUseIn;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {

    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
