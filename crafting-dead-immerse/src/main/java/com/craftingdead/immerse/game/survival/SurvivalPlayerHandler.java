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

package com.craftingdead.immerse.game.survival;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.entity.extension.LivingHandlerType;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;

public class SurvivalPlayerHandler implements PlayerHandler {

  public static final LivingHandlerType<SurvivalPlayerHandler> TYPE =
      new LivingHandlerType<>(new ResourceLocation(CraftingDeadImmerse.ID, "survival_player"));

  private static final EntityDataAccessor<Integer> DAYS_SURVIVED =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> ZOMBIES_KILLED =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> PLAYERS_KILLED =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.INT);

  private final PlayerExtension<?> player;

  private final SynchedData dataManager = new SynchedData();

  @Nullable
  private BlockPos basePos;

  public SurvivalPlayerHandler(PlayerExtension<?> player) {
    this.player = player;
    this.dataManager.register(DAYS_SURVIVED, 0);
    this.dataManager.register(ZOMBIES_KILLED, 0);
    this.dataManager.register(PLAYERS_KILLED, 0);
  }

  public Optional<BlockPos> getBasePos() {
    return Optional.ofNullable(this.basePos);
  }

  public void setBasePos(@Nullable BlockPos basePos) {
    this.basePos = basePos;
  }

  @Override
  public void tick() {
    if (this.player.getEntity() instanceof ServerPlayer) {
      int aliveTicks = ((ServerPlayer) this.player.getEntity()).getStats()
          .getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
      this.setDaysSurvived(aliveTicks / 20 / 60 / 20);
    }
  }

  @Override
  public boolean handleKill(Entity target) {
    if (target instanceof Zombie) {
      this.setZombiesKilled(this.getZombiesKilled() + 1);
    } else if (target instanceof ServerPlayer) {
      this.setPlayersKilled(this.getPlayersKilled() + 1);
    }
    return false;
  }

  public int getDaysSurvived() {
    return this.dataManager.get(DAYS_SURVIVED);
  }

  public void setDaysSurvived(int daysSurvived) {
    this.dataManager.set(DAYS_SURVIVED, daysSurvived);
  }

  public int getZombiesKilled() {
    return this.dataManager.get(ZOMBIES_KILLED);
  }

  public void setZombiesKilled(int zombiesKilled) {
    this.dataManager.set(ZOMBIES_KILLED, zombiesKilled);
  }

  public int getPlayersKilled() {
    return this.dataManager.get(PLAYERS_KILLED);
  }

  public void setPlayersKilled(int playersKilled) {
    this.dataManager.set(PLAYERS_KILLED, playersKilled);
  }

  @Override
  public boolean isCombatModeEnabled() {
    return false;
  }

  @Override
  public void copyFrom(PlayerExtension<ServerPlayer> that, boolean wasDeath) {
    if (!wasDeath) {
      that.getHandler(TYPE).ifPresent(extension -> {
        this.setDaysSurvived(extension.getDaysSurvived());
        this.setZombiesKilled(extension.getZombiesKilled());
        this.setPlayersKilled(extension.getPlayersKilled());
      });
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    tag.putInt("zombiesKilled", this.getZombiesKilled());
    tag.putInt("playersKilled", this.getPlayersKilled());
    this.getBasePos().ifPresent(pos -> tag.put("basePos", NbtUtils.writeBlockPos(pos)));
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.setZombiesKilled(tag.getInt("zombiesKilled"));
    this.setPlayersKilled(tag.getInt("playersKilled"));
    if (tag.contains("basePos", Tag.TAG_COMPOUND)) {
      this.setBasePos(NbtUtils.readBlockPos(tag.getCompound("basePos")));
    }
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    SynchedData.pack(writeAll
        ? this.dataManager.getAll()
        : this.dataManager.packDirty(), out);
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.dataManager.assignValues(SynchedData.unpack(in));
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty();
  }
}
