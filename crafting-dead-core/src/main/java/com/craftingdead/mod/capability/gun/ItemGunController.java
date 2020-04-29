package com.craftingdead.mod.capability.gun;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimation;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
import com.craftingdead.mod.item.FireMode;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.MagazineItem;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import com.craftingdead.mod.util.ParticleUtil;
import com.craftingdead.mod.util.RayTraceUtil;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemGunController implements IGunController {

  private static final float HEADSHOT_MULTIPLIER = 4;

  private final GunItem gunItem;

  private boolean triggerPressed;

  /**
   * Time of the last shot.
   */
  private long lastShotNanos = Integer.MIN_VALUE;

  private int reloadDurationTicks = 0;

  private int totalReloadDurationTicks;

  private FireMode fireMode;

  private ItemStack magazineStack = ItemStack.EMPTY;

  private int ammo;

  /**
   * The amount of shots since the last time the trigger was pressed.
   */
  private int shotsInARow;

  private Set<AttachmentItem> attachments;

  private ItemStack paintStack = ItemStack.EMPTY;

  private final Iterator<FireMode> fireModeInfiniteIterator;

  public ItemGunController(GunItem gunItem) {
    this.gunItem = gunItem;
    this.fireModeInfiniteIterator = Iterables.cycle(this.gunItem.getFireModes()).iterator();
    this.fireMode = this.fireModeInfiniteIterator.next();
    this.attachments = gunItem.getDefaultAttachments();
  }

  @Override
  public void tick(Entity entity, ItemStack itemStack) {
    boolean isMaxShotsReached =
        this.fireMode.getMaxShots().map(max -> this.shotsInARow >= max).orElse(false);

    if (!isMaxShotsReached) {
      this.tryShoot(entity, itemStack);
    }

    if (this.isReloading() && --this.reloadDurationTicks == 0) {
      ItemStack magazineStack = null;
      if (entity instanceof PlayerEntity) {
        PlayerEntity playerEntity = (PlayerEntity) entity;
        if (!playerEntity.isCreative()) {
          magazineStack = playerEntity.findAmmo(itemStack);
        }
      }

      if (magazineStack == null) {
        magazineStack = new ItemStack(this.gunItem.getAcceptedMagazines().iterator().next());
      }

      this.magazineStack = magazineStack;
      this.ammo = ((MagazineItem) magazineStack.getItem()).getSize();

      if (entity instanceof PlayerEntity) {
        PlayerEntity playerEntity = (PlayerEntity) entity;
        if (!playerEntity.isCreative()) {
          magazineStack.shrink(1);
          if (magazineStack.isEmpty()) {
            playerEntity.inventory.deleteStack(magazineStack);
          }
        }
      }
    }
  }

  @Override
  public void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed) {
    this.triggerPressed = triggerPressed;

    if (this.triggerPressed) {
      // Resets the counter
      this.shotsInARow = 0;

      this.tryShoot(entity, itemStack);
    }
  }

  @Override
  public boolean isReloading() {
    return this.reloadDurationTicks > 0;
  }

  @Override
  public int getTotalReloadDurationTicks() {
    return this.totalReloadDurationTicks;
  }

  @Override
  public int getReloadDurationTicks() {
    return this.reloadDurationTicks;
  }

  @Override
  public void stopReloading() {
    if (this.isReloading()) {
      Minecraft.getInstance().getSoundHandler().stop(null, SoundCategory.PLAYERS);
      this.reloadDurationTicks = 0;
    }
  }

  @Override
  public void startReloading(Entity entity, ItemStack itemStack) {
    boolean canReload =
        entity instanceof PlayerEntity
            ? (((PlayerEntity) entity).isCreative()
                || !((PlayerEntity) entity).findAmmo(itemStack).isEmpty())
            : true;
    if (!this.isReloading() && canReload) {
      entity.world
          .playMovingSound(null, entity, this.gunItem.getReloadSound().get(), SoundCategory.PLAYERS,
              1.0F, 1.0F);
      this.reloadDurationTicks =
          this.totalReloadDurationTicks = this.gunItem.getReloadDurationTicks();
    }
  }

  private void tryShoot(Entity entity, ItemStack itemStack) {
    if (!this.triggerPressed) {
      return;
    }

    if (this.isReloading()) {
      return;
    }

    if (this.ammo <= 0) {
      entity.playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      entity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> player.reload(false));
      this.triggerPressed = false;
      return;
    }

    long fireRateNanoseconds =
        TimeUnit.NANOSECONDS.convert(this.gunItem.getFireRate(), TimeUnit.MILLISECONDS);
    long time = System.nanoTime();
    long timeDelta = time - this.lastShotNanos;
    if (timeDelta > fireRateNanoseconds) {
      this.lastShotNanos = time;
      this.shoot(entity, itemStack);
    }
  }

  private void shoot(Entity entity, ItemStack itemStack) {
    if (this.ammo <= 0) {
      return;
    }

    this.shotsInARow++;
    this.ammo--;

    if (entity.world.isRemote()) {
      if (entity instanceof ClientPlayerEntity) {
        ((ClientDist) CraftingDead.getInstance().getModDist())
            .joltCamera(this.getAccuracy(entity, itemStack));
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
      // TODO Add per-bullet imprecision due to shotguns
      Optional<? extends RayTraceResult> rayTrace = RayTraceUtil.traceAllObjects(entity, 100, 1.0F);

      if (MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Pre(itemStack, entity, rayTrace))) {
        continue;
      }

      rayTrace.ifPresent(result -> {
        switch (result.getType()) {
          case BLOCK:
            this.hitBlock(entity, (BlockRayTraceResult) result);
            break;
          case ENTITY:
            this.hitEntity(entity, (EntityRayTraceResult) result);
            break;
          default:
            break;
        }
      });

      MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(itemStack, entity, rayTrace));
    }
  }

  private void hitEntity(Entity entity, EntityRayTraceResult rayTrace) {
    Entity entityHit = rayTrace.getEntity();
    Vec3d hitVec3d = rayTrace.getHitVec();
    World world = entityHit.getEntityWorld();
    float damage = this.gunItem.getDamage();
    double chinHeight = (entityHit.getY() + entityHit.getEyeHeight() - 0.2F);
    boolean headshot = (entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity)
        && rayTrace.getHitVec().y >= chinHeight;
    if (headshot) {
      // The sound is played at client side too
      world
          .playMovingSound(null, entity, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 2F,
              1.5F);
      damage *= HEADSHOT_MULTIPLIER;

      // Runs at server side only
      if (world instanceof ServerWorld) {
        ServerWorld serverWorld = (ServerWorld) world;

        // Sends to everyone near, except the player
        ParticleUtil
            .spawnParticleServerside(serverWorld,
                new BlockParticleData(ParticleTypes.BLOCK, Blocks.BONE_BLOCK.getDefaultState()),
                hitVec3d.getX(), hitVec3d.getY(), hitVec3d.getZ(), 12, 0D, 0D, 0D, 0D,
                (player) -> player != entityHit);
      }
    }
    // The sound is played at client side too
    world
        .playMovingSound(null, entityHit, ModSoundEvents.BULLET_IMPACT_FLESH.get(),
            SoundCategory.PLAYERS, 0.75F, (float) Math.random() + 0.7F);
    ModDamageSource
        .causeDamageWithoutKnockback(entityHit, ModDamageSource.causeGunDamage(entity, headshot),
            damage);

    // Runs at server side only
    if (world instanceof ServerWorld) {
      ServerWorld serverWorld = (ServerWorld) world;

      // Sends to everyone near, except the player
      ParticleUtil
          .spawnParticleServerside(serverWorld,
              new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
              hitVec3d.getX(), hitVec3d.getY(), hitVec3d.getZ(), 12, 0D, 0D, 0D, 0D,
              (player) -> player != entityHit);
    }

    entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity, headshot), damage);

    // Removes the temporary invincibility after causing the damage.
    // Allows more bullets to hit the same target at the same time.
    // Good for shotguns and teaming.
    entityHit.hurtResistantTime = 0;
  }

  private void hitBlock(Entity entity, BlockRayTraceResult rayTrace) {
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
  }

  @Override
  public float getAccuracy(Entity entity, ItemStack itemStack) {
    float accuracy = 1.0F;

    if (entity.getX() != entity.lastTickPosX || entity.getY() != entity.lastTickPosY
        || entity.getZ() != entity.lastTickPosZ) {

      accuracy = 0.5F;

      if (entity.isSprinting()) {
        accuracy = 0.25F;
      }

      if (entity.isSneaking() && entity.onGround) {
        accuracy = 1.0F;
      }
    }

    return accuracy * this.gunItem.getAccuracy()
        * this.getAttachmentMultiplier(MultiplierType.ACCURACY);
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
  }

  @Override
  public int getAmmo() {
    return this.ammo;
  }

  @Override
  public void setAmmo(int ammo) {
    this.ammo = ammo;
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
  public void toggleFireMode(Entity entity) {
    if (this.fireModeInfiniteIterator.hasNext()) {
      this.fireMode = fireModeInfiniteIterator.next();
    }

    entity.playSound(ModSoundEvents.TOGGLE_FIRE_MODE.get(), 1.0F, 1.0F);
    if (entity instanceof PlayerEntity) {
      ((PlayerEntity) entity)
          .sendStatusMessage(new TranslationTextComponent(fireMode.getTranslationKey()), true);
    }
  }

  @Override
  public boolean hasCrosshair() {
    return this.gunItem.hasCrosshair();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.magazineStack.write(new CompoundNBT()));
    nbt.putInt("ammo", this.ammo);
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
    this.setAmmo(nbt.getInt("ammo"));
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
    if (!stack.isEmpty()) {
      Validate
          .isInstanceOf(MagazineItem.class, stack.getItem(),
              "Non-empty ItemStack must be a magazine");
    }
    this.magazineStack = stack;
  }
}
