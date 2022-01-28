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

package com.craftingdead.survival.world.entity.grenade;

import com.craftingdead.core.particle.RGBFlashParticleData;
import com.craftingdead.core.world.entity.grenade.Grenade;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class PipeGrenade extends Grenade {

  private static final RGBFlashParticleData RED_FLASH =
      new RGBFlashParticleData(1F, 0.35F, 0.35F, 1F);

  public PipeGrenade(EntityType<? extends Grenade> entityType, Level level) {
    super(entityType, level);
  }

  public PipeGrenade(LivingEntity thrower, Level level) {
    super(SurvivalEntityTypes.PIPE_GRENADE.get(), thrower, level);
  }

  @Override
  public Integer getMinimumTicksUntilAutoActivation() {
    return 100;
  }

  @Override
  public void activatedChanged(boolean activated) {
    if (activated) {
      if (!this.level.isClientSide()) {
        this.kill();
        this.level.explode(this,
            this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 4F, false,
            Explosion.BlockInteraction.NONE);
      }
    }
  }

  @Override
  public void onGrenadeTick() {
    if (this.tickCount % 6 == 0) {
      if (this.level.isClientSide()) {
        this.level.addParticle(RED_FLASH, true,
            this.getX(), this.getY(), this.getZ(), 0D, 0D, 0D);
      } else {
        float pitchProgress =
            this.tickCount / (float) (this.getMinimumTicksUntilAutoActivation());
        float gradualPitch = Mth.lerp(pitchProgress, 1.0F, 2F);
        this.playSound(SoundEvents.NOTE_BLOCK_BELL, 1.7F, gradualPitch);
      }
    }
  }

  @Override
  public boolean isAttracting() {
    return true;
  }

  @Override
  public GrenadeItem asItem() {
    return SurvivalItems.PIPE_GRENADE.get();
  }

  @Override
  public void onMotionStop(int stopsCount) {}
}
