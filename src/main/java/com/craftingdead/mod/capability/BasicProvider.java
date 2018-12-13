package com.craftingdead.mod.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BasicProvider<C> implements ICapabilityProvider {

	private final C capability;
	private final Capability<C> capabilityHolder;

	public BasicProvider(C capability, Capability<C> capabilityHolder) {
		this.capability = capability;
		this.capabilityHolder = capabilityHolder;
	}

	public BasicProvider(Capability<C> capabilityHolder) {
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

}
