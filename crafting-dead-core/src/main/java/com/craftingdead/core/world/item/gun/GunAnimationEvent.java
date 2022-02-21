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

package com.craftingdead.core.world.item.gun;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum GunAnimationEvent implements StringRepresentable {

  SHOOT("shoot"), RELOAD("reload"), INSPECT("inspect");

  public static final Codec<GunAnimationEvent> CODEC =
      StringRepresentable.fromEnum(GunAnimationEvent::values, GunAnimationEvent::byName);
  private static final Map<String, GunAnimationEvent> BY_NAME = Arrays.stream(values())
      .collect(Collectors.toMap(GunAnimationEvent::getSerializedName, Function.identity()));

  private final String name;

  private GunAnimationEvent(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static GunAnimationEvent byName(String name) {
    return BY_NAME.get(name);
  }
}
