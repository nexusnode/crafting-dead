/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.renderer.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public record GunRendererProperties( 
    ResourceLocation modelLocation,
    Transformation muzzleFlashTransform,
    Transformation aimTransform,
    Transformation scopeAimTransform,
    Transformation sprintingTransform,
    Transformation backTransform,
    Map<ResourceLocation, Transformation> attachmentTransforms,
    Map<ResourceLocation, Transformation> magazineTransforms,
    Map<HandTransform, Pair<Transformation, Transformation>> handTransforms,
    List<Pair<ResourceLocation, Transformation>> ironSights) implements ItemRendererProperties {

  private static final Transformation DEFAULT_SPRINTING_TRANSFORM =
      new Transformation(
          new Vector3f(-0.3F, 0.0F, 0.15F),
          new Quaternion(-20, 40, 0, true),
          new Vector3f(1.0F, 1.0F, 1.0F),
          null);

  public static final Codec<GunRendererProperties> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              ResourceLocation.CODEC
                  .fieldOf("model")
                  .forGetter(GunRendererProperties::modelLocation),
              RenderUtil.TRANSFORMATION_MATRIX_CODEC
                  .fieldOf("muzzle_flash_transform")
                  .forGetter(GunRendererProperties::muzzleFlashTransform),
              RenderUtil.TRANSFORMATION_MATRIX_CODEC
                  .optionalFieldOf("aim_transform", Transformation.identity())
                  .forGetter(GunRendererProperties::aimTransform),
              RenderUtil.TRANSFORMATION_MATRIX_CODEC
                  .optionalFieldOf("scope_aim_transform", Transformation.identity())
                  .forGetter(GunRendererProperties::scopeAimTransform),
              RenderUtil.TRANSFORMATION_MATRIX_CODEC
                  .optionalFieldOf("sprinting_transform", DEFAULT_SPRINTING_TRANSFORM)
                  .forGetter(GunRendererProperties::sprintingTransform),
              RenderUtil.TRANSFORMATION_MATRIX_CODEC
                  .optionalFieldOf("back_transform", Transformation.identity())
                  .forGetter(GunRendererProperties::backTransform),
              Codec.unboundedMap(ResourceLocation.CODEC, RenderUtil.TRANSFORMATION_MATRIX_CODEC)
                  .optionalFieldOf("attachment_transforms", Collections.emptyMap())
                  .forGetter(GunRendererProperties::attachmentTransforms),
              Codec.unboundedMap(ResourceLocation.CODEC, RenderUtil.TRANSFORMATION_MATRIX_CODEC)
                  .optionalFieldOf("magazine_transforms", Collections.emptyMap())
                  .forGetter(GunRendererProperties::magazineTransforms),
              Codec
                  .unboundedMap(HandTransform.CODEC,
                      Codec.mapPair(
                          RenderUtil.TRANSFORMATION_MATRIX_CODEC.optionalFieldOf("right_hand",
                              Transformation.identity()),
                          RenderUtil.TRANSFORMATION_MATRIX_CODEC.optionalFieldOf("left_hand",
                              Transformation.identity()))
                          .codec())
                  .optionalFieldOf("hand_transforms", Collections.emptyMap())
                  .forGetter(GunRendererProperties::handTransforms),
              Codec.compoundList(ResourceLocation.CODEC, RenderUtil.TRANSFORMATION_MATRIX_CODEC)
                  .optionalFieldOf("iron_sights", Collections.emptyList())
                  .forGetter(GunRendererProperties::ironSights))
          .apply(instance, GunRendererProperties::new));

  @Override
  public ItemRendererType<?, ?> getItemRendererType() {
    return ItemRendererTypes.GUN;
  }

  public enum HandTransform implements StringRepresentable {

    HELD_RIGHT_HANDED("held_right_handed"),
    HELD_LEFT_HANDED("held_left_handed"),
    AIMING_RIGHT_HANDED("aiming_right_handed"),
    AIMING_LEFT_HANDED("aiming_left_handed");

    public static final Codec<HandTransform> CODEC =
        StringRepresentable.fromEnum(HandTransform::values, HandTransform::byName);
    private static final Map<String, HandTransform> BY_NAME = Arrays.stream(values())
        .collect(Collectors.toMap(HandTransform::getSerializedName, Function.identity()));

    private final String name;

    private HandTransform(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static HandTransform byName(String name) {
      return BY_NAME.get(name);
    }
  }
}
