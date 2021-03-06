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

package com.craftingdead.core.item;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.core.ammoprovider.IAmmoProvider;
import com.craftingdead.core.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimation;
import com.craftingdead.core.capability.gun.AimableGun;
import com.craftingdead.core.capability.gun.GunImpl;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.gun.IGunProvider;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.client.renderer.item.GunRenderer;
import com.craftingdead.core.client.renderer.item.IRendererProvider;
import com.craftingdead.core.inventory.CombatSlotType;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

public class GunItem extends ShootableItem implements IRendererProvider, IGunProvider {

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
   * Whether the player can aim with this gun or not.
   */
  private final boolean aimable;

  /**
   * Whether the crosshair should be rendered or not while holding this item.
   */
  private final boolean crosshair;

  /**
   * Whether the gun has bolt action
   */
  private final boolean boltAction;

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
   * A factory that creates a {@link GunRenderer} instance for this gun.
   */
  private final Supplier<DistExecutor.SafeCallable<GunRenderer>> rendererFactory;

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

  public GunItem(Properties properties) {
    super(properties);
    this.fireDelayMs = properties.fireDelayMs;
    this.damage = properties.damage;
    this.reloadDurationTicks = properties.reloadDurationTicks;
    this.accuracyPct = properties.accuracy;
    this.bulletAmountToFire = properties.bulletAmountToFire;
    this.aimable = properties.aimable;
    this.crosshair = properties.crosshair;
    this.boltAction = properties.boltAction;
    this.fireModes = properties.fireModes;
    this.shootSound = properties.shootSound;
    this.distantShootSound = properties.distantShootSound;
    this.silencedShootSound = properties.silencedShootSound;
    this.reloadSound = properties.reloadSound;
    this.animations = properties.animations;
    this.acceptedMagazines = properties.acceptedMagazines;
    this.defaultMagazine = properties.defaultMagazine;
    this.acceptedAttachments = properties.acceptedAttachments;
    this.acceptedPaints = properties.acceptedPaints;
    this.rendererFactory = properties.rendererFactory;
    this.rightMouseActionTriggerType = properties.rightMouseActionTriggerType;
    this.triggerPredicate = properties.triggerPredicate;
    this.rightMouseActionSound = properties.rightMouseActionSound;
    this.rightMouseActionSoundRepeatDelayMs = properties.rightMouseActionSoundRepeatDelayMs;
    this.range = properties.range;
    this.combatSlotType = properties.combatSlotType;
  }

  @Override
  public int getFireDelayMs() {
    return this.fireDelayMs;
  }

  public int getFireRateRPM() {
    return 60000 / this.getFireDelayMs();
  }

  @Override
  public float getDamage() {
    return this.damage;
  }

  @Override
  public int getReloadDurationTicks() {
    return this.reloadDurationTicks;
  }

  @Override
  public float getAccuracyPct() {
    return this.accuracyPct;
  }

  @Override
  public double getRange() {
    return this.range;
  }

  @Override
  public int getBulletAmountToFire() {
    return this.bulletAmountToFire;
  }

  @Override
  public boolean hasCrosshair() {
    return this.crosshair;
  }

  @Override
  public boolean hasBoltAction() {
    return this.boltAction;
  }

  @Override
  public Set<FireMode> getFireModes() {
    return this.fireModes;
  }

  @Override
  public Supplier<SoundEvent> getShootSound() {
    return this.shootSound;
  }

  @Override
  public Optional<SoundEvent> getDistantShootSound() {
    return Optional.ofNullable(this.distantShootSound.get());
  }

  @Override
  public Optional<SoundEvent> getSilencedShootSound() {
    return Optional.ofNullable(this.silencedShootSound.get());
  }

  @Override
  public Optional<SoundEvent> getReloadSound() {
    return Optional.ofNullable(this.reloadSound.get());
  }

  @Override
  public Map<AnimationType, Supplier<GunAnimation>> getAnimations() {
    return this.animations;
  }

  @Override
  public IAmmoProvider createAmmoProvider() {
    IAmmoProvider ammoProvider =
        new MagazineAmmoProvider(this.getDefaultMagazine().get().getDefaultInstance());
    ammoProvider.getExpectedMagazine().setSize(0);
    return ammoProvider;
  }

  @Override
  public Set<MagazineItem> getAcceptedMagazines() {
    return this.acceptedMagazines.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  @Override
  public Supplier<MagazineItem> getDefaultMagazine() {
    return this.defaultMagazine;
  }

  @Override
  public Set<AttachmentItem> getAcceptedAttachments() {
    return this.acceptedAttachments.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  @Override
  public Set<PaintItem> getAcceptedPaints() {
    return this.acceptedPaints.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  @Override
  public IGun.RightMouseActionTriggerType getRightMouseActionTriggerType() {
    return this.rightMouseActionTriggerType;
  }

  @Override
  public Predicate<IGun> getTriggerPredicate() {
    return this.triggerPredicate;
  }

  @Override
  public Supplier<SoundEvent> getRightMouseActionSound() {
    return this.rightMouseActionSound;
  }

  @Override
  public long getRightMouseActionSoundRepeatDelayMs() {
    return this.rightMouseActionSoundRepeatDelayMs;
  }

  @Override
  public CombatSlotType getCombatSlotType() {
    return this.combatSlotType;
  }

  @Override
  public GunRenderer getRenderer() {
    return DistExecutor.safeCallWhenOn(Dist.CLIENT, this.rendererFactory);
  }

  @Override
  public Predicate<ItemStack> getAllSupportedProjectiles() {
    return itemStack -> this.acceptedMagazines
        .stream()
        .map(Supplier::get)
        .anyMatch(itemStack.getItem()::equals);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new ICapabilitySerializable<CompoundNBT>() {

      private final IGun gun = GunItem.this.aimable ? new AimableGun(GunItem.this, itemStack)
          : new GunImpl(GunItem.this, itemStack);

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if ((GunItem.this.aimable && cap == ModCapabilities.SCOPE)
            || (cap == ModCapabilities.GUN) || cap == ModCapabilities.COMBAT_ITEM) {
          return LazyOptional.of(() -> this.gun).cast();
        } else if (cap == ModCapabilities.ANIMATION_PROVIDER) {
          return this.gun.getClient() == null ? LazyOptional.empty()
              : LazyOptional.of(this.gun::getClient).cast();
        }
        return LazyOptional.empty();
      }

      @Override
      public CompoundNBT serializeNBT() {
        return this.gun.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundNBT nbt) {
        this.gun.deserializeNBT(nbt);
      }
    };
  }

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
  public void appendHoverText(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);

    stack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
      ITextComponent ammoCount =
          Text.of(gun.getAmmoProvider().getMagazine().map(IMagazine::getSize).orElse(0))
              .withStyle(TextFormatting.RED);
      ITextComponent damageText = Text.of(this.damage).withStyle(TextFormatting.RED);
      ITextComponent headshotDamageText = Text
          .of((int) (this.damage * GunImpl.HEADSHOT_MULTIPLIER))
          .withStyle(TextFormatting.RED);
      ITextComponent accuracyText =
          Text.of((int) (this.accuracyPct * 100D) + "%").withStyle(TextFormatting.RED);
      ITextComponent rpmText = Text.of(this.getFireRateRPM()).withStyle(TextFormatting.RED);
      ITextComponent rangeText =
          Text.of(this.getRange() + " blocks").withStyle(TextFormatting.RED);

      lines.add(Text.translate("item_lore.gun_item.ammo_amount")
          .withStyle(TextFormatting.GRAY)
          .append(ammoCount));
      lines.add(Text.translate("item_lore.gun_item.damage")
          .withStyle(TextFormatting.GRAY)
          .append(damageText));
      lines.add(Text.translate("item_lore.gun_item.headshot_damage")
          .withStyle(TextFormatting.GRAY)
          .append(headshotDamageText));

      if (this.bulletAmountToFire > 1) {
        ITextComponent pelletsText =
            Text.of(this.bulletAmountToFire).withStyle(TextFormatting.RED);

        lines.add(Text.translate("item_lore.gun_item.pellets_shot")
            .withStyle(TextFormatting.GRAY)
            .append(pelletsText));
      }

      for (AttachmentItem attachment : gun.getAttachments()) {
        ITextComponent attachmentNameText =
            attachment.getDescription().plainCopy().withStyle(TextFormatting.RED);
        lines.add(Text.translate("item_lore.gun_item.attachment")
            .withStyle(TextFormatting.GRAY)
            .append(attachmentNameText));
      }

      lines.add(Text.translate("item_lore.gun_item.rpm")
          .withStyle(TextFormatting.GRAY)
          .append(rpmText));
      lines.add(Text.translate("item_lore.gun_item.accuracy")
          .withStyle(TextFormatting.GRAY)
          .append(accuracyText));
      lines.add(Text.translate("item_lore.gun_item.range")
          .withStyle(TextFormatting.GRAY)
          .append(rangeText));
    });
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return enchantment == Enchantments.FLAMING_ARROWS || enchantment == Enchantments.POWER_ARROWS
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

  public static class Properties extends Item.Properties {

    private int fireDelayMs;

    private int damage;

    private int reloadDurationTicks;

    private int bulletAmountToFire = 1;

    private float accuracy;

    private double range;

    private boolean aimable = true;

    private boolean crosshair = true;

    private boolean boltAction = false;

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

    private Supplier<DistExecutor.SafeCallable<GunRenderer>> rendererFactory;

    private IGun.RightMouseActionTriggerType rightMouseActionTriggerType =
        IGun.RightMouseActionTriggerType.CLICK;

    private Predicate<IGun> triggerPredicate = gun -> true;

    private Supplier<SoundEvent> rightMouseActionSound = () -> null;

    private long rightMouseActionSoundRepeatDelayMs = -1L;

    private CombatSlotType combatSlotType = CombatSlotType.PRIMARY;

    public Properties setFireDelayMs(int fireDelayMs) {
      this.fireDelayMs = fireDelayMs;
      return this;
    }

    public Properties setDamage(int damage) {
      this.damage = damage;
      return this;
    }

    public Properties setReloadDurationTicks(int reloadDurationTicks) {
      this.reloadDurationTicks = reloadDurationTicks;
      return this;
    }

    public Properties setBulletAmountToFire(int amount) {
      this.bulletAmountToFire = amount;
      return this;
    }

    public Properties setAimable(boolean aimable) {
      this.aimable = aimable;
      return this;
    }

    public Properties setCrosshair(boolean crosshair) {
      this.crosshair = crosshair;
      return this;
    }

    public Properties setBoltAction(boolean boltAction) {
      this.boltAction = boltAction;
      return this;
    }

    public Properties setAccuracy(float accuracy) {
      this.accuracy = accuracy;
      return this;
    }

    public Properties setRange(double range) {
      this.range = range;
      return this;
    }

    public Properties addFireMode(FireMode fireMode) {
      this.fireModes.add(fireMode);
      return this;
    }

    public Properties setShootSound(Supplier<SoundEvent> shootSound) {
      this.shootSound = shootSound;
      return this;
    }

    public Properties setDistantShootSound(Supplier<SoundEvent> distantShootSound) {
      this.distantShootSound = distantShootSound;
      return this;
    }

    public Properties setSilencedShootSound(Supplier<SoundEvent> silencedShootSound) {
      this.silencedShootSound = silencedShootSound;
      return this;
    }

    public Properties setReloadSound(Supplier<SoundEvent> reloadSound) {
      this.reloadSound = reloadSound;
      return this;
    }

    public Properties addAnimation(AnimationType type, Supplier<GunAnimation> animation) {
      this.animations.put(type, animation);
      return this;
    }

    public Properties addAcceptedMagazine(Supplier<MagazineItem> acceptedMagazine) {
      this.acceptedMagazines.add(acceptedMagazine);
      return this;
    }

    public Properties setDefaultMagazine(Supplier<MagazineItem> defaultMagazine) {
      if (this.defaultMagazine != null) {
        throw new IllegalArgumentException("Default magazine already set");
      }
      this.defaultMagazine = defaultMagazine;
      return this.addAcceptedMagazine(defaultMagazine);
    }

    public Properties addAcceptedAttachment(Supplier<AttachmentItem> acceptedAttachment) {
      this.acceptedAttachments.add(acceptedAttachment);
      return this;
    }

    public Properties addAcceptedPaint(Supplier<PaintItem> acceptedPaint) {
      this.acceptedPaints.add(acceptedPaint);
      return this;
    }

    public Properties setRendererFactory(
        Supplier<DistExecutor.SafeCallable<GunRenderer>> rendererFactory) {
      this.rendererFactory = rendererFactory;
      return this;
    }

    public Properties setRightMouseActionTriggerType(
        IGun.RightMouseActionTriggerType rightMouseActionTriggerType) {
      this.rightMouseActionTriggerType = rightMouseActionTriggerType;
      return this;
    }

    public Properties setTriggerPredicate(Predicate<IGun> triggerPredicate) {
      this.triggerPredicate = triggerPredicate;
      return this;
    }

    public Properties setRightMouseActionSound(Supplier<SoundEvent> rightMouseActionSound) {
      this.rightMouseActionSound = rightMouseActionSound;
      return this;
    }

    public Properties setRightMouseActionSoundRepeatDelayMs(
        long rightMouseActionSoundRepeatDelayMs) {
      this.rightMouseActionSoundRepeatDelayMs = rightMouseActionSoundRepeatDelayMs;
      return this;
    }

    public Properties setCombatSlotType(CombatSlotType combatSlotType) {
      this.combatSlotType = combatSlotType;
      return this;
    }
  }
}
