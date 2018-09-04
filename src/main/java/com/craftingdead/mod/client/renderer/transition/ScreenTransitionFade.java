package com.craftingdead.mod.client.renderer.transition;

import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;

public class ScreenTransitionFade extends ScreenTransition {

	@Override
	public void render(final FrameBufferObject active, final FrameBufferObject last, final int width, final int height,
			final float transitionPct) {
		if (last != null) {
			ScreenTransition.drawFBO(last, 0, 0, width, height, 0, 1.0F - transitionPct);
		}
		if (active != null) {
			ScreenTransition.drawFBO(active, 0, 0, width, height, 0, transitionPct);
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
