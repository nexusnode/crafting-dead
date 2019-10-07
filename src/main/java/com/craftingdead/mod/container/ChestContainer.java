package com.craftingdead.mod.container;

import com.craftingdead.mod.type.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChestContainer
        extends Container {

    private final IInventory inventory;
    private final ChestType chestType;
    private PlayerEntity playerEntity;

    public ChestContainer(int windowId, PlayerInventory playerInventory) {
        this(ModContainerType.ironchest, windowId, playerInventory, new Inventory(ChestType.WOOD.size), ChestType.WOOD);
    }

    public ChestContainer(ContainerType<?> containerType, int windowId, PlayerInventory playerInventory) {
        this(containerType, windowId, playerInventory, new Inventory(ChestType.WOOD.size), ChestType.WOOD);
    }

    public static ChestContainer createIronContainer(int windowId, PlayerInventory playerInventory) {
        return new ChestContainer(ModContainerType.ironchest, windowId, playerInventory, new Inventory(ChestType.IRON.size), ChestType.IRON);
    }

    public ChestContainer(ContainerType<?> containerType, int windowId, PlayerInventory playerInventory, IInventory inventory, ChestType chestType) {
        super(containerType, windowId);
        this.inventory = inventory;
        this.chestType = chestType;
    }

    @OnlyIn(value = Dist.CLIENT)
    public ChestType getChestType() {
        return this.chestType;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
