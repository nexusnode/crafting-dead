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

package com.craftingdead.survival.world.entity.monster;

import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.world.entity.ai.goal.ActionItemGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DoctorZombieEntity extends AdvancedZombie {

  private static final int COOLDOWN_TICKS = 20 * 20;

  private int ticks;

  public DoctorZombieEntity(EntityType<? extends AdvancedZombie> type, Level world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(1, new InteractGoal(this, Player.class, 8.0F, 1.0F));
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
