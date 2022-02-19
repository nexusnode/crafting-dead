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

package com.craftingdead.immerse.game.tdm;

import java.util.Optional;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.module.team.Team;
import com.craftingdead.immerse.game.module.team.TeamInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;

public enum TdmTeam implements Team {

  RED("Red", ChatFormatting.RED.getColor(),
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/skins/red_team.png")),
  BLUE("Blue", ChatFormatting.BLUE.getColor(),
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/skins/blue_team.png"));

  private static final EntityDataAccessor<Integer> SCORE =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.INT);

  private final String name;
  private final int colour;
  private final ResourceLocation skin;

  private TdmTeam(String name, int colour, ResourceLocation skin) {
    this.name = name;
    this.colour = colour;
    this.skin = skin;
  }

  public static void incrementScore(TeamInstance<?> team) {
    team.getDataManager().compute(SCORE, score -> ++score);
  }

  public static int getScore(TeamInstance<?> teamInstance) {
    return teamInstance.getDataManager().get(SCORE);
  }

  public static void reset(TeamInstance<?> teamInstance) {
    teamInstance.getDataManager().set(SCORE, 0);
  }

  @Override
  public Optional<ResourceLocation> getSkin() {
    return Optional.of(this.skin);
  }

  @Override
  public void registerDataParameters(SynchedData dataManager) {
    dataManager.register(SCORE, 0);
  }

  @Override
  public void save(TeamInstance<?> teamInstance, CompoundTag nbt) {}

  @Override
  public void load(TeamInstance<?> teamInstance, CompoundTag nbt) {}

  @Override
  public int getColour() {
    return this.colour;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
