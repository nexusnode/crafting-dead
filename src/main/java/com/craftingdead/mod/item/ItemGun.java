package com.craftingdead.mod.item;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.client.crosshair.CrosshairProvider;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.init.ModCreativeTabs;
import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemGun extends ExtendedItem implements CrosshairProvider {

	/**
	 * Time between shots in milliseconds
	 */
	private final int fireRate;

	private final int fireDistance = 120;

	private final int clipSize;

	private final int damage;

	private final int spread;

	/**
	 * A {@link Set} of {@link FireMode}s the gun can cycle through
	 */
	private final List<Supplier<FireMode>> fireModes;

	private final Supplier<SoundEvent> shootSound;

	public ItemGun(int fireRate, int clipSize, int damage, int spread, List<Supplier<FireMode>> fireModes,
			Supplier<SoundEvent> shootSound) {
		super(true, true);
		this.fireRate = fireRate;
		this.clipSize = clipSize;
		this.damage = damage;
		this.spread = spread;
		this.fireModes = ImmutableList.copyOf(fireModes);
		this.shootSound = shootSound;
		this.setCreativeTab(ModCreativeTabs.CRAFTING_DEAD);
	}

	public int getFireRate() {
		return fireRate;
	}

	public int getFireDistance() {
		return fireDistance;
	}

	public int getClipSize() {
		return this.clipSize;
	}

	public int getDamage() {
		return damage;
	}

	public int getSpread() {
		return spread;
	}

	public List<Supplier<FireMode>> getFireModes() {
		return this.fireModes;
	}

	public Supplier<SoundEvent> getShootSound() {
		return shootSound;
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

	@Override
	public float getDefaultSpread() {
		return 0.1F;
	}

}
