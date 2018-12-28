package com.craftingdead.mod.client.animation.fire;

import com.craftingdead.mod.client.animation.GunAnimation;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class PistolShootAnimation implements GunAnimation {

	private int aliveTicks = 0;

	private float rotation = 0F;
	private float lastRotation = 0F;
	private float maxRotation = 15;

	private float translation = 0F;
	private float lastTranslation = 0F;
	private float maxTranslation = 0.1F;

	private boolean up = true;

	@Override
	public void update() {
		aliveTicks++;
		lastRotation = rotation;
		lastTranslation = translation;

		float roationSpeed = 60F;
		float translationSpeed = 0.3F;

		if (this.aliveTicks == 5) {
			this.up = false;
		}

		if (up) {
			rotation += roationSpeed;
			translation += translationSpeed;
		} else {
			rotation -= roationSpeed;
			translation -= translationSpeed;
		}

		if (translation > maxTranslation) {
			translation = maxTranslation;
		}

		if (translation < 0) {
			translation = 0;
		}

		if (rotation > maxRotation) {
			rotation = maxRotation;
		}

		if (rotation < 0) {
			rotation = 0;
		}
	}

	@Override
	public void render(ItemStack itemStack, float partialTicks) {
		float transprogress = lastTranslation + (translation - lastTranslation) * partialTicks;
		GlStateManager.translate(0, 0, transprogress);

		float rotprogress = lastRotation + (rotation - lastRotation) * partialTicks;
		GlStateManager.rotate(rotprogress, 1.0F, 0.0F, 0.0F);
	}

	@Override
	public void onAnimationStopped(ItemStack itemStack) {
		;
	}

	@Override
	public boolean isFinished() {
		return aliveTicks >= 10;
	}
}
