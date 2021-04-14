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

package com.craftingdead.virus.entity.monster;

import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.tag.ModItemTags;
import com.craftingdead.virus.entity.ai.goal.ActionItemGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtWithoutMovingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DoctorZombieEntity extends AdvancedZombieEntity {

  private static final int COOLDOWN_TICKS = 20 * 20;

  private int ticks;

  public DoctorZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(1, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 8.0F, 1.0F));
    this.goalSelector.addGoal(1,
        new ActionItemGoal(this, () -> this.ticks == 0, () -> this.ticks = COOLDOWN_TICKS));
  }

  @Override
  protected ItemStack getHeldStack() {
    return ModItemTags.SYRINGES.getRandomElement(this.random).getDefaultInstance();
  }

  @Override
  protected ItemStack getClothingStack() {
    return ModItems.DOCTOR_CLOTHING.get().getDefaultInstance();
  }

  @Override
  protected ItemStack getHatStack() {
    return ModItems.DOCTOR_MASK.get().getDefaultInstance();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.ticks > 0) {
      this.ticks--;
    }
  }
}
