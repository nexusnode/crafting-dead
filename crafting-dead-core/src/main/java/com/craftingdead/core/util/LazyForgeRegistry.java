package com.craftingdead.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Implementation of {@link IForgeRegistry} that delegates to an {@link IForgeRegistry} instance
 * that's lazily loaded from {@link GameRegistry}.
 */
public class LazyForgeRegistry<V extends IForgeRegistryEntry<V>> implements IForgeRegistry<V> {

  private final LazyValue<IForgeRegistry<V>> registry;

  public LazyForgeRegistry(final Class<V> registryType) {
    registry = new LazyValue<>(() -> Objects.requireNonNull(
        GameRegistry.findRegistry(registryType),
        () -> String.format("Registry of type %s not present", registryType.getName())));
  }

  public static <V extends IForgeRegistryEntry<V>> IForgeRegistry<V> of(
      final Class<V> registryType) {
    return new LazyForgeRegistry<>(registryType);
  }

  private IForgeRegistry<V> getRegistry() {
    return this.registry.getValue();
  }

  @Override
  public ResourceLocation getRegistryName() {
    return this.getRegistry().getRegistryName();
  }

  @Override
  public Class<V> getRegistrySuperType() {
    return this.getRegistry().getRegistrySuperType();
  }

  @Override
  public void register(final V value) {
    this.getRegistry().register(value);
  }

  @SafeVarargs
  @Override
  public final void registerAll(final V... values) {
    this.getRegistry().registerAll(values);
  }

  @Override
  public boolean containsKey(final ResourceLocation key) {
    return this.getRegistry().containsKey(key);
  }

  @Override
  public boolean containsValue(final V value) {
    return this.getRegistry().containsValue(value);
  }

  @Override
  public boolean isEmpty() {
    return this.getRegistry().isEmpty();
  }

  @Override
  @Nullable
  public V getValue(final ResourceLocation key) {
    return this.getRegistry().getValue(key);
  }

  @Override
  @Nullable
  public ResourceLocation getKey(final V value) {
    return this.getRegistry().getKey(value);
  }

  @Override
  @Nullable
  public ResourceLocation getDefaultKey() {
    return this.getRegistry().getDefaultKey();
  }

  @Override
  @Nonnull
  public Set<ResourceLocation> getKeys() {
    return this.getRegistry().getKeys();
  }

  @Override
  @Nonnull
  public Collection<V> getValues() {
    return this.getRegistry().getValues();
  }

  @Override
  @Nonnull
  public Set<Map.Entry<ResourceLocation, V>> getEntries() {
    return this.getRegistry().getEntries();
  }

  @Override
  public <T> T getSlaveMap(final ResourceLocation slaveMapName, final Class<T> type) {
    return this.getRegistry().getSlaveMap(slaveMapName, type);
  }

  @Override
  public Iterator<V> iterator() {
    return this.getRegistry().iterator();
  }
}
