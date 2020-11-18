/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.server;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.IGameServer;
import com.craftingdead.immerse.network.login.SetupGameMessage;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LogicalServer extends WorldSavedData {

  private final MinecraftServer minecraftServer;

  private IGameServer<?> gameServer;

  public LogicalServer(MinecraftServer minecraftServer) {
    super(CraftingDead.ID);
    this.minecraftServer = minecraftServer;
  }

  public List<Pair<String, SetupGameMessage>> generateSetupGameMessage(boolean isLocal) {
    return Collections.singletonList(Pair.of(SetupGameMessage.class.getName(),
        new SetupGameMessage(this.gameServer.getGameType())));
  }

  public void init() {
    this.minecraftServer.getWorld(DimensionType.OVERWORLD).getSavedData().getOrCreate(() -> this,
        CraftingDead.ID);
    // Default to survival
    try {
      this.gameServer = GameTypes.SURVIVAL.get().createGameServer(this);
    } catch (Exception e) {
      throw new ReportedException(CrashReport.makeCrashReport(e, "Exception loading game"));
    }
  }

  public MinecraftServer getMinecraftServer() {
    return this.minecraftServer;
  }

  public IGameServer<?> getGameServer() {
    return this.gameServer;
  }

  @Override
  public void read(CompoundNBT nbt) {
    if (nbt.contains("gameType", Constants.NBT.TAG_STRING)) {
      GameType gameType = GameRegistry.findRegistry(GameType.class)
          .getValue(new ResourceLocation(nbt.getString("gameType")));
      if (gameType != null) {
        try {
          this.gameServer = gameType.createGameServer(this);
        } catch (Exception e) {
          CrashReport crashReport = CrashReport.makeCrashReport(e, "Exception loading game");
          crashReport.makeCategory("Game").addDetail("Type", gameType.getRegistryName());
          throw new ReportedException(crashReport);
        }
        this.gameServer.deserializeNBT(nbt.getCompound("game"));
      }
    }
  }

  @Override
  public CompoundNBT write(CompoundNBT nbt) {
    nbt.putString("gameType", this.gameServer.getGameType().getRegistryName().toString());
    nbt.put("game", this.gameServer.serializeNBT());
    return nbt;
  }
}
