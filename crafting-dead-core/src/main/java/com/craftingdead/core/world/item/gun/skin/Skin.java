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

package com.craftingdead.core.world.item.gun.skin;

import java.util.Collections;
import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;

public class Skin {

  public static final Codec<Skin> DIRECT_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          ResourceLocation.CODEC
              .fieldOf("name")
              .forGetter(Skin::getName),
          ResourceLocation.CODEC
              .listOf()
              .fieldOf("acceptedGuns")
              .forGetter(Skin::getAcceptedGuns))
          .apply(instance, Skin::new));

  public static final Codec<Holder<Skin>> CODEC =
      RegistryFileCodec.create(Skins.SKINS, DIRECT_CODEC);

  private final ResourceLocation name;
  private final List<ResourceLocation> acceptedGuns;

  public Skin(ResourceLocation name, List<ResourceLocation> acceptedGuns) {
    this.name = name;
    this.acceptedGuns = acceptedGuns;
  }

  public ResourceLocation getName() {
    return this.name;
  }

  public List<ResourceLocation> getAcceptedGuns() {
    return Collections.unmodifiableList(this.acceptedGuns);
  }

  public ResourceLocation getTextureLocation(ResourceLocation gunName) {
    return new ResourceLocation(this.getName().getNamespace(),
        "gun/" + gunName.getPath() + "_" + this.getName().getPath());
  }
}
