package com.craftingdead.mod.client.renderer.transition;

import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;

import net.minecraft.client.renderer.GlStateManager;

public class ScreenTransitionFadeGrow extends ScreenTransition {

	protected float scaleAmount = 0.075F;

	@Override
	public void render(FrameBufferObject active, FrameBufferObject last, int width, int height, float transitionPct) {
		if (last != null) {
			float scaleOffset = transitionPct * this.scaleAmount;
			GlStateManager.pushMatrix();
			GlStateManager.translate(width * -scaleOffset, height * -scaleOffset, 0.0F);
			GlStateManager.scale(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
			drawFBO(last, 0, 0, width, height, 0, 1.0F - transitionPct);
			GlStateManager.popMatrix();
		}
		if (active != null) {
			float scaleOffset = (1.0F - transitionPct) * this.scaleAmount;
			GlStateManager.pushMatrix();
			GlStateManager.translate(width * -scaleOffset, height * -scaleOffset, 0.0F);
			GlStateManager.scale(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
			drawFBO(active, 0, 0, width, height, 0, transitionPct);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean isHighMotion() {
		return false;
	}

	@Override
	public int getTransitionTime() {
		return 200;
	}

	@Override
	public TransitionType getTransitionType() {
		return TransitionType.Sine;
	}
}
