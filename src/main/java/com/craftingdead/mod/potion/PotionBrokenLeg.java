package com.craftingdead.mod.potion;

import net.minecraft.potion.Potion;

public class PotionBrokenLeg extends Potion {

	public PotionBrokenLeg(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
	}

	public Potion setIconIndex(int x, int y) {
		super.setIconIndex(x, y);
		return this;
	}

}
