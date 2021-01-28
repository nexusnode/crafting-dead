package com.craftingdead.immerse.world.schematic;

import java.io.IOException;
import java.io.InputStream;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class SchematicType<T extends ISchematic>
    extends ForgeRegistryEntry<SchematicType<?>> {

  public abstract T read(InputStream inputStream) throws IOException;
}
