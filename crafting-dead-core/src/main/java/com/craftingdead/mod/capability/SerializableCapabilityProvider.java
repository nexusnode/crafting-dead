package com.craftingdead.mod.capability;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class SerializableCapabilityProvider<C extends INBTSerializable<S>, S extends INBT>
    extends SimpleCapabilityProvider<C> implements INBTSerializable<S> {

  public SerializableCapabilityProvider(C capability, Supplier<Capability<? super C>> capabilityHolder) {
    super(capability, capabilityHolder);
  }

  public SerializableCapabilityProvider(C capability, Set<Supplier<Capability<? super C>>> capabilityHolder) {
    super(capability, capabilityHolder);
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
