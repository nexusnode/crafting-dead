package com.craftingdead.mod.test;



import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;



public class ModType {
    @ObjectHolder("craftingdead:backpack")
    public static ContainerType<BackpackContainer> backpack;
}