package com.craftingdead.core.inventory.container;

import com.craftingdead.core.CraftingDead;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

  public static final DeferredRegister<ContainerType<?>> CONTAINERS =
      new DeferredRegister<>(ForgeRegistries.CONTAINERS, CraftingDead.ID);

  public static final RegistryObject<ContainerType<ModInventoryContainer>> PLAYER =
      CONTAINERS.register("inventory", () -> new ContainerType<>(ModInventoryContainer::new));

  public static final RegistryObject<ContainerType<GenericContainer>> VEST =
      CONTAINERS.register("vest", () -> new ContainerType<>(GenericContainer::createVest));
}
