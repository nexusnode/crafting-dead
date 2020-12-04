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
package com.craftingdead.immerse.game;

import java.lang.reflect.Type;
import com.craftingdead.immerse.server.LogicalServer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryManager;

public interface IGameServer<T extends ITeam> extends IGame<T>, INBTSerializable<CompoundNBT> {

  public static class Deserializer implements JsonDeserializer<IGameServer<?>> {

    private final LogicalServer logicalServer;

    public Deserializer(LogicalServer logicalServer) {
      this.logicalServer = logicalServer;
    }

    @Override
    public IGameServer<?> deserialize(JsonElement json, Type typeOfT,
        JsonDeserializationContext context) throws JsonParseException {
      JsonObject jsonObject = json.getAsJsonObject();
      ResourceLocation gameTypeId = new ResourceLocation(jsonObject.get("gameType").getAsString());
      GameType gameType = RegistryManager.ACTIVE.getRegistry(GameType.class).getValue(gameTypeId);
      if (gameType == null) {
        throw new IllegalStateException(
            "Game type with id '" + gameTypeId.toString() + "' does not exist.");
      }
      return gameType.createGameServer(this.logicalServer, context, jsonObject);
    }
  }
}
