package com.craftingdead.mod.common.item;

import net.minecraft.item.ItemStack;

public class ItemArc extends ItemGun {

	@Override
	public boolean useBowAndArrowStance() {
		return true;
	}

	@Override
	public Bullet createBullet(ItemStack itemStack) {
		return new Bullet();
	}


}
