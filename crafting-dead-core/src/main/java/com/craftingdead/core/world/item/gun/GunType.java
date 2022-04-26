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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.util.FunctionalUtil;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GunType extends ForgeRegistryEntry<GunType> implements ItemLike {

  public static final Codec<GunType> CODEC =
      GunTypeFactory.CODEC.dispatch(GunType::getFactory, GunTypeFactory::getGunTypeCodec);

  public static final Codec<GunType> DIRECT_CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              GeneralAttributes.CODEC
                  .fieldOf("general_attributes")
                  .forGetter(GunType::getAttributes),
              Sounds.CODEC
                  .fieldOf("sounds")
                  .forGetter(GunType::getSounds))
          .apply(instance, GunType::new));

  private final Supplier<? extends GunItem> item;

  private final Map<GunAnimationEvent, Function<GunType, Animation>> animations;

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

  private final CombatSlot combatSlot;

  private final GeneralAttributes attributes;
  private final Sounds sounds;

  protected GunType(GeneralAttributes attributes, Sounds sounds) {
    this.attributes = attributes;
    this.sounds = sounds;

    // TODO Implement full serialization support
    throw new IllegalStateException("Not supported");
  }

  protected GunType(Builder<?> builder) {
    this.attributes = new GeneralAttributes(
        builder.fireDelayMs,
        builder.damage,
        builder.reloadDurationTicks,
        builder.accuracy,
        builder.recoil,
        builder.roundsPerShot,
        builder.range,
        builder.crosshair,
        builder.fireModes,
        builder.acceptedMagazines,
        builder.defaultMagazine,
        builder.acceptedAttachments);
    this.sounds = new Sounds(
        builder.shootSound,
        builder.distantShootSound,
        builder.silencedShootSound,
        builder.reloadSound);

    this.item = builder.item;
    this.animations = builder.animations;
    this.secondaryActionTrigger = builder.rightMouseActionTriggerType;
    this.triggerPredicate = builder.triggerPredicate;
    this.secondaryActionSound = builder.secondaryActionSound;
    this.secondaryActionSoundRepeatDelayMs = builder.secondaryActionSoundRepeatDelayMs;
    this.combatSlot = builder.combatSlot;
  }

  public GeneralAttributes getAttributes() {
    return this.attributes;
  }

  public Sounds getSounds() {
    return this.sounds;
  }

  public int getFireDelayMs() {
    return this.attributes.fireDelay();
  }

  public int getFireRateRPM() {
    return 60000 / this.getFireDelayMs();
  }

  public float getDamage() {
    return this.attributes.damage();
  }

  public int getReloadDurationTicks() {
    return this.attributes.reloadDuration();
  }

  public float getAccuracyPct() {
    return this.attributes.accuracy();
  }

  public float getRecoil() {
    return this.attributes.recoil();
  }

  public double getRange() {
    return this.attributes.range();
  }

  public int getRoundsPerShot() {
    return this.attributes.roundsPerShot();
  }

  public boolean hasCrosshair() {
    return this.attributes.hasCrossHair();
  }

  public Set<FireMode> getFireModes() {
    return this.attributes.fireModes();
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

  public Map<GunAnimationEvent, Function<GunType, Animation>> getAnimations() {
    return this.animations;
  }

  public AmmoProvider createAmmoProvider() {
    AmmoProvider ammoProvider =
        new MagazineAmmoProvider(this.getDefaultMagazine().getDefaultInstance());
    if (CraftingDead.serverConfig.reloadGunComeEmptyMag.get()) {
      ammoProvider.getExpectedMagazine().setSize(0);
    }
    return ammoProvider;
  }

  public Set<Item> getAcceptedMagazines() {
    return this.attributes.acceptedMagazines()
        .stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Item getDefaultMagazine() {
    return this.attributes.defaultMagazine().get();
  }

  public Set<Attachment> getAcceptedAttachments() {
    return this.attributes.acceptedAttachments()
        .stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Gun.SecondaryActionTrigger getSecondaryActionTrigger() {
    return this.secondaryActionTrigger;
  }

  public Predicate<Gun> getTriggerPredicate() {
    return this.triggerPredicate;
  }

  public Optional<SoundEvent> getSecondaryActionSound() {
    return FunctionalUtil.optional(this.secondaryActionSound);
  }

  public long getSecondaryActionSoundRepeatDelayMs() {
    return this.secondaryActionSoundRepeatDelayMs;
  }

  public CombatSlot getCombatSlot() {
    return this.combatSlot;
  }

  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(
        () -> TypedGun.create(this.getClientFactory(), itemStack, this),
        Gun.CAPABILITY, CombatSlotProvider.CAPABILITY);
  }

  public <T extends TypedGun<?>> Function<T, TypedGunClient<? super T>> getClientFactory() {
    return TypedGunClient::new;
  }

  @Override
  public @NotNull Item asItem() {
    return this.item.get();
  }

  public GunTypeFactory getFactory() {
    return GunTypeFactories.SIMPLE.get();
  }

  /**
   * @param fireDelay - Time between shots in milliseconds.
   * @param damage - Damage inflicted by a single shot from this gun.
   * @param reloadDuration - The duration of time this gun takes to reload in ticks.
   * @param accuracy - Accuracy as percentage.
   * @param recoil - Recoil.
   * @param roundsPerShot - Amount of rounds to be fired in a single shot. e.g. for shotguns
   * @param range - Range in blocks.
   * @param hasCrossHair - Whether the crosshair should be rendered or not while holding this item.
   * @param fireModes - {@link FireMode}s the gun can cycle through.
   * @param acceptedMagazines - A set of magazines that are supported by this gun.
   * @param defaultMagazine - The default magazine that is supplied with this gun when crafted.
   * @param acceptedAttachments - A set of attachments that are supported by this gun.
   */
  public record GeneralAttributes(
      int fireDelay,
      int damage,
      int reloadDuration,
      float accuracy,
      float recoil,
      int roundsPerShot,
      double range,
      boolean hasCrossHair,
      Set<FireMode> fireModes,
      Set<Supplier<Item>> acceptedMagazines,
      Supplier<Item> defaultMagazine,
      Set<Supplier<Attachment>> acceptedAttachments) {

    public static final Codec<GeneralAttributes> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.INT.optionalFieldOf("fire_delay", 0)
                    .forGetter(GeneralAttributes::fireDelay),
                Codec.INT.optionalFieldOf("damage", 1)
                    .forGetter(GeneralAttributes::damage),
                Codec.INT.optionalFieldOf("reload_duration", 1)
                    .forGetter(GeneralAttributes::reloadDuration),
                Codec.FLOAT.optionalFieldOf("accuracy", 1F)
                    .forGetter(GeneralAttributes::accuracy),
                Codec.FLOAT.optionalFieldOf("recoil", 0F)
                    .forGetter(GeneralAttributes::recoil),
                Codec.INT.optionalFieldOf("rounds_per_shot", 1)
                    .forGetter(GeneralAttributes::roundsPerShot),
                Codec.DOUBLE.optionalFieldOf("range", 10D)
                    .forGetter(GeneralAttributes::range),
                Codec.BOOL.optionalFieldOf("has_crosshair", true)
                    .forGetter(GeneralAttributes::hasCrossHair),
                Codec.list(FireMode.CODEC)
                    .optionalFieldOf("fire_mode", Collections.singletonList(FireMode.SEMI))
                    .xmap(to -> (Set<FireMode>) new HashSet<>(to), ArrayList::new)
                    .forGetter(GeneralAttributes::fireModes),
                Codec.list(ForgeRegistries.ITEMS.getCodec()
                    .xmap(to -> (Supplier<Item>) () -> to, Supplier::get))
                    .optionalFieldOf("accepted_magazines", Collections.emptyList())
                    .xmap(to -> (Set<Supplier<Item>>) new HashSet<>(to), ArrayList::new)
                    .forGetter(GeneralAttributes::acceptedMagazines),
                ForgeRegistries.ITEMS.getCodec().fieldOf("default_magazine")
                    .xmap(to -> (Supplier<Item>) () -> to, Supplier::get)
                    .forGetter(t -> t.defaultMagazine),
                Codec.list(Attachments.REGISTRY.get()
                    .getCodec().xmap(to -> (Supplier<Attachment>) () -> to, Supplier::get))
                    .optionalFieldOf("accepted_attachments", Collections.emptyList())
                    .xmap(to -> (Set<Supplier<Attachment>>) new HashSet<>(to), ArrayList::new)
                    .forGetter(GeneralAttributes::acceptedAttachments))
            .apply(instance, GeneralAttributes::new));
  }

  /**
   * @param shootSound - Sound to play for each shot of the gun.
   * @param distantShootSound - Sound to play for each shot of the gun when far away.
   * @param silencedShootSound - A 'silenced' version of the shoot sound.
   * @param reloadSound - Sound to play whilst the gun is being reloaded.
   */
  public record Sounds(
      Supplier<SoundEvent> shootSound,
      Supplier<SoundEvent> distantShootSound,
      Supplier<SoundEvent> silencedShootSound,
      Supplier<SoundEvent> reloadSound) {

    public static final Codec<Sounds> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .fieldOf("shoot_sound")
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::shootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("distant_shoot_sound", null)
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::distantShootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("silenced_shoot_sound", null)
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::silencedShootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("reload_sound", null)
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::reloadSound))
            .apply(instance, Sounds::new));
  }

  public static Builder<?> builder() {
    return new Builder<>(GunType::new);
  }

  public static class Builder<SELF extends Builder<SELF>> {

    private final Function<SELF, GunType> factory;

    private Supplier<? extends GunItem> item;

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

    private final Map<GunAnimationEvent, Function<GunType, Animation>> animations =
        new EnumMap<>(GunAnimationEvent.class);

    private final Set<Supplier<Item>> acceptedMagazines = new HashSet<>();

    private Supplier<Item> defaultMagazine;

    private final Set<Supplier<Attachment>> acceptedAttachments = new HashSet<>();

    private Gun.SecondaryActionTrigger rightMouseActionTriggerType =
        Gun.SecondaryActionTrigger.TOGGLE;

    private Predicate<Gun> triggerPredicate = gun -> true;

    private Supplier<SoundEvent> secondaryActionSound = () -> null;

    private long secondaryActionSoundRepeatDelayMs = -1L;

    private CombatSlot combatSlot = CombatSlot.PRIMARY;

    public Builder(Function<SELF, GunType> factory) {
      this.factory = factory;
    }

    public SELF setItem(Supplier<? extends GunItem> item) {
      this.item = item;
      return this.self();
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

    public SELF putAnimation(GunAnimationEvent event, Supplier<Animation> animation) {
      return this.putAnimation(event, __ -> animation.get());
    }

    public SELF putReloadAnimation(IntFunction<Animation> animation) {
      return this.putAnimation(GunAnimationEvent.RELOAD,
          gunType -> animation.apply(gunType.attributes.reloadDuration()));
    }

    public SELF putAnimation(GunAnimationEvent event,
        Function<GunType, Animation> animation) {
      this.animations.put(event, animation);
      return this.self();
    }

    public SELF addAcceptedMagazine(Supplier<? extends Item> acceptedMagazine) {
      this.acceptedMagazines.add(acceptedMagazine::get);
      return this.self();
    }

    public SELF setDefaultMagazine(Supplier<? extends Item> defaultMagazine) {
      if (this.defaultMagazine != null) {
        throw new IllegalArgumentException("Default magazine already set");
      }
      this.defaultMagazine = defaultMagazine::get;
      return this.addAcceptedMagazine(defaultMagazine);
    }

    public SELF addAcceptedAttachment(Supplier<? extends Attachment> acceptedAttachment) {
      this.acceptedAttachments.add(acceptedAttachment::get);
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

    public SELF setCombatSlot(CombatSlot combatSlot) {
      this.combatSlot = combatSlot;
      return this.self();
    }

    public GunType build() {
      return this.factory.apply(this.self());
    }

    @SuppressWarnings("unchecked")
    protected final SELF self() {
      return (SELF) this;
    }
  }
}
