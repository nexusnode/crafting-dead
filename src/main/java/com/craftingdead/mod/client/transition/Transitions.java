package com.craftingdead.mod.client.transition;

import com.craftingdead.mod.client.renderer.Graphics;
import com.craftingdead.mod.client.transition.TransitionManager.TransitionType;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public enum Transitions implements Transition {

	FADE {
		@Override
		public void draw(Framebuffer active, double width, double height, float progress) {
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, progress);
			active.bindFramebufferTexture();
			Graphics.drawTexturedRectangle(0, 0, width, height, 0.0D, 0.0D, 1.0D, 1.0D);
			GlStateManager.disableBlend();
		}

		@Override
		public int getTransitionTime() {
			return 200;
		}

		@Override
		public TransitionType getTransitionType() {
			return TransitionType.SINE;
		}
	},
	FADE_GROW {

		private static final float SCALE = 0.075F;

		@Override
		public void draw(Framebuffer active, double width, double height, float progress) {
			float scaleOffset = (1.0F - progress) * SCALE;
			GlStateManager.translate(width * -scaleOffset, height * -scaleOffset, 0.0F);
			GlStateManager.scale(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, progress);
			active.bindFramebufferTexture();
			Graphics.drawTexturedRectangle(0, 0, width, height, 0.0D, 0.0D, 1.0D, 1.0D);
			GlStateManager.disableBlend();
		}

		@Override
		public int getTransitionTime() {
			return 200;
		}

		@Override
		public TransitionType getTransitionType() {
			return TransitionType.SINE;
		}
	};

}
