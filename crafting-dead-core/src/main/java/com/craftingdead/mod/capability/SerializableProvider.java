package com.craftingdead.mod.capability;

import java.util.Set;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SerializableProvider<C extends INBTSerializable<S>, S extends INBT>
    implements ICapabilitySerializable<S> {

  private final C capability;
  private final Set<Supplier<Capability<? super C>>> capabilityHolder;

  public SerializableProvider(C capability, Supplier<Capability<? super C>> capabilityHolder) {
    this(capability, ImmutableSet.of(capabilityHolder));
  }

  public SerializableProvider(C capability, Set<Supplier<Capability<? super C>>> capabilityHolder) {
    this.capability = capability;
    this.capabilityHolder = capabilityHolder;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return cap != null ? this.capabilityHolder.stream().map(Supplier::get).anyMatch(cap::equals)
        ? LazyOptional.of(() -> this.capability).cast()
        : LazyOptional.empty() : LazyOptional.empty();
  }

  @Override
  public S serializeNBT() {
    return this.capability.serializeNBT();
  }

  @Override
  public void deserializeNBT(S nbt) {
    this.capability.deserializeNBT(nbt);
  }
}
