/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.world.level.extension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SerializableUUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class LegacyBase implements LandOwner {

  public static final Codec<LegacyBase> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              SerializableUUID.CODEC
                  .fieldOf("id")
                  .forGetter(LegacyBase::getId),
              SerializableUUID.CODEC
                  .fieldOf("ownerId")
                  .forGetter(LegacyBase::getOwnerId),
              SerializableUUID.CODEC
                  .listOf()
                  .<Set<UUID>>xmap(HashSet::new, List::copyOf)
                  .fieldOf("members")
                  .forGetter(LegacyBase::getMembers),
              BlockPos.CODEC
                  .fieldOf("blockPos")
                  .forGetter(LegacyBase::getBlockPos),
              BoundingBox.CODEC
                  .fieldOf("buildRegion")
                  .forGetter(LegacyBase::getBuildRegion))
          .apply(instance, LegacyBase::new));

  private static final Logger logger = LogUtils.getLogger();

  private final UUID id;

  private final UUID ownerId;

  private final Set<UUID> members;

  private final BlockPos blockPos;

  private BoundingBox buildRegion;

  public LegacyBase(UUID ownerId, BlockPos blockPos, int buildRadius) {
    this(UUID.randomUUID(), ownerId, new HashSet<>(), blockPos,
        new BoundingBox(blockPos).inflatedBy(buildRadius));
  }

  public LegacyBase(UUID id, UUID ownerId, Set<UUID> members, BlockPos blockPos,
      BoundingBox buildRegion) {
    this.id = id;
    this.ownerId = ownerId;
    this.members = members;
    this.blockPos = blockPos;
    this.buildRegion = buildRegion;
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }

  public BlockPos getBlockPos() {
    return this.blockPos;
  }

  public BoundingBox getBuildRegion() {
    return this.buildRegion;
  }

  @Override
  public boolean isAllowedToBuild(UUID playerId, BlockPos blockPos) {
    return (this.members.contains(playerId) || this.ownerId.equals(playerId))
        && this.buildRegion.isInside(blockPos);
  }

  public Set<UUID> getMembers() {
    return this.members;
  }

  @Override
  public LandOwnerType getType() {
    return LandOwnerTypes.LEGACY_BASE.get();
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public CompoundTag getUpdateTag() {
    var tag = new CompoundTag();
    var membersTag = new ListTag();
    for (var member : this.members) {
      membersTag.add(NbtUtils.createUUID(member));
    }
    tag.put("members", membersTag);

    tag.put("buildRegion", BoundingBox.CODEC.encodeStart(NbtOps.INSTANCE, this.buildRegion)
        .getOrThrow(false, logger::error));

    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    var membersTag = tag.getList("members", Tag.TAG_INT_ARRAY);
    this.members.clear();
    for (var memberTag : membersTag) {
      this.members.add(NbtUtils.loadUUID(memberTag));
    }

    this.buildRegion = BoundingBox.CODEC.parse(NbtOps.INSTANCE, tag.get("buildRegion"))
        .getOrThrow(false, logger::error);
  }
}
