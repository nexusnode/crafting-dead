package com.craftingdead.mod.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;




public class BackpackContainer extends Container {


    private final IInventory backpackInventory;
    private final Backpack backpack;

    public static BackpackContainer createGeneric9X1(int id, PlayerInventory player) {
        return new BackpackContainer(id, player, Backpack.SMALL);
    }

    public BackpackContainer(int id, PlayerInventory playerInventory, IInventory backpackInventory, Backpack backpack) {
        super(ModType.backpack, id);
        this.backpackInventory = backpackInventory;
        this.backpack = backpack;
        this.appendBackpackInventory(backpack.getSlotBackpackX(), backpack.getSlotBackpackY());
        this.appendPlayerInventory(playerInventory, backpack.getSlotPlayerX(), backpack.getSlotPlayerY());
    }

    public BackpackContainer(int id, PlayerInventory playerInventory, Backpack backpack) {
        super(ModType.backpack, id);
        this.backpackInventory = playerInventory;
        this.backpack = backpack;
        this.appendBackpackInventory(backpack.getSlotBackpackX(), backpack.getSlotBackpackY());
        this.appendPlayerInventory(playerInventory, backpack.getSlotPlayerX(), backpack.getSlotPlayerY());
    }

    public static BackpackContainer createClientContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        System.out.println("USED");
        Backpack backPack = buffer.readEnumValue(Backpack.class);
        return new BackpackContainer(id, playerInventory, backPack);
    }

    public void appendBackpackInventory(int x, int y) {
        int height = 0;
        while (height < this.backpack.getInventoryHeight()) {
            for (int width = 0; width < this.backpack.getInventoryWidth(); ++width) {
                this.addSlot(new Slot(this.backpackInventory, width + height * this.backpack.getInventoryWidth(), width * 18 + x, height * 18 + y));
            }
            ++height;
        }
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.backpackInventory.closeInventory(playerIn);
        System.out.println("CLOUSE");
    }

    public void appendPlayerInventory(PlayerInventory playerInventory, int x, int y) {
        int height = 0;
        while (height < 4) {
            for (int width = 0; width < 9; ++width) {
                if (height == 3) {
                    this.addSlot(new Slot(playerInventory, width, width * 18 + x, height * 18 + 4 + y));
                    continue;
                }
                this.addSlot(new Slot(playerInventory, width + height * 9 + 9, width * 18 + x, height * 18 + y));
            }
            ++height;
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.backpackInventory.isUsableByPlayer(playerIn);
    }


}
