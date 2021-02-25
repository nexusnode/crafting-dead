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

package com.craftingdead.immerse.game.deathmatch;

import java.time.Duration;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILivingExtension;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.SpawnPoint;
import com.craftingdead.immerse.game.deathmatch.state.DeathmatchState;
import com.craftingdead.immerse.game.deathmatch.state.GameStateInstance;
import com.craftingdead.immerse.game.shop.IShop;
import com.craftingdead.immerse.game.state.StateInstance;
import com.craftingdead.immerse.game.state.StateMachine;
import com.craftingdead.immerse.game.state.TimedStateInstance;
import com.craftingdead.immerse.game.team.ITeamGameServer;
import com.craftingdead.immerse.game.team.TeamInstance;
import com.craftingdead.immerse.server.LogicalServer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathmatchServer extends DeathmatchGame implements ITeamGameServer<DeathmatchTeam> {

  public static final Codec<DeathmatchServer> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          Codec.STRING.fieldOf("displayName").forGetter(DeathmatchServer::getDisplayName),
          Codec.INT.optionalFieldOf("maxScore", 100).forGetter(DeathmatchServer::getMaxScore),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("preGameDuration", Duration.ofMinutes(1L))
              .forGetter(DeathmatchServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("gameDuration", Duration.ofMinutes(10L))
              .forGetter(DeathmatchServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("postGameDuration", Duration.ofSeconds(30L))
              .forGetter(DeathmatchServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("buyDuration", Duration.ofSeconds(20L))
              .forGetter(DeathmatchServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("spawnProtectionDuration", Duration.ofSeconds(8L))
              .forGetter(DeathmatchServer::getPreGameDuration),
          Codec.STRING.xmap(Duration::parse, Duration::toString)
              .optionalFieldOf("ghostDuration", Duration.ofSeconds(5L))
              .forGetter(DeathmatchServer::getPreGameDuration),
          SpawnPoint.CODEC.fieldOf("redSpawnPoint").forGetter(DeathmatchServer::getRedSpawnPoint),
          SpawnPoint.CODEC.fieldOf("blueSpawnPoint").forGetter(DeathmatchServer::getBlueSpawnPoint))
      .apply(instance, DeathmatchServer::new));

  private final LogicalServer logicalServer = CraftingDeadImmerse.getInstance().getLogicalServer();

  private final StateMachine<DeathmatchState, DeathmatchServer> stateMachine;
  private final IShop shop = new DeathmatchShop(false);

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

  public DeathmatchServer(String displayName, int maxScore, Duration preGameDuration,
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
    this.stateMachine = new StateMachine<>(this, DeathmatchState.values(), this::setGameState);
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
    // @formatter:off
    this.getMinecraftServer().getPlayerList().getPlayers()
        .stream()
        .<IPlayer<?>>map(IPlayer::getExpected)
        .map(player -> (DeathmatchServerPlayerExtension) player.getExpectedExtension(DeathmatchPlayerExtension.EXTENSION_ID))
        .forEach(DeathmatchServerPlayerExtension::resetBuyTime);
    // @formatter:on
  }

  public void resetPlayerData() {
    for (ServerPlayerEntity playerEntity : this.getMinecraftServer().getPlayerList().getPlayers()) {
      this.deletePlayerData(playerEntity.getUniqueID());
    }
  }

  public void resetTeams() {
    for (DeathmatchTeam team : DeathmatchTeam.values()) {
      DeathmatchTeam.reset(this.getTeamInstance(team));
    }
  }

  @Override
  public Optional<SpawnPoint> getSpawnPoint(IPlayer<ServerPlayerEntity> player) {
    return this.getPlayerTeam(player).map(
        team -> team == DeathmatchTeam.RED ? this.getRedSpawnPoint() : this.getBlueSpawnPoint());
  }

  @Override
  protected DeathmatchPlayerExtension createPlayerExtension(IPlayer<?> player) {
    return new DeathmatchServerPlayerExtension(this, player);
  }

  @Override
  public Optional<IShop> getShop() {
    return Optional.of(this.shop);
  }

  @Override
  public void tick() {
    if (this.stateMachine.getCurrentState().getState() == DeathmatchState.IDLE
        && this.getMinecraftServer().getCurrentPlayerCount() > 0) {
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

    GameRules.BooleanValue daylightCycle = gameRules.get(GameRules.DO_DAYLIGHT_CYCLE);
    this.daylightCycleOld = daylightCycle.get();
    daylightCycle.set(false, this.getMinecraftServer());

    GameRules.BooleanValue weatherCycle = gameRules.get(GameRules.DO_WEATHER_CYCLE);
    this.weatherCycleOld = weatherCycle.get();
    weatherCycle.set(false, this.getMinecraftServer());

    BooleanValue showDeathMessages = gameRules.get(GameRules.SHOW_DEATH_MESSAGES);
    this.showDeathMessagesOld = showDeathMessages.get();
    showDeathMessages.set(false, this.getMinecraftServer());

    BooleanValue naturalRegeneration = gameRules.get(GameRules.NATURAL_REGENERATION);
    this.naturalRegenerationOld = naturalRegeneration.get();
    naturalRegeneration.set(false, this.getMinecraftServer());

    BooleanValue immediateRespawn = gameRules.get(GameRules.DO_IMMEDIATE_RESPAWN);
    this.immediateRespawnOld = immediateRespawn.get();
    immediateRespawn.set(true, this.getMinecraftServer());

    ServerWorld world = this.getMinecraftServer().getWorld(World.OVERWORLD);
    // Set weather to clear
    world.func_241113_a_(6000, 0, false, false);
    // Set time to day
    world.setDayTime(1000);


    this.oldDifficulty = this.getMinecraftServer().getServerConfiguration().getDifficulty();
    this.getMinecraftServer().setDifficultyForAllWorlds(Difficulty.PEACEFUL, true);

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void unload() {
    final GameRules gameRules = this.getMinecraftServer().getGameRules();

    gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(this.daylightCycleOld,
        this.getMinecraftServer());
    gameRules.get(GameRules.DO_WEATHER_CYCLE).set(this.weatherCycleOld, this.getMinecraftServer());
    gameRules.get(GameRules.SHOW_DEATH_MESSAGES).set(this.showDeathMessagesOld,
        this.getMinecraftServer());
    gameRules.get(GameRules.NATURAL_REGENERATION).set(this.naturalRegenerationOld,
        this.getMinecraftServer());
    gameRules.get(GameRules.DO_IMMEDIATE_RESPAWN).set(this.immediateRespawnOld,
        this.getMinecraftServer());

    this.getMinecraftServer().setDifficultyForAllWorlds(this.oldDifficulty, true);

    for (ServerPlayerEntity playerEntity : this.getMinecraftServer().getPlayerList().getPlayers()) {
      ((DeathmatchServerPlayerExtension) IPlayer.getExpected(playerEntity)
          .getExpectedExtension(DeathmatchPlayerExtension.EXTENSION_ID)).invalidate();
    }

    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @Override
  public boolean isFinished() {
    return this.finished;
  }

  @Override
  public boolean switchTeam(IPlayer<?> player, @Nullable TeamInstance<DeathmatchTeam> oldTeam,
      @Nullable TeamInstance<DeathmatchTeam> newTeam) {

    if (oldTeam != null && newTeam != null) {
      StateInstance<?> stateInstance = this.stateMachine.getCurrentState();
      final float maxScore = this.getMaxScore() * 0.75F;
      boolean scoresClose =
          (oldTeam != null && DeathmatchTeam.getScore(oldTeam) > maxScore)
              || DeathmatchTeam.getScore(newTeam) > maxScore;
      boolean tooLate = (stateInstance.getState() == DeathmatchState.GAME
          && ((GameStateInstance) stateInstance).getTimeElapsedSeconds() > 240)
          || stateInstance.getState() == DeathmatchState.POST_GAME;

      if (scoresClose || tooLate) {
        player.getEntity().sendMessage(
            GameUtil.formatMessage(Text.translate("message.no_switch_team")), Util.DUMMY_UUID);
        return false;
      }
    }

    DeathmatchPlayerExtension deathmatchPlayer =
        ((DeathmatchPlayerExtension) player
            .getExpectedExtension(DeathmatchPlayerExtension.EXTENSION_ID));
    deathmatchPlayer.setTeam(newTeam == null ? null : newTeam.getTeam());

    if (newTeam == null) {
      player.getEntity().setGameType(GameType.SPECTATOR);
    } else {
      player.getEntity().setGameType(GameType.ADVENTURE);
      this.logicalServer.respawnPlayer((ServerPlayerEntity) player.getEntity(), false);
      GameUtil.sendGameMessageToAll(
          Text.translate("message.joined_team", player.getEntity().getDisplayName().getString(),
              newTeam.getTeam().getDisplayName().getString()),
          this.getMinecraftServer());
    }

    return true;
  }

  @Override
  public void addPlayer(IPlayer<ServerPlayerEntity> player) {
    this.setPlayerTeam(player, null);
    GameUtil.sendGameMessageToAll(
        Text.translate("message.joined", player.getEntity().getDisplayName().getString()),
        this.getMinecraftServer());
  }

  @Override
  public void removePlayer(IPlayer<ServerPlayerEntity> player) {
    this.setPlayerTeam(player, null);
    this.deletePlayerData(player.getEntity().getUniqueID());
    GameUtil.sendGameMessageToAll(
        Text.translate("message.left", player.getEntity().getDisplayName().getString()),
        this.getMinecraftServer());
  }

  @SubscribeEvent
  public void handleLivingDeath(LivingDeathEvent event) {
    if (!event.getEntityLiving().getEntityWorld().isRemote()) {
      if (event.getSource().getTrueSource() instanceof PlayerEntity
          && event.getEntityLiving() instanceof PlayerEntity && !this.firstBloodDrawn) {
        GameUtil.sendGameMessageToAll(Text
            .translate("message.first_blood_drawn",
                event.getSource().getTrueSource().getDisplayName().getString())
            .mergeStyle(TextFormatting.DARK_RED), this.getMinecraftServer());
        this.firstBloodDrawn = true;
        this.getPlayerData(event.getSource().getTrueSource().getUniqueID()).incrementScore();
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleTriggerPressed(GunEvent.TriggerPressed event) {
    ILivingExtension handler =
        event.getLiving().getExpectedExtension(DeathmatchPlayerExtension.EXTENSION_ID);
    if (handler != null) {
      DeathmatchPlayerExtension player = (DeathmatchPlayerExtension) handler;
      player.setRemainingSpawnProtectionSeconds(0);
    }
  }

  @Override
  public boolean persistPlayerData() {
    return false;
  }

  @Override
  public boolean save() {
    return false;
  }
}
