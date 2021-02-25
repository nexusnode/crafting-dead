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

package com.craftingdead.immerse.game;

import java.util.function.Supplier;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GameType extends ForgeRegistryEntry<GameType> {

  public final static Codec<GameType> CODEC =
      ResourceLocation.CODEC.xmap(registryName -> CraftingDeadImmerse.getInstance()
          .getGameTypeRegistry().getValue(registryName), GameType::getRegistryName);

  private final Codec<? extends IGameServer> gameServerCodec;
  private final Supplier<SafeCallable<IGameClient>> gameClientFactory;

  public GameType(Codec<? extends IGameServer> gameServerCodec,
      Supplier<SafeCallable<IGameClient>> gameClientFactory) {
    this.gameServerCodec = gameServerCodec;
    this.gameClientFactory = gameClientFactory;
  }

  public Codec<? extends IGameServer> getGameServerCodec() {
    return this.gameServerCodec;
  }

  public IGameClient createGameClient() {
    IGameClient gameClient = DistExecutor.safeCallWhenOn(Dist.CLIENT, this.gameClientFactory);
    if (gameClient == null) {
      throw new IllegalStateException("Attempting to create game client on a server!");
    }
    return gameClient;
  }

  public ITextComponent getDisplayName() {
    return Text.translate(Util.makeTranslationKey("game", this.getRegistryName()));
  }
}
