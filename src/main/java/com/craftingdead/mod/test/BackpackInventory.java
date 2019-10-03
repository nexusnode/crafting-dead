package com.craftingdead.mod.test;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class BackpackInventory
        extends Inventory {

    private final ItemStack stack;

    public BackpackInventory(ItemStack stack, int count) {
        super(count);
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }

}

