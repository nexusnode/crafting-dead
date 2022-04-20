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

package com.craftingdead.core.world.entity.grenade;

import java.util.OptionalInt;
import com.craftingdead.core.world.damagesource.ModDamageSource;
import com.craftingdead.core.world.entity.BounceableProjectileEntity;
import com.craftingdead.core.world.entity.grenade.FireGrenadeEntity.BounceSound;
import com.craftingdead.core.world.item.GrenadeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class Grenade extends BounceableProjectileEntity {

  private static final BounceSound DEFAULT_BOUNCE_SOUND =
      new BounceSound(SoundEvents.SCAFFOLDING_BREAK, 0.5F, 2F);
  private static final EntityDataAccessor<Boolean> ACTIVATED =
      SynchedEntityData.defineId(Grenade.class, EntityDataSerializers.BOOLEAN);
  private int activatedTicksCount = 0;
  private int deactivatedTicksCount = 0;

  private boolean lastActivated;

  public Grenade(EntityType<? extends Grenade> type, Level level) {
    super(type, level);
  }

  public Grenade(EntityType<? extends Grenade> type, LivingEntity thrower, Level level) {
    super(type, thrower, level);
  }

  public abstract GrenadeItem asItem();

  /**
   * Called every <code>tick()</code> if the grenade is not marked as removed. Prefer using this
   * instead of overriding <code>tick()</code>.
   */
  public abstract void onGrenadeTick();

  /**
   * Called after the activation state is changed.
   */
  public abstract void activatedChanged(boolean activated);

  @Override
  public void tick() {
    super.tick();

    var activated = this.isActivated();
    if (this.lastActivated != activated) {
      this.lastActivated = activated;

      if (!activated) {
        this.deactivatedTicksCount = 0;
      } else {
        this.activatedTicksCount = 0;
      }

      this.activatedChanged(activated);
    }

    if (this.isAlive()) {
      if (this.isActivated()) {
        this.activatedTicksCount++;
      } else {
        this.deactivatedTicksCount++;
      }

      this.onGrenadeTick();
    }

    // Check again if it is alive.
    // Someone could kill the entity in a previous callback
    if (this.isAlive()) {
      this.getMinimumTicksUntilAutoActivation().ifPresent(ticks -> {
        if (!this.isActivated() && this.deactivatedTicksCount >= ticks) {
          this.setActivated(true);
        }
      });
    }

    // Check again if it is alive.
    // Someone could kill the entity in a previous callback
    if (this.isAlive()) {
      this.getMinimumTicksUntilAutoDeactivation().ifPresent(ticks -> {
        if (this.isActivated() && this.activatedTicksCount >= ticks) {
          this.setActivated(false);
        }
      });
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (source.isProjectile()) {
      EntityDamageSource entitySource = (EntityDamageSource) source;
      this.setDeltaMovement(entitySource.getEntity().getLookAngle().scale(1.5D));
    }
    return super.hurt(source, amount);
  }

  @Override
  public void onSurfaceHit(BlockHitResult blockRayTraceResult) {
    var bounceSound = this.getBounceSound(blockRayTraceResult);
    if (this.level.isClientSide()) {
      this.level.playLocalSound(this.getX(), this.getY(), this.getZ(),
          bounceSound.soundEvent(), SoundSource.NEUTRAL, bounceSound.volume(),
          bounceSound.pitch(), false);
    }
  }

  @Override
  public final InteractionResult interact(Player playerEntity, InteractionHand hand) {
    boolean canPickup = !playerEntity.isSecondaryUseActive() && this.canBePickedUp(playerEntity);
    if (canPickup) {
      this.kill();
      playerEntity.addItem(new ItemStack(this.asItem(), 1));
      this.level.playLocalSound(this.getX(), this.getY(), this.getZ(),
          SoundEvents.ITEM_PICKUP,
          SoundSource.PLAYERS, 0.2F,
          (this.random.nextFloat() - this.random.nextFloat()) * 1.4F + 2.0F, false);
    }
    return canPickup ? InteractionResult.SUCCESS : InteractionResult.PASS;
  }

  /**
   * Whether the grenade can be picked up from the ground
   */
  public boolean canBePickedUp(Player playerFrom) {
    return false;
  }

  /**
   * First decimal value is volume, second is pitch.
   */
  public BounceSound getBounceSound(BlockHitResult blockRayTraceResult) {
    return DEFAULT_BOUNCE_SOUND;
  }

  /**
   * Whether this grenade can be activated by its owner using a remote detonator
   */
  public boolean canBeRemotelyActivated() {
    return false;
  }

  /**
   * Whether this grenade is attracting zombies and maybe other entities.
   */
  public boolean isAttracting() {
    return false;
  }

  public void setActivated(boolean activated) {
    this.getEntityData().set(ACTIVATED, activated);
  }

  /**
   * The minimum amount of ticks the grenade will take to automatically activate.
   *
   * @return {@link Integer} - An amount of ticks or <code>null</code>.
   */
  public OptionalInt getMinimumTicksUntilAutoActivation() {
    return OptionalInt.empty();
  }

  /**
   * The minimum amount of ticks the grenade will take to automatically deactivate.
   *
   * @return {@link Integer} - An amount of ticks or <code>null</code>.
   */
  public OptionalInt getMinimumTicksUntilAutoDeactivation() {
    return OptionalInt.empty();
  }

  public boolean isActivated() {
    return this.getEntityData().get(ACTIVATED);
  }

  public int getActivatedTicksCount() {
    return this.activatedTicksCount;
  }

  @Override
  public boolean isPickable() {
    return true;
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("activated", this.isActivated());
    compound.putInt("activatedTicksCount", this.activatedTicksCount);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setActivated(compound.getBoolean("activated"));
    this.activatedTicksCount = compound.getInt("activatedTicksCount");
  }

  @Override
  public void writeSpawnData(FriendlyByteBuf buffer) {
    super.writeSpawnData(buffer);
    buffer.writeInt(this.activatedTicksCount);
  }

  @Override
  public void readSpawnData(FriendlyByteBuf buffer) {
    super.readSpawnData(buffer);
    this.activatedTicksCount = buffer.readInt();
  }

  @Override
  protected void defineSynchedData() {
    this.getEntityData().define(ACTIVATED, false);
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  public DamageSource createDamageSource() {
    return ModDamageSource.grenade(this, this.getSource().orElse(null));
  }

  public boolean isPushedByFluid() {
    return false;
  }

  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    double range = this.getBoundingBox().getSize() * 10.0D;
    if (Double.isNaN(range)) {
      range = 1.0D;
    }

    range *= 64.0D * getViewScale();
    return distance < range * range;
  }
}
