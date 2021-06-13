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

package com.craftingdead.core.world.gun;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.capability.Capabilities;
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
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProviderType;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.Attachments;
import com.craftingdead.core.world.gun.magazine.Magazine;
import com.craftingdead.core.world.hat.Hat;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.item.enchantment.ModEnchantments;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.TNTBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.CombatRules;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;

public abstract class AbstractGun implements Gun, INBTSerializable<CompoundNBT> {

  private static final Logger logger = LogManager.getLogger();

  private static final AtomicIntegerFieldUpdater<AbstractGun> triggerPressedUpdater =
      AtomicIntegerFieldUpdater.newUpdater(AbstractGun.class, "triggerPressed");

  /**
   * The constant difference between server and client entity positions.
   */
  private static final int BASE_SNAPSHOT_TICK_OFFSET = 3;

  public static final float HEADSHOT_MULTIPLIER = 4;

  public static final byte HIT_VALIDATION_DELAY_TICKS = 3;

  protected static final Random random = new Random();

  private static final ExecutorService executorService = Executors.newCachedThreadPool(
      new ThreadFactoryBuilder()
          .setNameFormat("gun-pool-%s")
          .setDaemon(true)
          .setPriority(Thread.MAX_PRIORITY)
          .build());

  private static final DataParameter<ItemStack> PAINT_STACK =
      new DataParameter<>(0x01, DataSerializers.ITEM_STACK);

  private final SynchedData dataManager = new SynchedData();

  protected final ItemStack itemStack;

  /**
   * If the gun trigger is pressed.
   * 
   * @see #triggerPressedUpdater
   */
  @SuppressWarnings("unused")
  @Deprecated
  private volatile int triggerPressed;
  private boolean wasTriggerPressed;

  /**
   * The amount of ticks the trigger has been pressed for. Used to determine if a hit validation
   * packet is valid.
   */
  private int triggerPressedTicks;

  /**
   * Time of the last shot in milliseconds.
   */
  protected long lastShotMs = Integer.MIN_VALUE;

  /**
   * The current {@link FireMode} being used.
   */
  private FireMode fireMode;

  /**
   * The amount of shots since the last time the trigger was pressed. Used to determine if the gun
   * can continue firing using the current fire mode.
   */
  private int shotCount;

  private Set<Attachment> attachments;
  private boolean attachmentsDirty;

  private final Iterator<FireMode> fireModeInfiniteIterator;

  private boolean performingSecondaryAction;

  private final Lazy<AbstractGunClient<?>> client;

  // Used to ensure the gun thread gets killed if we're not being ticked anymore.
  private volatile long lastTickMs;

  private AmmoProvider ammoProvider;
  private boolean ammoProviderChanged;

  @Nullable
  private Future<?> gunLoopFuture;

  @SuppressWarnings("unchecked")
  public <SELF extends AbstractGun> AbstractGun(
      Function<SELF, ? extends AbstractGunClient<? super SELF>> clientFactory,
      ItemStack itemStack, Set<FireMode> fireModes, AmmoProvider ammoProvider) {
    this.itemStack = itemStack;

    this.fireModeInfiniteIterator = Iterables.cycle(fireModes).iterator();
    this.fireMode = this.fireModeInfiniteIterator.next();
    this.client = FMLEnvironment.dist.isClient()
        ? Lazy.concurrentOf(() -> clientFactory.apply((SELF) this))
        : Lazy.of(() -> {
          throw new IllegalStateException("Cannot accesss gun client on dedicated server");
        });

    GunEvent.Initialize event = new GunEvent.Initialize(this, itemStack, ammoProvider);
    MinecraftForge.EVENT_BUS.post(event);

    this.setAmmoProvider(event.getAmmoProvider());
    this.attachments = new HashSet<>(event.getAttachments());

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
    if (this.performingSecondaryAction) {
      this.setPerformingSecondaryAction(living, false, false);
    }
  }

  @Override
  public void setTriggerPressed(LivingExtension<?, ?> living, boolean triggerPressed,
      boolean sendUpdate) {
    if (triggerPressed && (!this.canShoot(living)
        || MinecraftForge.EVENT_BUS
            .post(new GunEvent.TriggerPressed(this, this.itemStack, living)))) {
      return;
    }

    this.setTriggerPressed(triggerPressed);

    if (this.isTriggerPressed()
        && (this.gunLoopFuture == null || this.gunLoopFuture.isDone())) {
      this.gunLoopFuture = executorService.submit(() -> this.startGunLoop(living));
    } else {
      // Resets the counter
      this.shotCount = 0;
    }

    if (sendUpdate) {
      PacketTarget target = living.getLevel().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new TriggerPressedMessage(living.getEntity().getId(), triggerPressed));
    }
  }

  @Override
  public boolean isTriggerPressed() {
    return triggerPressedUpdater.get(this) != 0;
  }

  private void setTriggerPressed(boolean triggerPressed) {
    triggerPressedUpdater.set(this, triggerPressed ? 1 : 0);
  }

  @Override
  public void validatePendingHit(PlayerExtension<ServerPlayerEntity> player,
      LivingExtension<?, ?> hitLiving, PendingHit pendingHit) {
    final byte tickOffset = pendingHit.getTickOffset();
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
          .combineUntrustedSnapshot(pendingHit.getPlayerSnapshot());
    } catch (IndexOutOfBoundsException e) {
      return;
    }

    EntitySnapshot hitSnapshot;
    try {
      hitSnapshot = hitLiving.getSnapshot(tick - latencyTicks - BASE_SNAPSHOT_TICK_OFFSET)
          .combineUntrustedSnapshot(pendingHit.getHitSnapshot());
    } catch (IndexOutOfBoundsException e) {
      return;
    }

    if (!hitLiving.getEntity().isDeadOrDying()) {
      random.setSeed(pendingHit.getRandomSeed());
      hitSnapshot
          .rayTrace(player.getLevel(),
              playerSnapshot,
              this.getRange(),
              this.getAccuracy(player),
              shotCount,
              random)
          .ifPresent(hitPos -> this.hitEntity(player, hitLiving.getEntity(), hitPos, false));
    }
  }

  protected abstract double getRange();

  protected abstract long getFireDelayMs();

  private void startGunLoop(LivingExtension<?, ?> living) {
    long time;
    while (this.isTriggerPressed() && ((time = Util.getMillis()) - this.lastTickMs < 500L)) {
      long timeDelta = time - this.lastShotMs;
      if (timeDelta < this.getFireDelayMs()) {
        try {
          // Sleep to reduce CPU usage
          Thread.sleep(this.getFireDelayMs() - timeDelta);
        } catch (InterruptedException e) {
          ;
        }
        continue;
      }

      if (!this.canShoot(living)) {
        this.setTriggerPressed(false);
        return;
      }

      LogicalSide side =
          living.getLevel().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER;
      ThreadTaskExecutor<?> executor = LogicalSidedProvider.WORKQUEUE.get(side);

      if (this.ammoProvider.getMagazine().map(Magazine::getSize).orElse(0) <= 0) {
        if (side.isServer()) {
          executor.execute(() -> {
            living.getEntity().playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
            this.ammoProvider.reload(living);
          });
        }
        this.setTriggerPressed(false);
        return;
      }

      boolean isMaxShotsReached =
          this.fireMode.getMaxShots().map(max -> this.shotCount >= max).orElse(false);
      if (isMaxShotsReached) {
        this.setTriggerPressed(false);
        return;
      }

      this.shotCount++;
      this.lastShotMs = time;

      this.processShot(living, executor);

      if (side.isClient()) {
        this.getClient().handleShoot(living);
      }

      // Allow other threads to do work (mainly the main thread as we off-load a lot of tasks to it)
      Thread.yield();
    }
  }

  protected boolean canShoot(LivingExtension<?, ?> living) {
    return !living.getProgressMonitor().isPresent() && !living.getEntity().isSprinting()
        && !living.getEntity().isSpectator();
  }

  protected abstract int getRoundsPerShot();

  protected void processShot(LivingExtension<?, ?> living, ThreadTaskExecutor<?> executor) {
    Entity entity = living.getEntity();
    World level = living.getLevel();

    // Magazine size will be synced to clients so only decrement this on the server.
    if (!level.isClientSide()
        && !(living.getEntity() instanceof PlayerEntity
            && ((PlayerEntity) living.getEntity()).isCreative())) {
      final int unbreakingLevel =
          EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, this.itemStack);
      if (!UnbreakingEnchantment.shouldIgnoreDurabilityDrop(this.itemStack, unbreakingLevel,
          random)) {
        this.ammoProvider.getExpectedMagazine().decrementSize();
      }
    }

    // Used to avoid playing the same hit sound more than once.
    RayTraceResult lastRayTraceResult = null;
    for (int i = 0; i < this.getRoundsPerShot(); i++) {
      final long randomSeed = level.getGameTime() + i;
      random.setSeed(randomSeed);

      RayTraceResult rayTraceResult =
          CompletableFuture.supplyAsync(() -> RayTraceUtil.rayTrace(entity,
              this.getRange(),
              this.getAccuracy(living),
              this.shotCount,
              random).orElse(null), executor).join();

      if (rayTraceResult != null) {
        switch (rayTraceResult.getType()) {
          case BLOCK:
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
            final boolean playSound;
            if (lastRayTraceResult instanceof BlockRayTraceResult) {
              playSound =
                  !level.getBlockState(((BlockRayTraceResult) lastRayTraceResult).getBlockPos())
                      .equals(level.getBlockState(blockRayTraceResult.getBlockPos()));
            } else {
              playSound = true;
            }
            executor.execute(() -> this.hitBlock(living, (BlockRayTraceResult) rayTraceResult,
                playSound && level.isClientSide()));
            break;
          case ENTITY:
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;
            if (!entityRayTraceResult.getEntity().isAlive()) {
              break;
            }

            // Handled by validatePendingHit
            if (entityRayTraceResult.getEntity() instanceof ServerPlayerEntity
                && entity instanceof ServerPlayerEntity) {
              break;
            }

            if (level.isClientSide()) {
              this.getClient().handleHitEntityPre(living,
                  entityRayTraceResult.getEntity(),
                  entityRayTraceResult.getLocation(),
                  randomSeed);
            }

            final boolean playEntityHitSound = !(lastRayTraceResult instanceof EntityRayTraceResult)
                || !((EntityRayTraceResult) lastRayTraceResult).getEntity().getType()
                    .getRegistryName()
                    .equals(entityRayTraceResult.getEntity().getType().getRegistryName())
                    && level.isClientSide();

            executor.execute(() -> this.hitEntity(living, entityRayTraceResult.getEntity(),
                entityRayTraceResult.getLocation(), playEntityHitSound));
            break;
          default:
            break;
        }
        lastRayTraceResult = rayTraceResult;
      }
    }
  }

  protected abstract float getDamage();

  private void hitEntity(LivingExtension<?, ?> living, Entity hitEntity, Vector3d hitPos,
      boolean playSound) {
    final LivingEntity entity = living.getEntity();
    float damage = this.getDamage();

    float armorPenetration = Math.min((1.0F
        + (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ARMOR_PENETRATION.get(),
            this.itemStack) / 255.0F))
        * this.ammoProvider.getExpectedMagazine().getArmorPenetration(), 1.0F);
    if (armorPenetration > 0 && hitEntity instanceof LivingEntity) {
      LivingEntity livingEntityHit = (LivingEntity) hitEntity;
      float reducedDamage = damage - CombatRules
          .getDamageAfterAbsorb(damage, livingEntityHit.getArmorValue(),
              (float) livingEntityHit
                  .getAttribute(Attributes.ARMOR_TOUGHNESS)
                  .getValue());
      // Apply armor penetration by adding to the damage lost by armor absorption
      damage += reducedDamage * armorPenetration;
    }

    boolean headshot = false;
    if (hitEntity instanceof LivingEntity) {
      LivingExtension<?, ?> hitLiving = LivingExtension.getExpected((LivingEntity) hitEntity);
      double chinHeight = (hitEntity.getY() + hitEntity.getEyeHeight() - 0.2F);
      headshot = (hitEntity instanceof PlayerEntity || hitEntity instanceof ZombieEntity
          || hitEntity instanceof SkeletonEntity || hitEntity instanceof CreeperEntity
          || hitEntity instanceof EndermanEntity || hitEntity instanceof WitchEntity
          || hitEntity instanceof VillagerEntity || hitEntity instanceof VindicatorEntity
          || hitEntity instanceof WanderingTraderEntity) && hitPos.y >= chinHeight;
      if (headshot) {
        damage *= HEADSHOT_MULTIPLIER * (1.0F - hitLiving.getItemHandler()
            .getStackInSlot(ModEquipmentSlotType.HAT.getIndex())
            .getCapability(Capabilities.HAT)
            .map(Hat::getHeadshotReductionPercentage)
            .orElse(0.0F));
      }
    }

    // Post gun hit entity event
    GunEvent.HitEntity event =
        new GunEvent.HitEntity(this, itemStack, living, hitEntity, damage, hitPos, headshot);

    if (MinecraftForge.EVENT_BUS.post(event)) {
      return;
    }

    damage = event.getDamage();
    headshot = event.isHeadshot();

    // Simulated client-side effects
    if (living.getLevel().isClientSide()) {
      this.getClient().handleHitEntityPost(living, hitEntity, hitPos, playSound, headshot);
    } else {
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

      if (hitEntity instanceof LivingEntity) {
        final LivingEntity hitLivingEntity = (LivingEntity) hitEntity;

        // Alert client of hit (real hit as opposed to client prediction)
        if (entity instanceof ServerPlayerEntity) {
          boolean dead = hitLivingEntity.isDeadOrDying();
          NetworkChannel.PLAY.getSimpleChannel().send(
              PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity),
              new HitMessage(hitPos, dead));
        }
      }
    }
  }

  private void hitBlock(LivingExtension<?, ?> living, BlockRayTraceResult rayTrace,
      boolean playSound) {
    final LivingEntity entity = living.getEntity();
    BlockPos blockPos = rayTrace.getBlockPos();
    BlockState blockState = entity.level.getBlockState(blockPos);
    Block block = blockState.getBlock();
    World world = entity.level;

    // Post gun hit block event
    GunEvent.HitBlock event =
        new GunEvent.HitBlock(this, itemStack, block, blockPos, living, world);

    if (MinecraftForge.EVENT_BUS.post(event)) {
      return;
    }

    // Client-side effects
    if (world.isClientSide()) {
      this.getClient().handleHitBlock(living, rayTrace, playSound);
    } else {
      if (blockState.getBlock() instanceof BellBlock) {
        ((BellBlock) blockState.getBlock()).onHit(world, blockState, rayTrace,
            entity instanceof PlayerEntity ? (PlayerEntity) entity : null, playSound);
      }

      if (block instanceof TNTBlock) {
        block.catchFire(blockState, entity.level, blockPos, null,
            entity instanceof LivingEntity ? (LivingEntity) entity : null);
        entity.level.removeBlock(blockPos, false);
      }

      checkCreateExplosion(this.itemStack, entity, rayTrace.getLocation());

      if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS,
          this.itemStack) > 0) {
        if (CampfireBlock.canLight(blockState)) {
          world.setBlock(blockPos,
              blockState.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
        } else {
          BlockPos faceBlockPos = blockPos.relative(rayTrace.getDirection());
          if (AbstractFireBlock.canBePlacedAt(world, faceBlockPos, entity.getDirection())) {
            BlockState blockstate1 = AbstractFireBlock.getState(world, faceBlockPos);
            world.setBlock(faceBlockPos, blockstate1, 11);
          }
        }
      }
    }
  }

  @Override
  public Set<Attachment> getAttachments() {
    return Collections.unmodifiableSet(this.attachments);
  }

  @Override
  public void setAttachments(Set<Attachment> attachments) {
    this.attachments = attachments;
  }

  @Override
  public ItemStack getPaintStack() {
    return this.dataManager.get(PAINT_STACK);
  }

  @Override
  public void setPaintStack(ItemStack paintStack) {
    this.dataManager.set(PAINT_STACK, paintStack);
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
    if (living.getEntity() instanceof PlayerEntity) {
      ((PlayerEntity) living.getEntity())
          .displayClientMessage(new TranslationTextComponent("message.switch_fire_mode",
              new TranslationTextComponent(this.fireMode.getTranslationKey())), true);
    }

    if (sendUpdate) {
      PacketTarget target = living.getLevel().isClientSide()
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

  @Override
  public void setPerformingSecondaryAction(LivingExtension<?, ?> living,
      boolean performingAction, boolean sendUpdate) {
    if (performingAction && living.getEntity().isSprinting()) {
      return;
    }

    this.performingSecondaryAction = performingAction;
    if (living.getLevel().isClientSide()) {
      this.getClient().handleToggleSecondaryAction(living);
    }

    if (sendUpdate) {
      PacketTarget target = living.getLevel().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new SecondaryActionMessage(living.getEntity().getId(), this.performingSecondaryAction));
    }
  }

  protected int getShotCount() {
    return this.shotCount;
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
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putString("ammoProviderType", this.ammoProvider.getType().getRegistryName().toString());
    nbt.put("ammoProvider", this.ammoProvider.serializeNBT());
    ListNBT attachmentsNbt = this.attachments.stream()
        .map(Attachment::getRegistryName)
        .map(ResourceLocation::toString)
        .map(StringNBT::valueOf)
        .collect(ListNBT::new, ListNBT::add, List::addAll);
    nbt.put("attachments", attachmentsNbt);
    nbt.put("paintStack", this.getPaintStack().serializeNBT());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    if (nbt.contains("ammoProviderType", Constants.NBT.TAG_STRING)) {
      this.setAmmoProvider(GameRegistry.findRegistry(AmmoProviderType.class)
          .getValue(new ResourceLocation(nbt.getString("ammoProviderType"))).create());
      this.ammoProvider.deserializeNBT(nbt.getCompound("ammoProvider"));
      this.ammoProviderChanged = true;
    }
    this.setAttachments(nbt.getList("attachments", 8)
        .stream()
        .map(INBT::getAsString)
        .map(ResourceLocation::new)
        .map(Attachments.REGISTRY.get()::getValue)
        .collect(Collectors.toSet()));
    this.setPaintStack(ItemStack.of(nbt.getCompound("paintStack")));
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    SynchedData
        .writeEntries(writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), out);
    if (writeAll || this.attachmentsDirty) {
      out.writeVarInt(this.attachments.size());
      this.attachments.forEach(out::writeRegistryId);
    } else {
      out.writeVarInt(-1);
    }
    this.attachmentsDirty = false;

    if (this.ammoProviderChanged || writeAll) {
      out.writeBoolean(true);
      out.writeRegistryId(this.ammoProvider.getType());
    } else {
      out.writeBoolean(false);
    }
    this.ammoProvider.encode(out, this.ammoProviderChanged || writeAll);
    this.ammoProviderChanged = false;
  }

  @Override
  public void decode(PacketBuffer in) {
    this.dataManager.setEntryValues(SynchedData.readEntries(in));

    int size = in.readVarInt();
    if (size > -1) {
      this.attachments.clear();
      for (int i = 0; i < size; i++) {
        this.attachments.add(in.readRegistryIdSafe(Attachment.class));
      }
    }

    if (in.readBoolean()) {
      this.ammoProvider = in.readRegistryIdSafe(AmmoProviderType.class).create();
    }
    this.ammoProvider.decode(in);
  }

  @Override
  public boolean requiresSync() {
    return this.attachmentsDirty
        || this.dataManager.isDirty()
        || this.ammoProvider.requiresSync();
  }

  private static void checkCreateExplosion(ItemStack magazineStack, Entity entity,
      Vector3d position) {
    float explosionSize =
        EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, magazineStack)
            / (float) Enchantments.POWER_ARROWS.getMaxLevel();
    if (explosionSize > 0) {
      entity.level.explode(
          entity, position.x(), position.y(), position.z(), explosionSize, Explosion.Mode.NONE);
    }
  }
}
