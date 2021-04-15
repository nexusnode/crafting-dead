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

package com.craftingdead.core.item.gun;

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
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.MagazineItem;
import com.craftingdead.core.item.PaintItem;
import com.craftingdead.core.item.animation.gun.AnimationType;
import com.craftingdead.core.item.animation.gun.GunAnimation;
import com.craftingdead.core.item.combatslot.CombatSlotType;
import com.craftingdead.core.item.gun.ammoprovider.IAmmoProvider;
import com.craftingdead.core.item.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.item.gun.simple.SimpleGunClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public abstract class AbstractGunType<T extends AbstractGun<?, ?>> {

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
   * Amount of "pellets" to be fired in a single shot. It is used by shotguns.
   */
  private final int bulletAmountToFire;

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
  private final Set<Supplier<MagazineItem>> acceptedMagazines;

  /**
   * The default magazine that is supplied with this gun when crafted.
   */
  private final Supplier<MagazineItem> defaultMagazine;

  /**
   * A set of attachments that are supported by this gun.
   */
  private final Set<Supplier<AttachmentItem>> acceptedAttachments;

  /**
   * A set of paints that are supported by this gun.
   */
  private final Set<Supplier<PaintItem>> acceptedPaints;

  /**
   * Type of right mouse action. E.g. hold for minigun barrel rotation, click for toggling aim.
   */
  private final IGun.RightMouseActionTriggerType rightMouseActionTriggerType;

  /**
   * A {@link Predicate} used to determine if the gun can shoot or not.
   */
  private final Predicate<IGun> triggerPredicate;

  /**
   * Sound to be played when performing the right mouse action.
   */
  private final Supplier<SoundEvent> rightMouseActionSound;

  /**
   * A delay in milliseconds between repeating the right mouse action sound.
   */
  private final long rightMouseActionSoundRepeatDelayMs;

  /**
   * Range in blocks.
   */
  private final double range;

  private final CombatSlotType combatSlotType;

  private final Function<T, ? extends IGunClient> clientFactory;

  protected AbstractGunType(Builder<T, ?> builder) {
    this.fireDelayMs = builder.fireDelayMs;
    this.damage = builder.damage;
    this.reloadDurationTicks = builder.reloadDurationTicks;
    this.accuracyPct = builder.accuracy;
    this.bulletAmountToFire = builder.bulletAmountToFire;
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
    this.rightMouseActionTriggerType = builder.rightMouseActionTriggerType;
    this.triggerPredicate = builder.triggerPredicate;
    this.rightMouseActionSound = builder.rightMouseActionSound;
    this.rightMouseActionSoundRepeatDelayMs = builder.rightMouseActionSoundRepeatDelayMs;
    this.range = builder.range;
    this.combatSlotType = builder.combatSlotType;
    this.clientFactory = builder.clientFactory;
  }

  public Function<T, ? extends IGunClient> getClientFactory() {
    return this.clientFactory;
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

  public double getRange() {
    return this.range;
  }

  public int getBulletAmountToFire() {
    return this.bulletAmountToFire;
  }

  public boolean hasCrosshair() {
    return this.crosshair;
  }

  public Set<FireMode> getFireModes() {
    return this.fireModes;
  }

  public Supplier<SoundEvent> getShootSound() {
    return this.shootSound;
  }

  public Optional<SoundEvent> getDistantShootSound() {
    return Optional.ofNullable(this.distantShootSound.get());
  }

  public Optional<SoundEvent> getSilencedShootSound() {
    return Optional.ofNullable(this.silencedShootSound.get());
  }

  public Optional<SoundEvent> getReloadSound() {
    return Optional.ofNullable(this.reloadSound.get());
  }

  public Map<AnimationType, Supplier<GunAnimation>> getAnimations() {
    return this.animations;
  }

  public IAmmoProvider createAmmoProvider() {
    IAmmoProvider ammoProvider =
        new MagazineAmmoProvider(this.getDefaultMagazine().get().getDefaultInstance());
    ammoProvider.getExpectedMagazine().setSize(0);
    return ammoProvider;
  }

  public Set<MagazineItem> getAcceptedMagazines() {
    return this.acceptedMagazines.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Supplier<MagazineItem> getDefaultMagazine() {
    return this.defaultMagazine;
  }

  public Set<AttachmentItem> getAcceptedAttachments() {
    return this.acceptedAttachments.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Set<PaintItem> getAcceptedPaints() {
    return this.acceptedPaints.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public IGun.RightMouseActionTriggerType getRightMouseActionTriggerType() {
    return this.rightMouseActionTriggerType;
  }

  public Predicate<IGun> getTriggerPredicate() {
    return this.triggerPredicate;
  }

  public Supplier<SoundEvent> getRightMouseActionSound() {
    return this.rightMouseActionSound;
  }

  public long getRightMouseActionSoundRepeatDelayMs() {
    return this.rightMouseActionSoundRepeatDelayMs;
  }

  public CombatSlotType getCombatSlotType() {
    return this.combatSlotType;
  }

  public static class Builder<T extends AbstractGun<?, ?>, SELF extends Builder<T, SELF>> {

    private final Function<SELF, AbstractGunType<?>> factory;

    private int fireDelayMs;

    private int damage;

    private int reloadDurationTicks;

    private int bulletAmountToFire = 1;

    private float accuracy;

    private double range;

    private boolean crosshair = true;

    private final Set<FireMode> fireModes = EnumSet.noneOf(FireMode.class);

    private Supplier<SoundEvent> shootSound;

    private Supplier<SoundEvent> distantShootSound = () -> null;

    private Supplier<SoundEvent> silencedShootSound = () -> null;

    private Supplier<SoundEvent> reloadSound = () -> null;

    private final Map<AnimationType, Supplier<GunAnimation>> animations =
        new EnumMap<>(AnimationType.class);

    private final Set<Supplier<MagazineItem>> acceptedMagazines = new HashSet<>();

    private Supplier<MagazineItem> defaultMagazine;

    private final Set<Supplier<AttachmentItem>> acceptedAttachments = new HashSet<>();

    private final Set<Supplier<PaintItem>> acceptedPaints = new HashSet<>();

    private IGun.RightMouseActionTriggerType rightMouseActionTriggerType =
        IGun.RightMouseActionTriggerType.CLICK;

    private Predicate<IGun> triggerPredicate = gun -> true;

    private Supplier<SoundEvent> rightMouseActionSound = () -> null;

    private long rightMouseActionSoundRepeatDelayMs = -1L;

    private CombatSlotType combatSlotType = CombatSlotType.PRIMARY;

    private Function<T, ? extends IGunClient> clientFactory = SimpleGunClient::new;

    public Builder(Function<SELF, AbstractGunType<?>> factory) {
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

    public SELF setBulletAmountToFire(int amount) {
      this.bulletAmountToFire = amount;
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

    public SELF addAcceptedMagazine(Supplier<MagazineItem> acceptedMagazine) {
      this.acceptedMagazines.add(acceptedMagazine);
      return this.self();
    }

    public SELF setDefaultMagazine(Supplier<MagazineItem> defaultMagazine) {
      if (this.defaultMagazine != null) {
        throw new IllegalArgumentException("Default magazine already set");
      }
      this.defaultMagazine = defaultMagazine;
      return this.addAcceptedMagazine(defaultMagazine);
    }

    public SELF addAcceptedAttachment(Supplier<AttachmentItem> acceptedAttachment) {
      this.acceptedAttachments.add(acceptedAttachment);
      return this.self();
    }

    public SELF addAcceptedPaint(Supplier<PaintItem> acceptedPaint) {
      this.acceptedPaints.add(acceptedPaint);
      return this.self();
    }

    public SELF setRightMouseActionTriggerType(
        IGun.RightMouseActionTriggerType rightMouseActionTriggerType) {
      this.rightMouseActionTriggerType = rightMouseActionTriggerType;
      return this.self();
    }

    public SELF setTriggerPredicate(Predicate<IGun> triggerPredicate) {
      this.triggerPredicate = triggerPredicate;
      return this.self();
    }

    public SELF setRightMouseActionSound(Supplier<SoundEvent> rightMouseActionSound) {
      this.rightMouseActionSound = rightMouseActionSound;
      return this.self();
    }

    public SELF setRightMouseActionSoundRepeatDelayMs(
        long rightMouseActionSoundRepeatDelayMs) {
      this.rightMouseActionSoundRepeatDelayMs = rightMouseActionSoundRepeatDelayMs;
      return this.self();
    }

    public SELF setCombatSlotType(CombatSlotType combatSlotType) {
      this.combatSlotType = combatSlotType;
      return this.self();
    }

    public SELF setClientFactory(Function<T, ? extends IGunClient> clientFactory) {
      this.clientFactory = clientFactory;
      return this.self();
    }

    public AbstractGunType<?> build() {
      return this.factory.apply(this.self());
    }

    @SuppressWarnings("unchecked")
    protected final SELF self() {
      return (SELF) this;
    }
  }
}
