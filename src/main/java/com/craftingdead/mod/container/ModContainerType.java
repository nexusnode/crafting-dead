package com.craftingdead.mod.container;

import com.craftingdead.mod.CraftingDead;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;

public class ModContainerType {

  private static final List<ContainerType<?>> toRegister = new ArrayList<>();

  public static ContainerType<BackpackContainer> backpack;

  public static void initialize() {
    backpack = add("backpack", IForgeContainerType.create((windowId, inv, data) -> {
      return BackpackContainer.createClientContainer(windowId, inv, data);
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
