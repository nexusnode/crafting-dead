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

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.gun.ammoprovider.RefillableAmmoProvider;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GiantZombieEntity extends AdvancedZombieEntity {

  public GiantZombieEntity(EntityType<? extends AdvancedZombieEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected ItemStack getHeldStack() {
    ItemStack gunStack = ModItems.M4A1.get().getDefaultInstance();
    gunStack.getCapability(Capabilities.GUN).ifPresent(gun -> gun.setAmmoProvider(
        new RefillableAmmoProvider(ModItems.RPK_MAGAZINE.get().getDefaultInstance(), 0, true)));
    return gunStack;
  }

  @Override
  protected ItemStack getClothingStack() {
    return ModItems.ARMY_CLOTHING.get().getDefaultInstance();
  }

  @Override
  protected ItemStack getHatStack() {
    return ModItems.ARMY_HELMET.get().getDefaultInstance();
  }

  public static AttributeModifierMap.MutableAttribute registerAttributes() {
    return AdvancedZombieEntity.registerAttributes()
        .add(Attributes.MAX_HEALTH, 100.0D)
        .add(Attributes.ATTACK_DAMAGE, 50.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.5D);
  }

  @Override
  protected float getStandingEyeHeight(Pose pose, EntitySize entitySize) {
    return 10.440001F;
  }
}
