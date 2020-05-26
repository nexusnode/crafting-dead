package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

  public static final DeferredRegister<ContainerType<?>> CONTAINERS =
      new DeferredRegister<>(ForgeRegistries.CONTAINERS, CraftingDead.ID);

  public static final RegistryObject<ContainerType<ModInventoryContainer>> PLAYER =
      CONTAINERS.register("inventory", () -> new ContainerType<>(ModInventoryContainer::new));

  public static final RegistryObject<ContainerType<GenericContainer>> SMALL_BACKPACK = CONTAINERS
      .register("small_backpack", () -> new ContainerType<>(GenericContainer::createSmallBackpack));

  public static final RegistryObject<ContainerType<GenericContainer>> MEDIUM_BACKPACK = CONTAINERS
      .register("medium_backpack",
          () -> new ContainerType<>(GenericContainer::createMediumBackpack));

  public static final RegistryObject<ContainerType<GenericContainer>> LARGE_BACKPACK = CONTAINERS
      .register("large_backpack", () -> new ContainerType<>(GenericContainer::createLargeBackpack));

  public static final RegistryObject<ContainerType<GenericContainer>> GUN_BAG = CONTAINERS
      .register("gun_backpack", () -> new ContainerType<>(GenericContainer::createGunBag));

  public static final RegistryObject<ContainerType<GenericContainer>> QUIVER =
      CONTAINERS.register("quiver", () -> new ContainerType<>(GenericContainer::createQuiver));

  public static final RegistryObject<ContainerType<GenericContainer>> VEST =
      CONTAINERS.register("vest", () -> new ContainerType<>(GenericContainer::createVest));
}
