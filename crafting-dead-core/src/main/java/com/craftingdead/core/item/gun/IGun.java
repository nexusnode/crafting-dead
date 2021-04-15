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

package com.craftingdead.core.item.gun;

import java.util.Optional;
import java.util.Set;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.animation.IAnimationProvider;
import com.craftingdead.core.item.animation.gun.AnimationType;
import com.craftingdead.core.item.animation.gun.GunAnimation;
import com.craftingdead.core.item.animation.gun.GunAnimationController;
import com.craftingdead.core.item.combatslot.ICombatSlotProvider;
import com.craftingdead.core.item.gun.ammoprovider.IAmmoProvider;
import com.craftingdead.core.item.gun.paint.IPaint;
import com.craftingdead.core.living.ILiving;
import com.craftingdead.core.living.IPlayer;
import com.craftingdead.core.util.IBufferSerializable;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.LazyOptional;

public interface IGun extends ICombatSlotProvider, IBufferSerializable,
    IAnimationProvider<GunAnimationController> {

  void tick(ILiving<?, ?> living);

  void reset(ILiving<?, ?> living);

  void setTriggerPressed(ILiving<?, ?> living, boolean triggerPressed, boolean sendUpdate);

  boolean isTriggerPressed();

  void validatePendingHit(IPlayer<ServerPlayerEntity> player, ILiving<?, ?> hitLiving,
      PendingHit pendingHit);

  float getAccuracy(ILiving<?, ?> living);

  Set<AttachmentItem> getAttachments();

  default float getAttachmentMultiplier(AttachmentItem.MultiplierType multiplierType) {
    return this
        .getAttachments()
        .stream()
        .map(attachment -> attachment.getMultiplier(multiplierType))
        .reduce(1.0F, (x, y) -> x * y);
  }

  void setAttachments(Set<AttachmentItem> attachments);

  ItemStack getPaintStack();

  default LazyOptional<IPaint> getPaint() {
    return this.getPaintStack().getCapability(ModCapabilities.PAINT);
  }

  void setPaintStack(ItemStack paintStack);

  boolean isAcceptedPaintOrAttachment(ItemStack itemStack);

  void toggleFireMode(ILiving<?, ?> living, boolean sendUpdate);

  void setFireMode(ILiving<?, ?> living, FireMode fireMode, boolean sendUpdate);

  boolean hasCrosshair();

  boolean isPerformingRightMouseAction();

  void setPerformingRightMouseAction(ILiving<?, ?> living, boolean performingAction,
      boolean sendUpdate);

  RightMouseActionTriggerType getRightMouseActionTriggerType();

  Optional<SoundEvent> getReloadSound();

  int getReloadDurationTicks();

  boolean hasIronSight();

  int getShotCount();

  FireMode getFireMode();

  Optional<GunAnimation> getAnimation(AnimationType animationType);

  IGunClient getClient();

  IAmmoProvider getAmmoProvider();

  void setAmmoProvider(IAmmoProvider ammoProvider);

  Set<? extends Item> getAcceptedMagazines();

  ItemStack getDefaultMagazineStack();

  @Override
  default LazyOptional<GunAnimationController> getAnimationController() {
    return this.getClient() == null ? LazyOptional.empty()
        : LazyOptional.of(this.getClient()::getAnimationController);
  }

  public static enum RightMouseActionTriggerType {
    HOLD, CLICK;
  }
}
