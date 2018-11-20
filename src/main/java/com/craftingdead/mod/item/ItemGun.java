package com.craftingdead.mod.item;

import javax.annotation.Nullable;

import com.craftingdead.mod.capability.SimpleCapability;
import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.init.ModCapabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemGun extends ExtendedItem {

	/**
	 * Time between shots in milliseconds
	 */
	private int fireRate;

	private int blockInteractRate;

	private int fireDistance = 120;

	private int clipSize;

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

	public void setBlockInteractRate(int blockInteractRate) {
		this.blockInteractRate = blockInteractRate;
	}

	public int getFireDistance() {
		return fireDistance;
	}

	public void setFireDistance(int fireDistance) {
		this.fireDistance = fireDistance;
	}

	public int getClipSize() {
		return this.clipSize;
	}

	public ItemGun setClipSize(int clipSize) {
		this.clipSize = clipSize;
		return this;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new SimpleCapability.Provider<>(new GunController(this), ModCapabilities.TRIGGERABLE);
	}

	@Override
	public boolean cancelVanillaAttack() {
		return true;
	}

}
