package com.craftingdead.mod.client.crosshair;

import lombok.Data;
import net.minecraft.util.ResourceLocation;

@Data
public class Crosshair {

  private final ResourceLocation name;

  private final boolean isStatic;

  private final ResourceLocation top;
  private final ResourceLocation bottom;
  private final ResourceLocation left;
  private final ResourceLocation right;
  private final ResourceLocation middle;
}
