package com.craftingdead.mod.client.transition;

import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;

import net.minecraft.client.renderer.GlStateManager;

public enum Transition {

	FADE {
		@Override
		public void draw(FrameBufferObject active, FrameBufferObject last, int width, int height, float progress) {
			if (last != null)
				last.draw(0, 0, width, height, 1.0F - progress);
			if (active != null)
				active.draw(0, 0, width, height, progress);
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
		public Type getTransitionType() {
			return Type.SINE;
		}
	},
	GROW {
		protected float scaleAmount = 0.075F;

		@Override
		public void draw(FrameBufferObject active, FrameBufferObject last, int width, int height, float progress) {
			if (last != null) {
				float scaleOffset = progress * this.scaleAmount;
				GlStateManager.pushMatrix();
				GlStateManager.translate(width * -scaleOffset, height * -scaleOffset, 0.0F);
				GlStateManager.scale(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
				last.draw(0, 0, width, height, 1.0F - progress);
				GlStateManager.popMatrix();
			}
			if (active != null) {
				float scaleOffset = (1.0F - progress) * this.scaleAmount;
				GlStateManager.pushMatrix();
				GlStateManager.translate(width * -scaleOffset, height * -scaleOffset, 0.0F);
				GlStateManager.scale(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
				active.draw(0, 0, width, height, progress);
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
		public Type getTransitionType() {
			return Type.SINE;
		}
	};

	public abstract void draw(FrameBufferObject active, FrameBufferObject last, int width, int height,
			float progress);

	public abstract boolean isHighMotion();

	public abstract int getTransitionTime();

	public abstract Type getTransitionType();

	public enum Type {
		LINEAR {
			@Override
			public float interpolate(float pct) {
				return Math.min(1.0f, pct);
			}
		},
		SINE {
			@Override
			public float interpolate(float pct) {
				return (float) Math.min(1.0, Math.sin(3.141592653589793 * Math.min(0.5f, pct)));
			}
		},
		COSINE {

			@Override
			public float interpolate(float pct) {
				return (float) Math.min(1.0, 1.0 - Math.cos(3.141592653589793 * Math.min(0.5f, pct)));
			}
		},
		SMOOTH {
			@Override
			public float interpolate(float pct) {
				return (float) Math.min(1.0, (-Math.cos(6.283185307179586 * Math.min(0.5f, pct)) + 1.0) * 0.5);
			}
		};

		public abstract float interpolate(final float pct);
	}
}
