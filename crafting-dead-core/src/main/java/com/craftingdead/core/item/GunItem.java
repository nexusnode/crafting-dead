package com.craftingdead.core.item;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.gun.DefaultGun;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.RenderGun;
import com.craftingdead.core.client.renderer.item.gun.AnimationType;
import com.craftingdead.core.client.renderer.item.gun.GunAnimation;
import com.craftingdead.core.util.Text;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;

public class GunItem extends ShootableItem {

  /**
   * Time between shots in milliseconds.
   */
  private final int fireRateMs;

  private final int damage;

  private final int reloadDurationTicks;

  /**
   * Accuracy as percentage.
   */
  private final float accuracy;

  /**
   * Amount of "pellets" to be fired in a single shot. It is used by shotguns.
   */
  private final int bulletAmountToFire;

  /**
   * Whether the player can aim with this gun or not
   */
  private final boolean aimable;

  /**
   * Whether the crosshair should be rendered or not while holding this item
   */
  private final boolean crosshair;

  /**
   * {@link FireMode}s the gun can cycle through.
   */
  private final List<FireMode> fireModes;

  private final Supplier<SoundEvent> shootSound;

  private final Supplier<SoundEvent> silencedShootSound;

  private final Supplier<SoundEvent> reloadSound;

  private final Map<AnimationType, Supplier<GunAnimation>> animations;

  private final Set<Supplier<MagazineItem>> acceptedMagazines;

  private final Supplier<MagazineItem> defaultMagazine;

  private final Set<Supplier<AttachmentItem>> acceptedAttachments;

  private final Set<Supplier<PaintItem>> acceptedPaints;

  private final Set<Supplier<AttachmentItem>> defaultAttachments;

  private final RenderGun renderer;

  public GunItem(Properties properties) {
    super(properties);
    this.fireRateMs = properties.fireRate;
    this.damage = properties.damage;
    this.reloadDurationTicks = properties.reloadDurationTicks;
    this.accuracy = properties.accuracy;
    this.bulletAmountToFire = properties.bulletAmountToFire;
    this.aimable = properties.aimable;
    this.crosshair = properties.crosshair;
    this.fireModes = properties.fireModes;
    this.shootSound = properties.shootSound;
    this.silencedShootSound = properties.silencedShootSound;
    this.reloadSound = properties.reloadSound;
    this.animations = properties.animations;
    this.acceptedMagazines = properties.acceptedMagazines;
    this.defaultMagazine = properties.defaultMagazine;
    this.acceptedAttachments = properties.acceptedAttachments;
    this.defaultAttachments = properties.defaultAttachments;
    this.acceptedPaints = properties.acceptedPaints;
    this.renderer = DistExecutor.safeCallWhenOn(Dist.CLIENT, properties.rendererFactory);
    this
        .addPropertyOverride(new ResourceLocation("aiming"),
            (itemStack, world, entity) -> entity != null ? itemStack
                .getCapability(ModCapabilities.GUN)
                .map(gun -> gun.isAiming(entity, itemStack) ? 1.0F : 0.0F)
                .orElse(0.0F) : 0.0F);
  }

  public int getFireRateMs() {
    return this.fireRateMs;
  }

  public int getFireRateRPM() {
    return 60000 / this.getFireRateMs();
  }

  public int getDamage() {
    return this.damage;
  }

  public int getReloadDurationTicks() {
    return this.reloadDurationTicks;
  }

  public float getAccuracy() {
    return this.accuracy;
  }

  public int getBulletAmountToFire() {
    return this.bulletAmountToFire;
  }

  public boolean isAimable() {
    return this.aimable;
  }

  public boolean hasCrosshair() {
    return this.crosshair;
  }

  public List<FireMode> getFireModes() {
    return this.fireModes;
  }

  public Supplier<SoundEvent> getShootSound() {
    return this.shootSound;
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

  public Set<AttachmentItem> getDefaultAttachments() {
    return this.defaultAttachments.stream().map(Supplier::get).collect(Collectors.toSet());
  }

  public RenderGun getRenderer() {
    return this.renderer;
  }

  @Override
  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return itemStack -> this.acceptedMagazines
        .stream()
        .map(Supplier::get)
        .anyMatch(itemStack.getItem()::equals);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(new DefaultGun(this),
        ImmutableSet
            .of(() -> ModCapabilities.RENDERER_PROVIDER, () -> ModCapabilities.GUN,
                () -> ModCapabilities.SCOPE));
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
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity,
      Hand hand) {
    if (world.isRemote()) {
      playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> playerEntity
          .getHeldItem(hand)
          .getCapability(ModCapabilities.GUN)
          .ifPresent(gun -> gun.toggleAiming(living, true)));
    }
    return super.onItemRightClick(world, playerEntity, hand);
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    stack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
      ITextComponent magazineSizeText =
          Text.of(gun.getMagazineSize()).applyTextStyle(TextFormatting.RED);
      ITextComponent damageText = Text.of(this.damage).applyTextStyle(TextFormatting.RED);
      ITextComponent headshotDamageText = Text
          .of((int) (this.damage * DefaultGun.HEADSHOT_MULTIPLIER))
          .applyTextStyle(TextFormatting.RED);
      ITextComponent accuracyText =
          Text.of((int) (this.accuracy * 100D) + "%").applyTextStyle(TextFormatting.RED);
      ITextComponent rpmText = Text.of(this.getFireRateRPM()).applyTextStyle(TextFormatting.RED);

      lines
          .add(Text
              .translate("item_lore.gun_item.ammo_amount")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(magazineSizeText));
      lines
          .add(Text
              .translate("item_lore.gun_item.damage")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(damageText));
      lines
          .add(Text
              .translate("item_lore.gun_item.headshot_damage")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(headshotDamageText));

      if (this.bulletAmountToFire > 1) {
        ITextComponent pelletsText =
            Text.of(this.bulletAmountToFire).applyTextStyle(TextFormatting.RED);

        lines
            .add(Text
                .translate("item_lore.gun_item.pellets_shot")
                .applyTextStyle(TextFormatting.GRAY)
                .appendSibling(pelletsText));
      }

      for (AttachmentItem attachment : gun.getAttachments()) {
        ITextComponent attachmentNameText = attachment.getName().applyTextStyle(TextFormatting.RED);
        lines
            .add(Text
                .translate("item_lore.gun_item.attachment")
                .applyTextStyle(TextFormatting.GRAY)
                .appendSibling(attachmentNameText));
      }

      lines
          .add(Text
              .translate("item_lore.gun_item.rpm")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(rpmText));
      lines
          .add(Text
              .translate("item_lore.gun_item.accuracy")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(accuracyText));
    });
  }

  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = super.getShareTag(stack);
    if (nbt != null) {
      nbt.put("gun", stack.getCapability(ModCapabilities.GUN).map(IGun::serializeNBT).orElse(null));
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    super.readShareTag(stack, nbt);
    if (nbt != null && nbt.contains("gun", Constants.NBT.TAG_COMPOUND)) {
      stack
          .getCapability(ModCapabilities.GUN)
          .ifPresent(gun -> gun.deserializeNBT(nbt.getCompound("gun")));
    }
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return enchantment == Enchantments.FLAME || enchantment == Enchantments.POWER
        || super.canApplyAtEnchantingTable(stack, enchantment);
  }

  @Override
  public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> items) {
    if (this.isInGroup(itemGroup)) {
      ItemStack itemStack = new ItemStack(this);
      // Fill magazine
      itemStack.getCapability(ModCapabilities.GUN)
          .ifPresent(gun -> gun.getMagazineStack().setDamage(0));
      items.add(itemStack);
    }
  }

  @Override
  public int getItemEnchantability() {
    return 1;
  }

  public static class Properties extends Item.Properties {

    private int fireRate;

    private int damage;

    private int reloadDurationTicks;

    private int bulletAmountToFire = 1;

    private float accuracy;

    private boolean aimable;

    private boolean crosshair = true;

    private final List<FireMode> fireModes = new ArrayList<>();

    private Supplier<SoundEvent> shootSound;

    private Supplier<SoundEvent> silencedShootSound = () -> null;

    private Supplier<SoundEvent> reloadSound = () -> null;

    private final Map<AnimationType, Supplier<GunAnimation>> animations =
        new EnumMap<>(AnimationType.class);

    private final Set<Supplier<MagazineItem>> acceptedMagazines = new HashSet<>();

    private Supplier<MagazineItem> defaultMagazine;

    private final Set<Supplier<AttachmentItem>> acceptedAttachments = new HashSet<>();

    private final Set<Supplier<PaintItem>> acceptedPaints = new HashSet<>();

    private final Set<Supplier<AttachmentItem>> defaultAttachments = new HashSet<>();

    private Supplier<DistExecutor.SafeCallable<RenderGun>> rendererFactory;

    public Properties setFireRate(int fireRate) {
      this.fireRate = fireRate;
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

    public Properties setAccuracy(float accuracy) {
      this.accuracy = accuracy;
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

    public Properties addDefaultAttachment(Supplier<AttachmentItem> defaultAttachment) {
      this.defaultAttachments.add(defaultAttachment);
      return this.addAcceptedAttachment(defaultAttachment);
    }

    public Properties addAcceptedPaint(Supplier<PaintItem> acceptedPaint) {
      this.acceptedPaints.add(acceptedPaint);
      return this;
    }

    public Properties setRendererFactory(
        Supplier<DistExecutor.SafeCallable<RenderGun>> rendererFactory) {
      this.rendererFactory = rendererFactory;
      return this;
    }
  }
}
