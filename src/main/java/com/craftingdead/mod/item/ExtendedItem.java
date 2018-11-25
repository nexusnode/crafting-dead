package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class ExtendedItem extends Item {

	private boolean bowAndArrowPose;
	private boolean customCrosshair;
	private boolean cancelVanillaAttack;

	public boolean getBowAndArrowPose() {
		return this.bowAndArrowPose;
	}

	public ExtendedItem setBowAndArrowPose(boolean bowAndArrowPose) {
		this.bowAndArrowPose = bowAndArrowPose;
		return this;
	}

	public boolean hasCustomCrosshair() {
		return customCrosshair;
	}

	public ExtendedItem setCustomCrosshair(boolean customCrosshair) {
		this.customCrosshair = customCrosshair;
		return this;
	}

	public boolean getCancelVanillaAttack() {
		return this.cancelVanillaAttack;
	}

	public ExtendedItem setCancelVanillaAttack(boolean cancelVanillaAttack) {
		this.cancelVanillaAttack = cancelVanillaAttack;
		return this;
	}

}
