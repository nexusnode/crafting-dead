package com.craftingdead.mod.client.renderer.framebuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

public class FrameBufferProxy extends Framebuffer {

	private Framebuffer oldFramebuffer;
	private FrameBufferObject fbo;

	public FrameBufferProxy() {
		super(0, 0, false);
	}

	public void attach(Minecraft mc, FrameBufferObject fbo) {
		this.fbo = fbo;
		this.oldFramebuffer = mc.getFramebuffer();
		mc.framebuffer = this;
	}

	public void release(Minecraft mc) {
		mc.framebuffer = this.oldFramebuffer;
	}

	@Override
	public void createBindFramebuffer(int width, int height) {
	}

	@Override
	public void bindFramebuffer(boolean p_147610_1_) {
		this.fbo.bind();
	}

	@Override
	public void unbindFramebuffer() {
		this.fbo.end();
	}
}
