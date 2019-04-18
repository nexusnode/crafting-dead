package com.craftingdead.mod.item;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.craftingdead.mod.capability.triggerable.GunController;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.crosshair.CrosshairProvider;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.init.ModCreativeTabs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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

	private final int clipSize;

	private final int damage;

	private final float reloadTime;

	private final float spread;

	/**
	 * A {@link List} of {@link FireMode}s the gun can cycle through
	 */
	private final List<Supplier<FireMode>> fireModes;

	private final Supplier<SoundEvent> shootSound;

	private final Map<GunAnimation.Type, Supplier<GunAnimation>> animations;

	private ItemGun(Builder builder) {
		super(true, true);
		this.fireRate = builder.fireRate;
		this.clipSize = builder.clipSize;
		this.damage = builder.damage;
		this.reloadTime = builder.reloadTime;
		this.spread = builder.spread;
		this.fireModes = builder.fireModes;
		this.shootSound = builder.shootSound;
		this.animations = builder.animations;
		this.setCreativeTab(ModCreativeTabs.CRAFTING_DEAD);
	}

	public int getFireRate() {
		return fireRate;
	}

	public int getClipSize() {
		return this.clipSize;
	}

	public int getDamage() {
		return damage;
	}

	public float getReloadTime() {
		return this.reloadTime;
	}

	public List<Supplier<FireMode>> getFireModes() {
		return this.fireModes;
	}

	public Supplier<SoundEvent> getShootSound() {
		return shootSound;
	}

	public Map<GunAnimation.Type, Supplier<GunAnimation>> getAnimations() {
		return this.animations;
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
		return this.spread;
	}

	public static class Builder {

		private Integer fireRate;

		private Integer clipSize;

		private Integer damage;

		private Float reloadTime;

		private Float spread;

		private List<Supplier<FireMode>> fireModes;

		private Supplier<SoundEvent> shootSound;

		private Map<GunAnimation.Type, Supplier<GunAnimation>> animations;

		public Builder setFireRate(int fireRate) {
			this.fireRate = fireRate;
			return this;
		}

		public Builder setClipSize(int clipSize) {
			this.clipSize = clipSize;
			return this;
		}

		public Builder setDamage(int damage) {
			this.damage = damage;
			return this;
		}

		public Builder setReloadTime(float reloadTime) {
			this.reloadTime = reloadTime;
			return this;
		}

		public Builder setSpread(float spread) {
			this.spread = spread;
			return this;
		}

		public Builder setFireModes(List<Supplier<FireMode>> fireModes) {
			this.fireModes = ImmutableList.copyOf(fireModes);
			return this;
		}

		public Builder setShootSound(Supplier<SoundEvent> shootSound) {
			this.shootSound = shootSound;
			return this;
		}

		public Builder setAnimations(Map<GunAnimation.Type, Supplier<GunAnimation>> animations) {
			this.animations = ImmutableMap.copyOf(animations);
			return this;
		}

		public ItemGun build() {
			return new ItemGun(this);
		}

	}

}
