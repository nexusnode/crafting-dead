package com.craftingdead.mod.entity.grenade;

import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.item.GrenadeItem;
import com.craftingdead.mod.item.ModItems;
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
        this.world.createExplosion(this.getThrower().orElse(this), this.getX(), this.getY() + this.getHeight(),
            this.getZ(), 4F, Explosion.Mode.NONE);
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
