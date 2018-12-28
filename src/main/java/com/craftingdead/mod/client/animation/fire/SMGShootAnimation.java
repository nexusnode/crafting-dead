package com.craftingdead.mod.client.animation.fire;

import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.animation.GunAnimation;

import net.minecraft.item.ItemStack;

public class SMGShootAnimation implements GunAnimation {

	private int aliveTicks = 0;

	private float rotation1 = 0F;
	private float lastRotation1 = 0F;
	private float maxRotation1 = 2;

	private float trans1 = 0F;
	private float lastTrans1 = 0F;
	private float maxTrans1 = 0.15F;

	private boolean up = true;

	@Override
	public void update() {
		aliveTicks++;
		lastRotation1 = rotation1;
		lastTrans1 = trans1;

		float roation1Speed = 60F;
		float transSpeed = 0.25F;

		if (this.aliveTicks == 2) {
			this.up = false;
		}

		if (up) {
			rotation1 += roation1Speed;
			trans1 += transSpeed;
		} else {
			rotation1 -= roation1Speed;
			trans1 -= transSpeed;
		}

		if (trans1 > maxTrans1) {
			trans1 = maxTrans1;
		}

		if (trans1 < 0) {
			trans1 = 0;
		}

		if (rotation1 > maxRotation1) {
			rotation1 = maxRotation1;
		}

		if (rotation1 < 0) {
			rotation1 = 0;
		}
	}

	@Override
	public void render(ItemStack par1, float par2) {
		float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
		GL11.glTranslatef(-transprogress, 0, 0);

		float rotprogress = lastRotation1 + (rotation1 - lastRotation1) * par2;
		GL11.glRotatef(-rotprogress, 0.0F, 0.0F, 1.0F);
	}

	@Override
	public void onAnimationStopped(ItemStack par1) {
		;
	}

	@Override
	public boolean isFinished() {
		return aliveTicks >= 5;
	}
}
