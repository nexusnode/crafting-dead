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

package com.craftingdead.immerse.game.module.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import com.craftingdead.core.network.Synched;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;

public class TeamInstance<T extends Team>
    implements INBTSerializable<CompoundTag>, Synched {

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
      playerEntity.playNotifySound(ImmerseSoundEvents.VICTORY_MUSIC.get(), SoundSource.MASTER, 0.7F,
          1.0F);
      playerEntity.playNotifySound(teamVictoryCallout, SoundSource.MASTER, 0.7F, 1.0F);
    }, minecraftServer);
  }

  public void broadcastDefeatSounds(SoundEvent teamDefeatCallout,
      MinecraftServer minecraftServer) {
    this.forEach(playerEntity -> {
      playerEntity.playNotifySound(ImmerseSoundEvents.DEFEAT_MUSIC.get(), SoundSource.MASTER, 0.7F,
          1.0F);
      playerEntity.playNotifySound(teamDefeatCallout, SoundSource.MASTER, 0.7F, 1.0F);
    }, minecraftServer);
  }

  public void forEach(Consumer<ServerPlayer> action, MinecraftServer minecraftServer) {
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

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    this.team.save(this, nbt);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    this.team.load(this, nbt);
  }
}
