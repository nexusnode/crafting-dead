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

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.entity.extension.LivingHandler;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.combatslot.CombatSlotType;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.SpawnPoint;
import com.craftingdead.immerse.game.module.ServerModule;
import com.craftingdead.immerse.game.module.shop.ServerShopModule;
import com.craftingdead.immerse.game.module.shop.ShopCategory;
import com.craftingdead.immerse.game.module.shop.ShopItem;
import com.craftingdead.immerse.game.module.team.ServerTeamModule;
import com.craftingdead.immerse.game.module.team.ServerTeamModule.TeamHandler;
import com.craftingdead.immerse.game.module.team.TeamInstance;
import com.craftingdead.immerse.game.tdm.message.TdmServerMessage;
import com.craftingdead.immerse.game.tdm.state.GameStateInstance;
import com.craftingdead.immerse.game.tdm.state.TdmState;
import com.craftingdead.immerse.server.LogicalServer;
import com.craftingdead.immerse.util.state.StateInstance;
import com.craftingdead.immerse.util.state.StateMachine;
import com.craftingdead.immerse.util.state.TimedStateInstance;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent;

public class TdmServer extends TdmGame<ServerModule> implements GameServer, TeamHandler<TdmTeam> {

  public static final Codec<TdmServer> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          Codec.STRING.fieldOf("displayName").forGetter(TdmServer::getDisplayName),
          Codec.INT.optionalFieldOf("maxScore", 100).forGetter(TdmServer::getMaxScore),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("preGameDuration", Duration.ofMinutes(1L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("gameDuration", Duration.ofMinutes(10L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("postGameDuration", Duration.ofSeconds(30L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("buyDuration", Duration.ofSeconds(20L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("spawnProtectionDuration", Duration.ofSeconds(8L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("ghostDuration", Duration.ofSeconds(5L))
              .forGetter(TdmServer::getPreGameDuration),
          SpawnPoint.CODEC.fieldOf("redSpawnPoint").forGetter(TdmServer::getRedSpawnPoint),
          SpawnPoint.CODEC.fieldOf("blueSpawnPoint").forGetter(TdmServer::getBlueSpawnPoint))
      .apply(instance, TdmServer::new));

  private static final ITextComponent NO_SWITCH_TEAM =
      new TranslationTextComponent("message.no_switch_team");

  private final LogicalServer logicalServer = CraftingDeadImmerse.getInstance().getLogicalServer();

  private final StateMachine<TdmState, TdmServer> stateMachine;

  private ServerShopModule shopModule;
  private ServerTeamModule<TdmTeam> teamModule;

  private final int maxScore;
  private final Duration preGameDuration;
  private final Duration gameDuration;
  private final Duration postGameDuration;

  private final Duration buyDuration;
  private final Duration spawnProtectionDuration;
  private final Duration ghostDuration;

  private final SpawnPoint redSpawnPoint;
  private final SpawnPoint blueSpawnPoint;

  /*
   * Keeps track of game state before we were loaded so that we can reset everything afterwards.
   */
  private boolean daylightCycleOld;
  private boolean weatherCycleOld;
  private boolean showDeathMessagesOld;
  private boolean naturalRegenerationOld;
  private boolean immediateRespawnOld;
  private Difficulty oldDifficulty;

  private boolean firstBloodDrawn;
  private boolean finished;

  public TdmServer(String displayName, int maxScore, Duration preGameDuration,
      Duration gameDuration, Duration postGameDuration, Duration buyDuration,
      Duration spawnProtectionDuration, Duration ghostDuration, SpawnPoint redSpawnPoint,
      SpawnPoint blueSpawnPoint) {
    super(displayName);
    this.maxScore = maxScore;
    this.preGameDuration = preGameDuration;
    this.gameDuration = gameDuration;
    this.postGameDuration = postGameDuration;
    this.buyDuration = buyDuration;
    this.spawnProtectionDuration = spawnProtectionDuration;
    this.ghostDuration = ghostDuration;
    this.stateMachine = new StateMachine<>(this, TdmState.values(), this::setGameState);
    this.redSpawnPoint = redSpawnPoint;
    this.blueSpawnPoint = blueSpawnPoint;
  }

  public int getMaxScore() {
    return this.maxScore;
  }

  public Duration getPreGameDuration() {
    return this.preGameDuration;
  }

  public Duration getGameDuration() {
    return this.gameDuration;
  }

  public Duration getPostGameDuration() {
    return this.postGameDuration;
  }

  public Duration getBuyDuration() {
    return this.buyDuration;
  }

  public Duration getSpawnProtectionDuration() {
    return this.spawnProtectionDuration;
  }

  public Duration getGhostDuration() {
    return this.ghostDuration;
  }

  public SpawnPoint getRedSpawnPoint() {
    return this.redSpawnPoint;
  }

  public SpawnPoint getBlueSpawnPoint() {
    return this.blueSpawnPoint;
  }

  public LogicalServer getLogicalServer() {
    return this.logicalServer;
  }

  public MinecraftServer getMinecraftServer() {
    return this.getLogicalServer().getMinecraftServer();
  }

  public void resetBuyTimes() {
    this.getMinecraftServer().getPlayerList().getPlayers()
        .stream()
        .<PlayerExtension<?>>map(PlayerExtension::getExpected)
        .map(player -> (TdmServerPlayerHandler) player
            .getHandlerOrThrow(TdmPlayerHandler.ID))
        .forEach(TdmServerPlayerHandler::resetBuyTime);
  }

  public void resetPlayerData() {
    for (ServerPlayerEntity playerEntity : this.getMinecraftServer().getPlayerList().getPlayers()) {
      this.deletePlayerData(playerEntity.getUUID());
    }
  }

  public void resetTeams() {
    for (TdmTeam team : TdmTeam.values()) {
      TdmTeam.reset(this.getTeamModule().getTeamInstance(team));
    }
  }

  @Override
  public <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {
    if (message instanceof TdmServerMessage) {
      ((TdmServerMessage) message).handle(this, context);
    }
  }

  @Override
  public Optional<SpawnPoint> getSpawnPoint(PlayerExtension<ServerPlayerEntity> player) {
    return this.getTeamModule().getPlayerTeam(player.getEntity().getUUID())
        .map(team -> team == TdmTeam.RED ? this.getRedSpawnPoint() : this.getBlueSpawnPoint());
  }

  @Override
  public boolean disableBlockBurning() {
    return true;
  }

  @Override
  public void tick() {
    if (this.stateMachine.getCurrentState().getState() == TdmState.IDLE
        && this.getMinecraftServer().getPlayerCount() > 0) {
      this.stateMachine.nextState();
    }

    if (this.stateMachine.getCurrentState() instanceof TimedStateInstance) {
      this.setTimerValueSeconds(
          (int) ((TimedStateInstance<?>) this.stateMachine.getCurrentState())
              .getTimeRemainingSeconds());
    } else {
      this.setTimerValueSeconds(0);
    }

    this.finished = this.stateMachine.tick();
  }

  @Override
  public void load() {
    final GameRules gameRules = this.getMinecraftServer().getGameRules();

    GameRules.BooleanValue daylightCycle = gameRules.getRule(GameRules.RULE_DAYLIGHT);
    this.daylightCycleOld = daylightCycle.get();
    daylightCycle.set(false, this.getMinecraftServer());

    GameRules.BooleanValue weatherCycle = gameRules.getRule(GameRules.RULE_WEATHER_CYCLE);
    this.weatherCycleOld = weatherCycle.get();
    weatherCycle.set(false, this.getMinecraftServer());

    BooleanValue showDeathMessages = gameRules.getRule(GameRules.RULE_SHOWDEATHMESSAGES);
    this.showDeathMessagesOld = showDeathMessages.get();
    showDeathMessages.set(false, this.getMinecraftServer());

    BooleanValue naturalRegeneration = gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION);
    this.naturalRegenerationOld = naturalRegeneration.get();
    naturalRegeneration.set(false, this.getMinecraftServer());

    BooleanValue immediateRespawn = gameRules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
    this.immediateRespawnOld = immediateRespawn.get();
    immediateRespawn.set(true, this.getMinecraftServer());

    ServerWorld world = this.getMinecraftServer().getLevel(World.OVERWORLD);
    // Set weather to clear
    world.setWeatherParameters(6000, 0, false, false);
    // Set time to day
    world.setDayTime(1000);

    this.oldDifficulty = this.getMinecraftServer().getWorldData().getDifficulty();
    this.getMinecraftServer().setDifficulty(Difficulty.PEACEFUL, true);

    super.load();
  }

  @Override
  public void unload() {
    final GameRules gameRules = this.getMinecraftServer().getGameRules();

    gameRules.getRule(GameRules.RULE_DAYLIGHT).set(this.daylightCycleOld,
        this.getMinecraftServer());
    gameRules.getRule(GameRules.RULE_WEATHER_CYCLE).set(this.weatherCycleOld,
        this.getMinecraftServer());
    gameRules.getRule(GameRules.RULE_SHOWDEATHMESSAGES).set(this.showDeathMessagesOld,
        this.getMinecraftServer());
    gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(this.naturalRegenerationOld,
        this.getMinecraftServer());
    gameRules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(this.immediateRespawnOld,
        this.getMinecraftServer());

    this.getMinecraftServer().setDifficulty(this.oldDifficulty, true);

    for (ServerPlayerEntity playerEntity : this.getMinecraftServer().getPlayerList().getPlayers()) {
      ((TdmServerPlayerHandler) PlayerExtension.getExpected(playerEntity)
          .getHandlerOrThrow(TdmPlayerHandler.ID)).invalidate();
    }

    super.unload();
  }

  @Override
  public boolean isFinished() {
    return this.finished;
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayerEntity> player) {
    this.getTeamModule().setPlayerTeam(player, null);
    GameUtil.sendGameMessageToAll(
        new TranslationTextComponent("message.joined",
            player.getEntity().getDisplayName().getString()),
        this.getMinecraftServer());
  }

  @Override
  public void removePlayer(PlayerExtension<ServerPlayerEntity> player) {
    this.getTeamModule().setPlayerTeam(player, null);

    this.deletePlayerData(player.getEntity().getUUID());

    GameUtil.sendGameMessageToAll(
        new TranslationTextComponent("message.left",
            player.getEntity().getDisplayName().getString()),
        this.getMinecraftServer());
  }

  @Override
  public boolean persistPlayerData() {
    return false;
  }

  @Override
  public boolean save() {
    return false;
  }

  @Override
  public void registerModules(Consumer<ServerModule> registrar) {
    this.shopModule = new ServerShopModule(ServerShopModule.COMBAT_PURCHASE_HANDLER,
        (int) this.buyDuration.getSeconds());

    this.shopModule.addCategory(new ShopCategory(
        new StringTextComponent("Rifle"),
        new StringTextComponent("Assault rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M4A1.get()),
            new ShopItem(ModItems.AK47.get()),
            new ShopItem(ModItems.FNFAL.get()),
            new ShopItem(ModItems.ACR.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new StringTextComponent("SMG"),
        new StringTextComponent("Sub-machine gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MAC10.get()),
            new ShopItem(ModItems.P90.get()),
            new ShopItem(ModItems.VECTOR.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new StringTextComponent("Heavy"),
        new StringTextComponent("Heavy-based gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MOSSBERG.get()),
            new ShopItem(ModItems.M240B.get()),
            new ShopItem(ModItems.M1GARAND.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new StringTextComponent("Sniper"),
        new StringTextComponent("Sniper rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M107.get()),
            new ShopItem(ModItems.AS50.get()),
            new ShopItem(ModItems.AWP.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new StringTextComponent("Pistol"),
        new StringTextComponent("Side arm and pistol selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M1911.get()),
            new ShopItem(ModItems.G18.get()),
            new ShopItem(ModItems.DESERT_EAGLE.get()),
            new ShopItem(ModItems.P250.get()),
            new ShopItem(ModItems.FN57.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new StringTextComponent("Grenades"),
        new StringTextComponent("Utilities and grenades."),
        ImmutableList.of(
            new ShopItem(ModItems.FLASH_GRENADE.get()),
            new ShopItem(ModItems.DECOY_GRENADE.get()),
            new ShopItem(ModItems.SMOKE_GRENADE.get()),
            new ShopItem(ModItems.FIRE_GRENADE.get()),
            new ShopItem(ModItems.FRAG_GRENADE.get()))));

    registrar.accept(this.shopModule);

    this.teamModule = new ServerTeamModule<>(TdmTeam.class, this);
    registrar.accept(this.teamModule);

    super.registerModules(registrar);
  }

  @Override
  public ServerTeamModule<TdmTeam> getTeamModule() {
    return this.teamModule;
  }

  @Override
  public boolean canChangeTeam(PlayerExtension<ServerPlayerEntity> player,
      @Nullable TeamInstance<TdmTeam> oldTeam,
      @Nullable TeamInstance<TdmTeam> newTeam) {
    if (oldTeam != null && newTeam != null) {
      StateInstance<?> stateInstance = this.stateMachine.getCurrentState();
      final float maxScore = this.getMaxScore() * 0.75F;
      boolean scoresClose =
          (oldTeam != null && TdmTeam.getScore(oldTeam) > maxScore)
              || TdmTeam.getScore(newTeam) > maxScore;
      boolean tooLate = (stateInstance.getState() == TdmState.GAME
          && ((GameStateInstance) stateInstance).getTimeElapsedSeconds() > 240)
          || stateInstance.getState() == TdmState.POST_GAME;

      if (scoresClose || tooLate) {
        player.getEntity().sendMessage(GameUtil.formatMessage(NO_SWITCH_TEAM), Util.NIL_UUID);
        return false;
      }
    }
    return true;
  }

  @Override
  public void teamChanged(PlayerExtension<ServerPlayerEntity> player,
      @Nullable TeamInstance<TdmTeam> oldTeam,
      @Nullable TeamInstance<TdmTeam> newTeam) {
    if (newTeam == null) {
      player.getEntity().inventory.clearContent();
      player.getEntity().setGameMode(GameType.SPECTATOR);
    } else {
      this.shopModule.resetBuyTime(player.getEntity().getUUID());
      player.getEntity().setGameMode(GameType.ADVENTURE);
      this.logicalServer.respawnPlayer((ServerPlayerEntity) player.getEntity(), false);
      GameUtil.sendGameMessageToAll(
          new TranslationTextComponent("message.joined_team",
              player.getEntity().getDisplayName().getString(),
              newTeam.getTeam().getDisplayName().getString()),
          this.getMinecraftServer());
    }
  }

  @SubscribeEvent
  public void handleLivingLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension
        && !event.getLiving().getLevel().isClientSide()) {
      PlayerExtension<?> player = (PlayerExtension<?>) event.getLiving();
      player.registerHandler(TdmPlayerHandler.ID,
          new TdmServerPlayerHandler(this, player));
    }
  }

  @SubscribeEvent
  public void handleLivingDeath(LivingDeathEvent event) {
    if (this.getGameState() == TdmState.GAME
        && event.getSource().getEntity() instanceof ServerPlayerEntity
        && event.getEntityLiving() instanceof ServerPlayerEntity && !this.firstBloodDrawn) {
      GameUtil.sendGameMessageToAll(new TranslationTextComponent("message.first_blood_drawn",
          event.getSource().getEntity().getDisplayName().getString())
              .withStyle(TextFormatting.DARK_RED),
          this.getMinecraftServer());
      this.firstBloodDrawn = true;
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleTriggerPressed(GunEvent.TriggerPressed event) {
    LivingHandler handler =
        event.getLiving().getHandlerOrThrow(TdmPlayerHandler.ID);
    TdmPlayerHandler player = (TdmPlayerHandler) handler;
    player.setRemainingSpawnProtectionSeconds(0);
  }

  @SubscribeEvent
  public void handleEntityJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof ServerPlayerEntity) {
      ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getEntity();
      CombatSlotType.MELEE.addToInventory(ModItems.COMBAT_KNIFE.get().getDefaultInstance(),
          playerEntity.inventory, false);
      this.shopModule.resetBuyTime(event.getEntity().getUUID());
    }
  }
}
