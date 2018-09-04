package com.craftingdead.mod.common.asm.transformers;

import org.lwjgl.LWJGLException;

import com.craftingdead.asm.Callback;
import com.craftingdead.asm.CallbackInjectionTransformer;
import com.craftingdead.mod.client.renderer.loading.LoadingScreen;
import com.craftingdead.mod.client.renderer.loading.LoadingRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;

public class SplashProgressTransformer extends CallbackInjectionTransformer {

	@Override
	protected void addCallbacks() {
		addCallback("net.minecraftforge.fml.client.SplashProgress", "start", "()V",
				new Callback(Callback.CallbackType.REDIRECT, "start", this.getClass().getCanonicalName()));
		addCallback("net.minecraftforge.fml.client.SplashProgress", "finish", "()V",
				new Callback(Callback.CallbackType.REDIRECT, "finish", this.getClass().getCanonicalName()));
		addCallback("net.minecraftforge.fml.client.SplashProgress", "pause", "()V",
				new Callback(Callback.CallbackType.REDIRECT, "pause", this.getClass().getCanonicalName()));
		addCallback("net.minecraftforge.fml.client.SplashProgress", "resume", "()V",
				new Callback(Callback.CallbackType.REDIRECT, "resume", this.getClass().getCanonicalName()));
		addCallback("net.minecraftforge.fml.client.SplashProgress", "drawVanillaScreen",
				"(Lnet/minecraft/client/renderer/texture/TextureManager;)V",
				new Callback(Callback.CallbackType.REDIRECT, "drawVanillaScreen", this.getClass().getCanonicalName()));
	}

	public static void start() throws LWJGLException {
		LoadingRenderer.start(Minecraft.getMinecraft(), new LoadingScreen());
	}

	public static void finish() {
		LoadingRenderer.finish();
	}

	@SuppressWarnings("deprecation")
	public static void pause() {
		LoadingRenderer.pause();
	}

	@SuppressWarnings("deprecation")
	public static void resume() {
		LoadingRenderer.resume();
	}

	public static void drawVanillaScreen(TextureManager renderEngine) throws LWJGLException {
		;
	}

}
