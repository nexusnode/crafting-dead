package com.craftingdead.mod.entity.grenade;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.item.GrenadeItem;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.util.ModDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class DecoyGrenadeEntity extends GrenadeEntity {

  private long lastSoundNanos;
  private final GunItem gunItem = getRandomGun(this.rand);

  public DecoyGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public DecoyGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.decoyGrenade, thrower, worldIn);
  }

  @Override
  public void onMotionStop(int stopsCount) {
    if (stopsCount == 1) {
      this.setActivated(true);
    }
  }

  @Override
  public void onActivationStateChange(boolean activated) {
    if (!activated) {
      if (!this.world.isRemote()) {
        this.remove();
        this.world.createExplosion(this,
            ModDamageSource.causeUnscaledExplosionDamage(this.getThrower().orElse(null)),
            this.getX(), this.getY() + this.getHeight(), this.getZ(), 1.3F, false,
            Explosion.Mode.NONE);
      }
    } else {
      this.playFakeShoot();
    }
  }

  @Override
  public Integer getMinimumTicksUntilAutoDeactivation() {
    return 400;
  }

  @Override
  public void onGrenadeTick() {
    if (!this.isActivated()) {
      return;
    }

    if (!this.world.isRemote()) {
      if (this.rand.nextInt(20) == 0) {
        if (!this.isInFireDelay()) {
          this.playFakeShoot();
        }
      }
    } else {
      this.world.addParticle(ParticleTypes.SMOKE, true, this.getX(),
          this.getY() + 0.4D, this.getZ(), 0, 0, 0);
    }
  }

  public boolean isInFireDelay() {
    long currentNanos = System.nanoTime();
    long fireRateNanos =
        TimeUnit.NANOSECONDS.convert(this.gunItem.getFireRate(), TimeUnit.MILLISECONDS);

    return (currentNanos - this.lastSoundNanos) < fireRateNanos;
  }

  public void playFakeShoot() {
    long currentNanos = System.nanoTime();
    this.playSound(this.gunItem.getShootSound().get(), 1.5F, 1F);
    this.lastSoundNanos = currentNanos;
  }

  @Override
  public boolean isAttracting() {
    return this.isActivated();
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.DECOY_GRENADE.get();
  }

  private static GunItem getRandomGun(Random random) {
    List<GunItem> possibleGuns = ModItems.ITEMS.getEntries().stream()
        .map(RegistryObject::get)
        .filter(item -> item instanceof GunItem)
        .map(item -> ((GunItem) item))
        .collect(Collectors.toList());

    // Supposing the list will never be empty
    return possibleGuns.get(random.nextInt(possibleGuns.size()));
  }
}
