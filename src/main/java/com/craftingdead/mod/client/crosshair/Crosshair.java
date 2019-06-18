package com.craftingdead.mod.client.crosshair;

import lombok.Data;
import net.minecraft.util.ResourceLocation;

@Data
public class Crosshair {

	private final ResourceLocation name;
	private final boolean isStatic;
	private final ResourceLocation top, bottom, left, right, middle;
}
