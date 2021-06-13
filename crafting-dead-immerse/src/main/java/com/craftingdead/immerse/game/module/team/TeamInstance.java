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

package com.craftingdead.immerse.game.module.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.immerse.util.ModSoundEvents;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;

public class TeamInstance<T extends Team>
    implements INBTSerializable<CompoundNBT>, BufferSerializable {

  protected final SynchedData dataManager = new SynchedData();

  private final T team;

  private final List<UUID> members = new ArrayList<>();

  public TeamInstance(T team) {
    this.team = team;
    this.team.registerDataParameters(this.dataManager);
  }

  public void broadcastVictorySounds(SoundEvent teamVictoryCallout,
      MinecraftServer minecraftServer) {
    this.forEach(playerEntity -> {
      playerEntity.playNotifySound(ModSoundEvents.VICTORY_MUSIC.get(), SoundCategory.MASTER, 0.7F,
          1.0F);
      playerEntity.playNotifySound(teamVictoryCallout, SoundCategory.MASTER, 0.7F, 1.0F);
    }, minecraftServer);
  }

  public void broadcastDefeatSounds(SoundEvent teamDefeatCallout,
      MinecraftServer minecraftServer) {
    this.forEach(playerEntity -> {
      playerEntity.playNotifySound(ModSoundEvents.DEFEAT_MUSIC.get(), SoundCategory.MASTER, 0.7F,
          1.0F);
      playerEntity.playNotifySound(teamDefeatCallout, SoundCategory.MASTER, 0.7F, 1.0F);
    }, minecraftServer);
  }

  public void forEach(Consumer<ServerPlayerEntity> action, MinecraftServer minecraftServer) {
    for (UUID memberId : this.members) {
      action.accept(minecraftServer.getPlayerList().getPlayer(memberId));
    }
  }

  void addMember(UUID memberId) {
    this.members.add(memberId);
  }

  void removeMember(UUID memberId) {
    this.members.remove(memberId);
  }

  public SynchedData getDataManager() {
    return this.dataManager;
  }

  public List<UUID> getMembers() {
    return Collections.unmodifiableList(this.members);
  }

  public T getTeam() {
    return this.team;
  }

  @Override
  public void encode(PacketBuffer packetBuffer, boolean writeAll) {
    SynchedData.writeEntries(
        writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), packetBuffer);
  }

  @Override
  public void decode(PacketBuffer packetBuffer) {
    this.dataManager.setEntryValues(SynchedData.readEntries(packetBuffer));
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    this.team.save(this, nbt);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.team.load(this, nbt);
  }
}
