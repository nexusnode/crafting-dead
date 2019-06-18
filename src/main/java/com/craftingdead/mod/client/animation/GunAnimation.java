package com.craftingdead.mod.client.animation;

import net.minecraft.item.ItemStack;

public interface GunAnimation {

	public static enum Type {
		SHOOT;
	}

	void tick();

	void render(ItemStack itemStack, float partialTicks);

	void onAnimationStopped(ItemStack par1);

	boolean isFinished();
}
