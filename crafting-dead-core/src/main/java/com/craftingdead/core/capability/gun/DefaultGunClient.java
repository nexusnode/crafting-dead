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

package com.craftingdead.core.capability.gun;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimationController;
import com.craftingdead.core.capability.living.EntitySnapshot;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.ValidatePendingHitMessage;
import com.craftingdead.core.util.ModSoundEvents;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class DefaultGunClient<G extends DefaultGun> implements IGunClient {

  private static final int MUZZLE_FLASH_DURATION_TICKS = 2;

  private final ClientDist client = CraftingDead.getInstance().getClientDist();

  private final Minecraft minecraft = Minecraft.getInstance();

  protected final G gun;

  private final GunAnimationController animationController = new GunAnimationController();

  private int lastShotCount;

  private int remainingFlashTicks;

  private long rightMouseActionSoundStartTimeMs;

  private final Multimap<Integer, PendingHit> livingHitValidationBuffer =
      Multimaps.newMultimap(new Int2ObjectLinkedOpenHashMap<>(), ObjectArrayList::new);

  private byte hitValidationTicks = 0;

  public DefaultGunClient(G gun) {
    this.gun = gun;
  }

  @Override
  public void handleTick(ILiving<?, ?> living, ItemStack itemStack) {
    if (living.getEntity() instanceof ClientPlayerEntity
        && this.livingHitValidationBuffer.size() > 0
        && this.hitValidationTicks++ >= DefaultGun.HIT_VALIDATION_DELAY_TICKS) {
      this.hitValidationTicks = 0;
      NetworkChannel.PLAY.getSimpleChannel().sendToServer(
          new ValidatePendingHitMessage(new HashMap<>(this.livingHitValidationBuffer.asMap())));
      this.livingHitValidationBuffer.clear();
    }

    SoundEvent rightMouseActionSound = this.gun.getGunItem().getRightMouseActionSound().get();
    long rightMouseActionSoundDelta = Util.milliTime() - this.rightMouseActionSoundStartTimeMs + 50;
    if (this.gun.isPerformingRightMouseAction()
        && this.gun.getGunItem().getRightMouseActionSoundRepeatDelayMs() > 0L
        && rightMouseActionSoundDelta >= this.gun.getGunItem()
            .getRightMouseActionSoundRepeatDelayMs()
        && rightMouseActionSound != null) {
      this.rightMouseActionSoundStartTimeMs = Util.milliTime();
      living.getEntity().playSound(rightMouseActionSound, 1.0F, 1.0F);
    }

    if (!this.gun.isTriggerPressed()) {
      this.lastShotCount = 0;
    }

    if (this.remainingFlashTicks > 0) {
      this.remainingFlashTicks--;
    }
  }

  protected boolean canFlash(ILiving<?, ?> living, ItemStack itemStack) {
    return this.minecraft.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON
        && this.gun.getShotCount() != this.lastShotCount && this.gun.getShotCount() > 0;
  }

  @Override
  public void handleShoot(ILiving<?, ?> living, ItemStack itemStack) {
    Entity entity = living.getEntity();

    if (this.canFlash(living, itemStack)) {
      this.remainingFlashTicks = MUZZLE_FLASH_DURATION_TICKS;
    }

    this.lastShotCount = this.gun.getShotCount();

    if (entity instanceof ClientPlayerEntity) {
      final float baseJolt = 0.05F;
      this.client.getCameraManager()
          .joltCamera(1.0F - this.gun.getAccuracy(living, itemStack) + baseJolt, true);
    }

    this.gun.getAnimation(AnimationType.SHOOT).ifPresent(animation -> {
      this.animationController.removeCurrentAnimation();
      this.animationController.addAnimation(animation, null);
    });

    double sqrDistance = this.minecraft.gameRenderer.getActiveRenderInfo().getProjectedView()
        .squareDistanceTo(entity.getPosX(), entity.getPosY(), entity.getPosZ());

    boolean distant = sqrDistance > 1600.0D;

    final Supplier<SoundEvent> defaultShootSound = this.gun.getGunItem().getShootSound();

    @Nullable
    final SoundEvent shootSound;
    boolean amplifyDistantSound = this.gun.gunItem.getDistantShootSound().isPresent();
    if (this.gun.getAttachments().stream().anyMatch(AttachmentItem::isSoundSuppressor)) {
      shootSound = distant ? null
          : this.gun.getGunItem().getSilencedShootSound().orElseGet(defaultShootSound);
    } else {
      shootSound =
          distant ? this.gun.getGunItem().getDistantShootSound().orElseGet(defaultShootSound)
              : defaultShootSound.get();
    }

    if (!entity.isSilent() && shootSound != null) {
      // Sounds have to be played on the main thread (unfortunately)
      this.minecraft.execute(() -> entity.getEntityWorld().playSound(
          entity.getPosX(), entity.getPosY(), entity.getPosZ(), shootSound,
          entity.getSoundCategory(), distant && amplifyDistantSound ? 8.0F : 1.0F, 1.0F, true));
    }
  }

  @Override
  public void handleHitEntityPre(ILiving<?, ?> living, ItemStack itemStack, Entity hitEntity,
      Vector3d hitPos, long randomSeed) {
    if (living.getEntity() instanceof ClientPlayerEntity) {
      this.livingHitValidationBuffer.put(hitEntity.getEntityId(),
          new PendingHit((byte) (DefaultGun.HIT_VALIDATION_DELAY_TICKS - this.hitValidationTicks),
              new EntitySnapshot(living.getEntity()),
              new EntitySnapshot(hitEntity), randomSeed));
    }
  }

  @Override
  public void handleHitEntityPost(ILiving<?, ?> living, ItemStack itemStack, Entity hitEntity,
      Vector3d hitPos, boolean playSound, boolean headshot) {
    World world = hitEntity.getEntityWorld();
    Entity entity = living.getEntity();

    if (headshot) {
      final int particleCount = 12;
      for (int i = 0; i < particleCount; ++i) {
        world.addParticle(
            new BlockParticleData(ParticleTypes.BLOCK, Blocks.BONE_BLOCK.getDefaultState()),
            hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0.0D, 0.0D, 0.0D);
      }
    }

    world.playSound(entity instanceof PlayerEntity ? (PlayerEntity) entity : null,
        hitEntity.getPosition(), ModSoundEvents.BULLET_IMPACT_FLESH.get(), SoundCategory.PLAYERS,
        1.0F, 1.0F);

    // If local player
    if (hitEntity instanceof ClientPlayerEntity) {
      this.client.getCameraManager().joltCamera(1.5F, false);
    }

    final int particleCount = 12;
    for (int i = 0; i < particleCount; ++i) {
      world.addParticle(
          new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
          hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0.0D, 0.0D, 0.0D);
    }
  }

  @Override
  public void handleHitBlock(ILiving<?, ?> living, ItemStack itemStack,
      BlockRayTraceResult rayTrace, boolean playSound) {
    final Entity entity = living.getEntity();
    Vector3d hitVec3d = rayTrace.getHitVec();
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    World world = entity.getEntityWorld();

    // Gets the hit sound to be played
    SoundEvent hitSound = ModSoundEvents.BULLET_IMPACT_DIRT.get();
    Material blockMaterial = blockState.getMaterial();
    if (blockMaterial == Material.WOOD) {
      hitSound = ModSoundEvents.BULLET_IMPACT_WOOD.get();
    } else if (blockMaterial == Material.ROCK) {
      hitSound = ModSoundEvents.BULLET_IMPACT_STONE.get();
    } else if (blockMaterial == Material.IRON) {
      hitSound = Math.random() > 0.5D ? ModSoundEvents.BULLET_IMPACT_METAL.get()
          : ModSoundEvents.BULLET_IMPACT_METAL2.get();
    } else if (blockMaterial == Material.GLASS) {
      hitSound = ModSoundEvents.BULLET_IMPACT_GLASS.get();
    }

    world.playSound(entity instanceof PlayerEntity ? (PlayerEntity) entity : null, blockPos,
        hitSound, SoundCategory.BLOCKS, 1.0F, 1.0F);

    final int particleCount = 12;
    for (int i = 0; i < particleCount; ++i) {
      world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockState), hitVec3d.getX(),
          hitVec3d.getY(), hitVec3d.getZ(), 0.0D, 0.0D, 0.0D);
    }
  }

  @Override
  public void handleToggleRightMouseAction(ILiving<?, ?> living) {
    SoundEvent rightMouseActionSound = this.gun.getGunItem().getRightMouseActionSound().get();
    if (rightMouseActionSound != null) {
      if (this.gun.isPerformingRightMouseAction()) {
        this.rightMouseActionSoundStartTimeMs = Util.milliTime();
        living.getEntity().playSound(rightMouseActionSound, 1.0F, 1.0F);
      } else {
        Minecraft.getInstance().getSoundHandler()
            .stop(rightMouseActionSound.getRegistryName(), SoundCategory.PLAYERS);
      }
    }
  }

  @Override
  public float getPartialTicks() {
    return this.minecraft.getRenderPartialTicks();
  }

  @Override
  public boolean isFlashing() {
    return this.remainingFlashTicks > 0;
  }

  @Override
  public Optional<GunAnimationController> getAnimationController() {
    return Optional.of(this.animationController);
  }
}
