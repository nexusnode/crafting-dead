package com.craftingdead.mod.client.animation;

import com.craftingdead.mod.client.ClientMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class AnimationManager implements ITickable {

	private final ClientMod client;

	private GunAnimation currentAnimation;
	private GunAnimation nextAnimation;

	private ItemStack lastItemStack = null;
	private ItemStack itemStack = null;

	public AnimationManager(ClientMod client) {
		this.client = client;
	}

	@Override
	public void update() {
		if (client.getPlayer() != null && client.getPlayer().getVanillaEntity() != null) {

			this.lastItemStack = this.itemStack;
			this.itemStack = client.getPlayer().getVanillaEntity().getActiveItemStack();

			if (this.lastItemStack != null && this.itemStack != null && Item
					.getIdFromItem(this.itemStack.getItem()) != Item.getIdFromItem(this.lastItemStack.getItem())) {
				this.cancelCurrentAnimation();
				return;
			}

			if (this.nextAnimation != null) {
				this.currentAnimation = this.nextAnimation;
				this.nextAnimation = null;
			}

			if (this.currentAnimation != null) {
				if (this.itemStack == null) {
					this.cancelCurrentAnimation();
					return;
				}
			}

			if (this.itemStack != null) {
				if (this.currentAnimation != null) {
					this.currentAnimation.update(client, client.getPlayer(), itemStack);
					if (this.currentAnimation != null) {
						this.currentAnimation.animationTicker++;
						if (this.currentAnimation.animationTicker > this.currentAnimation.getLength()) {
							this.currentAnimation.onAnimationStopped(itemStack);
							this.currentAnimation = null;
						}
					}
				}
			}
		}
	}

	public void setNextGunAnimation(GunAnimation animation) {
		this.nextAnimation = animation;
	}

	public void cancelCurrentAnimation() {
		if (this.currentAnimation != null) {
			this.currentAnimation.onAnimationStopped(null);
			this.currentAnimation = null;
		}
	}

	public void cancelAllAnimations() {
		this.cancelCurrentAnimation();
		this.nextAnimation = null;
	}

	public GunAnimation getCurrentAnimation() {
		return this.currentAnimation;
	}

}
