package com.craftingdead.immerse.world.map;

import java.io.IOException;
import java.io.InputStream;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class MapFormat<T extends IMap>
    extends ForgeRegistryEntry<MapFormat<?>> {

  public abstract T read(InputStream inputStream) throws IOException;
}
