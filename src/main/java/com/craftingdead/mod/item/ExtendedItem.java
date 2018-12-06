package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class ExtendedItem extends Item {

	private final boolean bowAndArrowPose;
	private final boolean customCrosshair;

	public ExtendedItem(boolean bowAndArrowPose, boolean customCrosshair) {
		this.bowAndArrowPose = bowAndArrowPose;
		this.customCrosshair = customCrosshair;
	}

	public boolean getBowAndArrowPose() {
		return this.bowAndArrowPose;
	}

	public boolean hasCustomCrosshair() {
		return this.customCrosshair;
	}

	public boolean getCancelVanillaAttack() {
		return false;
	}

}
