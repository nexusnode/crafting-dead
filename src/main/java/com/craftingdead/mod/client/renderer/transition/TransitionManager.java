package com.craftingdead.mod.client.renderer.transition;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferObject;
import com.craftingdead.mod.client.renderer.framebuffer.FrameBufferProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TransitionManager {

	private Minecraft mc;

	private ScreenTransition transition;

	private FrameBufferObject[] transitionFBOs = new FrameBufferObject[2];

	private FrameBufferProxy proxy = new FrameBufferProxy();

	private long transitionBeginTime = 0L;

	private GuiScreen transitionScreen;

	private boolean previousWasScreen;

	private float transitionPct = 0.0F;

	private int currentFBOIndex = 0;

	private int transitionFrames = 2;

	public TransitionManager(ScreenTransition transition) {
		this.transition = transition;
		this.mc = Minecraft.getMinecraft();
		if (OpenGlHelper.framebufferSupported) {
			this.transitionFBOs[0] = new FrameBufferObject();
			this.transitionFBOs[1] = new FrameBufferObject();
		}
	}

	@SubscribeEvent
	public void onPreRender(DrawScreenEvent.Pre event) {
		GuiScreen screen = event.getGui();
		int mouseX = event.getMouseX(), mouseY = event.getMouseY();
		float partialTicks = event.getRenderPartialTicks();

		if (!OpenGlHelper.framebufferSupported
				|| (mc.currentScreen == null && mc.gameSettings.keyBindPlayerList.isPressed())) {
			return;
		}

		if (mc.world != null) {
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
		this.drawTransition(mouseX, mouseY, partialTicks);
		event.setCanceled(true);
	}

	private boolean checkBeginTransition(final GuiScreen currentScreen) {
		if (currentScreen != this.transitionScreen) {
			return true;
		}
		return false;
	}

	private void drawTransition(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution resolution = new ScaledResolution(this.mc);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();

		FrameBufferObject activeFBO = this.transitionFBOs[this.currentFBOIndex];
		if (this.mc.currentScreen != null && activeFBO != null) {
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

			this.mc.getFramebuffer().unbindFramebuffer();
			this.proxy.attach(this.mc, activeFBO);
			activeFBO.begin(this.mc.displayWidth, this.mc.displayHeight);

			this.mc.entityRenderer.setupOverlayRendering();
			this.mc.currentScreen.drawScreen(mouseX, mouseY, partialTicks);

			activeFBO.end();
			this.proxy.release(this.mc);
			this.mc.getFramebuffer().bindFramebuffer(true);

			if (this.mc.getFramebuffer().useDepth) {
				EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT,
						this.mc.getFramebuffer().depthBuffer);
			}

			GL11.glPopAttrib();
		}

		this.mc.entityRenderer.setupOverlayRendering();

		if (this.transitionPct < 1.0f && this.transition != null) {
			FrameBufferObject active = (this.mc.currentScreen != null) ? activeFBO : null;
			FrameBufferObject last = this.previousWasScreen ? this.transitionFBOs[1 - this.currentFBOIndex] : null;
			this.transition.render(active, last, width, height, this.transitionPct);
		} else if (this.mc.currentScreen != null) {
			ScreenTransition.drawFBO(activeFBO, 0, 0, width, height, 0, 1.0f, false);
		}
	}
}
