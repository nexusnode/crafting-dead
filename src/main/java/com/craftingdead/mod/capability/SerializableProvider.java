package com.craftingdead.mod.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

public class SerializableProvider<C extends INBTSerializable<S>, S extends NBTBase>
		implements ICapabilitySerializable<S> {

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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == this.capabilityHolder;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? this.capabilityHolder.cast(this.capability) : null;
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
