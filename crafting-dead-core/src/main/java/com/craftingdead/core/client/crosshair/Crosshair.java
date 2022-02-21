/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.crosshair;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public class Crosshair {

  public static final Codec<Crosshair> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          ResourceLocation.CODEC.fieldOf("name").forGetter(Crosshair::getName),
          ResourceLocation.CODEC.fieldOf("top").forGetter(Crosshair::getTop),
          ResourceLocation.CODEC.fieldOf("bottom").forGetter(Crosshair::getBottom),
          ResourceLocation.CODEC.fieldOf("left").forGetter(Crosshair::getLeft),
          ResourceLocation.CODEC.fieldOf("right").forGetter(Crosshair::getRight),
          ResourceLocation.CODEC.fieldOf("middle").forGetter(Crosshair::getMiddle))
      .apply(instance, Crosshair::new));

  private final ResourceLocation name;

  private final ResourceLocation top;
  private final ResourceLocation bottom;
  private final ResourceLocation left;
  private final ResourceLocation right;
  private final ResourceLocation middle;

  public Crosshair(ResourceLocation name, ResourceLocation top,
      ResourceLocation bottom, ResourceLocation left, ResourceLocation right,
      ResourceLocation middle) {
    this.name = name;
    this.top = top;
    this.bottom = bottom;
    this.left = left;
    this.right = right;
    this.middle = middle;
  }

  public ResourceLocation getName() {
    return this.name;
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
