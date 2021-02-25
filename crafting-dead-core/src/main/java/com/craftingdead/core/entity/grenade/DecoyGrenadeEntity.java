/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.entity.grenade;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.item.GrenadeItem;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Util;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class DecoyGrenadeEntity extends GrenadeEntity {

  private long lastShotMs;
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
        this.world.createExplosion(this, this.createDamageSource(), null,
            this.getPosX(), this.getPosY() + this.getHeight(), this.getPosZ(), 1.3F, false,
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
      if (this.rand.nextInt(20) == 0 && this.canShoot()) {
        this.playFakeShoot();
      }
    } else {
      this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(),
          this.getPosY() + 0.4D, this.getPosZ(), 0, 0, 0);
    }
  }

  public boolean canShoot() {
    final long fireDelayMs = this.gunItem.getFireDelayMs();
    return (Util.milliTime() - this.lastShotMs) >= fireDelayMs;
  }

  public void playFakeShoot() {
    this.playSound(this.gunItem.getShootSound().get(), 1.5F, 1F);
    this.lastShotMs = Util.milliTime();
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
