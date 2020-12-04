/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.action.ReloadAction;
import com.craftingdead.core.action.RemoveMagazineAction;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.DefaultAnimationProvider;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimation;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimationController;
import com.craftingdead.core.capability.clothing.IClothing;
import com.craftingdead.core.capability.living.EntitySnapshot;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.gui.KillFeedEntry;
import com.craftingdead.core.enchantment.ModEnchantments;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.AttachmentItem.MultiplierType;
import com.craftingdead.core.item.FireMode;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.HitMessage;
import com.craftingdead.core.network.message.play.KillFeedMessage;
import com.craftingdead.core.network.message.play.SyncGunMessage;
import com.craftingdead.core.network.message.play.ToggleFireModeMessage;
import com.craftingdead.core.network.message.play.ToggleRightMouseAbility;
import com.craftingdead.core.network.message.play.TriggerPressedMessage;
import com.craftingdead.core.network.message.play.ValidatePendingHitMessage;
import com.craftingdead.core.util.ModDamageSource;
import com.craftingdead.core.util.ModSoundEvents;
import com.craftingdead.core.util.RayTraceUtil;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.CombatRules;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.registries.ForgeRegistries;

public class DefaultGun extends DefaultAnimationProvider<GunAnimationController> implements IGun {

  private static final Logger logger = LogManager.getLogger();

  private static final int BASE_LATENCY = 4;

  public static final float HEADSHOT_MULTIPLIER = 4;

  private static final byte HIT_VALIDATION_DELAY_TICKS = 3;

  private static final Random random = new Random();

  private final GunItem gunItem;

  /**
   * If the gun trigger is pressed.
   */
  private boolean triggerPressed;
  private boolean wasTriggerPressed;

  /**
   * The amount of ticks the trigger has been pressed for. Used to determine if a hit validation
   * packet is valid.
   */
  private int triggerPressedTicks;

  /**
   * Time of the last shot in milliseconds.
   */
  private long lastShotMs = Integer.MIN_VALUE;

  /**
   * The current {@link FireMode} being used.
   */
  private FireMode fireMode;

  /**
   * The current loaded magazine {@link ItemStack}. This is checked for {@link IMagazine}
   * capabilities.
   */
  private ItemStack magazineStack = ItemStack.EMPTY;

  /**
   * The amount of shots since the last time the trigger was pressed. Used to determine if the gun
   * can continue firing using the current fire mode.
   */
  private int shotCount;

  private Set<AttachmentItem> attachments;

  private ItemStack paintStack = ItemStack.EMPTY;

  private final Iterator<FireMode> fireModeInfiniteIterator;

  private boolean performingRightMouseAction;

  private long rightMouseActionSoundStartTimeMs;

  private final Multimap<Integer, PendingHit> livingHitValidationBuffer =
      Multimaps.newMultimap(new Int2ObjectLinkedOpenHashMap<>(), ObjectArrayList::new);

  private byte hitValidationTicks = 0;

  public DefaultGun() {
    throw new UnsupportedOperationException("Specify gun item");
  }

  public DefaultGun(GunItem gunItem) {
    super(() -> GunAnimationController::new);
    this.gunItem = gunItem;
    this.fireModeInfiniteIterator = Iterables.cycle(this.gunItem.getFireModes()).iterator();
    this.fireMode = this.fireModeInfiniteIterator.next();
    this.attachments = gunItem.getDefaultAttachments();
    this.magazineStack = new ItemStack(gunItem.getDefaultMagazine().get());
  }

  @Override
  public void tick(ILiving<?, ?> living, ItemStack itemStack) {
    if (!living.getEntity().getEntityWorld().isRemote() && !this.triggerPressed
        && this.wasTriggerPressed) {
      this.triggerPressedTicks = living.getEntity().getServer().getTickCounter();
    }
    this.wasTriggerPressed = this.triggerPressed;

    this.tryShoot(living, itemStack);

    if (living.getEntity().getEntityWorld().isRemote() && this.livingHitValidationBuffer.size() > 0
        && this.hitValidationTicks++ >= HIT_VALIDATION_DELAY_TICKS) {
      this.hitValidationTicks = 0;
      NetworkChannel.PLAY.getSimpleChannel().sendToServer(
          new ValidatePendingHitMessage(new HashMap<>(this.livingHitValidationBuffer.asMap())));
      this.livingHitValidationBuffer.clear();
    }

    if (this.isPerformingRightMouseAction() && living.getEntity().isSprinting()) {
      this.toggleRightMouseAction(living, true);
    }
    SoundEvent rightMouseActionSound = this.gunItem.getRightMouseActionSound().get();
    long rightMouseActionSoundDelta = Util.milliTime() - this.rightMouseActionSoundStartTimeMs + 50;
    if (this.isPerformingRightMouseAction()
        && this.gunItem.getRightMouseActionSoundRepeatDelayMs() > 0L
        && rightMouseActionSoundDelta >= this.gunItem.getRightMouseActionSoundRepeatDelayMs()
        && rightMouseActionSound != null) {
      this.rightMouseActionSoundStartTimeMs = Util.milliTime();
      living.getEntity().playSound(rightMouseActionSound, 1.0F, 1.0F);
    }
  }

  @Override
  public void setTriggerPressed(ILiving<?, ?> living, ItemStack itemStack,
      boolean triggerPressed,
      boolean sendUpdate) {

    if (!this.canShoot(living)) {
      return;
    }

    this.triggerPressed = triggerPressed;

    if (this.triggerPressed) {
      this.tryShoot(living, itemStack);
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

    // Sync magazine size before/after shooting as due to network latency this can get out of sync
    if (!living.getEntity().getEntityWorld().isRemote()) {
      NetworkChannel.PLAY
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(living::getEntity),
              new SyncGunMessage(
                  living.getEntity().getEntityId(), this.paintStack, this.magazineStack,
                  this.getMagazineSize()));
    }
  }

  @Override
  public boolean isTriggerPressed() {
    return this.triggerPressed;
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
  public void validatePendingHit(IPlayer<ServerPlayerEntity> player, ItemStack itemStack,
      ILiving<?, ?> hitLiving, PendingHit pendingHit) {
    final byte tickOffset = pendingHit.getTickOffset();
    if (tickOffset > HIT_VALIDATION_DELAY_TICKS) {
      logger.warn("Bad living hit packet received, tick offset is too big!");
      return;
    }

    int latencyTicks = player.getEntity().ping / 1000 / 20 + tickOffset + BASE_LATENCY;
    int tick = player.getEntity().getServer().getTickCounter();

    if (tick - latencyTicks > this.triggerPressedTicks && !this.triggerPressed) {
      return;
    }

    EntitySnapshot playerSnapshot = player.getSnapshot(tick - tickOffset).orElse(null);
    playerSnapshot = playerSnapshot.combineUntrustedSnapshot(pendingHit.getPlayerSnapshot());

    EntitySnapshot hitSnapshot = hitLiving.getSnapshot(tick - latencyTicks).orElse(null);
    hitSnapshot = hitSnapshot.combineUntrustedSnapshot(pendingHit.getHitSnapshot());

    if (playerSnapshot != null && hitSnapshot != null) {
      random.setSeed(pendingHit.getRandomSeed());
      hitSnapshot.rayTrace(player.getEntity().getEntityWorld(), playerSnapshot, 100.0D,
          this.getAccuracy(player, itemStack), random).ifPresent(
              hitPos -> this.hitEntity(player, itemStack, hitLiving.getEntity(), hitPos, false));
    }
  }

  private boolean canShoot(ILiving<?, ?> living) {
    return !(living.getActionProgress().isPresent() || living.getEntity().isSprinting()
        || !this.gunItem.getTriggerPredicate().test(this));
  }

  private void tryShoot(ILiving<?, ?> living, ItemStack itemStack) {
    final Entity entity = living.getEntity();

    if (!this.triggerPressed) {
      return;
    }

    if (!this.canShoot(living)) {

      this.triggerPressed = false;
      return;
    }

    if (this.getMagazineSize() <= 0) {
      living.getEntity().playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      this.reload(living);
      this.triggerPressed = false;
      return;
    }

    long time = Util.milliTime();
    long timeDelta = time - this.lastShotMs;
    if (timeDelta < this.gunItem.getFireRateMs()) {
      return;
    }

    this.lastShotMs = time;

    boolean isMaxShotsReached =
        this.fireMode.getMaxShots().map(max -> this.shotCount >= max).orElse(false);
    if (isMaxShotsReached) {
      return;
    }
    this.shotCount++;

    if (!(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())) {
      final int unbreakingLevel =
          EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, itemStack);
      if (!UnbreakingEnchantment.negateDamage(itemStack, unbreakingLevel, random)) {
        this.getMagazine().ifPresent(IMagazine::decrementSize);
      }
    }

    float partialTicks = 1.0F;
    if (entity.getEntityWorld().isRemote()) {
      ClientDist clientDist = (ClientDist) CraftingDead.getInstance().getModDist();
      Minecraft minecraft = clientDist.getMinecraft();

      partialTicks = minecraft.getRenderPartialTicks();
      if (entity instanceof ClientPlayerEntity) {
        clientDist.getCameraManager().joltCamera(1.15F - this.getAccuracy(living, itemStack), true);
      }

      this.getAnimation(AnimationType.SHOOT).ifPresent(animation -> {
        this.getAnimationController().ifPresent(GunAnimationController::removeCurrentAnimation);
        this.getAnimationController().ifPresent(c -> c.addAnimation(animation, null));
      });

      SoundEvent shootSound = this.gunItem.getShootSound().get();
      if (this.getAttachments().stream().anyMatch(AttachmentItem::isSoundSuppressor)) {
        // Tries to get the silenced shoot sound, if it does not exist the default shoot sound is
        // used instead.
        shootSound = this.gunItem.getSilencedShootSound().orElse(shootSound);
      }

      if (!entity.isSilent()) {
        entity.getEntityWorld().playSound(
            clientDist.getPlayer().map(IPlayer::getEntity).orElse(null),
            entity.getPosX(), entity.getPosY(), entity.getPosZ(), shootSound,
            entity.getSoundCategory(), 0.25F, 1.0F);
      }
    }

    // Used to avoid playing the same hit sound more than once.
    RayTraceResult lastRayTraceResult = null;
    for (int i = 0; i < this.gunItem.getBulletAmountToFire(); i++) {
      final long randomSeed = entity.getEntityWorld().getGameTime() + i;
      random.setSeed(randomSeed);
      RayTraceResult rayTraceResult = RayTraceUtil
          .rayTrace(entity, 100.0D, partialTicks, this.getAccuracy(living, itemStack), random)
          .orElse(null);
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
            this.hitBlock(living, itemStack, (BlockRayTraceResult) rayTraceResult,
                playSound && entity.getEntityWorld().isRemote());
            break;
          case ENTITY:
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;
            if (!entityRayTraceResult.getEntity().isAlive()) {
              break;
            }

            if (entityRayTraceResult.getEntity() instanceof LivingEntity) {
              if (entity instanceof ServerPlayerEntity) {
                break;
              } else if (entity instanceof ClientPlayerEntity) {
                this.livingHitValidationBuffer.put(entityRayTraceResult.getEntity().getEntityId(),
                    new PendingHit((byte) (HIT_VALIDATION_DELAY_TICKS - this.hitValidationTicks),
                        new EntitySnapshot(entity),
                        new EntitySnapshot(entityRayTraceResult.getEntity()), randomSeed));
              }
            }

            this.hitEntity(living, itemStack, entityRayTraceResult.getEntity(),
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

  private void hitEntity(ILiving<?, ?> living, ItemStack itemStack, Entity hitEntity, Vec3d hitPos,
      boolean playSound) {
    final Entity entity = living.getEntity();
    final World world = hitEntity.getEntityWorld();
    float damage = this.gunItem.getDamage();

    float armorPenetration = Math.min((1.0F
        + (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ARMOR_PENETRATION.get(), itemStack)
            / 255.0F))
        * this.getMagazine().map(IMagazine::getArmorPenetration).orElse(0.0F), 1.0F);
    if (armorPenetration > 0 && hitEntity instanceof LivingEntity) {
      LivingEntity livingEntityHit = (LivingEntity) hitEntity;
      float reducedDamage = damage - CombatRules
          .getDamageAfterAbsorb(damage, livingEntityHit.getTotalArmorValue(),
              (float) livingEntityHit
                  .getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS)
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

      // Simulated client-side effects
      if (world.isRemote()) {
        final int particleCount = 12;
        for (int i = 0; i < particleCount; ++i) {
          world.addParticle(
              new BlockParticleData(ParticleTypes.BLOCK, Blocks.BONE_BLOCK.getDefaultState()),
              hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0.0D, 0.0D, 0.0D);
        }
      }
    }

    // Simulated client-side effects
    if (world.isRemote()) {
      world.playSound(entity instanceof PlayerEntity ? (PlayerEntity) entity : null,
          hitEntity.getPosition(), ModSoundEvents.BULLET_IMPACT_FLESH.get(), SoundCategory.PLAYERS,
          1.0F, 1.0F);

      if (hitEntity instanceof ClientPlayerEntity) {
        ((ClientDist) CraftingDead.getInstance().getModDist()).getCameraManager().joltCamera(1.5F,
            false);
      }

      final int particleCount = 12;
      for (int i = 0; i < particleCount; ++i) {
        world.addParticle(
            new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
            hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0.0D, 0.0D, 0.0D);
      }
    } else {
      // Resets the temporary invincibility before causing the damage, preventing
      // previous damages from blocking the gun damage.
      // Also, allows multiple bullets to hit the same target at the same time.
      hitEntity.hurtResistantTime = 0;

      ModDamageSource.causeDamageWithoutKnockback(hitEntity,
          ModDamageSource.causeGunDamage(entity, headshot), damage);

      checkCreateExplosion(itemStack, entity, hitPos);

      if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack) > 0) {
        hitEntity.setFire(100);
      }

      if (hitEntity instanceof LivingEntity) {
        final LivingEntity hitLivingEntity = (LivingEntity) hitEntity;

        // Alert client of hit (real hit data as opposed to client simulation)
        if (entity instanceof ServerPlayerEntity) {
          boolean dead = hitLivingEntity.getHealth() <= 0;
          NetworkChannel.PLAY.getSimpleChannel().send(
              PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity),
              new HitMessage(hitPos, dead));

          if (dead && hitLivingEntity instanceof PlayerEntity) {
            NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
                new KillFeedMessage(entity.getEntityId(), hitLivingEntity.getEntityId(), itemStack,
                    headshot ? KillFeedEntry.Type.HEADSHOT : KillFeedEntry.Type.NONE));
          }
        }

        if (hitLivingEntity instanceof PlayerEntity) {
          IPlayer<ServerPlayerEntity> hitPlayer =
              IPlayer.getExpected((ServerPlayerEntity) hitLivingEntity);
          hitPlayer.infect(
              (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INFECTION.get(), itemStack)
                  / 255.0F) * hitPlayer.getItemHandler()
                      .getStackInSlot(InventorySlotType.CLOTHING.getIndex())
                      .getCapability(ModCapabilities.CLOTHING)
                      .filter(IClothing::hasEnhancedProtection)
                      .map(clothing -> 0.5F).orElse(1.0F));
        }
      }
    }
  }

  private void hitBlock(ILiving<?, ?> living, ItemStack itemStack, BlockRayTraceResult rayTrace,
      boolean playSound) {
    final Entity entity = living.getEntity();
    Vec3d hitVec3d = rayTrace.getHitVec();
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    Block block = blockState.getBlock();
    World world = entity.getEntityWorld();

    // Client-side effects
    if (world.isRemote()) {
      // Gets the hit sound to be played
      SoundEvent hitSound = ModSoundEvents.BULLET_IMPACT_DIRT.get();
      Material blockMaterial = blockState.getMaterial();
      if (blockMaterial == Material.WOOD) {
        hitSound = ModSoundEvents.BULLET_IMPACT_WOOD.get();
      } else if (blockMaterial == Material.ROCK) {
        hitSound = ModSoundEvents.BULLET_IMPACT_STONE.get();
      } else if (blockMaterial == Material.IRON) {
        hitSound = Math.random() > 0.5D ? ModSoundEvents.BULLET_IMPACT_METAL.get()
            : ModSoundEvents.BULLET_IMPACT_METAL2.get();
      } else if (blockMaterial == Material.GLASS) {
        hitSound = ModSoundEvents.BULLET_IMPACT_GLASS.get();
      }

      world.playSound(entity instanceof PlayerEntity ? (PlayerEntity) entity : null, blockPos,
          hitSound, SoundCategory.BLOCKS, 1.0F, 1.0F);

      final int particleCount = 12;
      for (int i = 0; i < particleCount; ++i) {
        world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockState), hitVec3d.getX(),
            hitVec3d.getY(), hitVec3d.getZ(), 0.0D, 0.0D, 0.0D);
      }
    } else {
      if (block instanceof TNTBlock) {
        block.catchFire(blockState, entity.getEntityWorld(), blockPos, null,
            entity instanceof LivingEntity ? (LivingEntity) entity : null);
        entity.getEntityWorld().removeBlock(blockPos, false);
      }

      checkCreateExplosion(itemStack, entity, rayTrace.getHitVec());

      if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack) > 0) {
        BlockPos blockAbove = rayTrace.getPos().add(0, 1, 0);
        if (!world.isAirBlock(rayTrace.getPos()) && world.isAirBlock(blockAbove)) {
          world.setBlockState(blockAbove, Blocks.FIRE.getDefaultState());
        }
      }
    }
  }

  @Override
  public float getAccuracy(ILiving<?, ?> living, ItemStack itemStack) {
    float accuracy =
        this.gunItem.getAccuracy() * this.getAttachmentMultiplier(MultiplierType.ACCURACY);
    return Math.min(living.getModifiedAccuracy(accuracy, random), 1.0F);
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
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
    return this.paintStack;
  }

  @Override
  public void setPaintStack(ItemStack paintStack) {
    this.paintStack = paintStack;
  }

  @Override
  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return itemStack != null
        && (this.gunItem.getAcceptedAttachments().contains((itemStack.getItem()))
            || this.gunItem.getAcceptedPaints().contains((itemStack.getItem())));
  }

  @Override
  public void toggleFireMode(ILiving<?, ?> living, boolean sendUpdate) {
    if (this.fireModeInfiniteIterator.hasNext()) {
      this.fireMode = this.fireModeInfiniteIterator.next();
    }

    living.getEntity().playSound(ModSoundEvents.TOGGLE_FIRE_MODE.get(), 1.0F, 1.0F);
    if (living.getEntity() instanceof PlayerEntity) {
      ((PlayerEntity) living.getEntity())
          .sendStatusMessage(new TranslationTextComponent(this.fireMode.getTranslationKey()), true);
    }

    if (sendUpdate) {
      PacketTarget target =
          living.getEntity().getEntityWorld().isRemote()
              ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY
          .getSimpleChannel()
          .send(target, new ToggleFireModeMessage(living.getEntity().getEntityId()));
    }
  }

  @Override
  public boolean hasCrosshair() {
    return this.gunItem.hasCrosshair();
  }

  @Override
  public boolean isPerformingRightMouseAction() {
    return this.performingRightMouseAction;
  }

  @Override
  public void toggleRightMouseAction(ILiving<?, ?> living, boolean sendUpdate) {
    if (!this.performingRightMouseAction && living.getEntity().isSprinting()) {
      return;
    }
    this.performingRightMouseAction = !this.performingRightMouseAction;
    SoundEvent rightMouseActionSound = this.gunItem.getRightMouseActionSound().get();
    if (rightMouseActionSound != null) {
      if (this.performingRightMouseAction) {
        this.rightMouseActionSoundStartTimeMs = Util.milliTime();
        living.getEntity().playSound(rightMouseActionSound, 1.0F, 1.0F);
      } else if (living.getEntity().getEntityWorld().isRemote()) {
        Minecraft.getInstance().getSoundHandler()
            .stop(rightMouseActionSound.getRegistryName(), SoundCategory.PLAYERS);
      }
    }
    if (sendUpdate) {
      PacketTarget target =
          living.getEntity().getEntityWorld().isRemote()
              ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(living::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new ToggleRightMouseAbility(living.getEntity().getEntityId()));
    }
  }

  @Override
  public RightMouseActionTriggerType getRightMouseActionTriggerType() {
    return this.gunItem.getRightMouseActionTriggerType();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.magazineStack.serializeNBT());
    ListNBT attachmentsTag = this.attachments
        .stream()
        .map(attachment -> StringNBT.valueOf(attachment.getRegistryName().toString()))
        .collect(ListNBT::new, ListNBT::add, List::addAll);
    nbt.put("attachments", attachmentsTag);
    nbt.put("paintStack", this.paintStack.serializeNBT());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.setMagazineStack(ItemStack.read(nbt.getCompound("magazineStack")));
    this
        .setAttachments(nbt
            .getList("attachments", 8)
            .stream()
            .map(tag -> (AttachmentItem) ForgeRegistries.ITEMS
                .getValue(new ResourceLocation(tag.getString())))
            .collect(Collectors.toSet()));
    this.setPaintStack(ItemStack.read(nbt.getCompound("paintStack")));
  }

  @Override
  public void setMagazineStack(ItemStack stack) {
    this.magazineStack = stack;
  }

  @Override
  public Set<? extends Item> getAcceptedMagazines() {
    return this.gunItem.getAcceptedMagazines();
  }

  @Override
  public Optional<SoundEvent> getReloadSound() {
    return this.gunItem.getReloadSound();
  }

  @Override
  public int getReloadDurationTicks() {
    return this.gunItem.getReloadDurationTicks();
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
    return Optional.ofNullable(this.gunItem.getAnimations().get(animationType)).map(Supplier::get);
  }

  @Override
  public int getShotCount() {
    return this.shotCount;
  }

  private static void checkCreateExplosion(ItemStack magazineStack, Entity entity, Vec3d position) {
    float explosionSize = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, magazineStack)
        / Enchantments.POWER.getMaxLevel();
    if (explosionSize > 0) {
      entity
          .getEntityWorld()
          .createExplosion(entity, position.getX(), position.getY(), position.getZ(), explosionSize,
              Explosion.Mode.NONE);
    }
  }
}
