package com.craftingdead.mod.client.transition;

import com.craftingdead.mod.client.gui.ExtendedGuiScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class TransitionManager {
	/**
	 * {@link Minecraft} instance
	 */
	private final Minecraft minecraft;
	/**
	 * The default {@link Transitions} to use if no others are available
	 */
	private final Transition defaultTransition;
	/**
	 * If screen transitions are enabled
	 */
	private boolean enabled;
	/**
	 * The {@link Framebuffer} used to draw the transition to
	 */
	private Framebuffer transitionFramebuffer;
	/**
	 * The last recorded {@link GuiScreen} - used to check if the current screen has
	 * changed
	 */
	private GuiScreen lastScreen;
	/**
	 * The time at which the transition began - used to calculate the delta time
	 */
	private long transitionBeginTime;
	/**
	 * The progress of the current transition - ranges from 0.0F to 1.0F
	 */
	private float transitionProgress;

	public TransitionManager(Minecraft minecraft, Transition defaultTransition) {
		this.minecraft = minecraft;
		this.defaultTransition = defaultTransition;
		this.enabled = true;
	}

	public boolean checkDrawTransition(int mouseX, int mouseY, float partialTicks, GuiScreen screen) {
		Transition transition = screen instanceof ExtendedGuiScreen ? ((ExtendedGuiScreen) screen).getScreenTransition()
				: this.defaultTransition;

		// Transitions cause issues when in a world
		if (!this.enabled || this.minecraft.world != null)
			return false;

		if (screen != this.lastScreen) {
			this.transitionProgress = 0.0F;
			this.transitionBeginTime = Minecraft.getSystemTime();
			this.transitionFramebuffer = new Framebuffer(this.minecraft.displayWidth, this.minecraft.displayHeight,
					true);
			this.transitionFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
			this.lastScreen = screen;
		} else {
			long deltaTime = Minecraft.getSystemTime() - this.transitionBeginTime;
			float percentComplete = deltaTime / (transition.getTransitionTime() * 2.0F);
			this.transitionProgress = transition.getTransitionType().interpolate(percentComplete);
		}

		if (this.transitionProgress < 1.0F) {
			this.drawTransition(mouseX, mouseY, partialTicks, screen, transition);
			return true;

		}

		return false;
	}

	private void drawTransition(int mouseX, int mouseY, float partialTicks, GuiScreen screen, Transition transition) {
		GlStateManager.pushMatrix();
		{
			this.minecraft.getFramebuffer().unbindFramebuffer();

			this.transitionFramebuffer.framebufferClear();
			this.transitionFramebuffer.bindFramebuffer(false);
			GlStateManager.pushMatrix();
			{
				screen.drawScreen(mouseX, mouseY, partialTicks);
			}
			GlStateManager.popMatrix();
			this.transitionFramebuffer.unbindFramebuffer();

			this.minecraft.getFramebuffer().bindFramebuffer(false);
		}
		GlStateManager.popMatrix();

		ScaledResolution resolution = new ScaledResolution(this.minecraft);
		double width = resolution.getScaledWidth_double();
		double height = resolution.getScaledHeight_double();

		GlStateManager.pushMatrix();
		{
			transition.draw(this.transitionFramebuffer, width, height, this.transitionProgress);
		}
		GlStateManager.popMatrix();

	}

	public static enum TransitionType {
		LINEAR {
			@Override
			public float interpolate(float percentComplete) {
				return Math.min(1.0F, percentComplete);
			}
		},
		SINE {
			@Override
			public float interpolate(float percentComplete) {
				return (float) Math.min(1.0F, Math.sin(Math.PI * Math.min(0.5f, percentComplete)));
			}
		},
		COSINE {
			@Override
			public float interpolate(float percentComplete) {
				return (float) Math.min(1.0F, 1.0 - Math.cos(Math.PI * Math.min(0.5F, percentComplete)));
			}
		},
		SMOOTH {
			@Override
			public float interpolate(float percentComplete) {
				return (float) Math.min(1.0, (-Math.cos(Math.PI * 2 * Math.min(0.5F, percentComplete)) + 1.0) * 0.5);
			}
		};

		abstract float interpolate(float percentComplete);
	}
}
