package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class ExtendedItem extends Item {

	private boolean useBowAndArrowPose;

	public boolean useBowAndArrowPose() {
		return this.useBowAndArrowPose;
	}

	public ExtendedItem setUseBowAndArrowPose(boolean useBowAndArrowPose) {
		this.useBowAndArrowPose = useBowAndArrowPose;
		return this;
	}

	public boolean cancelVanillaAttack() {
		return false;
	}

}
