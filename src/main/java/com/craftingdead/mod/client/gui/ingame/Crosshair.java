package com.craftingdead.mod.client.gui.ingame;

import net.minecraft.util.ResourceLocation;

public interface Crosshair {

	ResourceLocation getTop();

	ResourceLocation getBottom();

	ResourceLocation getLeft();

	ResourceLocation getRight();

	ResourceLocation getMiddle();

	boolean isStatic();

}
