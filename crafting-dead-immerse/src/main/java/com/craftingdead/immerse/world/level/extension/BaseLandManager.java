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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.RegisterLandOwnerMessage;
import com.craftingdead.immerse.network.play.RemoveLandOwnerMessage;
import com.craftingdead.immerse.network.play.SyncLandChunkMessage;
import com.craftingdead.immerse.util.SplittingExecutor;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.network.PacketDistributor;

public class BaseLandManager implements LandManager {

  private static final int SYNC_AREA_THRESHOLD = 1000;

  private static final Logger logger = LogUtils.getLogger();

  protected final Level level;

  protected final Long2ObjectMap<LandSection> sections = new Long2ObjectOpenHashMap<>();
  protected final Map<UUID, LandOwner> landsOwners = new Object2ObjectLinkedOpenHashMap<>();
  protected final Multimap<UUID, LandClaim> landClaims = LinkedHashMultimap.create();

  private final SplittingExecutor executor = new SplittingExecutor();

  private final Set<ChunkPos> dirtyChunks = new ObjectOpenHashSet<>();

  public BaseLandManager(Level level) {
    this.level = level;
  }

  @Override
  public void serverTick(BooleanSupplier haveTime) {
    this.executor.tick();
    for (var chunkPos : this.dirtyChunks) {
      var chunk = this.level.getChunk(chunkPos.x, chunkPos.z);
      if (chunk != null) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        this.writeChunkToBuf(chunkPos, buf);
        NetworkChannel.PLAY.getSimpleChannel().send(
            PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
            new SyncLandChunkMessage(chunkPos, buf));
      }
    }
    this.dirtyChunks.clear();
  }

  @Override
  public void registerLandOwner(LandOwner landOwner) {
    if (this.landsOwners.put(landOwner.getId(), landOwner) != null) {
      throw new IllegalStateException(
          "Duplicate land owner with ID: " + landOwner.getId().toString());
    }
    if (!this.level.isClientSide()) {
      this.level.players().forEach(player -> NetworkChannel.PLAY.getSimpleChannel().send(
          PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
          new RegisterLandOwnerMessage(landOwner)));
    }
  }

  @Override
  public CompletionStage<Boolean> removeLandOwner(UUID landOwnerId) {
    if (this.landsOwners.remove(landOwnerId) == null) {
      return CompletableFuture.completedStage(false);
    }

    if (!this.level.isClientSide()) {
      this.level.players().forEach(player -> NetworkChannel.PLAY.getSimpleChannel().send(
          PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
          new RemoveLandOwnerMessage(landOwnerId)));
    }

    var landClaims = this.landClaims.get(landOwnerId);
    if (landClaims == null) {
      return CompletableFuture.completedStage(true);
    }

    return List.copyOf(landClaims).stream()
        .map(this::removeLandClaim)
        .reduce((a, b) -> a.thenCombine(b, (r1, r2) -> r1 && r2))
        .orElseGet(() -> CompletableFuture.completedStage(true));
  }

  @Override
  public LandOwner getLandOwner(UUID landOwnerId) {
    return this.landsOwners.get(landOwnerId);
  }

  protected boolean isSectionOutOfBounds(long sectionPos) {
    var y = SectionPos.sectionToBlockCoord(SectionPos.y(sectionPos));
    return this.level.isOutsideBuildHeight(y);
  }

  protected static long getSectionPos(ChunkPos chunkPos, int sectionY) {
    return SectionPos.asLong(chunkPos.x, sectionY, chunkPos.z);
  }

  @Nullable
  protected LandSection getOrLoadSection(long sectionPos) {
    return this.sections.get(sectionPos);
  }

  protected void sectionChanged(long sectionPos) {
    this.dirtyChunks.add(SectionPos.of(sectionPos).chunk());
  }

  @Override
  public CompletionStage<ClaimResult> registerLandClaim(LandClaim landClaim) {
    if (!this.landsOwners.containsKey(landClaim.ownerId())) {
      throw new IllegalStateException("Land owner must be registered before claiming land");
    }

    return this.forRegion(landClaim.boundingBox(), sectionPos -> {
      if (this.isSectionOutOfBounds(sectionPos)) {
        return Optional.of(ClaimResult.OUT_OF_BOUNDS);
      }

      var section = this.getOrLoadSection(sectionPos);
      if (section == null) {
        section = new LandSection();
        this.sections.put(sectionPos, section);
      }

      if (!section.registerLandClaim(landClaim)) {
        return Optional.of(ClaimResult.ALREADY_CLAIMED);
      }

      this.sectionChanged(sectionPos);
      return Optional.empty();
    }, ClaimResult.SUCCESS).thenCompose(result -> {
      if (result == ClaimResult.SUCCESS) {
        this.landClaims.put(landClaim.ownerId(), landClaim);
        return CompletableFuture.completedStage(ClaimResult.SUCCESS);
      } else {
        return this.removeLandClaim(landClaim).thenApply(__ -> result);
      }
    });
  }

  @Override
  public CompletionStage<Boolean> removeLandClaim(LandClaim landClaim) {
    return this.forRegion(landClaim.boundingBox(), sectionPos -> {
      var section = this.getOrLoadSection(sectionPos);
      if (section == null) {
        return Optional.of(false);
      }

      section.removeLandClaim(landClaim);

      this.sectionChanged(sectionPos);

      return Optional.empty();
    }, true).whenComplete(
        (result, exception) -> this.landClaims.remove(landClaim.ownerId(), landClaim));
  }

  @Override
  public LandClaim getLandClaimAt(BlockPos blockPos) {
    var sectionPos = SectionPos.asLong(blockPos);

    var section = this.getOrLoadSection(sectionPos);
    if (section == null) {
      return null;
    }

    return section.getLandClaim(blockPos);
  }

  @Override
  public CompoundTag save() {
    var tag = new CompoundTag();

    var landOwnersTag = new ListTag();
    this.landsOwners.forEach((ownerId, owner) -> {
      var entryTag = new CompoundTag();
      entryTag.putUUID("ownerId", ownerId);
      entryTag.put("owner", LandOwner.CODEC.encodeStart(NbtOps.INSTANCE, owner)
          .getOrThrow(false, logger::error));
      landOwnersTag.add(entryTag);
    });
    tag.put("landOwners", landOwnersTag);

    var landClaimsTag = new ListTag();
    this.landClaims.asMap().forEach((ownerId, landClaims) -> {
      var entryTag = new CompoundTag();
      entryTag.putUUID("ownerId", ownerId);
      var entryLandClaimsTag = landClaims.stream()
          .map(landClaim -> LandClaim.CODEC.encodeStart(NbtOps.INSTANCE, landClaim)
              .getOrThrow(false, logger::error))
          .collect(Collectors.toCollection(ListTag::new));
      entryTag.put("landClaims", entryLandClaimsTag);
      landClaimsTag.add(entryTag);
    });
    tag.put("landClaims", landClaimsTag);

    return tag;
  }

  @Override
  public void load(CompoundTag tag) {
    var landOwnersTag = tag.getList("landOwners", Tag.TAG_COMPOUND);
    for (int i = 0; i < landOwnersTag.size(); i++) {
      var entryTag = landOwnersTag.getCompound(i);
      this.landsOwners.put(entryTag.getUUID("ownerId"),
          LandOwner.CODEC.parse(NbtOps.INSTANCE, entryTag.get("owner"))
              .getOrThrow(false, logger::error));
    }

    var landClaimsTag = tag.getList("landClaims", Tag.TAG_COMPOUND);
    for (int i = 0; i < landClaimsTag.size(); i++) {
      var entryTag = landClaimsTag.getCompound(i);
      var ownerId = entryTag.getUUID("ownerId");
      var entryLandClaims = entryTag.getList("landClaims", Tag.TAG_COMPOUND).stream()
          .map(landClaimTag -> LandClaim.CODEC.parse(NbtOps.INSTANCE, landClaimTag)
              .getOrThrow(false, logger::error))
          .toList();
      this.landClaims.putAll(ownerId, entryLandClaims);
    }
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    out.writeVarInt(this.landsOwners.values().size());
    this.landsOwners.values().forEach(landOwner -> out.writeWithCodec(LandOwner.CODEC, landOwner));
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    this.landsOwners.clear();
    in.readWithCount(buf -> {
      var landOwner = buf.readWithCodec(LandOwner.CODEC);
      this.landsOwners.put(landOwner.getId(), landOwner);
    });
  }

  @Override
  public void writeChunkToBuf(ChunkPos chunkPos, FriendlyByteBuf out) {
    for (int i = this.level.getMinSection(); i < this.level.getMaxSection(); i++) {
      var sectionPos = getSectionPos(chunkPos, i);
      var section = this.getOrLoadSection(sectionPos);
      if (section != null) {
        out.writeBoolean(true);
        out.writeVarInt(i);
        out.writeWithCodec(LandSection.CODEC, section);
      }
    }
    out.writeBoolean(false);
  }

  @Override
  public void readChunkFromBuf(ChunkPos chunkPos, FriendlyByteBuf in) {
    while (in.readBoolean()) {
      var y = in.readVarInt();
      var section = in.readWithCodec(LandSection.CODEC);
      var sectionPos = getSectionPos(chunkPos, y);
      if (!this.isSectionOutOfBounds(sectionPos)) {
        this.sections.put(sectionPos, section);
      }
    }
  }

  @Override
  public void flush(ChunkPos chunkPos) {}

  @Override
  public void close() throws IOException {}

  private <T> CompletionStage<T> forRegion(BoundingBox boundingBox,
      LongFunction<Optional<T>> action, T successResult) {
    return this.forRegion(
        SectionPos.blockToSectionCoord(boundingBox.minX()),
        SectionPos.blockToSectionCoord(boundingBox.minY()),
        SectionPos.blockToSectionCoord(boundingBox.minZ()),
        SectionPos.blockToSectionCoord(boundingBox.maxX()),
        SectionPos.blockToSectionCoord(boundingBox.maxY()),
        SectionPos.blockToSectionCoord(boundingBox.maxZ()),
        action, successResult);
  }

  private <T> CompletionStage<T> forRegion(int minX, int minY, int minZ,
      int maxX, int maxY, int maxZ, LongFunction<Optional<T>> action, T successResult) {
    int area = 0;
    for (int x = minX; x < maxX; x++) {
      for (int y = minY; y < maxY; y++) {
        for (int z = minZ; z < maxZ; z++) {
          if (area++ > SYNC_AREA_THRESHOLD) {
            var nextX = x;
            var nextY = y;
            var nextZ = z;
            return this.executor
                .submit(() -> this.forRegion(nextX, nextY, nextZ, maxX, maxY, maxZ, action,
                    successResult))
                .thenCompose(Function.identity());
          }

          var result = action.apply(SectionPos.asLong(x, y, z));
          if (result.isPresent()) {
            return CompletableFuture.completedStage(result.get());
          }
        }
      }
    }
    return CompletableFuture.completedStage(successResult);
  }
}
