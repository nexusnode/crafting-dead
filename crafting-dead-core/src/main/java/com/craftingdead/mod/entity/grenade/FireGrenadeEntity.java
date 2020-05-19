package com.craftingdead.mod.entity.grenade;

import org.apache.commons.lang3.tuple.Triple;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.item.GrenadeItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.util.ModDamageSource;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class FireGrenadeEntity extends GrenadeEntity {

  private static final double FIRE_RADIUS = 2D;
  private static final Triple<SoundEvent, Float, Float> FIRE_GRENADE_BOUNCE_SOUND =
      Triple.of(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 0.9F);

  public FireGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public FireGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.fireGrenade, thrower, worldIn);
  }

  @Override
  public void onSurfaceHit(BlockRayTraceResult blockRayTraceResult) {
    super.onSurfaceHit(blockRayTraceResult);
    if (blockRayTraceResult.getFace() == Direction.UP) {
      this.setActivated(true);
    }
  }

  @Override
  public void onActivationStateChange(boolean activated) {
    if (activated) {
      if (!this.world.isRemote()) {
        this.remove();
        this.world.createExplosion(this,
            ModDamageSource.causeUnscaledExplosionDamage(this.getThrower().orElse(null)),
            this.getX(), this.getY() + this.getHeight(), this.getZ(), 2F, true,
            Explosion.Mode.NONE);

        BlockPos.getAllInBox(this.getPosition().add(-FIRE_RADIUS, 0, -FIRE_RADIUS),
            this.getPosition().add(FIRE_RADIUS, 0, FIRE_RADIUS)).forEach(blockPos -> {
              if (this.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                if (Math.random() <= 0.8D) {
                  this.world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
                }
              }
            });
      }
    }
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public GrenadeItem asItem() {
    return ModItems.FIRE_GRENADE.get();
  }

  @Override
  public Triple<SoundEvent, Float, Float> getBounceSound(BlockRayTraceResult blockRayTraceResult) {
    return blockRayTraceResult.getFace() == Direction.UP
        ? FIRE_GRENADE_BOUNCE_SOUND
        : super.getBounceSound(blockRayTraceResult);
  }

  @Override
  public void onMotionStop(int stopsCount) {}
}
