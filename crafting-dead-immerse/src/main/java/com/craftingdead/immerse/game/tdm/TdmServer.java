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

package com.craftingdead.immerse.game.tdm;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.LogicalServer;
import com.craftingdead.immerse.game.PlayerRemovalReason;
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
import com.craftingdead.immerse.util.state.StateMachine;
import com.craftingdead.immerse.util.state.TimedStateInstance;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

public class TdmServer extends TdmGame implements GameServer, TeamHandler<TdmTeam> {

  public static final Codec<TdmServer> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          Codec.STRING.fieldOf("display_name").forGetter(TdmServer::getDisplayName),
          Codec.INT.optionalFieldOf("maxScore", 100).forGetter(TdmServer::getMaxScore),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("pre_game_duration", Duration.ofMinutes(1L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("game_duration", Duration.ofMinutes(10L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("post_game_duration", Duration.ofSeconds(30L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("buy_duration", Duration.ofSeconds(20L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("spawn_protection_duration", Duration.ofSeconds(8L))
              .forGetter(TdmServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("ghost_duration", Duration.ofSeconds(5L))
              .forGetter(TdmServer::getPreGameDuration),
          GlobalPos.CODEC.fieldOf("red_spawn_point").forGetter(TdmServer::getRedSpawnPoint),
          GlobalPos.CODEC.fieldOf("blue_spawn_point").forGetter(TdmServer::getBlueSpawnPoint))
      .apply(instance, TdmServer::new));

  private static final Component NO_SWITCH_TEAM =
      new TranslatableComponent("message.no_switch_team");

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

  private final GlobalPos redSpawnPoint;
  private final GlobalPos blueSpawnPoint;

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
      Duration spawnProtectionDuration, Duration ghostDuration, GlobalPos redSpawnPoint,
      GlobalPos blueSpawnPoint) {
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

  public GlobalPos getRedSpawnPoint() {
    return this.redSpawnPoint;
  }

  public GlobalPos getBlueSpawnPoint() {
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
        .map(PlayerExtension::getOrThrow)
        .map(player -> (TdmServerPlayerHandler) player.getHandlerOrThrow(TdmPlayerHandler.TYPE))
        .forEach(TdmServerPlayerHandler::resetBuyTime);
  }

  public void resetPlayerData() {
    for (ServerPlayer playerEntity : this.getMinecraftServer().getPlayerList().getPlayers()) {
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
  public Optional<GlobalPos> getSpawnPoint(PlayerExtension<ServerPlayer> player) {
    return this.getTeamModule().getPlayerTeam(player.getEntity().getUUID())
        .map(team -> team == TdmTeam.RED ? this.getRedSpawnPoint() : this.getBlueSpawnPoint());
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
    var gameRules = this.getMinecraftServer().getGameRules();

    var daylightCycle = gameRules.getRule(GameRules.RULE_DAYLIGHT);
    this.daylightCycleOld = daylightCycle.get();
    daylightCycle.set(false, this.getMinecraftServer());

    var weatherCycle = gameRules.getRule(GameRules.RULE_WEATHER_CYCLE);
    this.weatherCycleOld = weatherCycle.get();
    weatherCycle.set(false, this.getMinecraftServer());

    var showDeathMessages = gameRules.getRule(GameRules.RULE_SHOWDEATHMESSAGES);
    this.showDeathMessagesOld = showDeathMessages.get();
    showDeathMessages.set(false, this.getMinecraftServer());

    var naturalRegeneration = gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION);
    this.naturalRegenerationOld = naturalRegeneration.get();
    naturalRegeneration.set(false, this.getMinecraftServer());

    var immediateRespawn = gameRules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
    this.immediateRespawnOld = immediateRespawn.get();
    immediateRespawn.set(true, this.getMinecraftServer());

    var level = this.getMinecraftServer().getLevel(Level.OVERWORLD);
    // Set weather to clear
    level.setWeatherParameters(6000, 0, false, false);
    // Set time to day
    level.setDayTime(1000);

    this.oldDifficulty = this.getMinecraftServer().getWorldData().getDifficulty();
    this.getMinecraftServer().setDifficulty(Difficulty.PEACEFUL, true);

    super.load();
  }

  @Override
  public void unload() {
    var gameRules = this.getMinecraftServer().getGameRules();

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

    for (var playerEntity : this.getMinecraftServer().getPlayerList().getPlayers()) {
      ((TdmServerPlayerHandler) PlayerExtension.getOrThrow(playerEntity)
          .getHandlerOrThrow(TdmPlayerHandler.TYPE)).invalidate();
    }

    super.unload();
  }

  @Override
  public boolean isFinished() {
    return this.finished;
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayer> player) {
    player.registerHandler(TdmPlayerHandler.TYPE,
        new TdmServerPlayerHandler(this, player));
    this.getTeamModule().setPlayerTeam(player, null);
    GameUtil.sendGameMessageToAll(
        new TranslatableComponent("message.joined", player.getEntity().getDisplayName())
            .withStyle(ChatFormatting.WHITE),
        this.getMinecraftServer());
  }

  @Override
  public void removePlayer(PlayerExtension<ServerPlayer> player, PlayerRemovalReason reason) {
    player.removeHandler(TdmPlayerHandler.TYPE);

    this.getTeamModule().setPlayerTeam(player, null);

    this.deletePlayerData(player.getEntity().getUUID());

    GameUtil.sendGameMessageToAll(
        new TranslatableComponent("message.left", player.getEntity().getDisplayName())
            .withStyle(ChatFormatting.WHITE),
        this.getMinecraftServer());
  }

  @Override
  public boolean persistPlayerData() {
    return false;
  }

  @Override
  public boolean persistGameData() {
    return false;
  }

  @Override
  public void registerServerModules(Consumer<ServerModule> registrar) {
    this.shopModule = new ServerShopModule(ServerShopModule.COMBAT_PURCHASE_HANDLER,
        (int) this.buyDuration.getSeconds());

    this.shopModule.addCategory(new ShopCategory(
        new TextComponent("Rifle"),
        new TextComponent("Assault rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M4A1.get()),
            new ShopItem(ModItems.AK47.get()),
            new ShopItem(ModItems.FNFAL.get()),
            new ShopItem(ModItems.ACR.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new TextComponent("SMG"),
        new TextComponent("Sub-machine gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MAC10.get()),
            new ShopItem(ModItems.P90.get()),
            new ShopItem(ModItems.VECTOR.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new TextComponent("Heavy"),
        new TextComponent("Heavy-based gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MOSSBERG.get()),
            new ShopItem(ModItems.M240B.get()),
            new ShopItem(ModItems.M1GARAND.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new TextComponent("Sniper"),
        new TextComponent("Sniper rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M107.get()),
            new ShopItem(ModItems.AS50.get()),
            new ShopItem(ModItems.AWP.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new TextComponent("Pistol"),
        new TextComponent("Side arm and pistol selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M1911.get()),
            new ShopItem(ModItems.G18.get()),
            new ShopItem(ModItems.DESERT_EAGLE.get()),
            new ShopItem(ModItems.P250.get()),
            new ShopItem(ModItems.FN57.get()))));

    this.shopModule.addCategory(new ShopCategory(
        new TextComponent("Grenades"),
        new TextComponent("Utilities and grenades."),
        ImmutableList.of(
            new ShopItem(ModItems.FLASH_GRENADE.get()),
            new ShopItem(ModItems.DECOY_GRENADE.get()),
            new ShopItem(ModItems.SMOKE_GRENADE.get()),
            new ShopItem(ModItems.FIRE_GRENADE.get()),
            new ShopItem(ModItems.FRAG_GRENADE.get()))));

    registrar.accept(this.shopModule);

    this.teamModule = new ServerTeamModule<>(TdmTeam.class, this);
    registrar.accept(this.teamModule);
  }

  @Override
  public ServerTeamModule<TdmTeam> getTeamModule() {
    return this.teamModule;
  }

  @Override
  public boolean canChangeTeam(PlayerExtension<ServerPlayer> player,
      @Nullable TeamInstance<TdmTeam> oldTeam,
      @Nullable TeamInstance<TdmTeam> newTeam) {
    if (oldTeam != null && newTeam != null) {
      var stateInstance = this.stateMachine.getCurrentState();
      var maxScore = this.getMaxScore() * 0.75F;
      var scoresClose =
          (oldTeam != null && TdmTeam.getScore(oldTeam) > maxScore)
              || TdmTeam.getScore(newTeam) > maxScore;
      var tooLate = (stateInstance.getState() == TdmState.GAME
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
  public void teamChanged(PlayerExtension<ServerPlayer> player,
      @Nullable TeamInstance<TdmTeam> oldTeam,
      @Nullable TeamInstance<TdmTeam> newTeam) {
    if (newTeam == null) {
      player.getEntity().getInventory().clearContent();
      player.getEntity().setGameMode(GameType.SPECTATOR);
    } else {
      this.shopModule.resetBuyTime(player.getEntity().getUUID());
      player.getEntity().setGameMode(GameType.ADVENTURE);
      this.logicalServer.respawnPlayer((ServerPlayer) player.getEntity(), false);
      GameUtil.sendGameMessageToAll(
          new TranslatableComponent("message.joined_team",
              player.getEntity().getDisplayName().getString(),
              newTeam.getTeam().getDisplayName().getString()),
          this.getMinecraftServer());
    }
  }

  @SubscribeEvent
  public void handleLivingDeath(LivingDeathEvent event) {
    if (this.getGameState() == TdmState.GAME
        && event.getSource().getEntity() instanceof ServerPlayer
        && event.getEntityLiving() instanceof ServerPlayer && !this.firstBloodDrawn) {
      GameUtil.sendGameMessageToAll(new TranslatableComponent("message.first_blood_drawn",
          event.getSource().getEntity().getDisplayName().getString())
              .withStyle(ChatFormatting.DARK_RED),
          this.getMinecraftServer());
      this.firstBloodDrawn = true;
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleTriggerPressed(GunEvent.TriggerPressed event) {
    var handler = event.getLiving().getHandlerOrThrow(TdmPlayerHandler.TYPE);
    handler.setRemainingSpawnProtectionSeconds(0);
  }

  @SubscribeEvent
  public void handleEntityJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof ServerPlayer) {
      ServerPlayer playerEntity = (ServerPlayer) event.getEntity();
      CombatSlot.MELEE.addToInventory(ModItems.COMBAT_KNIFE.get().getDefaultInstance(),
          playerEntity.getInventory(), false);
      this.shopModule.resetBuyTime(event.getEntity().getUUID());
    }
  }
}
