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

package com.craftingdead.immerse.game.team;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import com.craftingdead.core.network.util.NetworkDataManager;
import com.craftingdead.core.util.IBufferSerializable;
import com.craftingdead.immerse.util.ModSoundEvents;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;

public class TeamInstance<T extends Enum<T> & ITeam>
    implements INBTSerializable<CompoundNBT>, IBufferSerializable {

  protected final NetworkDataManager dataManager = new NetworkDataManager();

  private final T team;

  private final List<UUID> members = new ReferenceArrayList<>();
  private boolean membersChanged = false;

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
    this.membersChanged = true;
  }

  void removeMember(UUID memberId) {
    this.members.remove(memberId);
    this.membersChanged = true;
  }

  public NetworkDataManager getDataManager() {
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
    NetworkDataManager.writeEntries(
        writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), packetBuffer);
    if (writeAll || this.membersChanged) {
      packetBuffer.writeVarInt(this.members.size());
      for (UUID memberId : this.members) {
        packetBuffer.writeUUID(memberId);
      }
      this.membersChanged = false;
    } else {
      packetBuffer.writeVarInt(-1);
    }
  }

  @Override
  public void decode(PacketBuffer packetBuffer) {
    this.dataManager.setEntryValues(NetworkDataManager.readEntries(packetBuffer));
    int membersSize = packetBuffer.readVarInt();
    if (membersSize > -1) {
      this.members.clear();
      for (int i = 0; i < membersSize; i++) {
        this.members.add(packetBuffer.readUUID());
      }
    }
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty() || this.membersChanged;
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
