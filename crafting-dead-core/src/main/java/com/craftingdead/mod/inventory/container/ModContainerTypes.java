package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

  public static final DeferredRegister<ContainerType<?>> CONTAINERS =
      new DeferredRegister<>(ForgeRegistries.CONTAINERS, CraftingDead.ID);

  public static final RegistryObject<ContainerType<ModPlayerContainer>> PLAYER =
      CONTAINERS.register("player", () -> new ContainerType<>(ModPlayerContainer::new));
}
