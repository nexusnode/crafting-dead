/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.game.module.team;

import java.util.Optional;
import com.craftingdead.core.network.SynchedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Style;

public interface Team {

  void registerDataParameters(SynchedData dataManager);

  void save(TeamInstance<?> teamInstance, CompoundTag nbt);

  void load(TeamInstance<?> teamInstance, CompoundTag nbt);

  int getColour();

  default Optional<ResourceLocation> getSkin() {
    return Optional.empty();
  }

  default Style getColourStyle() {
    return Style.EMPTY.withColor(TextColor.fromRgb(this.getColour()));
  }

  String getName();

  default Component getDisplayName() {
    return new TextComponent(this.getName())
        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(this.getColour())));
  }
}
