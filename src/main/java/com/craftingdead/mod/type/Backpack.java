package com.craftingdead.mod.type;

import net.minecraft.item.Rarity;
import net.minecraft.util.IStringSerializable;

public enum Backpack implements IStringSerializable
{
    SMALL("small", Rarity.COMMON, 5, 3, 44, 24, 8, 82, 176, 164),
    MEDIUM("medium", Rarity.UNCOMMON, 9, 6, 8, 24, 8, 136, 176, 218),
    LARGE("large", Rarity.RARE, 13, 9, 8, 24, 44, 190, 248, 272);

    private final String name;
    private final Rarity rarity;
    private final int inventoryWidth;
    private final int inventoryHeight;
    private final int slotBackpackX;
    private final int slotBackpackY;
    private final int slotPlayerX;
    private final int slotPlayerY;
    private final int textureSizeX;
    private final int textureSizeY;

    private Backpack(String name, Rarity rarity, int inventoryWidth, int inventoryHeight, int slotBackpackX, int slotBackpackY, int slotPlayerX, int slotPlayerY, int textureSizeX, int textureSizeY) {
        this.name = name;
        this.rarity = rarity;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.slotBackpackX = slotBackpackX;
        this.slotBackpackY = slotBackpackY;
        this.slotPlayerX = slotPlayerX;
        this.slotPlayerY = slotPlayerY;
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public int getInventoryWidth() {
        return this.inventoryWidth;
    }

    public int getInventoryHeight() {
        return this.inventoryHeight;
    }

    public int getInventorySize() {
        return this.inventoryWidth * this.inventoryHeight;
    }

    public int getSlotBackpackX() {
        return this.slotBackpackX;
    }

    public int getSlotBackpackY() {
        return this.slotBackpackY;
    }

    public int getSlotPlayerX() {
        return this.slotPlayerX;
    }

    public int getSlotPlayerY() {
        return this.slotPlayerY;
    }

    public int getTextureSizeX() {
        return this.textureSizeX;
    }

    public int getTextureSizeY() {
        return this.textureSizeY;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
