package com.craftingdead.mod.capability;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.aimable.IAimable;
import com.craftingdead.mod.capability.animation.IAnimation;
import com.craftingdead.mod.capability.shootable.IShootable;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.IFireMode;
import com.craftingdead.mod.item.Magazine;
import com.craftingdead.mod.item.MagazineItem;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import com.craftingdead.mod.util.RayTraceUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

public class GunController implements IShootable, IAimable, INBTSerializable<CompoundNBT> {

  private static final float HEADSHOT_MULTIPLIER = 4;

  /**
   * The {@link GunItem}.
   */
  private final GunItem item;

  /**
   * If the trigger is currently pressed.
   */
  private boolean triggerPressed;

  /**
   * Time of the last shot.
   */
  private long lastShotNanos = Integer.MIN_VALUE;

  /**
   * The selected {@link IFireMode}.
   */
  private IFireMode fireMode;

  /**
   * The accuracy value (0.0F - 1.0F)
   */
  private float accuracy;

  private Magazine magazine;

  private final Map<AttachmentItem.Type, AttachmentItem> attachments =
      new EnumMap<>(AttachmentItem.Type.class);

  /**
   * Constructs a new {@link GunController}.
   *
   * @param item - the {@link GunItem} of the associated {@link ItemStack}
   */
  public GunController(GunItem item) {
    this.item = item;
    this.fireMode = item.getFireModes().get(0).get();
  }

  public GunItem getItem() {
    return this.item;
  }

  @Override
  public void tick(ItemStack itemStack, Entity entity) {
    this.updateAccuracy(entity);
    long fireRateNanoseconds =
        TimeUnit.NANOSECONDS.convert(this.item.getFireRate(), TimeUnit.MILLISECONDS);
    long time = System.nanoTime();
    long timeDelta = time - this.lastShotNanos;
    if (this.magazine != null && this.magazine.getSize() > 0
        && this.fireMode.canShoot(this.triggerPressed) && timeDelta > fireRateNanoseconds) {
      this.lastShotNanos = time;
      this.shoot(itemStack, entity);
    }
  }

  private void updateAccuracy(Entity entity) {
    this.accuracy = 1.0F;

    if (entity.getX() != entity.lastTickPosX || entity.getY() != entity.lastTickPosY
        || entity.getZ() != entity.lastTickPosZ) {

      this.accuracy = 0.5F;

      if (entity.isSprinting()) {
        this.accuracy = 0.25F;
      }

      if (entity.isSneaking() && entity.onGround) {
        this.accuracy = 1.0F;
      }
    }

    this.accuracy *= this.item.getAccuracy() * this
        .getAttachments()
        .values()
        .stream()
        .map(attachment -> attachment.getAccuracy())
        .reduce(1.0F, (a, b) -> a * b);
  }

  private void shoot(ItemStack itemStack, Entity entity) {
    this.magazine.decrementSize();

    if (this.isLocalPlayer(entity)) {
      ((ClientDist) CraftingDead.getInstance().getModDist()).getRecoilHelper().jolt(this.accuracy);
    }

    itemStack.getCapability(ModCapabilities.ANIMATION_CONTROLLER).ifPresent(animationController -> {
      IAnimation animation = this.item.getAnimations().get(GunItem.AnimationType.SHOOT).get();
      if (animation != null) {
        animationController.cancelCurrentAnimation();
        animationController.addAnimation(animation);
      }
    });

    entity.playSound(this.item.getShootSound().get(), 1.0F, 1.0F);

    Optional<? extends RayTraceResult> rayTrace = RayTraceUtil.traceAllObjects(entity, 100, 1.0F);

    if (MinecraftForge.EVENT_BUS
        .post(new GunEvent.ShootEvent.Pre(this, entity, itemStack, rayTrace))) {
      return;
    }

    rayTrace.ifPresent(result -> {
      switch (result.getType()) {
        case BLOCK:
          this.hitBlock(itemStack, entity, (BlockRayTraceResult) result);
          break;
        case ENTITY:
          this.hitEntity(entity, (EntityRayTraceResult) result);
          break;
        default:
          break;
      }
    });

    MinecraftForge.EVENT_BUS.post(new GunEvent.ShootEvent.Post(this, entity, itemStack, rayTrace));
  }

  private void hitEntity(Entity entity, EntityRayTraceResult rayTrace) {
    Entity entityHit = rayTrace.getEntity();
    float damage = this.item.getDamage();
    if ((entityHit instanceof PlayerEntity || entityHit instanceof ZombieEntity)
        && rayTrace.getHitVec().y >= (entityHit.getY() + entityHit.getEyeHeight())) {
      damage *= HEADSHOT_MULTIPLIER;
    }
    entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity), damage);
  }

  private void hitBlock(ItemStack itemStack, Entity entity, BlockRayTraceResult rayTrace) {
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    Block block = blockState.getBlock();
    if (block instanceof TNTBlock) {
      block
          .catchFire(blockState, entity.getEntityWorld(), blockPos, null,
              entity instanceof LivingEntity ? (LivingEntity) entity : null);
      entity.getEntityWorld().removeBlock(blockPos, false);
    }
  }

  private boolean isLocalPlayer(Entity entity) {
    return entity.getEntityWorld().isRemote() && entity == Minecraft.getInstance().player;
  }

  public Map<AttachmentItem.Type, AttachmentItem> getAttachments() {
    return this.attachments;
  }

  @Override
  public void setTriggerPressed(ItemStack itemStack, Entity entity, boolean triggerPressed) {
    if (triggerPressed && (this.magazine == null || this.magazine.getSize() <= 0)) {
      entity.playSound(ModSoundEvents.DRY_FIRE.get(), 1.0F, 1.0F);
      return;
    }
    this.triggerPressed = triggerPressed;
  }

  @Override
  public void reload(ItemStack itemStack, Entity entity) {
    boolean survivalPlayer =
        entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative();
    ItemStack ammoStack;
    if (survivalPlayer) {
      ammoStack = ((PlayerEntity) entity).findAmmo(itemStack);
    } else {
      ammoStack = new ItemStack(
          ForgeRegistries.ITEMS.getValue(this.item.getAcceptedMagazines().iterator().next()));
    }
    this.magazine = ((MagazineItem) ammoStack.getItem()).getMagazine();
    if (survivalPlayer) {
      ammoStack.shrink(1);
      if (ammoStack.isEmpty()) {
        ((PlayerEntity) entity).inventory.deleteStack(ammoStack);
      }
    }
  }

  @Override
  public int getReloadDuration() {
    return this.item.getReloadDurationTicks();
  }

  @Override
  public boolean canReload(ItemStack itemStack, Entity entity) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) entity;
      if (!playerEntity.isCreative() && playerEntity.findAmmo(itemStack).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public SoundEvent getReloadSound() {
    return this.item.getReloadSound().get();
  }

  @Override
  public float getAccuracy() {
    return this.accuracy;
  }

  @Override
  public float getCameraZoom() {
    float cameraZoom = this.item.getCameraZoom();
    AttachmentItem sight = this.attachments.get(AttachmentItem.Type.SIGHT);
    if (sight != null) {
      cameraZoom *= sight.getCameraZoom();
    }
    return cameraZoom;
  }

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}
}
