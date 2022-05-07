/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.grenade;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.gun.GunItem;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class DecoyGrenadeEntity extends Grenade {

  private long lastShotMs;
  private final GunItem gunItem = getRandomGun(this.random);

  public DecoyGrenadeEntity(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public DecoyGrenadeEntity(LivingEntity thrower, Level worldIn) {
    super(ModEntityTypes.DECOY_GRENADE.get(), thrower, worldIn);
  }

  @Override
  public void onMotionStop(int stopsCount) {
    if (stopsCount == 1) {
      this.setActivated(true);
    }
  }

  @Override
  public void activatedChanged(boolean activated) {
    if (!activated) {
      if (!this.level.isClientSide()) {
        this.kill();
        this.level.explode(this, this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 1.3F, false,
            Explosion.BlockInteraction.NONE);
      }
    } else {
      this.playFakeShoot();
    }
  }

  @Override
  public OptionalInt getMinimumTicksUntilAutoDeactivation() {
    return OptionalInt.of(
        CraftingDead.serverConfig.explosivesDecoyGrenadeTicksBeforeDeactivation.get());
  }

  @Override
  public void onGrenadeTick() {
    if (!this.isActivated()) {
      return;
    }

    if (!this.level.isClientSide()) {
      if (this.random.nextInt(20) == 0 && this.canShoot()) {
        this.playFakeShoot();
      }
    } else {
      this.level.addParticle(ParticleTypes.SMOKE, true, this.getX(),
          this.getY() + 0.4D, this.getZ(), 0, 0, 0);
    }
  }

  public boolean canShoot() {
    final long fireDelayMs = this.gunItem.getFireDelayMs();
    return (Util.getMillis() - this.lastShotMs) >= fireDelayMs;
  }

  public void playFakeShoot() {
    this.playSound(this.gunItem.getShootSound(), 1.5F, 1F);
    this.lastShotMs = Util.getMillis();
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
    List<GunItem> possibleGuns = ModItems.deferredRegister.getEntries().stream()
        .map(RegistryObject::get)
        .filter(GunItem.class::isInstance)
        .map(GunItem.class::cast)
        .collect(Collectors.toList());

    // Supposing the list will never be empty
    return possibleGuns.get(random.nextInt(possibleGuns.size()));
  }
}
