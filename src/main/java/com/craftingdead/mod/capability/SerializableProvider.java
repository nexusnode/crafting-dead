package com.craftingdead.mod.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SerializableProvider<C extends INBTSerializable<S>, S extends INBT> implements ICapabilitySerializable<S> {

	private final C capability;
	private final Capability<C> capabilityHolder;

	public SerializableProvider(C capability, Capability<C> capabilityHolder) {
		this.capability = capability;
		this.capabilityHolder = capabilityHolder;
	}

	public SerializableProvider(Capability<C> capabilityHolder) {
		this.capability = capabilityHolder.getDefaultInstance();
		this.capabilityHolder = capabilityHolder;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == this.capabilityHolder ? LazyOptional.of(() -> this.capability).cast() : LazyOptional.empty();
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
