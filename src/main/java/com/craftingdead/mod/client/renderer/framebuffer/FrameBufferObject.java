package com.craftingdead.mod.client.renderer.framebuffer;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.craftingdead.mod.client.renderer.RenderHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class FrameBufferObject {

	private boolean supported;
	private boolean useARB;
	private boolean created;
	private boolean active;
	private int depthBuffer;
	private int frameBuffer;
	private DynamicTexture texture;
	private int width;
	private int height;

	public FrameBufferObject() {
		supported = OpenGlHelper.framebufferSupported;
		useARB = GLContext.getCapabilities().GL_ARB_framebuffer_object;
	}

	public void begin(final int width, final int height) {
		if (!supported) {
			return;
		} else if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Attempted to create an FBO with zero or negative size");
		} else if (this.created && (width != this.width || height != this.height)) {
			this.dispose();
		}

		if (!this.created) {
			this.created = true;
			this.width = width;
			this.height = height;
			final BufferedImage textureImage = new BufferedImage(this.width, this.height, 1);
			this.texture = new DynamicTexture(textureImage);
			if (useARB) {
				this.frameBuffer = ARBFramebufferObject.glGenFramebuffers();
				this.depthBuffer = ARBFramebufferObject.glGenRenderbuffers();
				ARBFramebufferObject.glBindFramebuffer(36160, this.frameBuffer);
				ARBFramebufferObject.glFramebufferTexture2D(36160, 36064, 3553, this.texture.getGlTextureId(), 0);
				ARBFramebufferObject.glBindRenderbuffer(36161, this.depthBuffer);
				ARBFramebufferObject.glRenderbufferStorage(36161, 33190, this.width, this.height);
				ARBFramebufferObject.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthBuffer);
				ARBFramebufferObject.glBindFramebuffer(36160, 0);
				ARBFramebufferObject.glBindRenderbuffer(36161, 0);
			} else {
				this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
				this.depthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
				EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
				EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, this.texture.getGlTextureId(), 0);
				EXTFramebufferObject.glBindRenderbufferEXT(36161, this.depthBuffer);
				EXTFramebufferObject.glRenderbufferStorageEXT(36161, 33190, this.width, this.height);
				EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, this.depthBuffer);
				EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
				EXTFramebufferObject.glBindRenderbufferEXT(36161, 0);
			}
		}
		this.bind();
	}

	public void bind() {
		if (supported && this.created && this.bindAndRunChecks()) {
			this.active = true;
		}
	}

	public void end() {
		if (supported && this.active) {
			if (useARB) {
				ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
				ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, 0);
			} else {
				EXTFramebufferObject.glBindFramebufferEXT(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
				EXTFramebufferObject.glBindRenderbufferEXT(ARBFramebufferObject.GL_RENDERBUFFER, 0);
			}
			this.active = false;
		}
	}

	public void dispose() {
		if (!supported) {
			return;
		}
		this.end();
		if (this.texture != null) {
			GL11.glDeleteTextures(this.texture.getGlTextureId());
		}
		if (useARB) {
			ARBFramebufferObject.glDeleteRenderbuffers(this.depthBuffer);
			ARBFramebufferObject.glDeleteFramebuffers(this.frameBuffer);
		} else {
			EXTFramebufferObject.glDeleteRenderbuffersEXT(this.depthBuffer);
			EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer);
		}
		this.depthBuffer = 0;
		this.texture = null;
		this.frameBuffer = 0;
		this.created = false;
	}

	private boolean bindAndRunChecks() {
		if (useARB) {
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, this.frameBuffer);
			ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, this.depthBuffer);
		} else {
			EXTFramebufferObject.glBindFramebufferEXT(ARBFramebufferObject.GL_FRAMEBUFFER, this.frameBuffer);
			EXTFramebufferObject.glBindRenderbufferEXT(ARBFramebufferObject.GL_RENDERBUFFER, this.depthBuffer);
		}

		final int frameBufferStatus = useARB
				? ARBFramebufferObject.glCheckFramebufferStatus(ARBFramebufferObject.GL_FRAMEBUFFER)
				: EXTFramebufferObject.glCheckFramebufferStatusEXT(ARBFramebufferObject.GL_FRAMEBUFFER);

		if (frameBufferStatus == ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE) {
			return true;
		} else {
			return false;
		}
	}

	public void draw(double x, double y, double width, double height, float alpha) {
		if (this.supported && this.created) {
			GlStateManager.bindTexture(this.texture.getGlTextureId());
			RenderHelper.drawTexture(x, y, width, height, alpha, 0.0D, 0.0D, 1.0D, 1.0D);
		}
	}

}
