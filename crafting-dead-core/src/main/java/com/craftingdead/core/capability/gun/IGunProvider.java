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

package com.craftingdead.core.capability.gun;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimation;
import com.craftingdead.core.capability.gun.IGun.RightMouseActionTriggerType;
import com.craftingdead.core.inventory.CombatSlotType;
import com.craftingdead.core.item.FireMode;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public interface IGunProvider {

  Set<FireMode> getFireModes();

  double getRange();

  Predicate<IGun> getTriggerPredicate();

  int getBulletAmountToFire();

  int getFireDelayMs();

  float getDamage();

  float getAccuracyPct();

  Set<? extends Item> getAcceptedMagazines();

  Set<? extends Item> getAcceptedAttachments();

  Set<? extends Item> getAcceptedPaints();

  boolean hasCrosshair();

  RightMouseActionTriggerType getRightMouseActionTriggerType();

  Supplier<SoundEvent> getShootSound();

  Optional<SoundEvent> getDistantShootSound();

  Optional<SoundEvent> getSilencedShootSound();

  Optional<SoundEvent> getReloadSound();

  int getReloadDurationTicks();

  Map<AnimationType, Supplier<GunAnimation>> getAnimations();

  CombatSlotType getCombatSlotType();

  Supplier<? extends Item> getDefaultMagazine();

  boolean hasBoltAction();

  Supplier<SoundEvent> getRightMouseActionSound();

  long getRightMouseActionSoundRepeatDelayMs();


}
