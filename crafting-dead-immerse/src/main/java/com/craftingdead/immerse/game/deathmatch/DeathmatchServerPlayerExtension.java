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

import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.DisplayKilledMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.network.PacketDistributor;

public class DeathmatchServerPlayerExtension extends DeathmatchPlayerExtension {

  private static final float ASSIST_DAMAGE_PCT = 0.4F;

  private int secondTicker;

  private PlayerEntity lastSignificantDamage;

  private boolean wasGhost;

  private boolean ghost;
  private ServerPlayerEntity pendingSpectate;

  private DeathmatchServer game;

  private boolean valid = true;

  public DeathmatchServerPlayerExtension(DeathmatchServer game, IPlayer<?> player) {
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
      ((ServerPlayerEntity) this.getPlayer().getEntity()).setSpectatingEntity(this.pendingSpectate);
    }

    if (this.secondTicker++ == 20) {
      this.secondTicker = 0;
      if (this.ghost) {
        if (this.dataManager.getUpdate(REMAINING_GHOST_TIME_SECONDS,
            ghostTime -> --ghostTime) <= 0) {
          this.ghost = false;
          this.getPlayer().getEntity().setGameType(GameType.ADVENTURE);
          ((DeathmatchServer) this.game).getLogicalServer()
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
    if (this.getRemainingSpawnProtectionSeconds() > 0) {
      return true;
    }

    if (source.getTrueSource() instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) source.getTrueSource();

      if (playerEntity.getHeldItemMainhand().isEmpty()) {
        return true;
      }

      if (amount > this.getPlayer().getEntity().getMaxHealth() * ASSIST_DAMAGE_PCT) {
        this.lastSignificantDamage = playerEntity;
      }

      if (ILiving.getOptional(playerEntity)
          .flatMap(e -> e.getExtension(EXTENSION_ID))
          .map(extension -> ((DeathmatchPlayerExtension) extension).getTeam().orElse(null) == this
              .getTeam().orElse(null))
          .orElse(false)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onKill(Entity target) {
    if (target instanceof ServerPlayerEntity) {
      DeathmatchPlayerData playerData = this.getPlayerData();
      playerData.incrementKills();
      playerData.incrementScore();
      this.getTeam().map(this.game::getTeamInstance).ifPresent(DeathmatchTeam::incrementScore);

      NetworkChannel.PLAY.getSimpleChannel().send(
          PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) target),
          new DisplayKilledMessage(this.getPlayer().getEntity().getEntityId(),
              this.getPlayer().getEntity().getHeldItemMainhand()));
    }
    return false;
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    if (this.getPlayer().getEntity() instanceof ServerPlayerEntity) {
      ServerPlayerEntity playerEntity = (ServerPlayerEntity) this.getPlayer().getEntity();

      this.getPlayerData().incrementDeaths();

      if (this.lastSignificantDamage != null
          && this.lastSignificantDamage != cause.getTrueSource()) {
        this.game.getPlayerData(this.lastSignificantDamage.getUniqueID()).incrementAssists();
      }

      this.ghost = true;
      playerEntity.getEntityWorld().addParticle(ParticleTypes.EXPLOSION,
          playerEntity.getPosX(),
          playerEntity.getPosY(),
          playerEntity.getPosZ(), 0.0D, 0.0D, 0.0D);
      playerEntity.setGameType(GameType.SPECTATOR);
      if (cause.getTrueSource() instanceof ServerPlayerEntity) {
        this.pendingSpectate = (ServerPlayerEntity) cause.getTrueSource();
      }
    }
    return false;
  }

  @Override
  public void copyFrom(IPlayer<?> that, boolean wasDeath) {
    DeathmatchServerPlayerExtension extension =
        (DeathmatchServerPlayerExtension) that.getExpectedExtension(EXTENSION_ID);
    if (extension.valid) {
      this.setTeam(extension.getTeam().orElse(null));
      this.wasGhost = extension.wasGhost;
      this.ghost = extension.ghost;
      this.pendingSpectate = extension.pendingSpectate;
    }
  }

  public void invalidate() {
    this.valid = false;
  }
}
