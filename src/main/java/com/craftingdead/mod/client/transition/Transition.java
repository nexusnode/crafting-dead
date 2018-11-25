package com.craftingdead.mod.client.transition;

import com.craftingdead.mod.client.transition.TransitionManager.TransitionType;

import net.minecraft.client.shader.Framebuffer;

public interface Transition {

	void draw(Framebuffer framebuffer, double width, double height, float progress);

	int getTransitionTime();

	TransitionType getTransitionType();

}
