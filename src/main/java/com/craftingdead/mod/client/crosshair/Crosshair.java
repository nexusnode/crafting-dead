package com.craftingdead.mod.client.crosshair;

import net.minecraft.util.ResourceLocation;

public class Crosshair {

	private final ResourceLocation name;
	private final boolean isStatic;
	private final ResourceLocation top, bottom, left, right, middle;

	public Crosshair(ResourceLocation name, boolean isStatic, ResourceLocation top, ResourceLocation bottom,
			ResourceLocation left, ResourceLocation right, ResourceLocation middle) {
		this.name = name;
		this.isStatic = isStatic;
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.middle = middle;
	}

	public ResourceLocation getName() {
		return this.name;
	}

	public boolean isStatic() {
		return this.isStatic;
	}

	public ResourceLocation getTop() {
		return this.top;
	}

	public ResourceLocation getBottom() {
		return this.bottom;
	}

	public ResourceLocation getLeft() {
		return this.left;
	}

	public ResourceLocation getRight() {
		return this.right;
	}

	public ResourceLocation getMiddle() {
		return this.middle;
	}

}
