package com.craftingdead.immerse.world.level.block.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.google.common.base.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseCenterBlockEntity extends BlockEntity {

  private UUID owner;
  private final Set<UUID> members = new HashSet<>();

  public BaseCenterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  public boolean hasAccess(UUID playerId) {
    return Objects.equal(this.owner, playerId) || this.isMember(playerId);
  }

  public void addMember(UUID playerId) {
    if (!this.members.contains(playerId)) {
      this.members.add(playerId);
    }
  }

  public void removeMember(UUID playerId) {
    this.members.remove(playerId);
  }

  public boolean isMember(UUID playerId) {
    return this.members.contains(playerId);
  }

  public Set<UUID> getMembers() {
    return this.members;
  }

  public void setOwner(UUID playerId) {
    this.owner = playerId;
  }

  public UUID getOwner() {
    return this.owner;
  }

  public void markUpdated() {
    this.setChanged();
    this.getLevel()
        .sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    var tag = new CompoundTag();
    this.saveAdditional(tag);
    return tag;
  }

  @Override
  public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
    this.loadMembers(packet.getTag());
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    tag.putUUID("owner", this.owner);
    tag.put("members", this.members.stream()
        .map(NbtUtils::createUUID)
        .collect(Collectors.toCollection(ListTag::new)));
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.loadMembers(tag);
  }

  private void loadMembers(CompoundTag tag) {
    this.owner = tag.getUUID("owner");
    var members = tag.getList("members", Tag.TAG_INT_ARRAY);
    this.members.clear();
    for (var memberTag : members) {
      this.members.add(NbtUtils.loadUUID(memberTag));
    }
  }
}
