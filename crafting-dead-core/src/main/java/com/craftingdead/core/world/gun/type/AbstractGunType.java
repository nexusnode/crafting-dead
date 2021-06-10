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

package com.craftingdead.core.world.gun.type;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.craftingdead.core.client.animation.AnimationType;
import com.craftingdead.core.client.animation.GunAnimation;
import com.craftingdead.core.world.gun.FireMode;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.item.combatslot.CombatSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public abstract class AbstractGunType {

  /**
   * Time between shots in milliseconds.
   */
  private final int fireDelayMs;

  /**
   * Damage inflicted by a single shot from this gun.
   */
  private final int damage;

  /**
   * The duration of time this gun takes to reload in ticks.
   */
  private final int reloadDurationTicks;

  /**
   * Accuracy as percentage.
   */
  private final float accuracyPct;

  /**
   * Recoil.
   */
  private final float recoil;

  /**
   * Amount of rounds to be fired in a single shot. e.g. for shotguns
   */
  private final int roundsPerShot;

  /**
   * Whether the crosshair should be rendered or not while holding this item.
   */
  private final boolean crosshair;

  /**
   * {@link FireMode}s the gun can cycle through.
   */
  private final Set<FireMode> fireModes;

  /**
   * Sound to play for each shot of the gun.
   */
  private final Supplier<SoundEvent> shootSound;

  /**
   * Sound to play for each shot of the gun when far away.
   */
  private final Supplier<SoundEvent> distantShootSound;

  /**
   * A 'silenced' version of the shoot sound.
   */
  private final Supplier<SoundEvent> silencedShootSound;

  /**
   * Sound to play whilst the gun is being reloaded.
   */
  private final Supplier<SoundEvent> reloadSound;

  /**
   * All the animations used by this gun.
   */
  private final Map<AnimationType, Supplier<GunAnimation>> animations;

  /**
   * A set of magazines that are supported by this gun.
   */
  private final Set<Supplier<? extends Item>> acceptedMagazines;

  /**
   * The default magazine that is supplied with this gun when crafted.
   */
  private final Supplier<? extends Item> defaultMagazine;

  /**
   * A set of attachments that are supported by this gun.
   */
  private final Set<Supplier<? extends Attachment>> acceptedAttachments;

  /**
   * A set of paints that are supported by this gun.
   */
  private final Set<Supplier<? extends Item>> acceptedPaints;

  /**
   * Type of right mouse action. E.g. hold for minigun barrel rotation, click for toggling aim.
   */
  private final Gun.SecondaryActionTrigger secondaryActionTrigger;

  /**
   * A {@link Predicate} used to determine if the gun can shoot or not.
   */
  private final Predicate<Gun> triggerPredicate;

  /**
   * Sound to be played when performing the right mouse action.
   */
  private final Supplier<SoundEvent> secondaryActionSound;

  /**
   * A delay in milliseconds between repeating the secondary action sound.
   */
  private final long secondaryActionSoundRepeatDelayMs;

  /**
   * Range in blocks.
   */
  private final double range;

  private final CombatSlotType combatSlotType;

  protected AbstractGunType(Builder<?> builder) {
    this.fireDelayMs = builder.fireDelayMs;
    this.damage = builder.damage;
    this.reloadDurationTicks = builder.reloadDurationTicks;
    this.accuracyPct = builder.accuracy;
    this.recoil = builder.recoil;
    this.roundsPerShot = builder.roundsPerShot;
    this.crosshair = builder.crosshair;
    this.fireModes = builder.fireModes;
    this.shootSound = builder.shootSound;
    this.distantShootSound = builder.distantShootSound;
    this.silencedShootSound = builder.silencedShootSound;
    this.reloadSound = builder.reloadSound;
    this.animations = builder.animations;
    this.acceptedMagazines = builder.acceptedMagazines;
    this.defaultMagazine = builder.defaultMagazine;
    this.acceptedAttachments = builder.acceptedAttachments;
    this.acceptedPaints = builder.acceptedPaints;
    this.secondaryActionTrigger = builder.rightMouseActionTriggerType;
    this.triggerPredicate = builder.triggerPredicate;
    this.secondaryActionSound = builder.secondaryActionSound;
    this.secondaryActionSoundRepeatDelayMs = builder.secondaryActionSoundRepeatDelayMs;
    this.range = builder.range;
    this.combatSlotType = builder.combatSlotType;
  }

  public abstract ICapabilityProvider createCapabilityProvider(ItemStack itemStack);

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

  public float getAccuracyPct() {
    return this.accuracyPct;
  }

  public float getRecoil() {
    return this.recoil;
  }

  public double getRange() {
    return this.range;
  }

  public int getRoundsPerShot() {
    return this.roundsPerShot;
  }

  public boolean hasCrosshair() {
    return this.crosshair;
  }

  public Set<FireMode> getFireModes() {
    return this.fireModes;
  }

  public SoundEvent getShootSound() {
    return this.shootSound.get();
  }

  public Optional<SoundEvent> getDistantShootSound() {
    return Optional.ofNullable(this.distantShootSound).map(Supplier::get);
  }

  public Optional<SoundEvent> getSilencedShootSound() {
    return Optional.ofNullable(this.silencedShootSound).map(Supplier::get);
  }

  public Optional<SoundEvent> getReloadSound() {
    return Optional.ofNullable(this.reloadSound).map(Supplier::get);
  }

  public Map<AnimationType, Supplier<GunAnimation>> getAnimations() {
    return this.animations;
  }

  public AmmoProvider createAmmoProvider() {
    AmmoProvider ammoProvider =
        new MagazineAmmoProvider(this.getDefaultMagazine().getDefaultInstance());
    ammoProvider.getExpectedMagazine().setSize(0);
    return ammoProvider;
  }

  public Set<Item> getAcceptedMagazines() {
    return this.acceptedMagazines.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Item getDefaultMagazine() {
    return this.defaultMagazine.get();
  }

  public Set<Attachment> getAcceptedAttachments() {
    return this.acceptedAttachments.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Set<Item> getAcceptedPaints() {
    return this.acceptedPaints.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Gun.SecondaryActionTrigger getSecondaryActionTrigger() {
    return this.secondaryActionTrigger;
  }

  public Predicate<Gun> getTriggerPredicate() {
    return this.triggerPredicate;
  }

  public Optional<SoundEvent> getSecondaryActionSound() {
    return Optional.ofNullable(this.secondaryActionSound).map(Supplier::get);
  }

  public long getSecondaryActionSoundRepeatDelayMs() {
    return this.secondaryActionSoundRepeatDelayMs;
  }

  public CombatSlotType getCombatSlotType() {
    return this.combatSlotType;
  }

  public static class Builder<SELF extends Builder<SELF>> {

    private final Function<SELF, AbstractGunType> factory;

    private int fireDelayMs;

    private int damage;

    private int reloadDurationTicks;

    private int roundsPerShot = 1;

    private float accuracy;

    private float recoil;

    private double range;

    private boolean crosshair = true;

    private final Set<FireMode> fireModes = EnumSet.noneOf(FireMode.class);

    private Supplier<SoundEvent> shootSound;

    private Supplier<SoundEvent> distantShootSound = () -> null;

    private Supplier<SoundEvent> silencedShootSound = () -> null;

    private Supplier<SoundEvent> reloadSound = () -> null;

    private final Map<AnimationType, Supplier<GunAnimation>> animations =
        new EnumMap<>(AnimationType.class);

    private final Set<Supplier<? extends Item>> acceptedMagazines = new HashSet<>();

    private Supplier<? extends Item> defaultMagazine;

    private final Set<Supplier<? extends Attachment>> acceptedAttachments = new HashSet<>();

    private final Set<Supplier<? extends Item>> acceptedPaints = new HashSet<>();

    private Gun.SecondaryActionTrigger rightMouseActionTriggerType =
        Gun.SecondaryActionTrigger.TOGGLE;

    private Predicate<Gun> triggerPredicate = gun -> true;

    private Supplier<SoundEvent> secondaryActionSound = () -> null;

    private long secondaryActionSoundRepeatDelayMs = -1L;

    private CombatSlotType combatSlotType = CombatSlotType.PRIMARY;

    public Builder(Function<SELF, AbstractGunType> factory) {
      this.factory = factory;
    }

    public SELF setFireDelayMs(int fireDelayMs) {
      this.fireDelayMs = fireDelayMs;
      return this.self();
    }

    public SELF setDamage(int damage) {
      this.damage = damage;
      return this.self();
    }

    public SELF setReloadDurationTicks(int reloadDurationTicks) {
      this.reloadDurationTicks = reloadDurationTicks;
      return this.self();
    }

    public SELF setRoundsPerShot(int roundsPerShot) {
      this.roundsPerShot = roundsPerShot;
      return this.self();
    }

    public SELF setCrosshair(boolean crosshair) {
      this.crosshair = crosshair;
      return this.self();
    }

    public SELF setAccuracy(float accuracy) {
      this.accuracy = accuracy;
      return this.self();
    }

    public SELF setRecoil(float recoil) {
      this.recoil = recoil;
      return this.self();
    }

    public SELF setRange(double range) {
      this.range = range;
      return this.self();
    }

    public SELF addFireMode(FireMode fireMode) {
      this.fireModes.add(fireMode);
      return this.self();
    }

    public SELF setShootSound(Supplier<SoundEvent> shootSound) {
      this.shootSound = shootSound;
      return this.self();
    }

    public SELF setDistantShootSound(Supplier<SoundEvent> distantShootSound) {
      this.distantShootSound = distantShootSound;
      return this.self();
    }

    public SELF setSilencedShootSound(Supplier<SoundEvent> silencedShootSound) {
      this.silencedShootSound = silencedShootSound;
      return this.self();
    }

    public SELF setReloadSound(Supplier<SoundEvent> reloadSound) {
      this.reloadSound = reloadSound;
      return this.self();
    }

    public SELF addAnimation(AnimationType type, Supplier<GunAnimation> animation) {
      this.animations.put(type, animation);
      return this.self();
    }

    public SELF addAcceptedMagazine(Supplier<? extends Item> acceptedMagazine) {
      this.acceptedMagazines.add(acceptedMagazine);
      return this.self();
    }

    public SELF setDefaultMagazine(Supplier<? extends Item> defaultMagazine) {
      if (this.defaultMagazine != null) {
        throw new IllegalArgumentException("Default magazine already set");
      }
      this.defaultMagazine = defaultMagazine;
      return this.addAcceptedMagazine(defaultMagazine);
    }

    public SELF addAcceptedAttachment(Supplier<? extends Attachment> acceptedAttachment) {
      this.acceptedAttachments.add(acceptedAttachment);
      return this.self();
    }

    public SELF addAcceptedPaint(Supplier<? extends Item> acceptedPaint) {
      this.acceptedPaints.add(acceptedPaint);
      return this.self();
    }

    public SELF setRightMouseActionTriggerType(
        Gun.SecondaryActionTrigger rightMouseActionTriggerType) {
      this.rightMouseActionTriggerType = rightMouseActionTriggerType;
      return this.self();
    }

    public SELF setTriggerPredicate(Predicate<Gun> triggerPredicate) {
      this.triggerPredicate = triggerPredicate;
      return this.self();
    }

    public SELF setSecondaryActionSound(Supplier<SoundEvent> secondaryActionSound) {
      this.secondaryActionSound = secondaryActionSound;
      return this.self();
    }

    public SELF setSecondaryActionSoundRepeatDelayMs(
        long secondaryActionSoundRepeatDelayMs) {
      this.secondaryActionSoundRepeatDelayMs = secondaryActionSoundRepeatDelayMs;
      return this.self();
    }

    public SELF setCombatSlotType(CombatSlotType combatSlotType) {
      this.combatSlotType = combatSlotType;
      return this.self();
    }

    public AbstractGunType build() {
      return this.factory.apply(this.self());
    }

    @SuppressWarnings("unchecked")
    protected final SELF self() {
      return (SELF) this;
    }
  }
}
