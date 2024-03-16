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

package com.craftingdead.immerse.game.survival;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.entity.extension.LivingHandlerType;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.core.world.entity.grenade.FragGrenade;
import com.craftingdead.core.world.entity.grenade.Grenade;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.event.WaterDecayEvent;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.AddKillFeedEntryMessage;
import com.craftingdead.immerse.world.ImmerseDamageSource;
import com.craftingdead.immerse.world.KillFeedEntry;
import com.craftingdead.immerse.world.level.extension.LegacyBase;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.network.PacketDistributor;

public class SurvivalPlayerHandler implements PlayerHandler {

  public static final LivingHandlerType<SurvivalPlayerHandler> TYPE =
      new LivingHandlerType<>(new ResourceLocation(CraftingDeadImmerse.ID, "survival_player"));

  private static final EntityDataAccessor<Integer> DAYS_SURVIVED =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> ZOMBIES_KILLED =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> PLAYERS_KILLED =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Optional<UUID>> BASE_ID =
      new EntityDataAccessor<>(0x03, EntityDataSerializers.OPTIONAL_UUID);

  private static final EntityDataAccessor<Integer> WATER =
      new EntityDataAccessor<>(0x04, EntityDataSerializers.INT);

  private static final EntityDataAccessor<Integer> MAX_WATER =
      new EntityDataAccessor<>(0x05, EntityDataSerializers.INT);

  private final SurvivalGame game;

  private final PlayerExtension<?> player;

  private final SynchedData dataManager = new SynchedData();

  private int waterTicks;

  public SurvivalPlayerHandler(SurvivalGame game, PlayerExtension<?> player) {
    this.game = game;
    this.player = player;
    this.dataManager.register(DAYS_SURVIVED, 0);
    this.dataManager.register(ZOMBIES_KILLED, 0);
    this.dataManager.register(PLAYERS_KILLED, 0);
    this.dataManager.register(BASE_ID, Optional.empty());
    this.dataManager.register(WATER, 20);
    this.dataManager.register(MAX_WATER, 20);
  }

  public Optional<LegacyBase> getBase() {
    return this.getBaseId()
        .map(baseId -> LevelExtension.getOrThrow(this.player.level())
            .getLandManager().getLandOwner(baseId))
        .map(LegacyBase.class::cast);
  }

  public Optional<UUID> getBaseId() {
    return this.dataManager.get(BASE_ID);
  }

  public void setBaseId(@Nullable UUID baseId) {
    this.dataManager.set(BASE_ID, Optional.ofNullable(baseId));
  }

  @Override
  public void tick() {
    if (this.player.entity() instanceof ServerPlayer player) {
      int aliveTicks = player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
      this.setDaysSurvived(aliveTicks / 20 / 60 / 20);
    }
  }

  @Override
  public void playerTick() {
    if (this.game instanceof SurvivalServer server) {
      server.getThirstSettings().ifPresent(this::updateThirst);
    }
  }

  private void updateThirst(ThirstSettings settings) {
    var entity = this.player.entity();
    if (entity.getLevel().getDifficulty() != Difficulty.PEACEFUL
        && !entity.getAbilities().invulnerable) {
      this.waterTicks++;
      if (this.getWater() <= 0) {
        if (this.waterTicks >= settings.damageIntervalTicks() && this.getWater() == 0) {
          entity.hurt(ImmerseDamageSource.DEHYDRATION, 1.0F);
          this.waterTicks = 0;
        }
      } else if (this.waterTicks >= settings.decayIntervalTicks()) {
        if (!MinecraftForge.EVENT_BUS.post(new WaterDecayEvent(this.player.entity()))) {
          this.setWater(this.getWater() - settings.decayAmountFor(entity));
        }
        this.waterTicks = 0;
      }
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

  @Override
  public boolean handleDeath(DamageSource cause) {
    
    System.out.println(cause.getDirectEntity());
    System.out.println(cause.getEntity());
    
    Entity attackItemEntity = cause.getDirectEntity();
    Entity attacker = cause.getEntity();
    Entity entityPlayer = this.player.entity();
   
    if(attacker.getStringUUID().equalsIgnoreCase(entityPlayer.getStringUUID())){
      if(attackItemEntity instanceof Grenade) {
        NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
            new AddKillFeedEntryMessage(new KillFeedEntry((LivingEntity) entityPlayer,
                entityPlayer, new ItemStack(ModItems.FRAG_GRENADE.get()),
                KillFeedEntry.Type.GRENADE)));
      } else {
        NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
            new AddKillFeedEntryMessage(new KillFeedEntry((LivingEntity) entityPlayer,
                entityPlayer, this.player.mainHandItem(),
                KillFeedEntry.Type.SUICIDE))); 
      }
    }
    
    if(cause == ImmerseDamageSource.DEHYDRATION || cause == ImmerseDamageSource.HUNGER) {
      NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
          new AddKillFeedEntryMessage(new KillFeedEntry((LivingEntity) entityPlayer,
              entityPlayer, this.player.mainHandItem(),
              cause == ImmerseDamageSource.DEHYDRATION ? KillFeedEntry.Type.DEHYDRATION : KillFeedEntry.Type.HUNGER)));
    }
    if(cause == DamageSource.FALL) {
      NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
          new AddKillFeedEntryMessage(new KillFeedEntry((LivingEntity) entityPlayer,
              entityPlayer, this.player.mainHandItem(),
              KillFeedEntry.Type.FALL)));
    }
    return PlayerHandler.super.handleDeath(cause);
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

  public int getWater() {
    return this.dataManager.get(WATER);
  }

  public void setWater(int water) {
    this.dataManager.set(WATER, Mth.clamp(water, 0, this.getMaxWater()));
  }

  public void hydrate() {
    this.setWater(this.getMaxWater());
  }

  public int getMaxWater() {
    return this.dataManager.get(MAX_WATER);
  }

  public void setMaxWater(int maxWater) {
    this.dataManager.set(MAX_WATER, maxWater);
  }

  @Override
  public boolean isCombatModeEnabled() {
    return false;
  }

  @Override
  public void copyFrom(PlayerExtension<ServerPlayer> that, boolean wasDeath) {
    if (!wasDeath) {
      that.getHandler(TYPE).ifPresent(handler -> {
        this.setDaysSurvived(handler.getDaysSurvived());
        this.setZombiesKilled(handler.getZombiesKilled());
        this.setPlayersKilled(handler.getPlayersKilled());
        handler.getBaseId().ifPresent(this::setBaseId);
        this.setWater(handler.getWater());
        this.setMaxWater(handler.getMaxWater());
      });
    }
  }

  @Override
  public boolean handleBlockBreak(BlockPos pos, BlockState block, MutableInt xp) {
    LevelExtension.getOrThrow(player.level()).getLandManager()
        .getLandOwnerAt(pos)
        .ifPresent(base -> base.playerRemovedBlock(player, pos));
    return false;
  }

  @Override
  public boolean handleBlockPlace(BlockSnapshot replacedBlock, BlockState placedBlock,
      BlockState placedAgainst) {
    LevelExtension.getOrThrow(player.level()).getLandManager()
        .getLandOwnerAt(replacedBlock.getPos())
        .ifPresent(base -> base.playerPlacedBlock(player, replacedBlock.getPos()));
    return false;
  }

  @Override
  public boolean handleMultiBlockPlace(List<BlockSnapshot> replacedBlocks, BlockState placedBlock,
      BlockState placedAgainst) {
    var pos = new ArrayList<BlockPos>(replacedBlocks.size());
    for (BlockSnapshot block : replacedBlocks) {
      // Work around neighbours updating their state when you place a block but not actually being
      // replaced
      if (!block.getCurrentBlock().is(block.getReplacedBlock().getBlock())) {
        pos.add(block.getPos());
      }
    }

    // Use the first replaced block as point of reference
    var refBlock = pos.get(0);
    LevelExtension.getOrThrow(player.level()).getLandManager()
        .getLandOwnerAt(refBlock)
        .ifPresent(base -> base.playerPlacedBlock(player, pos.toArray(new BlockPos[0])));
    return false;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    tag.putInt("zombiesKilled", this.getZombiesKilled());
    tag.putInt("playersKilled", this.getPlayersKilled());
    this.getBaseId().ifPresent(baseId -> tag.putUUID("baseId", baseId));
    tag.putInt("water", this.getWater());
    tag.putInt("maxWater", this.getMaxWater());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.setZombiesKilled(tag.getInt("zombiesKilled"));
    this.setPlayersKilled(tag.getInt("playersKilled"));
    if (tag.hasUUID("baseId")) {
      this.setBaseId(tag.getUUID("baseId"));
    }
    this.setWater(tag.getInt("water"));
    this.setMaxWater(tag.getInt("maxWater"));
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
