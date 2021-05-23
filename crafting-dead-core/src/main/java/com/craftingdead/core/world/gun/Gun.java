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

package com.craftingdead.core.world.gun;

import java.util.Optional;
import java.util.Set;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.client.animation.AnimationProvider;
import com.craftingdead.core.client.animation.gun.AnimationType;
import com.craftingdead.core.client.animation.gun.GunAnimation;
import com.craftingdead.core.client.animation.gun.GunAnimationController;
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.paint.Paint;
import com.craftingdead.core.world.item.AttachmentItem;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.LazyOptional;

public interface Gun extends CombatSlotProvider, BufferSerializable,
    AnimationProvider<GunAnimationController> {

  void tick(LivingExtension<?, ?> living);

  void reset(LivingExtension<?, ?> living);

  void setTriggerPressed(LivingExtension<?, ?> living, boolean triggerPressed, boolean sendUpdate);

  boolean isTriggerPressed();

  void validatePendingHit(PlayerExtension<ServerPlayerEntity> player, LivingExtension<?, ?> hitLiving,
      PendingHit pendingHit);

  float getAccuracy(LivingExtension<?, ?> living);

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

  default LazyOptional<Paint> getPaint() {
    return this.getPaintStack().getCapability(ModCapabilities.PAINT);
  }

  void setPaintStack(ItemStack paintStack);

  boolean isAcceptedPaintOrAttachment(ItemStack itemStack);

  void toggleFireMode(LivingExtension<?, ?> living, boolean sendUpdate);

  void setFireMode(LivingExtension<?, ?> living, FireMode fireMode, boolean sendUpdate);

  boolean hasCrosshair();

  boolean isPerformingRightMouseAction();

  void setPerformingRightMouseAction(LivingExtension<?, ?> living, boolean performingAction,
      boolean sendUpdate);

  RightMouseActionTriggerType getRightMouseActionTriggerType();

  Optional<SoundEvent> getReloadSound();

  int getReloadDurationTicks();

  boolean hasIronSight();

  int getShotCount();

  FireMode getFireMode();

  Optional<GunAnimation> getAnimation(AnimationType animationType);

  GunClient getClient();

  AmmoProvider getAmmoProvider();

  void setAmmoProvider(AmmoProvider ammoProvider);

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
