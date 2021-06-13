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

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.DisplayKilledMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.network.PacketDistributor;

public class TdmServerPlayerHandler extends TdmPlayerHandler {

  private static final float ASSIST_DAMAGE_PCT = 0.4F;

  private int secondTicker;

  private PlayerEntity lastSignificantDamage;

  private boolean wasGhost;

  private boolean ghost;
  private ServerPlayerEntity pendingSpectate;

  private TdmServer game;

  private boolean valid = true;

  public TdmServerPlayerHandler(TdmServer game, PlayerExtension<?> player) {
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
        ((ServerPlayerEntity) this.getPlayer().getEntity()).setCamera(this.pendingSpectate);
      }
      this.pendingSpectate = null;
    }

    if (this.secondTicker++ == 20) {
      this.secondTicker = 0;
      if (this.ghost) {
        if (this.dataManager.getUpdate(REMAINING_GHOST_TIME_SECONDS,
            ghostTime -> --ghostTime) <= 0) {
          this.ghost = false;
          this.getPlayer().getEntity().setGameMode(GameType.ADVENTURE);
          ((TdmServer) this.game).getLogicalServer()
              .respawnPlayer((ServerPlayerEntity) this.getPlayer().getEntity(), false);
        }
      } else if (!this.getPlayer().getEntity().isSpectator()) {
        this.dataManager.getUpdate(REMAINING_BUY_TIME_SECONDS,
            buyTime -> buyTime > 0 ? --buyTime : buyTime);
        this.dataManager.getUpdate(REMAINING_SPAWN_PROTECTION_SECONDS,
            spawnProtection -> spawnProtection > 0
                && --spawnProtection < this.game.getSpawnProtectionDuration().getSeconds() * 0.8F
                && this.getPlayer().isMoving() ? 0 : spawnProtection);
      }
    }
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    if (this.getRemainingSpawnProtectionSeconds() > 0 || this.isMovementBlocked()) {
      return true;
    }

    if (source.getEntity() instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) source.getEntity();

      if (playerEntity.getMainHandItem().isEmpty()) {
        return true;
      }

      if (amount > this.getPlayer().getEntity().getMaxHealth() * ASSIST_DAMAGE_PCT) {
        this.lastSignificantDamage = playerEntity;
      }

      if (playerEntity.getCapability(Capabilities.LIVING)
          .resolve()
          .flatMap(e -> e.getHandler(ID))
          .map(extension -> ((TdmPlayerHandler) extension).getTeam().orElse(null) == this.getTeam()
              .orElse(null))
          .orElse(false)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onKill(Entity target) {
    if (target instanceof ServerPlayerEntity) {
      TdmPlayerData playerData = this.getPlayerData();
      playerData.incrementKills();
      playerData.incrementScore();
      this.getTeam().map(this.game.getTeamModule()::getTeamInstance)
          .ifPresent(TdmTeam::incrementScore);

      NetworkChannel.PLAY.getSimpleChannel().send(
          PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) target),
          new DisplayKilledMessage(this.getPlayer().getEntity().getId(),
              this.getPlayer().getEntity().getMainHandItem()));
    }
    return false;
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    if (this.getPlayer().getEntity() instanceof ServerPlayerEntity) {
      ServerPlayerEntity playerEntity = (ServerPlayerEntity) this.getPlayer().getEntity();

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
      if (cause.getEntity() instanceof ServerPlayerEntity) {
        this.pendingSpectate = (ServerPlayerEntity) cause.getEntity();
      }
    }
    return false;
  }

  @Override
  public void copyFrom(PlayerExtension<?> that, boolean wasDeath) {
    TdmServerPlayerHandler extension =
        (TdmServerPlayerHandler) that.getHandlerOrThrow(ID);
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
