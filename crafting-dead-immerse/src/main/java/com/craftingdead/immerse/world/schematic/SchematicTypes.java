package com.craftingdead.immerse.world.schematic;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.schematic.sponge.SpongeSchematicType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class SchematicTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<SchematicType<?>> SCHEMATIC_TYPES =
      DeferredRegister.create((Class<SchematicType<?>>) (Class<?>) SchematicType.class,
          CraftingDeadImmerse.ID);

  public static final RegistryObject<SchematicType<?>> SPONGE = SCHEMATIC_TYPES
      .register("sponge", SpongeSchematicType::new);
}
