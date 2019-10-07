package com.craftingdead.mod.type;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ModContainerType {

    private static final List<ContainerType<?>> toRegister = new ArrayList<>();

    public static ContainerType<ChestContainer> ironchest;

    public static void initialize() {
        ironchest = add("new_block", IForgeContainerType.create((windowId, inv, data) -> {
            return ChestContainer.createIronContainer(windowId,inv);
        }));
    }

    public static void register(RegistryEvent.Register<ContainerType<?>> event) {
        toRegister.forEach(event.getRegistry()::register);
    }

    private static <T extends Container> ContainerType<T> add(String registryName,
                                                              ContainerType<T> containerType) {
        toRegister
                .add(containerType.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName)));
        return containerType;
    }
}