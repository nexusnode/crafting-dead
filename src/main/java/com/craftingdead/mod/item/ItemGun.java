package com.craftingdead.mod.item;

import javax.annotation.Nullable;

import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.init.ModCapabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemGun extends ExtendedItem {

	/**
	 * Time between shots in milliseconds
	 */
	private int fireRate;

	private int blockInteractRate;

	private int fireDistance = 120;

	private int clipSize;

	private int damage;

	private int spread;

	public int getFireRate() {
		return fireRate;
	}

	public ItemGun setFireRate(int fireRate) {
		this.fireRate = fireRate;
		return this;
	}

	public int getBlockInteractRate() {
		return blockInteractRate;
	}

	public ItemGun setBlockInteractRate(int blockInteractRate) {
		this.blockInteractRate = blockInteractRate;
		return this;
	}

	public int getFireDistance() {
		return fireDistance;
	}

	public ItemGun setFireDistance(int fireDistance) {
		this.fireDistance = fireDistance;
		return this;
	}

	public int getClipSize() {
		return this.clipSize;
	}

	public ItemGun setClipSize(int clipSize) {
		this.clipSize = clipSize;
		return this;
	}

	public int getDamage() {
		return damage;
	}

	public ItemGun setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	public int getSpread() {
		return spread;
	}

	public ItemGun setSpread(int spread) {
		this.spread = spread;
		return this;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new Capabilities();
	}

	@Override
	public boolean getCancelVanillaAttack() {
		return true;
	}

	private class Capabilities implements ICapabilityProvider {

		private final GunController gunController = new GunController(ItemGun.this);

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ModCapabilities.TRIGGERABLE;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == ModCapabilities.TRIGGERABLE) {
				return ModCapabilities.TRIGGERABLE.cast(this.gunController);
			}
			return null;
		}

	}

}
