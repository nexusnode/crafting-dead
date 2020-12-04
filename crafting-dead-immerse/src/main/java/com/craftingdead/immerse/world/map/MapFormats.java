package com.craftingdead.immerse.world.map;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.map.spongeschematic.SpongeSchematicMapFormat;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class MapFormats {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<MapFormat<?>> MAP_FORMATS =
      DeferredRegister.create((Class<MapFormat<?>>) (Class<?>) MapFormat.class,
          CraftingDeadImmerse.ID);

  public static final RegistryObject<MapFormat<?>> SPONGE_SCHEMATIC = MAP_FORMATS
      .register("sponge_schematic", SpongeSchematicMapFormat::new);
}
