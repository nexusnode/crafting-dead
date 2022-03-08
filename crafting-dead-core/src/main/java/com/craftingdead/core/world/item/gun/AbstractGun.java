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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.network.message.play.HitMessage;
import com.craftingdead.core.network.message.play.SecondaryActionMessage;
import com.craftingdead.core.network.message.play.SetFireModeMessage;
import com.craftingdead.core.network.message.play.TriggerPressedMessage;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.world.damagesource.ModDamageSource;
import com.craftingdead.core.world.entity.extension.EntitySnapshot;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.item.enchantment.ModEnchantments;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProviderType;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProviderTypes;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import com.craftingdead.core.world.item.gun.skin.Paint;
import com.craftingdead.core.world.item.gun.skin.Skin;
import com.craftingdead.core.world.item.hat.Hat;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;

public abstract class AbstractGun implements Gun, INBTSerializable<CompoundTag> {

  private static final Logger logger = LogUtils.getLogger();

  public static final byte HIT_VALIDATION_DELAY_TICKS = 3;

  private static final ScheduledExecutorService executorService =
      Executors.newScheduledThreadPool(3,
          new ThreadFactoryBuilder()
              .setNameFormat("gun-pool-%s")
              .setDaemon(true)
              .setPriority(Thread.MAX_PRIORITY)
              .build());

  private static final EntityDataAccessor<ItemStack> PAINT_STACK =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.ITEM_STACK);

  private final SynchedData dataManager = new SynchedData();

  protected final ItemStack itemStack;

  /**
   * If the gun's trigger is pressed.
   * 
   * @see #triggerPressedUpdater
   */
  private boolean wasTriggerPressed;

  /**
   * The amount of ticks the trigger has been pressed for. Used to determine if a hit validation
   * packet is valid.
   */
  private volatile int triggerPressedTicks;

  /**
   * The current {@link FireMode} being used.
   */
  private volatile FireMode fireMode;

  /**
   * The amount of shots since the last time the trigger was pressed. Used to determine if the gun
   * can continue firing using the current fire mode.
   */
  private final AtomicInteger shotCount = new AtomicInteger();

  /**
   * Immutable set of attachments.
   */
  private Set<Attachment> attachments;
  private boolean attachmentsDirty;

  private final Iterator<FireMode> fireModeInfiniteIterator;

  private volatile boolean performingSecondaryAction;

  private final Lazy<AbstractGunClient<?>> client;

  // Used to ensure the gun thread gets killed if we're not being ticked anymore.
  private volatile long lastTickMs;

  private volatile AmmoProvider ammoProvider;
  private boolean ammoProviderChanged;

  @Nullable
  private volatile Future<?> gunFuture;

  protected volatile long lastShotMs;

  private boolean initialized;

  @Nullable
  private Holder<Skin> skin;
  private boolean skinDirty;

  @SuppressWarnings("unchecked")
  public <SELF extends AbstractGun> AbstractGun(
      Function<SELF, ? extends AbstractGunClient<? super SELF>> clientFactory,
      ItemStack itemStack, Set<FireMode> fireModes) {
    this.itemStack = itemStack;
    this.fireModeInfiniteIterator = Iterables.cycle(Iterables.filter(fireModes,
        (mode) -> mode != FireMode.BURST || CraftingDead.serverConfig.burstfireEnabled.get()))
        .iterator();
    this.fireMode = this.fireModeInfiniteIterator.next();
    this.client = FMLEnvironment.dist.isClient()
        ? Lazy.concurrentOf(() -> clientFactory.apply((SELF) this))
        : Lazy.of(() -> {
          throw new IllegalStateException("Cannot access gun client on dedicated server");
        });
  }

  /**
   * Initialise the gun - must be called before usage. This is not done in the constructor so that
   * the {@link GunEvent.Initialize} event is posted after the gun is constructed, preventing access
   * to uninitialised fields.
   */
  protected void initialize() {
    if (this.initialized) {
      throw new IllegalStateException("Already initialized");
    }
    this.initialized = true;

    GunEvent.Initialize event =
        new GunEvent.Initialize(this, this.itemStack, this.createAmmoProvider());
    MinecraftForge.EVENT_BUS.post(event);

    this.setAmmoProvider(event.getAmmoProvider());
    this.attachments = Set.copyOf(event.getAttachments());

    this.dataManager.register(PAINT_STACK, ItemStack.EMPTY);
  }

  protected abstract Set<FireMode> getFireModes();

  protected abstract AmmoProvider createAmmoProvider();

  @Override
  public void tick(LivingExtension<?, ?> living) {
    this.lastTickMs = Util.getMillis();

    if (!living.getLevel().isClientSide() && !this.isTriggerPressed()
        && this.wasTriggerPressed) {
      this.triggerPressedTicks = living.getEntity().getServer().getTickCount();
    }
    this.wasTriggerPressed = this.isTriggerPressed();


    if (this.isPerformingSecondaryAction() && living.getEntity().isSprinting()) {
      this.setPerformingSecondaryAction(living, false, true);
    }

    if (living.getLevel().isClientSide()) {
      this.getClient().handleTick(living);
    }
  }

  @Override
  public void reset(LivingExtension<?, ?> living) {
    this.setTriggerPressed(living, false, false);
    if (this.isPerformingSecondaryAction()) {
      this.setPerformingSecondaryAction(living, false, false);
    }
  }

  @Override
  public void setTriggerPressed(LivingExtension<?, ?> living, boolean triggerPressed,
      boolean sendUpdate) {
    if (triggerPressed == this.isTriggerPressed() || (triggerPressed && (!this.canShoot(living)
        || MinecraftForge.EVENT_BUS.post(
            new GunEvent.TriggerPressed(this, this.itemStack, living))))) {
      return;
    }

    if (triggerPressed) {
      this.gunFuture = executorService.scheduleAtFixedRate(() -> this.shoot(living), 0L,
          this.getFireDelayMs(), TimeUnit.MILLISECONDS);
    } else {
      this.stopShooting();
    }

    if (sendUpdate) {
      var target = living.getLevel().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new TriggerPressedMessage(living.getEntity().getId(), triggerPressed));
    }
  }

  @Override
  public boolean isTriggerPressed() {
    return this.gunFuture != null && !this.gunFuture.isDone();
  }

  private void stopShooting() {
    this.shotCount.set(0);
    if (this.gunFuture != null) {
      this.gunFuture.cancel(false);
    }
  }

  @Override
  public void validatePendingHit(PlayerExtension<ServerPlayer> player,
      LivingExtension<?, ?> hitLiving, PendingHit pendingHit) {
    final byte tickOffset = pendingHit.tickOffset();

    if (tickOffset > HIT_VALIDATION_DELAY_TICKS) {
      logger.warn("Bad living hit packet received, tick offset is too big!");
      return;
    }

    int latencyTicks = (player.getEntity().latency / 1000) * 20 + tickOffset;
    int tick = player.getEntity().getServer().getTickCount();

    if (tick - latencyTicks > this.triggerPressedTicks && !this.isTriggerPressed()) {
      return;
    }

    EntitySnapshot playerSnapshot;
    try {
      playerSnapshot = player.getSnapshot(tick - latencyTicks)
          .combineUntrustedSnapshot(pendingHit.playerSnapshot());
    } catch (IndexOutOfBoundsException e) {
      return;
    }

    EntitySnapshot hitSnapshot;
    try {
      hitSnapshot = hitLiving.getSnapshot(tick - latencyTicks)
          .combineUntrustedSnapshot(pendingHit.hitSnapshot());
    } catch (IndexOutOfBoundsException e) {
      return;
    }

    if (hitLiving.getEntity().isAlive()) {
      var random = player.getRandom();
      random.setSeed(pendingHit.randomSeed());
      rayTrace(player.getLevel(), playerSnapshot, hitSnapshot, this.getRange(),
          this.getAccuracy(player, random), pendingHit.shotCount(), random)
              .ifPresent(hitPos -> this.hitEntity(player, hitLiving.getEntity(), hitPos, false));
    }
  }

  protected abstract double getRange();

  protected abstract long getFireDelayMs();

  /*
   * Warning: this is not called on the main thread.
   */
  private void shoot(LivingExtension<?, ?> living) {
    long time = Util.getMillis();

    if (!this.isTriggerPressed()
        || (time - this.lastTickMs >= 500L)
        || !this.canShoot(living)) {
      this.stopShooting();
      return;
    }

    // Add 5 to account for inconsistencies in timing
    if (time - this.lastShotMs + 10 < this.getFireDelayMs()) {
      return;
    }
    this.lastShotMs = time;

    LogicalSide side = living.getLevel().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER;
    BlockableEventLoop<?> executor = LogicalSidedProvider.WORKQUEUE.get(side);

    if (this.ammoProvider.getMagazine().map(Magazine::getSize).orElse(0) <= 0) {
      if (side.isServer()) {
        executor.execute(() -> {
          living.getEntity().playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
          this.ammoProvider.reload(living);
        });
      }
      this.stopShooting();
      return;
    }

    int shotCount = this.shotCount.getAndIncrement();
    int maxShots = this.fireMode.getMaxShots().orElse(Integer.MAX_VALUE);
    if (shotCount >= maxShots) {
      this.stopShooting();
      return;
    }

    executor.execute(() -> this.processShot(living));

    if (side.isClient()) {
      this.getClient().handleShoot(living);
    }

    Thread.yield();
  }

  protected boolean canShoot(LivingExtension<?, ?> living) {
    return !living.getActionObserver().isPresent() && !living.getEntity().isSprinting()
        && !living.getEntity().isSpectator()
        && !(living instanceof PlayerExtension<?> player && player.isHandcuffed());
  }

  protected abstract int getRoundsPerShot();

  protected void processShot(LivingExtension<?, ?> living) {
    var entity = living.getEntity();
    var level = living.getLevel();
    var random = entity.getRandom();

    MinecraftForge.EVENT_BUS.post(new GunEvent.Shoot(this, this.itemStack, living));

    // Magazine size will be synced to clients so only decrement this on the server.
    if (!level.isClientSide()
        && !(living.getEntity() instanceof Player
            && ((Player) living.getEntity()).isCreative())) {
      final int unbreakingLevel =
          EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, this.itemStack);
      if (!DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(
          this.itemStack, unbreakingLevel, level.getRandom())) {
        this.ammoProvider.getExpectedMagazine().decrementSize();
      }
    }

    // Used to avoid playing the same hit sound more than once.
    boolean hitEntity = false;
    Set<BlockState> blocksHit = new HashSet<>();

    for (int i = 0; i < this.getRoundsPerShot(); i++) {
      final long randomSeed = level.getGameTime() + i;
      random.setSeed(randomSeed);

      var partialTick = level.isClientSide() ? this.getClient().getPartialTick() : 1.0F;
      var hitResult = rayTrace(entity, this.getRange(), partialTick,
          this.getAccuracy(living, random), this.getShotCount(), random).orElse(null);

      if (hitResult != null) {
        switch (hitResult.getType()) {
          case BLOCK:
            var blockHitResult = (BlockHitResult) hitResult;
            var blockState = level.getBlockState(blockHitResult.getBlockPos());
            this.hitBlock(living, blockHitResult, blockState,
                level.isClientSide() && (i == 0 || !blocksHit.contains(blockState)));
            blocksHit.add(blockState);
            break;
          case ENTITY:
            var entityHitResult = (EntityHitResult) hitResult;
            if (!entityHitResult.getEntity().isAlive()) {
              break;
            }

            // Handled by validatePendingHit
            if (entityHitResult.getEntity() instanceof LivingEntity
                && entity instanceof ServerPlayer) {
              break;
            }

            if (level.isClientSide()) {
              this.getClient().handleHitEntityPre(living,
                  entityHitResult.getEntity(),
                  entityHitResult.getLocation(),
                  randomSeed);
            }

            this.hitEntity(living, entityHitResult.getEntity(),
                entityHitResult.getLocation(), !hitEntity && level.isClientSide());
            hitEntity = true;
            break;
          default:
            break;
        }
      }
    }
  }

  protected abstract float getDamage();

  private void hitEntity(LivingExtension<?, ?> living, Entity hitEntity, Vec3 hitPos,
      boolean playSound) {
    final var entity = living.getEntity();
    var damage = this.getDamage();
    if (CraftingDead.serverConfig.damageDropOffEnable.get()) {
      var distance = hitEntity.distanceTo(living.getEntity());
      // Ensure minimum damage
      var minDamage =
          Math.min(damage, CraftingDead.serverConfig.damageDropOffMinimumDamage.get().floatValue());
      damage = Math.max(minDamage, damage
          - (float) (((CraftingDead.serverConfig.damageDropOffLoss.get() / 100) * this.getRange())
              * distance));
    }

    var armorPenetration = Math.min((1.0F
        + (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ARMOR_PENETRATION.get(),
            this.itemStack) / 255.0F))
        * this.ammoProvider.getExpectedMagazine().getArmorPenetration(), 1.0F);
    if (armorPenetration > 0 && hitEntity instanceof LivingEntity livingEntityHit) {
      var reducedDamage =
          damage - CombatRules.getDamageAfterAbsorb(damage, livingEntityHit.getArmorValue(),
              (float) livingEntityHit.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue());
      // Apply armor penetration by adding to the damage lost by armor absorption
      damage += reducedDamage * armorPenetration;
    }

    boolean headshot = false;
    if (hitEntity instanceof LivingEntity) {
      var hitLiving = LivingExtension.getOrThrow((LivingEntity) hitEntity);
      double chinHeight = (hitEntity.getY() + hitEntity.getEyeHeight() - 0.2F);
      headshot = CraftingDead.serverConfig.headshotEnabled.get() &&
          (hitEntity instanceof Player || hitEntity instanceof Zombie
              || hitEntity instanceof Skeleton || hitEntity instanceof Creeper
              || hitEntity instanceof EnderMan || hitEntity instanceof Witch
              || hitEntity instanceof Villager || hitEntity instanceof Vindicator
              || hitEntity instanceof WanderingTrader)
          && hitPos.y >= chinHeight;
      if (headshot) {
        var damagePercentage = 1.0F - hitLiving.getItemHandler()
            .getStackInSlot(ModEquipmentSlot.HAT.getIndex())
            .getCapability(Hat.CAPABILITY)
            .map(Hat::getHeadshotReductionPercentage)
            .orElse(0.0F);
        if (damagePercentage < 1.0F) {
          damage *= damagePercentage;
        } else {
          damage *= CraftingDead.serverConfig.headshotBonusDamage.get();
        }
      }
    }

    // Post gun hit entity event
    var event =
        new GunEvent.HitEntity(this, itemStack, living, hitEntity, damage, hitPos, headshot);
    if (MinecraftForge.EVENT_BUS.post(event)) {
      return;
    }

    damage = event.getDamage();
    headshot = event.isHeadshot();

    // Simulated client-side effects
    if (living.getLevel().isClientSide()) {
      this.getClient().handleHitEntityPost(living, hitEntity, hitPos, playSound, headshot);
      return;
    }

    // Resets the temporary invincibility before causing the damage, preventing
    // previous damages from blocking the gun damage.
    // Also, allows multiple bullets to hit the same target at the same time.
    hitEntity.invulnerableTime = 0;

    ModDamageSource.causeDamageWithoutKnockback(hitEntity,
        ModDamageSource.causeGunDamage(entity, this.itemStack, headshot), damage);

    checkCreateExplosion(this.itemStack, entity, hitPos);

    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS,
        this.itemStack) > 0) {
      hitEntity.setSecondsOnFire(100);
    }

    if (hitEntity instanceof LivingEntity hitLivingEntity
        && entity instanceof ServerPlayer player) {
      // Alert client of hit (real hit as opposed to client prediction)
      NetworkChannel.PLAY.getSimpleChannel().send(
          PacketDistributor.PLAYER.with(() -> player),
          new HitMessage(hitPos, hitLivingEntity.isDeadOrDying()));
    }
  }

  private void hitBlock(LivingExtension<?, ?> living, BlockHitResult result,
      BlockState blockState, boolean playSound) {
    final var entity = living.getEntity();
    final var block = blockState.getBlock();
    final var level = entity.getLevel();
    final var blockPos = result.getBlockPos();

    // Post gun hit block event
    var event =
        new GunEvent.HitBlock(this, itemStack, result, blockState, living, level);
    if (MinecraftForge.EVENT_BUS.post(event)) {
      return;
    }

    // Client-side effects
    if (level.isClientSide()) {
      this.getClient().handleHitBlock(living, result, blockState, playSound);
      return;
    }

    if (block instanceof BellBlock bell) {
      bell.onHit(level, blockState, result, entity instanceof Player player ? player : null,
          playSound);
    }

    if (block instanceof TntBlock tnt) {
      tnt.onCaughtFire(blockState, level, blockPos, null, entity);
      level.removeBlock(blockPos, false);
    }

    checkCreateExplosion(this.itemStack, entity, result.getLocation());

    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS,
        this.itemStack) > 0) {
      if (CampfireBlock.canLight(blockState)) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.LIT, true));
      } else {
        var directedPos = blockPos.relative(result.getDirection());
        if (BaseFireBlock.canBePlacedAt(level, directedPos, entity.getDirection())) {
          level.setBlockAndUpdate(directedPos, BaseFireBlock.getState(level, directedPos));
        }
      }
    }
  }

  @Override
  public Set<Attachment> getAttachments() {
    return this.attachments;
  }

  @Override
  public void setAttachments(Set<Attachment> attachments) {
    this.attachments = Set.copyOf(attachments);
  }

  @Override
  public ItemStack getPaintStack() {
    return this.dataManager.get(PAINT_STACK);
  }

  @Override
  public void setPaintStack(ItemStack paintStack) {
    this.dataManager.set(PAINT_STACK, paintStack);
    this.setSkin(paintStack.getCapability(Paint.CAPABILITY)
        .map(Paint::getSkin)
        .orElse(null));
  }

  @Override
  public Skin getSkin() {
    return this.skin == null ? null : this.skin.value();
  }

  @Override
  public void setSkin(Holder<Skin> skin) {
    this.skin = skin;
    this.skinDirty = true;
  }

  @Override
  public void toggleFireMode(LivingExtension<?, ?> living, boolean sendUpdate) {
    if (this.fireModeInfiniteIterator.hasNext()) {
      this.setFireMode(living, this.fireModeInfiniteIterator.next(), sendUpdate);
    }
  }

  @Override
  public void setFireMode(LivingExtension<?, ?> living, FireMode fireMode, boolean sendUpdate) {
    this.fireMode = fireMode;

    living.getEntity().playSound(ModSoundEvents.TOGGLE_FIRE_MODE.get(), 1.0F, 1.0F);
    if (living.getEntity() instanceof Player player) {
      player.displayClientMessage(new TranslatableComponent("message.switch_fire_mode",
          new TranslatableComponent(this.fireMode.getTranslationKey())), true);
    }

    if (sendUpdate) {
      var target = living.getLevel().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY
          .getSimpleChannel()
          .send(target, new SetFireModeMessage(living.getEntity().getId(), this.fireMode));
    }
  }

  @Override
  public boolean isPerformingSecondaryAction() {
    return this.performingSecondaryAction;
  }

  protected boolean canPerformSecondaryAction(LivingExtension<?, ?> living) {
    return !living.getEntity().isSprinting()
        && !(living instanceof PlayerExtension<?> player && player.isHandcuffed());
  }

  @Override
  public void setPerformingSecondaryAction(LivingExtension<?, ?> living,
      boolean performingAction, boolean sendUpdate) {
    if (performingAction == this.performingSecondaryAction
        || (performingAction && !this.canPerformSecondaryAction(living))) {
      return;
    }

    this.performingSecondaryAction = performingAction;

    if (living.getLevel().isClientSide()) {
      this.getClient().handleToggleSecondaryAction(living);
    }

    if (sendUpdate) {
      var target = living.getLevel().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new SecondaryActionMessage(living.getEntity().getId(),
              this.isPerformingSecondaryAction()));
    }
  }

  protected int getShotCount() {
    return this.shotCount.get();
  }

  @Override
  public FireMode getFireMode() {
    return this.fireMode;
  }

  @Override
  public AbstractGunClient<?> getClient() {
    return this.client.get();
  }

  @Override
  public AmmoProvider getAmmoProvider() {
    return this.ammoProvider;
  }

  @Override
  public void setAmmoProvider(AmmoProvider ammoProvider) {
    this.ammoProvider = ammoProvider;
    this.ammoProviderChanged = true;
  }

  @Override
  public ItemStack getItemStack() {
    return this.itemStack;
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putString("ammoProviderType", this.ammoProvider.getType().getRegistryName().toString());
    nbt.put("ammoProvider", this.ammoProvider.serializeNBT());
    ListTag attachmentsNbt = this.attachments.stream()
        .map(Attachment::getRegistryName)
        .map(ResourceLocation::toString)
        .map(StringTag::valueOf)
        .collect(ListTag::new, ListTag::add, List::addAll);
    nbt.put("attachments", attachmentsNbt);
    nbt.put("paintStack", this.getPaintStack().serializeNBT());
    if (this.skin != null) {
      nbt.put("skin", Skin.CODEC.encodeStart(NbtOps.INSTANCE, this.skin)
          .getOrThrow(false, logger::error));
    }
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    if (nbt.contains("ammoProviderType", Tag.TAG_STRING)) {
      this.setAmmoProvider(AmmoProviderTypes.registry.get().getValue(
          new ResourceLocation(nbt.getString("ammoProviderType"))).create());
      this.ammoProvider.deserializeNBT(nbt.getCompound("ammoProvider"));
      this.ammoProviderChanged = true;
    }
    this.setAttachments(nbt.getList("attachments", 8)
        .stream()
        .map(Tag::getAsString)
        .map(ResourceLocation::new)
        .map(Attachments.REGISTRY.get()::getValue)
        .collect(Collectors.toSet()));
    this.setPaintStack(ItemStack.of(nbt.getCompound("paintStack")));
    this.skin = Skin.CODEC.parse(NbtOps.INSTANCE, nbt.get("skin")).result().orElse(null);
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    SynchedData.pack(writeAll
        ? this.dataManager.getAll()
        : this.dataManager.packDirty(), out);
    if (writeAll || this.attachmentsDirty) {
      out.writeVarInt(this.attachments.size());
      this.attachments.forEach(out::writeRegistryId);
    } else {
      out.writeVarInt(-1);
    }
    this.attachmentsDirty = false;

    if (this.ammoProviderChanged || writeAll) {
      this.ammoProviderChanged = false;
      out.writeBoolean(true);
      out.writeRegistryId(this.ammoProvider.getType());
    } else {
      out.writeBoolean(false);
    }
    this.ammoProvider.encode(out, this.ammoProviderChanged || writeAll);

    if (this.skinDirty || writeAll) {
      this.skinDirty = false;
      out.writeBoolean(true);
      if (this.skin == null) {
        out.writeBoolean(true);
      } else {
        out.writeBoolean(false);
        out.writeWithCodec(Skin.CODEC, this.skin);
      }
    } else {
      out.writeBoolean(false);
    }
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.dataManager.assignValues(SynchedData.unpack(in));

    int size = in.readVarInt();
    if (size > -1) {
      var builder = ImmutableSet.<Attachment>builderWithExpectedSize(size);
      for (int i = 0; i < size; i++) {
        builder.add(in.readRegistryIdSafe(Attachment.class));
      }
      this.attachments = builder.build();
    }

    if (in.readBoolean()) {
      this.ammoProvider = in.readRegistryIdSafe(AmmoProviderType.class).create();
    }
    this.ammoProvider.decode(in);

    if (in.readBoolean()) {
      if (in.readBoolean()) {
        this.skin = null;
      } else {
        this.skin = in.readWithCodec(Skin.CODEC);
      }
    }
  }

  @Override
  public boolean requiresSync() {
    return this.attachmentsDirty
        || this.dataManager.isDirty()
        || this.ammoProvider.requiresSync()
        || this.skinDirty;
  }

  private static void checkCreateExplosion(ItemStack magazineStack, Entity entity,
      Vec3 position) {
    float explosionSize =
        EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, magazineStack)
            / (float) Enchantments.POWER_ARROWS.getMaxLevel();
    if (explosionSize > 0) {
      entity.getLevel().explode(
          entity, position.x(), position.y(), position.z(), explosionSize,
          Explosion.BlockInteraction.NONE);
    }
  }

  public static Optional<? extends HitResult> rayTrace(Entity fromEntity,
      double distance, float partialTick, float accuracy, int shotCount, Random random) {
    return RayTraceUtil.rayTrace(fromEntity, distance, partialTick,
        getAccuracyOffset(accuracy, shotCount, random),
        getAccuracyOffset(accuracy, shotCount, random));
  }

  public static Optional<Vec3> rayTrace(Level level, EntitySnapshot fromSnapshot,
      EntitySnapshot targetSnapshot, double distance, float accuracy,
      int shotCount, Random random) {
    if (!fromSnapshot.complete() || !targetSnapshot.complete()) {
      return Optional.empty();
    }

    var startPos = fromSnapshot.position().add(0.0D, fromSnapshot.eyeHeight(), 0.0D);
    var look = RayTraceUtil.calculateViewVector(
        fromSnapshot.rotation().x + getAccuracyOffset(accuracy, shotCount, random),
        fromSnapshot.rotation().y + getAccuracyOffset(accuracy, shotCount, random));

    var blockRayTraceResult = RayTraceUtil.rayTraceBlocks(startPos, distance, look, level);

    var scaledLook = look.scale(distance);

    var endPos = blockRayTraceResult
        .map(HitResult::getLocation)
        .orElse(startPos.add(scaledLook));

    var potentialHit = targetSnapshot.boundingBox().clip(startPos, endPos);
    if (targetSnapshot.boundingBox().contains(startPos)) {
      return Optional.of(potentialHit.orElse(startPos));
    } else {
      return potentialHit;
    }
  }

  public static float getAccuracyOffset(float accuracy, int shotCount, Random random) {
    return (1.0F - (accuracy * accuracy)) * (Math.min(20, shotCount + 1) / 2.0F)
        * ((1.0F - accuracy) * (random.nextInt(9) + 1))
        * (random.nextInt(5) % 2 == 0 ? -1.0F : 1.0F);
  }
}
