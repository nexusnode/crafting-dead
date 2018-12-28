package com.craftingdead.mod.client.animation;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public interface GunAnimation extends ITickable {

	public static enum Type {
		SHOOT;
	}

	void render(ItemStack itemStack, float partialTicks);

	void onAnimationStopped(ItemStack par1);

	boolean isFinished();
}
