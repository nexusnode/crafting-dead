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

package com.craftingdead.core.world.item.gun;

import java.util.HashMap;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.client.animation.AnimationController;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.ValidatePendingHitMessage;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.entity.extension.EntitySnapshot;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.Util;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public abstract class AbstractGunClient<T extends AbstractGun> implements GunClient {

  private static final int MUZZLE_FLASH_DURATION_TICKS = 2;

  protected final ClientDist client = CraftingDead.getInstance().getClientDist();

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final T gun;

  private final AnimationController animationController = new AnimationController();

  private int lastShotCount;

  private int remainingFlashTicks;

  private long secondaryActionSoundStartTimeMs;

  private final Multimap<Integer, PendingHit> livingHitValidationBuffer =
      Multimaps.synchronizedListMultimap(
          Multimaps.newListMultimap(new Int2ObjectLinkedOpenHashMap<>(), ObjectArrayList::new));

  private byte hitValidationTicks = 0;

  @Nullable
  private Animation currentShootAnimation;

  public AbstractGunClient(T gun) {
    this.gun = gun;
  }

  public float getPartialTick() {
    return this.minecraft.getFrameTime();
  }

  protected abstract Optional<SoundEvent> getSecondaryActionSound();

  protected abstract long getSecondaryActionSoundRepeatDelayMs();

  public void handleTick(LivingExtension<?, ?> living) {
    if (living.getEntity() instanceof LocalPlayer
        && this.livingHitValidationBuffer.size() > 0
        && this.hitValidationTicks++ >= AbstractGun.HIT_VALIDATION_DELAY_TICKS) {
      this.hitValidationTicks = 0;
      NetworkChannel.PLAY.getSimpleChannel().sendToServer(
          new ValidatePendingHitMessage(new HashMap<>(this.livingHitValidationBuffer.asMap())));
      this.livingHitValidationBuffer.clear();
    }

    this.getSecondaryActionSound().ifPresent(sound -> {
      long delta =
          Util.getMillis() - this.secondaryActionSoundStartTimeMs + 50;
      long repeatDelay = this.getSecondaryActionSoundRepeatDelayMs();
      if (this.gun.isPerformingSecondaryAction()
          && repeatDelay > 0L
          && delta >= repeatDelay) {
        this.secondaryActionSoundStartTimeMs = Util.getMillis();
        living.getEntity().playSound(sound, 1.0F, 1.0F);
      }
    });

    if (!this.gun.isTriggerPressed()) {
      this.lastShotCount = 0;
    }

    if (this.remainingFlashTicks > 0) {
      this.remainingFlashTicks--;
    }

    this.animationController.tick();
  }

  protected abstract float getRecoil();

  protected abstract SoundEvent getShootSound();

  protected abstract Optional<SoundEvent> getDistantShootSound();

  protected abstract Optional<SoundEvent> getSilencedShootSound();

  protected boolean canFlash(LivingExtension<?, ?> living) {
    return this.minecraft.options.getCameraType() == CameraType.FIRST_PERSON
        && this.gun.getShotCount() != this.lastShotCount && this.gun.getShotCount() > 0;
  }

  public void handleShoot(LivingExtension<?, ?> living) {
    Entity entity = living.getEntity();

    if (this.canFlash(living)) {
      this.remainingFlashTicks = MUZZLE_FLASH_DURATION_TICKS;
    }

    this.lastShotCount = this.gun.getShotCount();

    if (this.currentShootAnimation != null) {
      this.currentShootAnimation.remove();
    }

    this.currentShootAnimation = this.getAnimation(GunAnimationEvent.SHOOT);
    this.animationController.addAnimation(this.currentShootAnimation);

    if (entity == this.minecraft.getCameraEntity()) {
      this.client.getCameraManager().randomRecoil(this.getRecoil(), true);
    }

    double sqrDistance = this.minecraft.gameRenderer.getMainCamera().getPosition()
        .distanceToSqr(entity.getX(), entity.getY(), entity.getZ());

    boolean farAway = sqrDistance > 1600.0D;

    final SoundEvent defaultShootSound = this.getShootSound();

    @Nullable
    final SoundEvent shootSound;
    boolean amplifyDistantSound = this.getDistantShootSound().isPresent();
    if (this.gun.getAttachments().stream().anyMatch(Attachment::isSoundSuppressor)) {
      shootSound = farAway
          ? null
          : this.getSilencedShootSound().orElse(defaultShootSound);
    } else {
      shootSound = farAway
          ? this.getDistantShootSound().orElse(defaultShootSound)
          : defaultShootSound;
    }

    if (!entity.isSilent() && shootSound != null) {
      // Sounds have to be played on the main thread (unfortunately)
      this.minecraft.execute(() -> entity.level.playLocalSound(
          entity.getX(), entity.getY(), entity.getZ(), shootSound,
          entity.getSoundSource(), farAway && amplifyDistantSound ? 8.0F : 1.0F, 1.0F, true));
    }
  }


  public void handleHitEntityPre(LivingExtension<?, ?> living, Entity hitEntity,
      Vec3 hitPos, long randomSeed) {
    if (living.getEntity() instanceof LocalPlayer
        && hitEntity instanceof LivingEntity) {
      this.livingHitValidationBuffer.put(hitEntity.getId(),
          new PendingHit((byte) (AbstractGun.HIT_VALIDATION_DELAY_TICKS - this.hitValidationTicks),
              new EntitySnapshot(living.getEntity(), this.minecraft.getFrameTime()),
              new EntitySnapshot(hitEntity, this.minecraft.getFrameTime()), randomSeed,
              this.gun.getShotCount()));
    }
  }

  public void handleHitEntityPost(LivingExtension<?, ?> living, Entity hitEntity,
      Vec3 hitPos, boolean playSound, boolean headshot) {
    if (!(hitEntity instanceof LivingEntity)) {
      return;
    }

    final Level level = hitEntity.level;

    if (headshot) {
      final int particleCount = 12;
      for (int i = 0; i < particleCount; ++i) {
        level.addParticle(
            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BONE_BLOCK.defaultBlockState()),
            hitPos.x(), hitPos.y(), hitPos.z(), 0.0D, 0.0D, 0.0D);
      }
    }

    level.playSound(
        living.getEntity() instanceof Player ? (Player) living.getEntity() : null,
        hitEntity.blockPosition(), ModSoundEvents.BULLET_IMPACT_FLESH.get(), SoundSource.PLAYERS,
        1.0F, 1.0F);

    // If local player
    if (hitEntity == this.minecraft.getCameraEntity()) {
      this.client.getCameraManager().randomRecoil(1.5F, false);
    }

    final int particleCount = 12;
    for (int i = 0; i < particleCount; ++i) {
      level.addParticle(
          new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
          hitPos.x(), hitPos.y(), hitPos.z(), 0.0D, 0.0D, 0.0D);
    }
  }

  public void handleHitBlock(LivingExtension<?, ?> living,
      BlockHitResult result, BlockState blockState, boolean playSound) {
    Entity entity = living.getEntity();
    Level level = entity.level;
    Vec3 location = result.getLocation();

    if (playSound) {
      // Gets the hit sound to be played
      SoundEvent hitSound = ModSoundEvents.BULLET_IMPACT_DIRT.get();
      Material blockMaterial = blockState.getMaterial();
      if (blockMaterial == Material.WOOD) {
        hitSound = ModSoundEvents.BULLET_IMPACT_WOOD.get();
      } else if (blockMaterial == Material.STONE) {
        hitSound = ModSoundEvents.BULLET_IMPACT_STONE.get();
      } else if (blockMaterial == Material.METAL) {
        hitSound = Math.random() > 0.5D ? ModSoundEvents.BULLET_IMPACT_METAL.get()
            : ModSoundEvents.BULLET_IMPACT_METAL2.get();
      } else if (blockMaterial == Material.GLASS) {
        hitSound = ModSoundEvents.BULLET_IMPACT_GLASS.get();
      }

      level.playSound(entity instanceof Player ? (Player) entity : null,
          result.getBlockPos(), hitSound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    final int particleCount = 12;
    for (int i = 0; i < particleCount; ++i) {
      level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), location.x(),
          location.y(), location.z(), 0.0D, 0.0D, 0.0D);
    }
  }

  public void handleToggleSecondaryAction(LivingExtension<?, ?> living) {
    this.getSecondaryActionSound().ifPresent(sound -> {
      if (this.gun.isPerformingSecondaryAction()) {
        this.secondaryActionSoundStartTimeMs = Util.getMillis();
        living.getEntity().playSound(sound, 1.0F, 1.0F);
      } else {
        this.minecraft.getSoundManager().stop(sound.getRegistryName(), SoundSource.PLAYERS);
      }
    });
  }

  @Override
  public boolean isFlashing() {
    return this.remainingFlashTicks > 0;
  }

  @Override
  public AnimationController getAnimationController() {
    return this.animationController;
  }
}
