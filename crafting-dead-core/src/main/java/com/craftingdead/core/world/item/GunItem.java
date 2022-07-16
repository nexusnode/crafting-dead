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

package com.craftingdead.core.world.item;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.client.animation.AnimationProperties;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.FireMode;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunAnimationEvent;
import com.craftingdead.core.world.item.gun.GunConfiguration;
import com.craftingdead.core.world.item.gun.GunConfigurations;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
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

  private final ResourceKey<GunConfiguration> configurationKey;

  private final Map<GunAnimationEvent, BiFunction<Gun, AnimationProperties, Animation>> animations;

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
   * A {@link Predicate} used to determine if the gun can shoot or not.
   */
  private final Predicate<Gun> triggerPredicate;

  private final CombatSlot combatSlot;

  protected GunItem(Builder<?> builder) {
    super(builder.properties);
    this.configurationKey = builder.configurationKey;
    this.animations = builder.animations;
    this.acceptedMagazines = builder.acceptedMagazines;
    this.defaultMagazine = builder.defaultMagazine;
    this.acceptedAttachments = builder.acceptedAttachments;
    this.triggerPredicate = builder.triggerPredicate;
    this.combatSlot = builder.combatSlot;
  }

  @Override
  public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
    return itemStack -> this.getAcceptedMagazines()
        .stream()
        .anyMatch(itemStack.getItem()::equals);
  }

  @Override
  public abstract ICapabilityProvider initCapabilities(ItemStack itemStack,
      @Nullable CompoundTag nbt);

  public Map<GunAnimationEvent, BiFunction<Gun, AnimationProperties, Animation>> getAnimations() {
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

  public Predicate<Gun> getTriggerPredicate() {
    return this.triggerPredicate;
  }

  public CombatSlot getCombatSlot() {
    return this.combatSlot;
  }

  public GunConfiguration getConfiguration(RegistryAccess registryAccess) {
    return this.getConfiguration(registryAccess.registryOrThrow(GunConfigurations.REGISTRY_KEY));
  }

  public GunConfiguration getConfiguration(Registry<GunConfiguration> registry) {
    return registry.get(this.configurationKey);
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
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, lines, tooltipFlag);

    if (level != null) {
      var configuration = this.getConfiguration(level.registryAccess());
      var damageText =
          new TextComponent(String.valueOf(configuration.getDamage()))
              .withStyle(ChatFormatting.RED);
      var headshotDamageText = new TextComponent(
          String.valueOf((int) (configuration.getDamage()
              * CraftingDead.serverConfig.headshotBonusDamage.get())))
                  .withStyle(ChatFormatting.RED);
      var accuracyText =
          new TextComponent((int) (configuration.getAccuracyPercent() * 100.0F) + "%")
              .withStyle(ChatFormatting.RED);
      var rpmText =
          new TextComponent(String.valueOf(configuration.getFireRateRPM()))
              .withStyle(ChatFormatting.RED);
      var rangeText =
          new TextComponent(configuration.getRange() + " blocks")
              .withStyle(ChatFormatting.RED);
      if (configuration.getRoundsPerShot() > 1) {
        var pelletsText = new TextComponent(String.valueOf(configuration.getRoundsPerShot()))
            .withStyle(ChatFormatting.RED);

        lines.add(new TranslatableComponent("gun.pellets_shot")
            .withStyle(ChatFormatting.GRAY)
            .append(pelletsText));
      }

      lines.add(new TranslatableComponent("gun.rpm")
          .withStyle(ChatFormatting.GRAY)
          .append(rpmText));

      lines.add(new TranslatableComponent("gun.damage")
          .withStyle(ChatFormatting.GRAY)
          .append(damageText));
      lines.add(new TranslatableComponent("gun.headshot_damage")
          .withStyle(ChatFormatting.GRAY)
          .append(headshotDamageText));

      lines.add(new TranslatableComponent("gun.accuracy")
          .withStyle(ChatFormatting.GRAY)
          .append(accuracyText));
      lines.add(new TranslatableComponent("gun.range")
          .withStyle(ChatFormatting.GRAY)
          .append(rangeText));
    }

    itemStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {
      var ammoCount = new TextComponent(String.valueOf(gun.getAmmoProvider().getMagazine()
          .map(Magazine::getSize)
          .orElse(0))).withStyle(ChatFormatting.RED);

      lines.add(new TranslatableComponent("gun.ammo_amount")
          .withStyle(ChatFormatting.GRAY)
          .append(ammoCount));

      for (var attachment : gun.getAttachments().values()) {
        Component attachmentName = attachment.getDescription()
            .plainCopy()
            .withStyle(ChatFormatting.RED);
        lines.add(new TranslatableComponent("gun.attachment")
            .withStyle(ChatFormatting.GRAY)
            .append(attachmentName));
      }
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

    private final ResourceKey<GunConfiguration> configurationKey;

    private final Properties properties = new Properties().stacksTo(1).tab(ModItems.COMBAT_TAB);

    private final Set<FireMode> fireModes = EnumSet.noneOf(FireMode.class);

    private final Map<GunAnimationEvent, BiFunction<Gun, AnimationProperties, Animation>> animations =
        new EnumMap<>(GunAnimationEvent.class);

    private final Set<Supplier<? extends Item>> acceptedMagazines = new HashSet<>();

    private Supplier<? extends Item> defaultMagazine;

    private final Set<Supplier<? extends Attachment>> acceptedAttachments = new HashSet<>();

    private Predicate<Gun> triggerPredicate = gun -> true;

    private CombatSlot combatSlot = CombatSlot.PRIMARY;

    public Builder(Function<SELF, GunItem> factory, ResourceKey<GunConfiguration> configurationKey) {
      this.factory = factory;
      this.configurationKey = configurationKey;
    }

    public SELF properties(Consumer<Properties> consumer) {
      consumer.accept(this.properties);
      return this.self();
    }

    public SELF addFireMode(FireMode fireMode) {
      this.fireModes.add(fireMode);
      return this.self();
    }

    public SELF putAnimation(GunAnimationEvent event, Supplier<Animation> animation) {
      return this.putAnimation(event, (__, ___) -> animation.get());
    }

    public SELF putReloadAnimation(IntFunction<Animation> animation) {
      return this.putAnimation(GunAnimationEvent.RELOAD,
          (gun, properties) -> animation.apply(gun.getReloadDurationTicks()));
    }

    public SELF putAnimation(GunAnimationEvent event,
        BiFunction<Gun, AnimationProperties, Animation> animation) {
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

    public SELF setTriggerPredicate(Predicate<Gun> triggerPredicate) {
      this.triggerPredicate = triggerPredicate;
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
