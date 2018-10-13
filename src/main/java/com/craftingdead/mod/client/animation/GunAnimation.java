package com.craftingdead.mod.client.animation;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.client.multiplayer.PlayerSP;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public abstract class GunAnimation {

	public int animationTicker;

	public abstract void update(ClientProxy client, PlayerSP player, ItemStack itemStack);

	/** Called to render the animation right before the gun model is rendered */
	public abstract void doRender(ItemStack itemStack, float partialTicks);

	/** Called to render the animation right before the gun model is rendered */
	public abstract void preRenderHand(ItemStack itemStack, float partialTicks, EnumHandSide handSide);

	public boolean shouldRenderHand(EnumHandSide handSide) {
		return true;
	}

	public abstract void onAnimationStopped(ItemStack itemStack);

	/**
	 * Get the length of the animation in ticks
	 * 
	 * @return the length
	 */
	public abstract float getLength();
}
