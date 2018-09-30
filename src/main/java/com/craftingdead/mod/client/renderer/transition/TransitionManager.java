package com.craftingdead.mod.client.renderer.transition;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;
import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferProxy;
import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TransitionManager {

	private Minecraft minecraft;

	private boolean enabled;

	private ScreenTransition transition;

	private FrameBufferObject[] transitionFBOs = new FrameBufferObject[2];

	private FrameBufferProxy framebuffer;

	private long transitionBeginTime = 0L;

	private GuiScreen transitionScreen;

	private boolean previousWasScreen;

	private float transitionPct = 0.0F;

	private int currentFBOIndex = 0;

	private int transitionFrames = 2;

	public TransitionManager(ScreenTransition transition, Minecraft minecraft) {
		this.transition = transition;
		this.minecraft = minecraft;
		if (OpenGlHelper.framebufferSupported) {
			try {
				this.framebuffer = new FrameBufferProxy();
			} catch (NoSuchFieldException | SecurityException e) {
				CraftingDead.LOGGER.error("Could not load framebuffer", e);
				return;
			}
			this.transitionFBOs[0] = new FrameBufferObject();
			this.transitionFBOs[1] = new FrameBufferObject();
			this.enabled = true;
		}
	}

	@SubscribeEvent
	public void onPreRender(DrawScreenEvent.Pre event) {
		GuiScreen screen = event.getGui();
		int mouseX = event.getMouseX(), mouseY = event.getMouseY();
		float partialTicks = event.getRenderPartialTicks();

		if (!enabled || (minecraft.currentScreen == null && minecraft.gameSettings.keyBindPlayerList.isPressed())) {
			return;
		}

		if (minecraft.world != null) {
			this.transitionFBOs[0].dispose();
			this.transitionFBOs[1].dispose();
			return;
		}

		if (this.checkBeginTransition(screen)) {
			if (this.transitionFrames > 1) {
				this.currentFBOIndex = 1 - this.currentFBOIndex;
				this.previousWasScreen = (this.transitionScreen != null);
				this.transitionPct = 0.0f;
				this.transitionBeginTime = Minecraft.getSystemTime();
			}
			this.transitionScreen = screen;
			this.transitionFrames = 0;
		} else {
			++this.transitionFrames;
			if (this.transition != null) {
				final long deltaTime = Minecraft.getSystemTime() - this.transitionBeginTime;
				final float pct = deltaTime / (this.transition.getTransitionTime() * 2.0F);
				this.transitionPct = this.transition.getTransitionType().interpolate(pct);
			}
		}
		try {
			this.drawTransition(mouseX, mouseY, partialTicks);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CraftingDead.LOGGER.error("A reflection based error occurred while drawing the transition");
		}
		event.setCanceled(true);
	}

	private boolean checkBeginTransition(final GuiScreen currentScreen) {
		if (currentScreen != this.transitionScreen) {
			return true;
		}
		return false;
	}

	private void drawTransition(int mouseX, int mouseY, float partialTicks)
			throws IllegalArgumentException, IllegalAccessException {
		ScaledResolution resolution = new ScaledResolution(this.minecraft);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();

		FrameBufferObject activeFbo = this.transitionFBOs[this.currentFBOIndex];
		if (this.minecraft.currentScreen != null && activeFbo != null) {
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

			this.minecraft.getFramebuffer().unbindFramebuffer();
			this.framebuffer.attach(this.minecraft, activeFbo);
			activeFbo.begin(this.minecraft.displayWidth, this.minecraft.displayHeight);

			this.minecraft.entityRenderer.setupOverlayRendering();
			this.minecraft.currentScreen.drawScreen(mouseX, mouseY, partialTicks);

			activeFbo.end();
			this.framebuffer.release(this.minecraft);
			this.minecraft.getFramebuffer().bindFramebuffer(true);

			if (this.minecraft.getFramebuffer().useDepth) {
				EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT,
						this.minecraft.getFramebuffer().depthBuffer);
			}

			GL11.glPopAttrib();
		}

		this.minecraft.entityRenderer.setupOverlayRendering();

		if (this.transitionPct < 1.0f && this.transition != null) {
			FrameBufferObject active = (this.minecraft.currentScreen != null) ? activeFbo : null;
			FrameBufferObject last = this.previousWasScreen ? this.transitionFBOs[1 - this.currentFBOIndex] : null;
			this.transition.render(active, last, width, height, this.transitionPct);
		} else if (this.minecraft.currentScreen != null) {
			ScreenTransition.draw(activeFbo, 0, 0, width, height, 0, 1.0f, false);
		}
	}
}
