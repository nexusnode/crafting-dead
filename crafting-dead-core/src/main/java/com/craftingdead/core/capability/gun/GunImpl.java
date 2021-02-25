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

package com.craftingdead.core.capability.gun;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.action.ReloadAction;
import com.craftingdead.core.action.RemoveMagazineAction;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimation;
import com.craftingdead.core.capability.clothing.IClothing;
import com.craftingdead.core.capability.living.EntitySnapshot;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.enchantment.ModEnchantments;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.inventory.CombatSlotType;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.AttachmentItem.MultiplierType;
import com.craftingdead.core.item.FireMode;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.HitMessage;
import com.craftingdead.core.network.message.play.RightMouseAction;
import com.craftingdead.core.network.message.play.SetFireModeMessage;
import com.craftingdead.core.network.message.play.TriggerPressedMessage;
import com.craftingdead.core.network.util.NetworkDataManager;
import com.craftingdead.core.util.ModDamageSource;
import com.craftingdead.core.util.ModSoundEvents;
import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.util.Text;
import com.google.common.collect.ImmutableSet;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.CombatRules;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class GunImpl implements IGun {

  private static final Logger logger = LogManager.getLogger();

  private static final AtomicIntegerFieldUpdater<GunImpl> triggerPressedUpdater =
      AtomicIntegerFieldUpdater.newUpdater(GunImpl.class, "triggerPressed");

  private static final int BASE_LATENCY = 4;

  public static final float HEADSHOT_MULTIPLIER = 4;

  static final byte HIT_VALIDATION_DELAY_TICKS = 3;

  private static final Random random = new Random();

  // @formatter:off
  private static final ExecutorService executorSerivce = Executors.newCachedThreadPool(
      new ThreadFactoryBuilder()
          .setNameFormat("gun-pool-%s")
          .build());
  // @formatter:on

  private static final DataParameter<ItemStack> MAGAZINE_STACK =
      new DataParameter<>(0x00, DataSerializers.ITEMSTACK);
  private static final DataParameter<ItemStack> PAINT_STACK =
      new DataParameter<>(0x01, DataSerializers.ITEMSTACK);

  private final NetworkDataManager dataManager = new NetworkDataManager();

  protected final IGunProvider gunProvider;

  protected final ItemStack gunStack;

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

  private Set<AttachmentItem> attachments;
  private boolean attachmentsDirty;

  private final Iterator<FireMode> fireModeInfiniteIterator;

  private boolean performingRightMouseAction;

  private final IGunClient clientHandler;

  private final IItemHandlerModifiable ammoReserve;
  private boolean ammoReserveDirty;
  private int cachedAmmoReserveSize;

  // Used to ensure the gun thread gets killed if we're not being ticked anymore.
  private volatile long lastTickMs;

  public GunImpl() {
    throw new UnsupportedOperationException("Specify gun item");
  }

  public GunImpl(IGunProvider gunProvider, ItemStack gunStack) {
    this.gunProvider = gunProvider;
    this.gunStack = gunStack;

    this.fireModeInfiniteIterator = Iterables.cycle(this.gunProvider.getFireModes()).iterator();
    this.fireMode = this.fireModeInfiniteIterator.next();
    this.clientHandler = this.createClientHandler();

    GunEvent.Initialize event = new GunEvent.Initialize(this, gunStack);
    MinecraftForge.EVENT_BUS.post(event);

    this.attachments = new HashSet<>(event.getAttachments());

    Collection<ItemStack> ammoReserve = event.getAmmoReserve();
    if (ammoReserve.isEmpty()) {
      ammoReserve = Collections.singleton(this.getDefaultMagazineStack());
    }

    this.ammoReserve =
        new ItemStackHandler(NonNullList.from(null, ammoReserve.toArray(new ItemStack[0]))) {
          @Override
          protected void onContentsChanged(int slot) {
            GunImpl.this.updateAmmoReserveSize();
            GunImpl.this.ammoReserveDirty = true;
          }
        };

    this.dataManager.register(MAGAZINE_STACK, this.ammoReserve.extractItem(0, 1, false));
    this.dataManager.register(PAINT_STACK, ItemStack.EMPTY);

    this.updateAmmoReserveSize();
  }

  private void updateAmmoReserveSize() {
    this.cachedAmmoReserveSize = 0;
    for (int i = 0; i < this.ammoReserve.getSlots(); i++) {
      ItemStack itemStack = this.ammoReserve.getStackInSlot(i);
      if (!itemStack.isEmpty()) {
        this.cachedAmmoReserveSize += itemStack
            .getCapability(ModCapabilities.MAGAZINE)
            .orElseThrow(() -> new IllegalStateException("Expecting magazine capability"))
            .getSize();
      }
    }
  }

  protected IGunClient createClientHandler() {
    return DistExecutor.unsafeCallWhenOn(Dist.CLIENT,
        () -> () -> new GunClientImpl<>(this));
  }

  @Override
  public void tick(ILiving<?, ?> living) {
    this.lastTickMs = Util.milliTime();

    if (!living.getEntity().getEntityWorld().isRemote() && !this.isTriggerPressed()
        && this.wasTriggerPressed) {
      this.triggerPressedTicks = living.getEntity().getServer().getTickCounter();
    }
    this.wasTriggerPressed = this.isTriggerPressed();


    if (this.isPerformingRightMouseAction() && living.getEntity().isSprinting()) {
      this.setPerformingRightMouseAction(living, false, true);
    }

    if (living.getEntity().getEntityWorld().isRemote()) {
      this.clientHandler.handleTick(living);
    }
  }

  @Override
  public void reset(ILiving<?, ?> living) {
    this.setTriggerPressed(living, false, false);
    if (this.performingRightMouseAction) {
      this.setPerformingRightMouseAction(living, false, false);
    }
  }

  @Override
  public void setTriggerPressed(ILiving<?, ?> living, boolean triggerPressed, boolean sendUpdate) {
    if (triggerPressed && (!this.canShoot(living)
        || MinecraftForge.EVENT_BUS
            .post(new GunEvent.TriggerPressed(this, this.gunStack, living)))) {
      return;
    }

    this.setTriggerPressed(triggerPressed);

    if (this.isTriggerPressed()) {
      executorSerivce.execute(() -> {
        while (this.isTriggerPressed() && (Util.milliTime() - this.lastTickMs < 500L)) {
          this.tryShoot(living);
        }
      });
    } else {
      // Resets the counter
      this.shotCount = 0;
    }

    if (sendUpdate) {
      PacketTarget target =
          living.getEntity().getEntityWorld().isRemote()
              ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY
          .getSimpleChannel()
          .send(target,
              new TriggerPressedMessage(living.getEntity().getEntityId(),
                  triggerPressed));
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
  public void reload(ILiving<?, ?> living) {
    living.performAction(new ReloadAction(living), true);
  }

  @Override
  public void removeMagazine(ILiving<?, ?> living) {
    living.performAction(new RemoveMagazineAction(living), true);
  }

  @Override
  public void validatePendingHit(IPlayer<ServerPlayerEntity> player,
      ILiving<?, ?> hitLiving, PendingHit pendingHit) {
    final byte tickOffset = pendingHit.getTickOffset();
    if (tickOffset > HIT_VALIDATION_DELAY_TICKS) {
      logger.warn("Bad living hit packet received, tick offset is too big!");
      return;
    }

    int latencyTicks = player.getEntity().ping / 1000 / 20 + tickOffset + BASE_LATENCY;
    int tick = player.getEntity().getServer().getTickCounter();

    if (tick - latencyTicks > this.triggerPressedTicks && !this.isTriggerPressed()) {
      return;
    }

    EntitySnapshot playerSnapshot = player.getSnapshot(tick - tickOffset)
        .map(s -> s.combineUntrustedSnapshot(pendingHit.getPlayerSnapshot()))
        .orElse(null);

    EntitySnapshot hitSnapshot = hitLiving.getSnapshot(tick - latencyTicks)
        .map(s -> s.combineUntrustedSnapshot(pendingHit.getHitSnapshot()))
        .orElse(null);

    if (playerSnapshot != null && hitSnapshot != null && !hitLiving.getEntity().getShouldBeDead()) {
      random.setSeed(pendingHit.getRandomSeed());
      // @formatter:off
      hitSnapshot
          .rayTrace(player.getEntity().getEntityWorld(),
              playerSnapshot,
              this.gunProvider.getRange(),
              this.getAccuracy(player),
              random)
          .ifPresent(hitPos -> this.hitEntity(player, hitLiving.getEntity(), hitPos, false));
      // @formatter:on
    }
  }

  private boolean canShoot(ILiving<?, ?> living) {
    return !living.getProgressMonitor().isPresent() && !living.getEntity().isSprinting()
        && !living.getEntity().isSpectator()
        && this.gunProvider.getTriggerPredicate().test(this);
  }

  protected void processShot(ILiving<?, ?> living) {
    final Entity entity = living.getEntity();

    // Magazine size will be synced to clients so only decrement this on the server.
    if (!entity.getEntityWorld().isRemote()
        && !(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())) {
      final int unbreakingLevel =
          EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this.gunStack);
      if (!UnbreakingEnchantment.negateDamage(this.gunStack, unbreakingLevel, random)) {
        this.getMagazine().ifPresent(IMagazine::decrementSize);
      }
    }

    float partialTicks = 1.0F;
    if (entity.getEntityWorld().isRemote()) {
      partialTicks = this.clientHandler.getPartialTicks();
    }

    // Used to avoid playing the same hit sound more than once.
    RayTraceResult lastRayTraceResult = null;
    for (int i = 0; i < this.gunProvider.getBulletAmountToFire(); i++) {
      final long randomSeed = entity.getEntityWorld().getGameTime() + i;
      random.setSeed(randomSeed);
      // @formatter:off
      RayTraceResult rayTraceResult = RayTraceUtil
          .rayTrace(entity,
              this.gunProvider.getRange(),
              partialTicks,
              this.getAccuracy(living),
              random)
          .orElse(null);
      // @formatter:on
      if (rayTraceResult != null) {
        switch (rayTraceResult.getType()) {
          case BLOCK:
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
            boolean playSound = true;
            if (lastRayTraceResult instanceof BlockRayTraceResult) {
              playSound = entity.getEntityWorld()
                  .getBlockState(((BlockRayTraceResult) lastRayTraceResult).getPos()) != entity
                      .getEntityWorld().getBlockState(blockRayTraceResult.getPos());
            }
            this.hitBlock(living, (BlockRayTraceResult) rayTraceResult,
                playSound && entity.getEntityWorld().isRemote());
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
            } else if (entity.getEntityWorld().isRemote()) {
              this.clientHandler.handleHitEntityPre(living,
                  entityRayTraceResult.getEntity(),
                  entityRayTraceResult.getHitVec(), randomSeed);
            }

            this.hitEntity(living, entityRayTraceResult.getEntity(),
                entityRayTraceResult.getHitVec(),
                !(lastRayTraceResult instanceof EntityRayTraceResult)
                    || !((EntityRayTraceResult) lastRayTraceResult).getEntity().getType()
                        .getRegistryName()
                        .equals(entityRayTraceResult.getEntity().getType().getRegistryName())
                        && entity.getEntityWorld().isRemote());
            break;
          default:
            break;
        }
        lastRayTraceResult = rayTraceResult;
      }
    }
  }

  private void tryShoot(ILiving<?, ?> living) {
    if (!this.canShoot(living)) {
      this.setTriggerPressed(false);
      return;
    }

    LogicalSide side =
        living.getEntity().getEntityWorld().isRemote() ? LogicalSide.CLIENT : LogicalSide.SERVER;
    Executor executor = LogicalSidedProvider.WORKQUEUE.get(side);

    if (this.getMagazineSize() <= 0) {
      if (side.isServer()) {
        executor.execute(() -> {
          living.getEntity().playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
          this.reload(living);
        });
      }
      this.setTriggerPressed(false);
      return;
    }

    long time = Util.milliTime();
    long timeDelta = time - this.lastShotMs;
    if (timeDelta < this.gunProvider.getFireDelayMs()) {
      return;
    }

    this.lastShotMs = time;

    boolean isMaxShotsReached =
        this.fireMode.getMaxShots().map(max -> this.shotCount >= max).orElse(false);
    if (isMaxShotsReached) {
      return;
    }

    this.shotCount++;

    executor.execute(() -> this.processShot(living));

    if (side == LogicalSide.CLIENT) {
      this.clientHandler.handleShoot(living);
    }
  }

  private void hitEntity(ILiving<?, ?> living, Entity hitEntity, Vector3d hitPos,
      boolean playSound) {
    final LivingEntity entity = living.getEntity();
    final World world = hitEntity.getEntityWorld();
    float damage = this.gunProvider.getDamage();

    float armorPenetration = Math.min((1.0F
        + (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ARMOR_PENETRATION.get(),
            this.gunStack) / 255.0F))
        * this.getMagazine().map(IMagazine::getArmorPenetration).orElse(0.0F), 1.0F);
    if (armorPenetration > 0 && hitEntity instanceof LivingEntity) {
      LivingEntity livingEntityHit = (LivingEntity) hitEntity;
      float reducedDamage = damage - CombatRules
          .getDamageAfterAbsorb(damage, livingEntityHit.getTotalArmorValue(),
              (float) livingEntityHit
                  .getAttribute(Attributes.ARMOR_TOUGHNESS)
                  .getValue());
      // Apply armor penetration by adding to the damage lost by armor absorption
      damage += reducedDamage * armorPenetration;
    }

    double chinHeight = (hitEntity.getPosY() + hitEntity.getEyeHeight() - 0.2F);
    boolean headshot = (hitEntity instanceof PlayerEntity || hitEntity instanceof ZombieEntity
        || hitEntity instanceof SkeletonEntity || hitEntity instanceof CreeperEntity
        || hitEntity instanceof EndermanEntity || hitEntity instanceof WitchEntity
        || hitEntity instanceof VillagerEntity || hitEntity instanceof VindicatorEntity
        || hitEntity instanceof WanderingTraderEntity) && hitPos.y >= chinHeight;
    if (headshot) {
      damage *= HEADSHOT_MULTIPLIER;
    }

    // Simulated client-side effects
    if (world.isRemote()) {
      this.clientHandler.handleHitEntityPost(living, hitEntity, hitPos, playSound,
          headshot);
    } else {
      // Resets the temporary invincibility before causing the damage, preventing
      // previous damages from blocking the gun damage.
      // Also, allows multiple bullets to hit the same target at the same time.
      hitEntity.hurtResistantTime = 0;

      ModDamageSource.causeDamageWithoutKnockback(hitEntity,
          ModDamageSource.causeGunDamage(entity, this.gunStack, headshot), damage);

      checkCreateExplosion(this.gunStack, entity, hitPos);

      if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, this.gunStack) > 0) {
        hitEntity.setFire(100);
      }

      if (hitEntity instanceof LivingEntity) {
        final LivingEntity hitLivingEntity = (LivingEntity) hitEntity;

        // Alert client of hit (real hit as opposed to client prediction)
        if (entity instanceof ServerPlayerEntity) {
          boolean dead = hitLivingEntity.getShouldBeDead();
          NetworkChannel.PLAY.getSimpleChannel().send(
              PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity),
              new HitMessage(hitPos, dead));
        }

        if (hitLivingEntity instanceof PlayerEntity) {
          IPlayer<ServerPlayerEntity> hitPlayer =
              IPlayer.getExpected((ServerPlayerEntity) hitLivingEntity);
          hitPlayer.infect(
              (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INFECTION.get(), this.gunStack)
                  / (float) ModEnchantments.INFECTION.get().getMaxLevel())
                  * hitPlayer.getItemHandler()
                      .getStackInSlot(InventorySlotType.CLOTHING.getIndex())
                      .getCapability(ModCapabilities.CLOTHING)
                      .filter(IClothing::hasEnhancedProtection)
                      .map(clothing -> 0.5F).orElse(1.0F));
        }
      }
    }
  }

  private void hitBlock(ILiving<?, ?> living, BlockRayTraceResult rayTrace,
      boolean playSound) {
    final Entity entity = living.getEntity();
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    Block block = blockState.getBlock();
    World world = entity.getEntityWorld();

    // Client-side effects
    if (world.isRemote()) {
      this.clientHandler.handleHitBlock(living, rayTrace, playSound);
    } else {
      if (blockState.getBlock() instanceof BellBlock) {
        ((BellBlock) blockState.getBlock()).attemptRing(world, blockState, rayTrace,
            entity instanceof PlayerEntity ? (PlayerEntity) entity : null, playSound);
      }

      if (block instanceof TNTBlock) {
        block.catchFire(blockState, entity.getEntityWorld(), blockPos, null,
            entity instanceof LivingEntity ? (LivingEntity) entity : null);
        entity.getEntityWorld().removeBlock(blockPos, false);
      }

      checkCreateExplosion(this.gunStack, entity, rayTrace.getHitVec());

      if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, this.gunStack) > 0) {
        if (CampfireBlock.canBeLit(blockState)) {
          world.setBlockState(blockPos,
              blockState.with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
        } else {
          BlockPos faceBlockPos = blockPos.offset(rayTrace.getFace());
          if (AbstractFireBlock.canLightBlock(world, faceBlockPos, entity.getHorizontalFacing())) {
            BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(world, faceBlockPos);
            world.setBlockState(faceBlockPos, blockstate1, 11);
          }
        }
      }
    }
  }

  @Override
  public float getAccuracy(ILiving<?, ?> living) {
    float accuracy =
        this.gunProvider.getAccuracyPct() * this.getAttachmentMultiplier(MultiplierType.ACCURACY);
    return Math.min(living.getModifiedAccuracy(accuracy, random), 1.0F);
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.dataManager.get(MAGAZINE_STACK);
  }

  @Override
  public Set<AttachmentItem> getAttachments() {
    return ImmutableSet.copyOf(this.attachments);
  }

  @Override
  public void setAttachments(Set<AttachmentItem> attachments) {
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
  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return itemStack != null
        && (this.gunProvider.getAcceptedAttachments().contains(itemStack.getItem())
            || this.gunProvider.getAcceptedPaints().contains(itemStack.getItem()));
  }

  @Override
  public void toggleFireMode(ILiving<?, ?> living, boolean sendUpdate) {
    if (this.fireModeInfiniteIterator.hasNext()) {
      this.setFireMode(living, this.fireModeInfiniteIterator.next(), sendUpdate);
    }
  }

  @Override
  public void setFireMode(ILiving<?, ?> living, FireMode fireMode, boolean sendUpdate) {
    this.fireMode = fireMode;

    living.getEntity().playSound(ModSoundEvents.TOGGLE_FIRE_MODE.get(), 1.0F, 1.0F);
    if (living.getEntity() instanceof PlayerEntity) {
      ((PlayerEntity) living.getEntity())
          .sendStatusMessage(Text.translate("message.switch_fire_mode",
              Text.translate(this.fireMode.getTranslationKey())), true);
    }

    if (sendUpdate) {
      PacketTarget target =
          living.getEntity().getEntityWorld().isRemote()
              ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY
          .getSimpleChannel()
          .send(target, new SetFireModeMessage(living.getEntity().getEntityId(), this.fireMode));
    }
  }


  @Override
  public boolean hasCrosshair() {
    return this.gunProvider.hasCrosshair();
  }

  @Override
  public boolean isPerformingRightMouseAction() {
    return this.performingRightMouseAction;
  }

  @Override
  public void setPerformingRightMouseAction(ILiving<?, ?> living,
      boolean performingRightMouseAction, boolean sendUpdate) {
    if (performingRightMouseAction && living.getEntity().isSprinting()) {
      return;
    }

    this.performingRightMouseAction = !this.performingRightMouseAction;
    if (living.getEntity().getEntityWorld().isRemote()) {
      this.clientHandler.handleToggleRightMouseAction(living);
    }

    if (sendUpdate) {
      PacketTarget target =
          living.getEntity().getEntityWorld().isRemote()
              ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new RightMouseAction(living.getEntity().getEntityId(), this.performingRightMouseAction));
    }
  }

  @Override
  public RightMouseActionTriggerType getRightMouseActionTriggerType() {
    return this.gunProvider.getRightMouseActionTriggerType();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.getMagazineStack().serializeNBT());
    ListNBT attachmentsTag = this.attachments
        .stream()
        .map(attachment -> StringNBT.valueOf(attachment.getRegistryName().toString()))
        .collect(ListNBT::new, ListNBT::add, List::addAll);
    nbt.put("attachments", attachmentsTag);
    nbt.put("paintStack", this.getPaintStack().serializeNBT());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.setMagazineStack(ItemStack.read(nbt.getCompound("magazineStack")));
    this.setAttachments(nbt.getList("attachments", 8)
        .stream()
        .map(tag -> (AttachmentItem) ForgeRegistries.ITEMS
            .getValue(new ResourceLocation(tag.getString())))
        .collect(Collectors.toSet()));
    this.setPaintStack(ItemStack.read(nbt.getCompound("paintStack")));
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    NetworkDataManager
        .writeEntries(writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), out);
    if (writeAll || this.attachmentsDirty) {
      out.writeVarInt(this.attachments.size());
      for (Item item : this.attachments) {
        out.writeRegistryId(item);
      }
    } else {
      out.writeVarInt(-1);
    }
    this.attachmentsDirty = false;

    this.getMagazine().ifPresent(magazine -> magazine.encode(out, writeAll));

    if (writeAll || this.ammoReserveDirty) {
      for (int i = 0; i < this.ammoReserve.getSlots(); i++) {
        out.writeVarInt(i);
        out.writeItemStack(this.ammoReserve.getStackInSlot(i));
      }
    }
    out.writeVarInt(-1);

    this.ammoReserveDirty = false;
  }

  @Override
  public void decode(PacketBuffer in) {
    this.dataManager.setEntryValues(NetworkDataManager.readEntries(in));

    int size = in.readVarInt();
    if (size > -1) {
      this.attachments.clear();
      for (int i = 0; i < size; i++) {
        this.attachments.add((AttachmentItem) in.readRegistryIdSafe(Item.class));
      }
    }

    this.getMagazine().ifPresent(magazine -> magazine.decode(in));

    int slot;
    while ((slot = in.readVarInt()) != -1) {
      this.ammoReserve.setStackInSlot(slot, in.readItemStack());
    }
    this.updateAmmoReserveSize();
  }

  @Override
  public boolean requiresSync() {
    return this.attachmentsDirty || this.dataManager.isDirty()
        || this.getMagazine().map(IMagazine::requiresSync).orElse(false);
  }

  @Override
  public void setMagazineStack(ItemStack magazineStack) {
    this.dataManager.set(MAGAZINE_STACK, magazineStack);
  }

  @Override
  public Set<? extends Item> getAcceptedMagazines() {
    return this.gunProvider.getAcceptedMagazines();
  }

  @Override
  public Optional<SoundEvent> getReloadSound() {
    return this.gunProvider.getReloadSound();
  }

  @Override
  public int getReloadDurationTicks() {
    return this.gunProvider.getReloadDurationTicks();
  }

  @Override
  public boolean hasIronSight() {
    for (AttachmentItem attachmentItem : this.attachments) {
      if (attachmentItem.getInventorySlot() == CraftingInventorySlotType.OVERBARREL_ATTACHMENT) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Optional<GunAnimation> getAnimation(AnimationType animationType) {
    return Optional.ofNullable(this.gunProvider.getAnimations().get(animationType))
        .map(Supplier::get);
  }

  @Override
  public int getShotCount() {
    return this.shotCount;
  }

  @Override
  public FireMode getFireMode() {
    return this.fireMode;
  }

  @Override
  public IGunClient getClient() {
    return this.clientHandler;
  }

  @Override
  public CombatSlotType getSlotType() {
    return this.gunProvider.getCombatSlotType();
  }

  @Override
  public ItemStack getDefaultMagazineStack() {
    return this.gunProvider.getDefaultMagazine().get().getDefaultInstance();
  }

  @Override
  public IItemHandler getAmmoReserve() {
    return this.ammoReserve;
  }

  @Override
  public int getAmmoReserveSize() {
    return this.cachedAmmoReserveSize;
  }

  private static void checkCreateExplosion(ItemStack magazineStack, Entity entity,
      Vector3d position) {
    float explosionSize = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, magazineStack)
        / (float) Enchantments.POWER.getMaxLevel();
    if (explosionSize > 0) {
      entity.getEntityWorld().createExplosion(entity, position.getX(), position.getY(),
          position.getZ(), explosionSize, Explosion.Mode.NONE);
    }
  }
}
