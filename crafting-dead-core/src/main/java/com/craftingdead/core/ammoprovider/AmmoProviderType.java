package com.craftingdead.core.ammoprovider;

import java.util.function.Supplier;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AmmoProviderType extends ForgeRegistryEntry<AmmoProviderType> {

  private final Supplier<IAmmoProvider> factory;

  public AmmoProviderType(Supplier<IAmmoProvider> factory) {
    this.factory = factory;
  }

  public IAmmoProvider create() {
    return this.factory.get();
  }
}
