package com.craftingdead.core.capability.gun;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animation.DefaultAnimationController;
import com.craftingdead.core.capability.animation.IAnimation;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.player.IPlayer;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.enchantment.ModEnchantments;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.AttachmentItem.MultiplierType;
import com.craftingdead.core.item.FireMode;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.main.GunActionMessage;
import com.craftingdead.core.network.message.main.SyncGunMessage;
import com.craftingdead.core.network.message.main.ToggleAimingMessage;
import com.craftingdead.core.network.message.main.ToggleFireModeMessage;
import com.craftingdead.core.network.message.main.TriggerPressedMessage;
import com.craftingdead.core.util.ModDamageSource;
import com.craftingdead.core.util.ModSoundEvents;
import com.craftingdead.core.util.RayTraceUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
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
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class DefaultGun extends DefaultAnimationController implements IGun {

  public static final float HEADSHOT_MULTIPLIER = 4;

  private static final Random random = new Random();

  private final GunItem gunItem;

  private boolean triggerPressed;

  /**
   * Time of the last shot.
   */
  private long lastShotNanos = Integer.MIN_VALUE;

  private int actionDurationTicks = 0;

  private int totalActionDurationTicks;

  private FireMode fireMode;

  private ItemStack magazineStack = ItemStack.EMPTY;

  /**
   * The amount of shots since the last time the trigger was pressed.
   */
  private int shotCount;

  private Set<AttachmentItem> attachments;

  private ItemStack paintStack = ItemStack.EMPTY;

  private final Iterator<FireMode> fireModeInfiniteIterator;

  private boolean aiming;

  private boolean reloading;

  public DefaultGun() {
    this(null);
  }

  public DefaultGun(GunItem gunItem) {
    this.gunItem = gunItem;
    this.fireModeInfiniteIterator = Iterables.cycle(this.gunItem.getFireModes()).iterator();
    this.fireMode = this.fireModeInfiniteIterator.next();
    this.attachments = gunItem.getDefaultAttachments();
    this.magazineStack = new ItemStack(gunItem.getDefaultMagazine().get());
    this.magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .ifPresent(magazine -> magazine.setSize(0));
  }

  @Override
  public void tick(Entity entity, ItemStack itemStack) {
    this.tryShoot(entity, itemStack);

    if (this.isPerformingAction() && --this.actionDurationTicks == 0) {
      ItemStack oldMagazine = this.magazineStack;
      if (this.reloading) {
        this.magazineStack = entity
            .getCapability(ModCapabilities.LIVING)
            .map(living -> this.findAmmo(living, false))
            .orElse(ItemStack.EMPTY);
      } else {
        this.magazineStack = ItemStack.EMPTY;
      }
      if (!oldMagazine.isEmpty() && entity instanceof PlayerEntity) {
        ((PlayerEntity) entity).addItemStackToInventory(oldMagazine);
      }
    }
  }

  @Override
  public void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed,
      boolean sendUpdate) {
    this.triggerPressed = triggerPressed;

    if (this.triggerPressed) {
      // Resets the counter
      this.shotCount = 0;
      this.tryShoot(entity, itemStack);
    }

    if (sendUpdate) {
      PacketTarget target = entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(() -> entity);
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(target, new TriggerPressedMessage(entity.getEntityId(), triggerPressed));
    }

    // Sync magazine size before/after shooting as due to network latency this can get out of sync
    if (!entity.getEntityWorld().isRemote()) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncGunMessage(
              entity.getEntityId(), this.paintStack, this.magazineStack, this.getMagazineSize()));
    }
  }

  @Override
  public boolean isTriggerPressed() {
    return this.triggerPressed;
  }

  @Override
  public int getTotalReloadDurationTicks() {
    return this.totalActionDurationTicks;
  }

  @Override
  public int getReloadDurationTicks() {
    return this.actionDurationTicks;
  }

  @Override
  public boolean isPerformingAction() {
    return this.actionDurationTicks > 0;
  }

  @Override
  public boolean isReloading() {
    return this.isPerformingAction() && this.reloading;
  }

  @Override
  public void cancelActions(Entity entity, ItemStack itemStack) {
    if (this.isPerformingAction()) {
      if (this.reloading && this.gunItem.getReloadSound().isPresent()) {
        // Stop reload sound
        Minecraft
            .getInstance()
            .getSoundHandler()
            .stop(this.gunItem.getReloadSound().get().getRegistryName(), SoundCategory.PLAYERS);
      }
      this.actionDurationTicks = 0;
    }
    this.setTriggerPressed(entity, itemStack, false, false);
  }

  @Override
  public void reload(Entity entity, ItemStack itemStack, boolean sendUpdate) {
    boolean canReload = !entity
        .getCapability(ModCapabilities.LIVING)
        .map(living -> this.findAmmo(living, true))
        .orElse(ItemStack.EMPTY)
        .isEmpty();
    if (!this.isPerformingAction() && canReload) {
      this.reloading = true;
      // Some guns may not have a reload sound
      this.gunItem.getReloadSound().ifPresent(sound -> {
        entity.world.playMovingSound(null, entity, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
      });
      this.actionDurationTicks =
          this.totalActionDurationTicks = this.gunItem.getReloadDurationTicks();
      if (sendUpdate) {
        PacketTarget target = entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
            : PacketDistributor.TRACKING_ENTITY.with(() -> entity);
        NetworkChannel.MAIN
            .getSimpleChannel()
            .send(target, new GunActionMessage(entity.getEntityId(), true));
      }
    }
  }

  @Override
  public void removeMagazine(Entity entity, ItemStack itemStack, boolean sendUpdate) {
    if (!this.isPerformingAction() && !this.magazineStack.isEmpty()) {
      this.reloading = false;
      this.actionDurationTicks =
          this.totalActionDurationTicks = this.gunItem.getReloadDurationTicks() / 2;
      if (sendUpdate) {
        PacketTarget target = entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
            : PacketDistributor.TRACKING_ENTITY.with(() -> entity);
        NetworkChannel.MAIN
            .getSimpleChannel()
            .send(target, new GunActionMessage(entity.getEntityId(), false));
      }
    }
  }

  private void tryShoot(Entity entity, ItemStack itemStack) {
    if (!this.triggerPressed) {
      return;
    }

    if (this.isPerformingAction()) {
      return;
    }

    if (this.getMagazineSize() <= 0) {
      this.triggerPressed = false;
      entity.playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      this.reload(entity, itemStack, false);
      return;
    }

    long fireRateNanoseconds =
        TimeUnit.NANOSECONDS.convert(this.gunItem.getFireRateMs(), TimeUnit.MILLISECONDS);
    long time = System.nanoTime();
    long timeDelta = time - this.lastShotNanos;
    if (timeDelta < fireRateNanoseconds) {
      return;
    }
    this.lastShotNanos = time;

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
        this.magazineStack
            .getCapability(ModCapabilities.MAGAZINE)
            .ifPresent(IMagazine::decrementSize);
      }
    }

    if (entity.world.isRemote()) {
      if (entity instanceof ClientPlayerEntity) {
        ((ClientDist) CraftingDead.getInstance().getModDist())
            .joltCamera(1.0F - this.getAccuracy(entity, itemStack), true);
      }

      itemStack
          .getCapability(ModCapabilities.ANIMATION_CONTROLLER)
          .ifPresent(animationController -> {
            IAnimation animation =
                this.gunItem.getAnimations().get(GunItem.AnimationType.SHOOT).get();
            if (animation != null) {
              animationController.cancelCurrentAnimation();
              animationController.addAnimation(animation);
            }
          });
    }

    SoundEvent shootSound = this.gunItem.getShootSound().get();
    if (this.getAttachments().stream().anyMatch(AttachmentItem::isSoundSuppressor)) {
      // Tries to get the silenced shoot sound.
      // If it does not exists, the default shoot sound is used instead.
      shootSound = this.gunItem.getSilencedShootSound().orElse(shootSound);
    }
    entity.playSound(shootSound, 0.25F, 1.0F);

    for (int i = 0; i < this.gunItem.getBulletAmountToFire(); i++) {
      Optional<? extends RayTraceResult> rayTrace = RayTraceUtil
          .traceAllObjects(entity, 100, 1.0F, this.getAccuracy(entity, itemStack), random);

      if (MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Pre(itemStack, entity, rayTrace))) {
        continue;
      }

      rayTrace.ifPresent(result -> {
        switch (result.getType()) {
          case BLOCK:
            this.hitBlock(entity, itemStack, (BlockRayTraceResult) result);
            break;
          case ENTITY:
            this.hitEntity(entity, itemStack, (EntityRayTraceResult) result);
            break;
          default:
            break;
        }
      });

      MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(itemStack, entity, rayTrace));
    }
  }

  private void hitEntity(Entity entity, ItemStack itemStack, EntityRayTraceResult rayTrace) {
    Entity entityHit = rayTrace.getEntity();
    Vec3d hitVec3d = rayTrace.getHitVec();
    World world = entityHit.getEntityWorld();
    float damage = this.gunItem.getDamage();

    float armorPenetration = (1.0F
        + (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ARMOR_PENETRATION.get(), itemStack)
            / 255.0F))
        * this.magazineStack
            .getCapability(ModCapabilities.MAGAZINE)
            .map(IMagazine::getArmorPenetration)
            .orElse(0.0F);
    armorPenetration = armorPenetration > 1.0F ? 1.0F : armorPenetration;
    if (armorPenetration > 0 && entityHit instanceof LivingEntity) {
      LivingEntity livingEntityHit = (LivingEntity) entityHit;
      float reducedDamage = damage - CombatRules
          .getDamageAfterAbsorb(damage, livingEntityHit.getTotalArmorValue(),
              (float) livingEntityHit
                  .getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS)
                  .getValue());
      // Apply armor penetration by adding to the damage lost by armor absorption
      damage += reducedDamage * armorPenetration;
    }

    double chinHeight = (entityHit.getY() + entityHit.getEyeHeight() - 0.2F);
    boolean headshot = (entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity
        || entityHit instanceof SkeletonEntity || entityHit instanceof CreeperEntity
        || entityHit instanceof EndermanEntity || entityHit instanceof WitchEntity
        || entityHit instanceof VillagerEntity || entityHit instanceof VindicatorEntity
        || entityHit instanceof WanderingTraderEntity) && rayTrace.getHitVec().y >= chinHeight;
    if (headshot) {
      // The sound is played at client side too
      world
          .playMovingSound(null, entity, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 2F,
              1.5F);
      damage *= HEADSHOT_MULTIPLIER;

      // Runs at server side only
      if (world instanceof ServerWorld) {
        ServerWorld serverWorld = (ServerWorld) world;
        // Sends to everyone near
        serverWorld
            .getPlayers()
            .stream()
            .filter(player -> player != entityHit)
            .forEach(
                player -> serverWorld
                    .spawnParticle(player,
                        new BlockParticleData(ParticleTypes.BLOCK,
                            Blocks.BONE_BLOCK.getDefaultState()),
                        false, hitVec3d.getX(), hitVec3d.getY(), hitVec3d.getZ(), 12, 0.0D, 0.0D,
                        0.0D, 0.0D));
      }
    }
    // The sound is played at client side too
    world
        .playMovingSound(null, entityHit, ModSoundEvents.BULLET_IMPACT_FLESH.get(),
            SoundCategory.PLAYERS, 0.75F, (float) Math.random() + 0.7F);

    if (entityHit instanceof ClientPlayerEntity) {
      ((ClientDist) CraftingDead.getInstance().getModDist()).joltCamera(1.5F, false);
    }

    // Resets the temporary invincibility before causing the damage, preventing
    // previous damages from blocking the gun damage.
    // Also, allows multiple bullets to hit the same target at the same time.
    entityHit.hurtResistantTime = 0;

    boolean damageWasCaused = ModDamageSource
        .causeDamageWithoutKnockback(entityHit, ModDamageSource.causeGunDamage(entity, headshot),
            damage);

    if (damageWasCaused) {
      // Runs at server side only
      if (world instanceof ServerWorld) {
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld
            .getPlayers()
            .stream()
            .filter(player -> player != entityHit)
            .forEach(player -> serverWorld
                .spawnParticle(player,
                    new BlockParticleData(ParticleTypes.BLOCK,
                        Blocks.REDSTONE_BLOCK.getDefaultState()),
                    false, hitVec3d.getX(), hitVec3d.getY(), hitVec3d.getZ(), 12, 0.0D, 0.0D, 0.0D,
                    0.0D));
      }
    }

    checkCreateExplosion(itemStack, entity, rayTrace.getHitVec());

    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack) > 0) {
      rayTrace.getEntity().setFire(100);
    }

    entity
        .getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .<IPlayer<?>>cast()
        .ifPresent(player -> player
            .infect(
                EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INFECTION.get(), itemStack)
                    / 255.0F));

    float dropChance = this.magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .map(IMagazine::getEntityHitDropChance)
        .orElse(0.0F);

    if (random.nextFloat() < dropChance) {
      ItemEntity ammoEntity = new ItemEntity(world, hitVec3d.getX(), hitVec3d.getY(),
          hitVec3d.getZ(), this.magazineStack);
      world.addEntity(ammoEntity);
    }
  }

  private void hitBlock(Entity entity, ItemStack itemStack, BlockRayTraceResult rayTrace) {
    Vec3d hitVec3d = rayTrace.getHitVec();
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    Block block = blockState.getBlock();
    World world = entity.getEntityWorld();
    if (block instanceof TNTBlock) {
      block
          .catchFire(blockState, entity.getEntityWorld(), blockPos, null,
              entity instanceof LivingEntity ? (LivingEntity) entity : null);
      entity.getEntityWorld().removeBlock(blockPos, false);
    }

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

    world.playSound(null, blockPos, hitSound, SoundCategory.BLOCKS, 1.2F, 1.0F); // volume, pitch

    // Runs at server side only
    if (world instanceof ServerWorld) {
      ServerWorld serverWorld = (ServerWorld) world;
      serverWorld
          .spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, blockState), hitVec3d.getX(),
              hitVec3d.getY(), hitVec3d.getZ(), 12, 0D, 0D, 0D, 0D);
    }

    checkCreateExplosion(itemStack, entity, rayTrace.getHitVec());
    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack) > 0) {
      BlockPos blockAbove = rayTrace.getPos().add(0, 1, 0);
      if (!world.isAirBlock(rayTrace.getPos()) && world.isAirBlock(blockAbove)) {
        world.setBlockState(blockAbove, Blocks.FIRE.getDefaultState());
      }
    }

    float dropChance = this.magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .map(IMagazine::getBlockHitDropChance)
        .orElse(0.0F);

    if (random.nextFloat() < dropChance) {
      ItemEntity ammoEntity = new ItemEntity(world, hitVec3d.getX(), hitVec3d.getY(),
          hitVec3d.getZ(), new ItemStack(this.magazineStack.getItem()));
      world.addEntity(ammoEntity);
    }
  }

  @Override
  public float getAccuracy(Entity entity, ItemStack itemStack) {
    float accuracy = 1.0F;
    if (entity.getX() != entity.prevPosX || entity.getY() != entity.prevPosY
        || entity.getZ() != entity.prevPosZ) {
      accuracy = 0.5F;

      if (entity.isSprinting()) {
        accuracy = 0.25F;
      }

      if (entity.isSneaking() && entity.onGround) {
        accuracy = 1.0F;
      }
    }
    accuracy = accuracy * this.gunItem.getAccuracy()
        * this.getAttachmentMultiplier(MultiplierType.ACCURACY);
    return accuracy > 1.0F ? 1.0F : accuracy;
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
  }

  @Override
  public int getMagazineSize() {
    return this.magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .map(IMagazine::getSize)
        .orElse(0);
  }

  @Override
  public void setMagazineSize(int size) {
    this.magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .ifPresent(magazine -> magazine.setSize(size));
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
  public void toggleFireMode(Entity entity, boolean sendUpdate) {
    if (this.fireModeInfiniteIterator.hasNext()) {
      this.fireMode = this.fireModeInfiniteIterator.next();
    }

    entity.playSound(ModSoundEvents.TOGGLE_FIRE_MODE.get(), 1.0F, 1.0F);
    if (entity instanceof PlayerEntity) {
      ((PlayerEntity) entity)
          .sendStatusMessage(new TranslationTextComponent(this.fireMode.getTranslationKey()), true);
    }

    if (sendUpdate) {
      PacketTarget target = entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(() -> entity);
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(target, new ToggleFireModeMessage(entity.getEntityId()));
    }
  }

  @Override
  public boolean hasCrosshair() {
    return this.gunItem.hasCrosshair();
  }

  @Override
  public void toggleAiming(Entity entity, boolean sendUpdate) {
    this.aiming = !this.aiming;
    if (sendUpdate) {
      PacketTarget target = entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY.with(() -> entity);
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(target, new ToggleAimingMessage(entity.getEntityId()));
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.magazineStack.write(new CompoundNBT()));
    ListNBT attachmentsTag = this.attachments
        .stream()
        .map(attachment -> StringNBT.of(attachment.getRegistryName().toString()))
        .collect(ListNBT::new, ListNBT::add, List::addAll);
    nbt.put("attachments", attachmentsTag);
    nbt.put("paintStack", this.paintStack.write(new CompoundNBT()));
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
  public boolean isAiming(Entity entity, ItemStack itemStack) {
    return this.aiming;
  }

  @Override
  public float getFovModifier(Entity entity, ItemStack itemStack) {
    return this.getAttachmentMultiplier(AttachmentItem.MultiplierType.FOV);
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity, ItemStack itemStack) {
    return Optional.empty();
  }

  @Override
  public int getOverlayTextureWidth() {
    return 2048;
  }

  @Override
  public int getOverlayTextureHeight() {
    return 512;
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

  private ItemStack findAmmo(ILiving<?> living, boolean simulate) {
    List<ICapabilityProvider> ammoProviders = ImmutableList
        .of(living.getStackInSlot(InventorySlotType.VEST.getIndex()),
            living.getStackInSlot(InventorySlotType.BACKPACK.getIndex()), living.getEntity());
    for (ICapabilityProvider ammoProvider : ammoProviders) {
      ItemStack ammoStack = ammoProvider
          .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
          .map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); ++i) {
              ItemStack itemStack = itemHandler.getStackInSlot(i);
              if (this.gunItem.getAcceptedMagazines().contains(itemStack.getItem()) && !itemStack
                  .getCapability(ModCapabilities.MAGAZINE)
                  .map(IMagazine::isEmpty)
                  .orElse(true)) {
                ItemStack magazineStack = simulate ? itemStack.copy() : itemStack.split(1);
                if (!simulate && living.getEntity() instanceof PlayerEntity) {
                  PlayerEntity playerEntity = (PlayerEntity) living.getEntity();
                  if (itemStack.isEmpty()) {
                    playerEntity.inventory.deleteStack(itemStack);
                  }
                }
                return magazineStack;
              }
            }
            return ItemStack.EMPTY;
          })
          .orElse(ItemStack.EMPTY);
      if (ammoStack.getCapability(ModCapabilities.MAGAZINE).isPresent()) {
        return ammoStack;
      }
    }
    return ItemStack.EMPTY;
  }
}
