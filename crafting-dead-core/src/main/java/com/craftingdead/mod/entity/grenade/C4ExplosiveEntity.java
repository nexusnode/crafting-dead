package com.craftingdead.mod.entity.grenade;

import org.apache.commons.lang3.tuple.Triple;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.item.GrenadeItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.util.ModDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class C4ExplosiveEntity extends GrenadeEntity {

  private static final Triple<SoundEvent, Float, Float> C4_BOUNCE_SOUND =
      Triple.of(SoundEvents.ENTITY_PLAYER_SMALL_FALL, 1.0F, 1.5F);

  public C4ExplosiveEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public C4ExplosiveEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.c4Explosive, thrower, worldIn);
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (ModDamageSource.isGunDamage(source) || source.isExplosion() || source.isFireDamage()) {
      // TODO Save who activated the grenade, so the true source
      // of this DamageSource could be used when the grenade explodes.
      this.setActivated(true);
    }
    return super.attackEntityFrom(source, amount);
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public void onActivationStateChange(boolean activated) {
    if (activated) {
      if (!this.world.isRemote()) {
        this.remove();
        this.world.createExplosion(this,
            ModDamageSource.causeUnscaledExplosionDamage(this.getThrower().orElse(null)),
            this.getX(), this.getY() + this.getHeight(), this.getZ(), 4F, false,
            Explosion.Mode.NONE);
      }
    }
  }

  @Override
  public boolean canBePickedUp(PlayerEntity playerFrom) {
    return this.isStoppedInGround();
  }

  @Override
  public Float getBounceFactor(BlockRayTraceResult blockRayTraceResult) {
    return blockRayTraceResult.getFace() == Direction.UP ?
        null : super.getBounceFactor(blockRayTraceResult);
  }

  @Override
  public boolean canBeRemotelyActivated() {
    return true;
  }

  @Override
  public Triple<SoundEvent, Float, Float> getBounceSound(BlockRayTraceResult blockRayTraceResult) {
    return C4_BOUNCE_SOUND;
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.C4.get();
  }

  @Override
  public void onMotionStop(int stopsCount) {}
}
