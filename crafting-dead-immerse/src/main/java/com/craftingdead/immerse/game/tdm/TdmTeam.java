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

package com.craftingdead.immerse.game.tdm;

import java.util.Optional;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.module.team.Team;
import com.craftingdead.immerse.game.module.team.TeamInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public enum TdmTeam implements Team {

  // @formatter:off
  RED("Red", TextFormatting.RED.getColor(),
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/skins/red_team.png")),
  BLUE("Blue", TextFormatting.BLUE.getColor(),
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/skins/blue_team.png"));
  // @formatter:on

  private static final DataParameter<Integer> SCORE =
      new DataParameter<>(0x00, DataSerializers.INT);

  private final String name;
  private final int colour;
  private final ResourceLocation skin;

  private TdmTeam(String name, int colour, ResourceLocation skin) {
    this.name = name;
    this.colour = colour;
    this.skin = skin;
  }

  public static void incrementScore(TeamInstance<?> team) {
    team.getDataManager().getUpdate(SCORE, score -> ++score);
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
  public void save(TeamInstance<?> teamInstance, CompoundNBT nbt) {}

  @Override
  public void load(TeamInstance<?> teamInstance, CompoundNBT nbt) {}

  @Override
  public int getColour() {
    return this.colour;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
