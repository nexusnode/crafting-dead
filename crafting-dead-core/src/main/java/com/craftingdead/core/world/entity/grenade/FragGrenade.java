/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the
 * "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor"). By installing or otherwise using
 * Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this
 * Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy,
 * reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.grenade;

import java.util.OptionalInt;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.ExplosionSource;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FragGrenade extends Grenade implements ExplosionSource {

  public FragGrenade(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public FragGrenade(LivingEntity thrower, Level worldIn) {
    super(ModEntityTypes.FRAG_GRENADE.get(), thrower, worldIn);
  }

  @Override
  public OptionalInt getMinimumTicksUntilAutoActivation() {
    return OptionalInt.of(
        CraftingDead.serverConfig.explosivesFragGrenadeTicksBeforeActivation.get());
  }

  @Override
  public void onMotionStop(int stopsCount) {}

  @Override
  public void activatedChanged(boolean activated) {
    if (activated) {
      if (!this.level.isClientSide()) {
        this.kill();
        this.level.explode(this, this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
            CraftingDead.serverConfig.explosivesFragGrenadeRadius.get().floatValue(), false,
            CraftingDead.serverConfig.explosivesFragGrenadeExplosionMode.get());
      }
    }
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public GrenadeItem asItem() {
    return ModItems.FRAG_GRENADE.get();
  }

  @Override
  public float getDamageMultiplier() {
    return CraftingDead.serverConfig.explosivesFragGrenadeDamageMultiplier.get().floatValue();
  }

  @Override
  public double getKnockbackMultiplier() {
    return CraftingDead.serverConfig.explosivesFragGrenadeKnockbackMultiplier.get();
  }
}
