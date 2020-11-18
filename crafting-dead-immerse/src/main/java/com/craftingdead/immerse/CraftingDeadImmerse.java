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
package com.craftingdead.immerse;

import javax.annotation.Nullable;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.server.LogicalServer;
import com.craftingdead.immerse.server.ServerDist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(CraftingDeadImmerse.ID)
public class CraftingDeadImmerse {

  public static final String ID = "craftingdeadimmerse";

  public static final String VERSION;

  public static final String DISPLAY_NAME;

  static {
    VERSION = JarVersionLookupHandler
        .getImplementationVersion(CraftingDeadImmerse.class)
        .orElse("[version]");
    DISPLAY_NAME = JarVersionLookupHandler
        .getImplementationTitle(CraftingDeadImmerse.class)
        .orElse("[display_name]");
  }

  /**
   * Singleton.
   */
  private static CraftingDeadImmerse instance;

  /**
   * Mod distribution.
   */
  private final IModDist modDist;

  @Nullable
  private LogicalServer logicalServer;

  public CraftingDeadImmerse() {
    instance = this;
    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    GameTypes.GAME_TYPES.makeRegistry("game_type", RegistryBuilder::new);
    GameTypes.GAME_TYPES.register(modEventBus);
  }

  @Nullable
  public LogicalServer getLogicalServer() {
    return this.logicalServer;
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public static CraftingDeadImmerse getInstance() {
    return instance;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleServerAboutToStart(FMLServerStartingEvent event) {
    this.logicalServer = this.modDist.createLogicalServer(event.getServer());
    this.logicalServer.init();
    MinecraftForge.EVENT_BUS.register(this.logicalServer);
  }

  @SubscribeEvent
  public void handleServerStopping(FMLServerStoppingEvent event) {
    MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
    this.logicalServer = null;
  }
}
