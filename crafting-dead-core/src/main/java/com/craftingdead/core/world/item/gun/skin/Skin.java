/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.item.gun.skin;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryKeyCodec;

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

  public static final Codec<Supplier<Skin>> CODEC =
      RegistryKeyCodec.create(Skins.SKINS, DIRECT_CODEC);

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
