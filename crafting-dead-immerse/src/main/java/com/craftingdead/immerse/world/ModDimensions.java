package com.craftingdead.immerse.world;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.map.MapDimension;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDimensions {

  public static final DeferredRegister<ModDimension> MOD_DIMENSIONS =
      DeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, CraftingDeadImmerse.ID);

  public static final RegistryObject<ModDimension> MAP = MOD_DIMENSIONS
      .register("map", () -> ModDimension.withFactory(MapDimension::new));
}
