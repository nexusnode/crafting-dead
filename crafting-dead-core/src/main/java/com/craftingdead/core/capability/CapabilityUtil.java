package com.craftingdead.core.capability;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityUtil {

  public static <T extends CapabilityProvider<T>> Predicate<T> capabilityPresent(
      Capability<?> capability) {
    return provider -> provider.getCapability(capability).isPresent();
  }

  @Nonnull
  public static <T, R extends T> R getOrThrow(Capability<T> capability,
      ICapabilityProvider provider, Class<R> clazz) {
    return provider.getCapability(capability)
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .orElseThrow(
            () -> new IllegalStateException("Expecting capability: " + capability.getName()));
  }
}
