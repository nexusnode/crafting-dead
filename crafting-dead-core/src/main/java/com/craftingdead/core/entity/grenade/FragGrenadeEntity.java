package com.craftingdead.core.entity.grenade;

import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.item.GrenadeItem;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.util.ModDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class FragGrenadeEntity extends GrenadeEntity {

  public FragGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public FragGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.fragGrenade, thrower, worldIn);
  }

  @Override
  public Integer getMinimumTicksUntilAutoActivation() {
    return 35;
  }

  @Override
  public void onMotionStop(int stopsCount) {}

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
  public void onGrenadeTick() {}

  @Override
  public GrenadeItem asItem() {
    return ModItems.FRAG_GRENADE.get();
  }
}
