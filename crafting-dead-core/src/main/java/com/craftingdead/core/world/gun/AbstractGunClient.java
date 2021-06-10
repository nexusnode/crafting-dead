package com.craftingdead.core.world.gun;

import java.util.HashMap;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.animation.AnimationType;
import com.craftingdead.core.client.animation.GunAnimationController;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.ValidatePendingHitMessage;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.entity.extension.EntitySnapshot;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.attachment.Attachment;
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class AbstractGunClient<T extends AbstractGun> implements GunClient {

  private static final int MUZZLE_FLASH_DURATION_TICKS = 2;

  protected final ClientDist client = CraftingDead.getInstance().getClientDist();

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final T gun;

  private final GunAnimationController animationController = new GunAnimationController();

  private int lastShotCount;

  private int remainingFlashTicks;

  private long secondaryActionSoundStartTimeMs;

  private final Multimap<Integer, PendingHit> livingHitValidationBuffer =
      Multimaps.synchronizedListMultimap(
          Multimaps.newListMultimap(new Int2ObjectLinkedOpenHashMap<>(), ObjectArrayList::new));

  private byte hitValidationTicks = 0;

  public AbstractGunClient(T gun) {
    this.gun = gun;
  }

  protected abstract Optional<SoundEvent> getSecondaryActionSound();

  protected abstract long getSecondaryActionSoundRepeatDelayMs();

  public void handleTick(LivingExtension<?, ?> living) {
    if (living.getEntity() instanceof ClientPlayerEntity
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

    this.animationController.tick(living.getEntity(), this.gun.getItemStack());
  }

  protected abstract float getRecoil();

  protected abstract SoundEvent getShootSound();

  protected abstract Optional<SoundEvent> getDistantShootSound();

  protected abstract Optional<SoundEvent> getSilencedShootSound();

  protected boolean canFlash(LivingExtension<?, ?> living) {
    return this.minecraft.options.getCameraType() == PointOfView.FIRST_PERSON
        && this.gun.getShotCount() != this.lastShotCount && this.gun.getShotCount() > 0;
  }

  public void handleShoot(LivingExtension<?, ?> living) {
    Entity entity = living.getEntity();

    if (this.canFlash(living)) {
      this.remainingFlashTicks = MUZZLE_FLASH_DURATION_TICKS;
    }

    this.lastShotCount = this.gun.getShotCount();

    this.getAnimation(AnimationType.SHOOT).ifPresent(animation -> {
      this.animationController.removeCurrentAnimation();
      this.animationController.addAnimation(animation, null);
    });

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
      Vector3d hitPos, long randomSeed) {
    if (living.getEntity() instanceof ClientPlayerEntity
        && hitEntity instanceof LivingEntity) {
      this.livingHitValidationBuffer.put(hitEntity.getId(),
          new PendingHit((byte) (AbstractGun.HIT_VALIDATION_DELAY_TICKS - this.hitValidationTicks),
              new EntitySnapshot(living.getEntity()),
              new EntitySnapshot(hitEntity), randomSeed));
    }
  }

  public void handleHitEntityPost(LivingExtension<?, ?> living, Entity hitEntity,
      Vector3d hitPos, boolean playSound, boolean headshot) {
    if (!(hitEntity instanceof LivingEntity)) {
      return;
    }

    final World level = hitEntity.level;

    if (headshot) {
      final int particleCount = 12;
      for (int i = 0; i < particleCount; ++i) {
        level.addParticle(
            new BlockParticleData(ParticleTypes.BLOCK, Blocks.BONE_BLOCK.defaultBlockState()),
            hitPos.x(), hitPos.y(), hitPos.z(), 0.0D, 0.0D, 0.0D);
      }
    }

    level.playSound(
        living.getEntity() instanceof PlayerEntity ? (PlayerEntity) living.getEntity() : null,
        hitEntity.blockPosition(), ModSoundEvents.BULLET_IMPACT_FLESH.get(), SoundCategory.PLAYERS,
        1.0F, 1.0F);

    // If local player
    if (hitEntity == this.minecraft.getCameraEntity()) {
      this.client.getCameraManager().randomRecoil(1.5F, false);
    }

    final int particleCount = 12;
    for (int i = 0; i < particleCount; ++i) {
      level.addParticle(
          new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
          hitPos.x(), hitPos.y(), hitPos.z(), 0.0D, 0.0D, 0.0D);
    }
  }

  public void handleHitBlock(LivingExtension<?, ?> living,
      BlockRayTraceResult rayTrace, boolean playSound) {
    Entity entity = living.getEntity();
    Vector3d hitVec3d = rayTrace.getLocation();
    BlockPos blockPos = rayTrace.getBlockPos();
    World level = entity.level;
    BlockState blockState = level.getBlockState(blockPos);

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

      level.playSound(entity instanceof PlayerEntity ? (PlayerEntity) entity : null, blockPos,
          hitSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    final int particleCount = 12;
    for (int i = 0; i < particleCount; ++i) {
      level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockState), hitVec3d.x(),
          hitVec3d.y(), hitVec3d.z(), 0.0D, 0.0D, 0.0D);
    }
  }

  public void handleToggleSecondaryAction(LivingExtension<?, ?> living) {
    this.getSecondaryActionSound().ifPresent(sound -> {
      if (this.gun.isPerformingSecondaryAction()) {
        this.secondaryActionSoundStartTimeMs = Util.getMillis();
        living.getEntity().playSound(sound, 1.0F, 1.0F);
      } else {
        Minecraft.getInstance().getSoundManager().stop(
            sound.getRegistryName(), SoundCategory.PLAYERS);
      }
    });
  }

  @Override
  public boolean isFlashing() {
    return this.remainingFlashTicks > 0;
  }

  @Override
  public GunAnimationController getAnimationController() {
    return this.animationController;
  }
}
