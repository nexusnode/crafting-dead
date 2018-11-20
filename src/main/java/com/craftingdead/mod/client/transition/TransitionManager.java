package com.craftingdead.mod.client.transition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.gui.ExtendedGuiScreen;
import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;
import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TransitionManager {

	private static final Logger LOGGER = LogManager.getLogger();

	private final Minecraft minecraft;

	private boolean enabled;

	private final Transition defaultTransition;

	private FrameBufferObject[] transitionFbos = new FrameBufferObject[2];

	private FrameBufferProxy frameBuffer;

	private long transitionBeginTime = 0L;

	private GuiScreen transitionScreen;

	private boolean previousWasScreen;

	private float progress = 0.0F;

	private int currentFboIndex = 0;

	private int transitionFrames = 2;

	public TransitionManager(Transition defaultTransition, Minecraft minecraft) {
		this.defaultTransition = defaultTransition;
		this.minecraft = minecraft;
		if (OpenGlHelper.isFramebufferEnabled()) {
			try {
				this.frameBuffer = new FrameBufferProxy();
			} catch (NoSuchFieldException | SecurityException e) {
				LOGGER.error("Could not load frame buffer", e);
				return;
			}
			this.transitionFbos[0] = new FrameBufferObject();
			this.transitionFbos[1] = new FrameBufferObject();
			this.enabled = true;
		}
	}

	@SubscribeEvent
	public void onPreRender(DrawScreenEvent.Pre event) {
		GuiScreen screen = event.getGui();
		int mouseX = event.getMouseX(), mouseY = event.getMouseY();
		float partialTicks = event.getRenderPartialTicks();
		Transition transition = screen instanceof ExtendedGuiScreen ? ((ExtendedGuiScreen) screen).getScreenTransition()
				: this.defaultTransition;

		if (!enabled || (minecraft.currentScreen == null && minecraft.gameSettings.keyBindPlayerList.isPressed())) {
			return;
		}

		if (minecraft.world != null) {
			this.transitionFbos[0].dispose();
			this.transitionFbos[1].dispose();
			return;
		}

		if (screen != this.transitionScreen) {
			if (this.transitionFrames > 1) {
				this.currentFboIndex = 1 - this.currentFboIndex;
				this.previousWasScreen = (this.transitionScreen != null);
				this.progress = 0.0f;
				this.transitionBeginTime = Minecraft.getSystemTime();
			}
			this.transitionScreen = screen;
			this.transitionFrames = 0;
		} else {
			++this.transitionFrames;
			if (transition != null) {
				final long deltaTime = Minecraft.getSystemTime() - this.transitionBeginTime;
				final float pct = deltaTime / (transition.getTransitionTime() * 2.0F);
				this.progress = transition.getTransitionType().interpolate(pct);
			}
		}
		try {
			this.drawTransition(mouseX, mouseY, partialTicks, transition);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOGGER.error("A reflection based error occurred while drawing the transition");
		}
		event.setCanceled(true);
	}

	private void drawTransition(int mouseX, int mouseY, float partialTicks, Transition transition)
			throws IllegalArgumentException, IllegalAccessException {
		ScaledResolution resolution = new ScaledResolution(this.minecraft);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();

		FrameBufferObject activeFbo = this.transitionFbos[this.currentFboIndex];
		if (this.minecraft.currentScreen != null && activeFbo != null) {
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

			this.minecraft.getFramebuffer().unbindFramebuffer();
			this.frameBuffer.attach(this.minecraft, activeFbo);
			activeFbo.begin(this.minecraft.displayWidth, this.minecraft.displayHeight);

			this.minecraft.entityRenderer.setupOverlayRendering();
			this.minecraft.currentScreen.drawScreen(mouseX, mouseY, partialTicks);

			activeFbo.end();
			this.frameBuffer.release(this.minecraft);
			this.minecraft.getFramebuffer().bindFramebuffer(true);

			if (this.minecraft.getFramebuffer().useDepth) {
				EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT,
						this.minecraft.getFramebuffer().depthBuffer);
			}

			GL11.glPopAttrib();
		}

		this.minecraft.entityRenderer.setupOverlayRendering();

		if (this.progress < 1.0F && transition != null) {
			FrameBufferObject active = (this.minecraft.currentScreen != null) ? activeFbo : null;
			FrameBufferObject last = this.previousWasScreen ? this.transitionFbos[1 - this.currentFboIndex] : null;
			transition.draw(active, last, width, height, this.progress);
		} else if (this.minecraft.currentScreen != null) {
			activeFbo.draw(0, 0, width, height, 1.0F);
		}
	}
}
