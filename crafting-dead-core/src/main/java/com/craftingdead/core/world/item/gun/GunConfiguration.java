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

package com.craftingdead.core.world.item.gun;

import com.craftingdead.core.util.FunctionalUtil;
import com.craftingdead.core.world.item.gun.Gun.SecondaryActionTrigger;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class GunConfiguration extends ForgeRegistryEntry<GunConfiguration> {

  public static final Codec<GunConfiguration> DIRECT_CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Codec.INT.fieldOf("fire_delay_ms")
                  .forGetter(GunConfiguration::getFireDelayMs),
              Codec.FLOAT.fieldOf("damage")
                  .forGetter(GunConfiguration::getDamage),
              Codec.INT.fieldOf("reload_duration_ticks")
                  .forGetter(GunConfiguration::getReloadDurationTicks),
              Codec.FLOAT.fieldOf("accuracy_percent")
                  .forGetter(GunConfiguration::getAccuracyPercent),
              Codec.FLOAT.fieldOf("recoil")
                  .forGetter(GunConfiguration::getRecoil),
              Codec.INT.optionalFieldOf("rounds_per_shot", 1)
                  .forGetter(GunConfiguration::getRoundsPerShot),
              Codec.DOUBLE.fieldOf("range")
                  .forGetter(GunConfiguration::getRange),
              Codec.BOOL.optionalFieldOf("crosshair_enabled", true)
                  .forGetter(GunConfiguration::isCrosshairEnabled),
              Gun.SecondaryActionTrigger.CODEC.fieldOf("secondary_action_trigger")
                  .forGetter(GunConfiguration::getSecondaryActionTrigger),
              Codec.list(FireMode.CODEC)
                  .optionalFieldOf("fire_modes", List.of(FireMode.SEMI))
                  // Use Guava's ImmutableSet as it maintains insertion order.
                  .<Set<FireMode>>xmap(ImmutableSet::copyOf, ArrayList::new)
                  .forGetter(GunConfiguration::getFireModes),
              AimAttributes.CODEC
                  .optionalFieldOf("aim_settings")
                  .forGetter(GunConfiguration::getAimSettings),
              Sounds.CODEC
                  .fieldOf("sounds")
                  .forGetter(GunConfiguration::getSounds))
          .apply(instance, GunConfiguration::new));

  /**
   * Time between shots in milliseconds.
   */
  private final int fireDelayMs;

  /**
   * Damage inflicted by a single shot from this gun.
   */
  private final float damage;

  /**
   * The duration of time this gun takes to reload in ticks.
   */
  private final int reloadDurationTicks;

  /**
   * Accuracy as percentage.
   */
  private final float accuracyPercent;

  /**
   * Recoil.
   */
  private final float recoil;

  /**
   * Amount of rounds to be fired in a single shot. e.g. for shotguns
   */
  private final int roundsPerShot;

  /**
   * Range in blocks.
   */
  private final double range;

  /**
   * Whether the crosshair should be rendered or not while holding this item.
   */
  private final boolean crosshairEnabled;

  /**
   * Type of right mouse action. E.g. hold for minigun barrel rotation, click for toggling aim.
   */
  private final Gun.SecondaryActionTrigger secondaryActionTrigger;

  /**
   * {@link FireMode}s the gun can cycle through.
   */
  private final Set<FireMode> fireModes;

  private final Optional<AimAttributes> aimAttributes;
  private final Sounds sounds;

  public GunConfiguration(int fireDelayMs, float damage, int reloadDurationTicks,
      float accuracyPercent, float recoil, int roundsPerShot, double range,
      boolean crosshairVisible, SecondaryActionTrigger secondaryActionTrigger,
      Set<FireMode> fireModes, Optional<AimAttributes> aimAttributes, Sounds sounds) {
    this.fireDelayMs = fireDelayMs;
    this.damage = damage;
    this.reloadDurationTicks = reloadDurationTicks;
    this.accuracyPercent = accuracyPercent;
    this.recoil = recoil;
    this.roundsPerShot = roundsPerShot;
    this.range = range;
    this.crosshairEnabled = crosshairVisible;
    this.secondaryActionTrigger = secondaryActionTrigger;
    this.fireModes = fireModes;
    this.aimAttributes = aimAttributes;
    this.sounds = sounds;
  }

  public int getFireDelayMs() {
    return this.fireDelayMs;
  }

  public int getFireRateRPM() {
    return 60000 / this.getFireDelayMs();
  }

  public float getDamage() {
    return this.damage;
  }

  public int getReloadDurationTicks() {
    return this.reloadDurationTicks;
  }

  public float getAccuracyPercent() {
    return this.accuracyPercent;
  }

  public float getRecoil() {
    return this.recoil;
  }

  public int getRoundsPerShot() {
    return this.roundsPerShot;
  }

  public double getRange() {
    return this.range;
  }

  public boolean isCrosshairEnabled() {
    return this.crosshairEnabled;
  }

  public Gun.SecondaryActionTrigger getSecondaryActionTrigger() {
    return this.secondaryActionTrigger;
  }

  public Set<FireMode> getFireModes() {
    return this.fireModes;
  }

  public Optional<AimAttributes> getAimSettings() {
    return this.aimAttributes;
  }

  public Sounds getSounds() {
    return this.sounds;
  }

  public SoundEvent getShootSound() {
    return this.sounds.shootSound().get();
  }

  public Optional<SoundEvent> getDistantShootSound() {
    return Optional.ofNullable(this.sounds.distantShootSound()).map(Supplier::get);
  }

  public Optional<SoundEvent> getSilencedShootSound() {
    return Optional.ofNullable(this.sounds.silencedShootSound()).map(Supplier::get);
  }

  public Optional<SoundEvent> getReloadSound() {
    return Optional.ofNullable(this.sounds.reloadSound()).map(Supplier::get);
  }

  public Optional<SoundEvent> getSecondaryActionSound() {
    return FunctionalUtil.optional(this.sounds.secondaryActionSound);
  }

  public long getSecondaryActionSoundRepeatDelayMs() {
    return this.sounds.secondaryActionSoundRepeatDelay;
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * @param shootSound - Sound to play for each shot of the gun.
   * @param distantShootSound - Sound to play for each shot of the gun when far away.
   * @param silencedShootSound - A 'silenced' version of the shoot sound.
   * @param reloadSound - Sound to play whilst the gun is being reloaded.
   * @param secondaryActionSound - Sound to be played when performing the right mouse action.
   * @param secondaryActionSoundRepeatDelay - A delay in milliseconds between repeating the
   *        secondary action sound.
   */
  public record Sounds(
      Supplier<SoundEvent> shootSound,
      Supplier<SoundEvent> distantShootSound,
      Supplier<SoundEvent> silencedShootSound,
      Supplier<SoundEvent> reloadSound,
      Supplier<SoundEvent> secondaryActionSound,
      long secondaryActionSoundRepeatDelay) {

    public static final Codec<Sounds> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .fieldOf("shoot_sound")
                    .xmap(FunctionalUtil::supplier, Supplier::get)
                    .forGetter(Sounds::shootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("distant_shoot_sound", null)
                    .xmap(FunctionalUtil::supplier, Supplier::get)
                    .forGetter(Sounds::distantShootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("silenced_shoot_sound", null)
                    .xmap(FunctionalUtil::supplier, Supplier::get)
                    .forGetter(Sounds::silencedShootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("reload_sound", null)
                    .xmap(FunctionalUtil::supplier, Supplier::get)
                    .forGetter(Sounds::reloadSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("secondary_action_sound", null)
                    .xmap(FunctionalUtil::supplier, Supplier::get)
                    .forGetter(Sounds::secondaryActionSound),
                Codec.LONG.optionalFieldOf("secondary_action_sound_repeat_delay", -1L)
                    .forGetter(Sounds::secondaryActionSoundRepeatDelay))
            .apply(instance, Sounds::new));
  }

  public static class Builder {

    /*
     * General attributes
     */
    private int fireDelayMs;
    private float damage;
    private int reloadDurationTicks;
    private int roundsPerShot = 1;
    private float accuracyPercent;
    private float recoil;
    private double range;
    private boolean crosshairEnabled = true;
    private Gun.SecondaryActionTrigger rightMouseActionTriggerType =
        Gun.SecondaryActionTrigger.TOGGLE;
    private final Set<FireMode> fireModes = EnumSet.noneOf(FireMode.class);

    /*
     * Aim attributes
     */
    private boolean aimable;
    private boolean boltAction;
    private Map<ResourceLocation, Float> pitchOffset = new HashMap<>();

    /*
     * Sounds
     */
    private Supplier<SoundEvent> shootSound;
    private Supplier<SoundEvent> distantShootSound = () -> null;
    private Supplier<SoundEvent> silencedShootSound = () -> null;
    private Supplier<SoundEvent> reloadSound = () -> null;
    private Supplier<SoundEvent> secondaryActionSound = () -> null;
    private long secondaryActionSoundRepeatDelayMs = -1L;

    public Builder setFireDelayMs(int fireDelayMs) {
      this.fireDelayMs = fireDelayMs;
      return this;
    }

    public Builder setDamage(float damage) {
      this.damage = damage;
      return this;
    }

    public Builder setReloadDurationTicks(int reloadDurationTicks) {
      this.reloadDurationTicks = reloadDurationTicks;
      return this;
    }

    public Builder setRoundsPerShot(int roundsPerShot) {
      this.roundsPerShot = roundsPerShot;
      return this;
    }

    public Builder setCrosshairEnabled(boolean crosshairEnabled) {
      this.crosshairEnabled = crosshairEnabled;
      return this;
    }

    public Builder setAccuracy(float accuracy) {
      this.accuracyPercent = accuracy;
      return this;
    }

    public Builder setRecoil(float recoil) {
      this.recoil = recoil;
      return this;
    }

    public Builder setRange(double range) {
      this.range = range;
      return this;
    }

    public Builder setRightMouseActionTriggerType(
        Gun.SecondaryActionTrigger rightMouseActionTriggerType) {
      this.rightMouseActionTriggerType = rightMouseActionTriggerType;
      return this;
    }

    public Builder addFireMode(FireMode fireMode) {
      this.fireModes.add(fireMode);
      return this;
    }

    public Builder aimable(boolean boltAction) {
      this.aimable = true;
      this.boltAction = boltAction;
      return this;
    }

    public Builder addScopingOffset(RegistryObject<Attachment> attachment, float offset) {
      this.aimable = true;
      this.pitchOffset.put(attachment.getId(), offset);
      return this;
    }

    public Builder setShootSound(Supplier<SoundEvent> shootSound) {
      this.shootSound = shootSound;
      return this;
    }

    public Builder setDistantShootSound(Supplier<SoundEvent> distantShootSound) {
      this.distantShootSound = distantShootSound;
      return this;
    }

    public Builder setSilencedShootSound(Supplier<SoundEvent> silencedShootSound) {
      this.silencedShootSound = silencedShootSound;
      return this;
    }

    public Builder setReloadSound(Supplier<SoundEvent> reloadSound) {
      this.reloadSound = reloadSound;
      return this;
    }

    public Builder setSecondaryActionSound(Supplier<SoundEvent> secondaryActionSound) {
      this.secondaryActionSound = secondaryActionSound;
      return this;
    }

    public Builder setSecondaryActionSoundRepeatDelayMs(
        long secondaryActionSoundRepeatDelayMs) {
      this.secondaryActionSoundRepeatDelayMs = secondaryActionSoundRepeatDelayMs;
      return this;
    }

    public GunConfiguration build() {
      AimAttributes aimAttributes = null;
      if (this.aimable) {
        aimAttributes = new AimAttributes(
            this.boltAction,
            Collections.unmodifiableMap(this.pitchOffset));
      }
      return new GunConfiguration(
          this.fireDelayMs,
          this.damage,
          this.reloadDurationTicks,
          this.accuracyPercent,
          this.recoil,
          this.roundsPerShot,
          this.range,
          this.crosshairEnabled,
          this.rightMouseActionTriggerType,
          this.fireModes,
          Optional.ofNullable(aimAttributes),
          new Sounds(this.shootSound,
              this.distantShootSound,
              this.silencedShootSound,
              this.reloadSound,
              this.secondaryActionSound,
              this.secondaryActionSoundRepeatDelayMs));
    }
  }
}
