package com.craftingdead.mod.type;

import com.craftingdead.mod.block.ModBlocks;
import com.craftingdead.mod.tileentity.ChestTileEntity;
import com.craftingdead.mod.tileentity.IronChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum ChestType implements IStringSerializable
{
    IRON(54, 9, "new_block_container.png", ChestTileEntity.class, "craftingdead:new_block", 184, 222, new ResourceLocation("new_block", "textures/gui/new_block_container.png"), 256, 256),
       WOOD(0, 0, "", null, null, 0, 0, null, 0, 0);

    public static final ChestType[] VALUES;
    public final String name = this.name().toLowerCase();
    public final int size;
    public final int rowLength;
    public final String modelTexture;
    public final Class<? extends TileEntity> clazz;
    public final String itemName;
    public final int xSize;
    public final int ySize;
    public final ResourceLocation guiTexture;
    public final int textureXSize;
    public final int textureYSize;

    private ChestType(int size, int rowLength, String modelTexture, Class<? extends ChestTileEntity> clazz, String itemName, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this.size = size;
        this.rowLength = rowLength;
        this.modelTexture = modelTexture;
        this.clazz = clazz;
        this.itemName = itemName;
        this.xSize = xSize;
        this.ySize = ySize;
        this.guiTexture = guiTexture;
        this.textureXSize = textureXSize;
        this.textureYSize = textureYSize;
    }

    public String func_176610_l() {
        return this.name;
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public boolean isTransparent() {
        return true;
    }

    public static ChestType get(ResourceLocation resourceLocation) {
        return IRON;
    }

    public static BlockState get(ChestType type) {
        return ModBlocks.chestblock.getDefaultState();
    }

    public IronChestTileEntity makeEntity() {
        return new IronChestTileEntity();
    }

    static {
        VALUES = ChestType.values();
    }

    @Override
    public String getName() {
        return null;
    }
}
