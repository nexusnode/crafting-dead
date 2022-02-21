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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.util.FunctionalUtil;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.RegisterGunColor;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@RegisterGunColor
public abstract class GunItem extends ProjectileWeaponItem {

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

  private final Map<GunAnimationEvent, Function<GunItem, Animation>> animations;

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

  private final CombatSlot combatSlot;

  protected GunItem(Builder<?> builder) {
    super(builder.properties);
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
    this.secondaryActionTrigger = builder.rightMouseActionTriggerType;
    this.triggerPredicate = builder.triggerPredicate;
    this.secondaryActionSound = builder.secondaryActionSound;
    this.secondaryActionSoundRepeatDelayMs = builder.secondaryActionSoundRepeatDelayMs;
    this.range = builder.range;
    this.combatSlot = builder.combatSlot;
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

  public Map<GunAnimationEvent, Function<GunItem, Animation>> getAnimations() {
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
    return this.acceptedMagazines.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public Item getDefaultMagazine() {
    return this.defaultMagazine.get();
  }

  public Set<Attachment> getAcceptedAttachments() {
    return this.acceptedAttachments.stream().map(Supplier::get).collect(Collectors.toSet());
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

  @Override
  public Predicate<ItemStack> getAllSupportedProjectiles() {
    return itemStack -> this.getAcceptedMagazines()
        .stream()
        .anyMatch(itemStack.getItem()::equals);
  }

  @Override
  public abstract ICapabilityProvider initCapabilities(ItemStack itemStack,
      @Nullable CompoundTag nbt);

  @Override
  public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
    return true;
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {
    return oldStack.getItem() != newStack.getItem();
  }

  @Override
  public void appendHoverText(ItemStack itemStack, Level world,
      List<Component> lines, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, world, lines, tooltipFlag);

    itemStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {
      Component ammoCount =
          new TextComponent(String.valueOf(gun.getAmmoProvider().getMagazine()
              .map(Magazine::getSize)
              .orElse(0))).withStyle(ChatFormatting.RED);
      Component damageText =
          new TextComponent(String.valueOf(this.getDamage()))
              .withStyle(ChatFormatting.RED);
      Component headshotDamageText = new TextComponent(
          String.valueOf((int) (this.getDamage() * CraftingDead.serverConfig.headshotBonusDamage.get())))
              .withStyle(ChatFormatting.RED);
      Component accuracyText =
          new TextComponent((int) (this.getAccuracyPct() * 100D) + "%")
              .withStyle(ChatFormatting.RED);
      Component rpmText =
          new TextComponent(String.valueOf(this.getFireRateRPM()))
              .withStyle(ChatFormatting.RED);
      Component rangeText =
          new TextComponent(this.getRange() + " blocks")
              .withStyle(ChatFormatting.RED);

      lines.add(new TranslatableComponent("gun.ammo_amount")
          .withStyle(ChatFormatting.GRAY)
          .append(ammoCount));
      lines.add(new TranslatableComponent("gun.damage")
          .withStyle(ChatFormatting.GRAY)
          .append(damageText));
      lines.add(new TranslatableComponent("gun.headshot_damage")
          .withStyle(ChatFormatting.GRAY)
          .append(headshotDamageText));

      if (this.getRoundsPerShot() > 1) {
        Component pelletsText =
            new TextComponent(String.valueOf(this.getRoundsPerShot()))
                .withStyle(ChatFormatting.RED);

        lines.add(new TranslatableComponent("gun.pellets_shot")
            .withStyle(ChatFormatting.GRAY)
            .append(pelletsText));
      }

      for (Attachment attachment : gun.getAttachments()) {
        Component attachmentName = attachment.getDescription()
            .plainCopy()
            .withStyle(ChatFormatting.RED);
        lines.add(new TranslatableComponent("gun.attachment")
            .withStyle(ChatFormatting.GRAY)
            .append(attachmentName));
      }

      lines.add(new TranslatableComponent("gun.rpm")
          .withStyle(ChatFormatting.GRAY)
          .append(rpmText));
      lines.add(new TranslatableComponent("gun.accuracy")
          .withStyle(ChatFormatting.GRAY)
          .append(accuracyText));
      lines.add(new TranslatableComponent("gun.range")
          .withStyle(ChatFormatting.GRAY)
          .append(rangeText));
    });
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return enchantment == Enchantments.FLAMING_ARROWS
        || enchantment == Enchantments.POWER_ARROWS
        || enchantment == Enchantments.UNBREAKING
        || super.canApplyAtEnchantingTable(stack, enchantment);
  }

  @Override
  public int getEnchantmentValue() {
    return 1;
  }

  @Override
  public int getDefaultProjectileRange() {
    return 0;
  }

  public static class Builder<SELF extends Builder<SELF>> {

    private final Function<SELF, GunItem> factory;

    private final Properties properties = new Properties().stacksTo(1).tab(ModItems.COMBAT_TAB);

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

    private final Map<GunAnimationEvent, Function<GunItem, Animation>> animations =
        new EnumMap<>(GunAnimationEvent.class);

    private final Set<Supplier<? extends Item>> acceptedMagazines = new HashSet<>();

    private Supplier<? extends Item> defaultMagazine;

    private final Set<Supplier<? extends Attachment>> acceptedAttachments = new HashSet<>();

    private Gun.SecondaryActionTrigger rightMouseActionTriggerType =
        Gun.SecondaryActionTrigger.TOGGLE;

    private Predicate<Gun> triggerPredicate = gun -> true;

    private Supplier<SoundEvent> secondaryActionSound = () -> null;

    private long secondaryActionSoundRepeatDelayMs = -1L;

    private CombatSlot combatSlot = CombatSlot.PRIMARY;

    public Builder(Function<SELF, GunItem> factory) {
      this.factory = factory;
    }

    public SELF properties(Consumer<Properties> consumer) {
      consumer.accept(this.properties);
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
          gunType -> animation.apply(gunType.reloadDurationTicks));
    }

    public SELF putAnimation(GunAnimationEvent event,
        Function<GunItem, Animation> animation) {
      this.animations.put(event, animation);
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

    public GunItem build() {
      return this.factory.apply(this.self());
    }

    @SuppressWarnings("unchecked")
    protected final SELF self() {
      return (SELF) this;
    }
  }
}
