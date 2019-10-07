package com.craftingdead.mod.tileentity;

import com.craftingdead.mod.container.ChestContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class ChestTileEntity extends TileEntity implements INamedContainerProvider {

    public ChestTileEntity() {
        super(ModTileEntityTypes.ironchest);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {
        return new ChestContainer(i,playerInventory);
    }

}
