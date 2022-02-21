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

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.DisplayKilledMessage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.network.PacketDistributor;

public class TdmServerPlayerHandler extends TdmPlayerHandler<ServerPlayer> {

  private static final float ASSIST_DAMAGE_PCT = 0.4F;

  private int secondTicker;

  private Player lastSignificantDamage;

  private boolean wasGhost;

  private boolean ghost;
  private ServerPlayer pendingSpectate;

  private TdmServer game;

  private boolean valid = true;

  public TdmServerPlayerHandler(TdmServer game, PlayerExtension<ServerPlayer> player) {
    super(game, player, (int) game.getBuyDuration().getSeconds(),
        (int) game.getSpawnProtectionDuration().getSeconds(),
        (int) game.getGhostDuration().getSeconds());
    this.game = game;
  }

  public void resetBuyTime() {
    this.setRemainingBuyTimeSeconds((int) this.game.getBuyDuration().getSeconds());
  }

  @Override
  public void tick() {
    super.tick();

    if (this.wasGhost != this.ghost) {
      this.wasGhost = this.ghost;
      this.getPlayerData().setDead(this.ghost);
    }

    if (this.ghost && this.pendingSpectate != null) {
      if (this.pendingSpectate.isAlive() && !this.pendingSpectate.isSpectator()) {
        ((ServerPlayer) this.getPlayer().getEntity()).setCamera(this.pendingSpectate);
      }
      this.pendingSpectate = null;
    }

    if (this.secondTicker++ == 20) {
      this.secondTicker = 0;
      if (this.ghost) {
        if (this.dataManager.compute(REMAINING_GHOST_TIME_SECONDS, ghostTime -> --ghostTime) <= 0) {
          this.ghost = false;
          this.getPlayer().getEntity().setGameMode(GameType.ADVENTURE);
          ((TdmServer) this.game).getLogicalServer()
              .respawnPlayer((ServerPlayer) this.getPlayer().getEntity(), false);
        }
      } else if (!this.getPlayer().getEntity().isSpectator()) {
        this.dataManager.compute(REMAINING_BUY_TIME_SECONDS,
            buyTime -> buyTime > 0 ? --buyTime : buyTime);
        this.dataManager.compute(REMAINING_SPAWN_PROTECTION_SECONDS,
            spawnProtection -> spawnProtection > 0
                && --spawnProtection < this.game.getSpawnProtectionDuration().getSeconds() * 0.8F
                && this.getPlayer().isMoving() ? 0 : spawnProtection);
      }
    }
  }

  @Override
  public boolean handleHurt(DamageSource source, float amount) {
    if (this.getRemainingSpawnProtectionSeconds() > 0 || this.isMovementBlocked()) {
      return true;
    }

    if (source.getEntity() instanceof Player) {
      Player playerEntity = (Player) source.getEntity();

      if (playerEntity.getMainHandItem().isEmpty()) {
        return true;
      }

      if (amount > this.getPlayer().getEntity().getMaxHealth() * ASSIST_DAMAGE_PCT) {
        this.lastSignificantDamage = playerEntity;
      }

      if (playerEntity.getCapability(LivingExtension.CAPABILITY)
          .resolve()
          .flatMap(e -> e.getHandler(TYPE))
          .map(extension -> extension.getTeam().orElse(null) == this
              .getTeam()
              .orElse(null))
          .orElse(false)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean handleKill(Entity target) {
    if (target instanceof ServerPlayer) {
      TdmPlayerData playerData = this.getPlayerData();
      playerData.incrementKills();
      playerData.incrementScore();
      this.getTeam().map(this.game.getTeamModule()::getTeamInstance)
          .ifPresent(TdmTeam::incrementScore);

      NetworkChannel.PLAY.getSimpleChannel().send(
          PacketDistributor.PLAYER.with(() -> (ServerPlayer) target),
          new DisplayKilledMessage(this.getPlayer().getEntity().getId(),
              this.getPlayer().getEntity().getMainHandItem()));
    }
    return false;
  }

  @Override
  public boolean handleDeath(DamageSource cause) {
    if (this.getPlayer().getEntity() instanceof ServerPlayer) {
      ServerPlayer playerEntity = (ServerPlayer) this.getPlayer().getEntity();

      this.getPlayerData().incrementDeaths();

      if (this.lastSignificantDamage != null
          && this.lastSignificantDamage != cause.getEntity()) {
        this.game.getPlayerData(this.lastSignificantDamage.getUUID()).incrementAssists();
      }

      this.ghost = true;
      playerEntity.getLevel().addParticle(ParticleTypes.EXPLOSION,
          playerEntity.getX(),
          playerEntity.getY(),
          playerEntity.getZ(), 0.0D, 0.0D, 0.0D);
      playerEntity.setGameMode(GameType.SPECTATOR);
      if (cause.getEntity() instanceof ServerPlayer) {
        this.pendingSpectate = (ServerPlayer) cause.getEntity();
      }
    }
    return false;
  }

  @Override
  public void copyFrom(PlayerExtension<ServerPlayer> that, boolean wasDeath) {
    var extension = (TdmServerPlayerHandler) that.getHandlerOrThrow(TYPE);
    if (extension.valid) {
      this.wasGhost = extension.wasGhost;
      this.ghost = extension.ghost;
      this.pendingSpectate = extension.pendingSpectate;
    }
  }

  public void invalidate() {
    this.valid = false;
  }
}
