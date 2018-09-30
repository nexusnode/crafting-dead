package com.craftingdead.mod.client.renderer.transition;

import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;

public abstract class ScreenTransition {

	public abstract void render(final FrameBufferObject p0, final FrameBufferObject p1, final int p2, final int p3, final float p4);

	public abstract boolean isHighMotion();

	public abstract int getTransitionTime();

	public abstract TransitionType getTransitionType();

	public static final void drawFBO(final FrameBufferObject fbo, final int x, final int y, final int x2, final int y2, final int z,
			final float alpha) {
		draw(fbo, x, y, x2, y2, z, alpha, true);
	}

	public static final void draw(final FrameBufferObject fbo, final int x, final int y, final int x2, final int y2, final int z,
			final float alpha, final boolean blend) {
		drawFBO(fbo, x, y, x2, y2, z, alpha, blend, 0.0, 0.0, 1.0, 1.0);
	}

	public static void drawFBO(final FrameBufferObject fbo, final double x, final double y, final double x2, final double y2,
			final double z, final float alpha, final boolean blend, final double u, final double v, final double u2,
			final double v2) {

		fbo.draw(x, y, x2, y2, z, alpha, u, v, u2, v2);

	}

	public enum TransitionType {
		Linear, Sine, Cosine, Smooth;

		public float interpolate(final float pct) {
			if (this == TransitionType.Sine) {
				return (float) Math.min(1.0, Math.sin(3.141592653589793 * Math.min(0.5f, pct)));
			}
			if (this == TransitionType.Cosine) {
				return (float) Math.min(1.0, 1.0 - Math.cos(3.141592653589793 * Math.min(0.5f, pct)));
			}
			if (this == TransitionType.Smooth) {
				return (float) Math.min(1.0, (-Math.cos(6.283185307179586 * Math.min(0.5f, pct)) + 1.0) * 0.5);
			}
			return Math.min(1.0f, pct);
		}
	}
}
