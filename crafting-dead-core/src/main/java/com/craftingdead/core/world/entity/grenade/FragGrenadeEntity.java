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

package com.craftingdead.core.world.entity.grenade;

import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class FragGrenadeEntity extends GrenadeEntity {

  public FragGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public FragGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.FRAG_GRENADE.get(), thrower, worldIn);
  }

  @Override
  public Integer getMinimumTicksUntilAutoActivation() {
    return 35;
  }

  @Override
  public void onMotionStop(int stopsCount) {}

  @Override
  public void activatedChanged(boolean activated) {
    if (activated) {
      if (!this.level.isClientSide()) {
        this.remove();
        this.level.explode(this,
            this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 4F, false,
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
