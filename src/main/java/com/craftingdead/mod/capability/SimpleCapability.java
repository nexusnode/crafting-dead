package com.craftingdead.mod.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface SimpleCapability {

	@Nullable
	NBTTagCompound writeNBT(EnumFacing side);

	void readNBT(EnumFacing side, NBTTagCompound nbt);

	public static class Storage<C extends SimpleCapability> implements Capability.IStorage<C> {

		@Override
		public NBTTagCompound writeNBT(Capability<C> capability, C instance, EnumFacing side) {
			return instance.writeNBT(side);
		}

		@Override
		public void readNBT(Capability<C> capability, C instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT(side, (NBTTagCompound) nbt);
		}

	}

	public static class Provider<C extends SimpleCapability> implements ICapabilitySerializable<NBTBase> {

		private final C capability;
		private final Capability<C> capabilityHolder;

		public Provider(C capability, Capability<C> capabilityHolder) {
			this.capability = capability;
			this.capabilityHolder = capabilityHolder;
		}

		public Provider(Capability<C> capabilityHolder) {
			this.capability = capabilityHolder.getDefaultInstance();
			this.capabilityHolder = capabilityHolder;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == this.capabilityHolder;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return this.hasCapability(capability, facing) ? (T) this.capability : null;
		}

		@Override
		public NBTBase serializeNBT() {
			return this.capabilityHolder.getStorage().writeNBT(this.capabilityHolder, this.capability, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			this.capabilityHolder.getStorage().readNBT(this.capabilityHolder, this.capability, null, nbt);
		}

	}

}
