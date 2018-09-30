package com.craftingdead.mod.client.renderer.framebuffer;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

public class FrameBufferProxy extends Framebuffer {

	private static final String FRAMEBUFFER_FIELD_NAME = "framebuffer";

	private Field framebuffer;

	private Framebuffer oldFramebuffer;
	private FrameBufferObject fbo;

	public FrameBufferProxy() throws NoSuchFieldException, SecurityException {
		super(0, 0, false);
		this.framebuffer = Minecraft.class.getDeclaredField(FRAMEBUFFER_FIELD_NAME);
		this.framebuffer.setAccessible(true);
	}

	public void attach(Minecraft mc, FrameBufferObject fbo) throws IllegalArgumentException, IllegalAccessException {
		this.fbo = fbo;
		this.oldFramebuffer = mc.getFramebuffer();
		this.framebuffer.set(mc, this);
	}

	public void release(Minecraft mc) throws IllegalArgumentException, IllegalAccessException {
		this.framebuffer.set(mc, oldFramebuffer);
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
