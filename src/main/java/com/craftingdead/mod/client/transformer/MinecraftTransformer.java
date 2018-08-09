package com.craftingdead.mod.client.transformer;

import org.lwjgl.LWJGLException;

import com.craftingdead.asm.Callback;
import com.craftingdead.asm.CallbackInjectionTransformer;
import com.craftingdead.mod.client.ClientHooks;

import net.minecraft.client.Minecraft;

public class MinecraftTransformer extends CallbackInjectionTransformer {

	@Override
	protected void addCallbacks() {
		addCallback("net.minecraft.client.Minecraft", "createDisplay", "()V",
				new Callback(Callback.CallbackType.REDIRECT, "createDisplay", this.getClass().getCanonicalName()));
	}

	public static void createDisplay(Minecraft mc) throws LWJGLException {
		ClientHooks.createDisplay(mc);
	}

}
