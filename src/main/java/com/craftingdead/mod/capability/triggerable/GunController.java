package com.craftingdead.mod.capability.triggerable;

import com.craftingdead.mod.capability.aimable.IAimable;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.IFireMode;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.RayTraceUtil;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

public class GunController implements ITriggerable, IAimable {

  private static final float HEADSHOT_MULTIPLIER = 4;

  /**
   * The {@link GunItem}.
   */
  @Getter
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
   * Reload ticks remaining.
   */
  private float reloadDuration;

  /**
   * The accuracy value (0.0F - 1.0F)
   */
  private float accuracy;

  /**
   * Constructs a new {@link GunController}.
   *
   * @param item - the {@link GunItem} of the associated {@link ItemStack}
   */
  public GunController(GunItem item) {
    this.item = item;
    this.fireMode = item.getFireModes().get(0).get();
  }

  @Override
  public void tick(ItemStack itemStack, Entity entity) {
    this.updateAccuracy(entity);

    // On finished reloading
    if (this.reloadDuration-- == 0) {
      ;
    }

    long fireRateNanoseconds =
        TimeUnit.NANOSECONDS.convert(this.item.getFireRate(), TimeUnit.MILLISECONDS);
    long time = System.nanoTime();
    long timeDelta = time - this.lastShotNanos;
    if (this.fireMode.canShoot(this.triggerPressed) && timeDelta > fireRateNanoseconds) {
      this.lastShotNanos = time;
      this.shoot(itemStack, entity);
    }
  }

  private void updateAccuracy(Entity entity) {
    this.accuracy = 1.0F;

    if (entity.posX != entity.lastTickPosX || entity.posY != entity.lastTickPosY
        || entity.posZ != entity.lastTickPosZ) {

      this.accuracy = 0.5F;

      if (entity.isSprinting()) {
        this.accuracy = 0.25F;
      }

      if (entity.isSneaking() && entity.onGround) {
        this.accuracy = 1.0F;
      }
    }

    this.accuracy *= this.item.getAccuracy();

  }

  private void shoot(ItemStack itemStack, Entity entity) {
    entity.playSound(this.item.getShootSound().get(), 1.0F, 1.0F);

    Optional<? extends RayTraceResult> rayTrace = RayTraceUtil.traceAllObjects(entity, 100, 1.0F);

    if (MinecraftForge.EVENT_BUS
        .post(new GunEvent.ShootEvent.Pre(this, entity, itemStack, rayTrace))) {
      return;
    }

    rayTrace.ifPresent((result) -> {
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
        && rayTrace.getHitVec().y >= (entityHit.posY + entityHit.getEyeHeight())) {
      damage *= HEADSHOT_MULTIPLIER;
    }
    entityHit.attackEntityFrom(ModDamageSource.causeGunDamage(entity), damage);
  }

  private void hitBlock(ItemStack itemStack, Entity entity, BlockRayTraceResult rayTrace) {
    BlockPos blockPos = rayTrace.getPos();
    BlockState blockState = entity.getEntityWorld().getBlockState(blockPos);
    Block block = blockState.getBlock();
    if (block instanceof TNTBlock) {
      TNTBlock.explode(entity.getEntityWorld(), blockPos);
      entity.getEntityWorld().setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
    }
  }

  public void reload() {
    // If not already reloading
    if (this.reloadDuration == 0) {
      // Set reload time
      this.reloadDuration = this.item.getReloadTime();
    }
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity) {
    this.triggerPressed = triggerPressed;
  }

  @Override
  public float getAccuracy() {
    return this.accuracy;
  }
}
