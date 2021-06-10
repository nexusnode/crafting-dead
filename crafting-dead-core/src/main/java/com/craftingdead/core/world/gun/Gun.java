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
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.Attachment.MultiplierType;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public interface Gun extends CombatSlotProvider, BufferSerializable {

  /**
   * Ticked when held by the passed in {@link LivingExtension}.
   * 
   * @param living - the entity holding the gun
   */
  void tick(LivingExtension<?, ?> living);

  /**
   * Reset any actions currently being performed (e.g. secondary action or trigger).
   * 
   * @param living - the entity holding the gun
   */
  void reset(LivingExtension<?, ?> living);

  /**
   * Set the trigger pressed state.
   * 
   * @param living - the entity holding the gun
   * @param triggerPressed - whether the trigger is pressed
   * @param sendUpdate - send an update to the opposing logical side (client/server)
   */
  void setTriggerPressed(LivingExtension<?, ?> living, boolean triggerPressed, boolean sendUpdate);

  /**
   * Whether the trigger is currently pressed.
   * 
   * @return <code>true</code> if pressed, <code>false</code> otherwise.
   */
  boolean isTriggerPressed();

  /**
   * Handles pending hit messages sent by the client.
   * 
   * @param player - the player who sent the message
   * @param hitLiving - the entity hit
   * @param pendingHit - hit data
   */
  void validatePendingHit(PlayerExtension<ServerPlayerEntity> player,
      LivingExtension<?, ?> hitLiving, PendingHit pendingHit);

  /**
   * Get current accuracy percentage of the gun.
   * 
   * @param living - the entity holding the gun
   * @return the accuracy percentage
   */
  float getAccuracy(LivingExtension<?, ?> living);

  /**
   * Get current attachments on the gun.
   * 
   * @return the attachments
   */
  Set<Attachment> getAttachments();

  /**
   * Get the combined attachment multiplier of the specified {@link MultiplierType}.
   * 
   * @param multiplierType - the type of multiplier
   * @return the combined multiplier
   */
  default float getAttachmentMultiplier(Attachment.MultiplierType multiplierType) {
    return this.getAttachments().stream()
        .map(attachment -> attachment.getMultiplier(multiplierType))
        .reduce(1.0F, (x, y) -> x * y);
  }

  /**
   * Set the attachments on the gun.
   * 
   * @param attachments - the attachments to set
   */
  void setAttachments(Set<Attachment> attachments);

  /**
   * If the gun has an iron sight (no over-barrel attachments).
   * 
   * @return <code>true</code> if the gun has an iron sight, <code>false</code> otherwise
   */
  default boolean hasIronSight() {
    for (Attachment attachmentItem : this.getAttachments()) {
      if (attachmentItem.getInventorySlot() == GunCraftSlotType.OVERBARREL_ATTACHMENT) {
        return false;
      }
    }
    return true;
  }

  /**
   * Get the {@link ItemStack} used to paint the gun with.
   * 
   * @return the {@link ItemStack}
   */
  ItemStack getPaintStack();

  /**
   * Paint the gun with the specified {@link ItemStack}.
   * 
   * @param paintStack - the paint {@link ItemStack}
   */
  void setPaintStack(ItemStack paintStack);

  /**
   * Get an optional skin to be used for this gun.
   * 
   * @return the optional skin name
   */
  default Optional<ResourceLocation> getSkinName() {
    return this.getPaintStack().isEmpty()
        ? Optional.empty()
        : Optional.of(this.getPaintStack().getItem().getRegistryName());
  }

  /**
   * Check if the specified {@link ItemStack} is an acceptable paint or attachment for this gun.
   * 
   * @param itemStack - the {@link ItemStack} to check
   * @return <code>true</code> if accepted, <code>false</code> otherwise
   */
  boolean isAcceptedPaintOrAttachment(ItemStack itemStack);

  /**
   * Get the currently selected {@link FireMode}.
   * 
   * @return the {@link FireMode}
   */
  FireMode getFireMode();

  /**
   * Set the current {@link FireMode}.
   * 
   * @param living - the entity holding the gun
   * @param fireMode - the selected {@link FireMode}
   * @param sendUpdate - send an update to the opposing logical side (client/server)
   */
  void setFireMode(LivingExtension<?, ?> living, FireMode fireMode, boolean sendUpdate);

  /**
   * Toggle the next {@link FireMode}.
   * 
   * @param living - the entity holding the gun
   * @param sendUpdate - send an update to the opposing logical side (client/server)
   */
  void toggleFireMode(LivingExtension<?, ?> living, boolean sendUpdate);

  /**
   * If the secondary action of this gun is being performed (e.g. aiming).
   * 
   * @return <code>true</code> if being performed, otherwise <code>false</code>
   */
  boolean isPerformingSecondaryAction();

  /**
   * Set the secondary action performing state.
   * 
   * @param living - the entity holding the gun
   * @param performingAction - whether to perform the action or not
   * @param sendUpdate - send an update to the opposing logical side (client/server)
   */
  void setPerformingSecondaryAction(LivingExtension<?, ?> living, boolean performingAction,
      boolean sendUpdate);

  /**
   * Get the type of trigger for the secondary action.
   * 
   * @return the {@link SecondaryActionTrigger}
   */
  SecondaryActionTrigger getSecondaryActionTrigger();

  /**
   * Get the sound to be played when reloading this gun.
   * 
   * @return an optional {@link SoundEvent}
   */
  Optional<SoundEvent> getReloadSound();

  /**
   * Get the reload duration in ticks of this gun.
   * 
   * @return the duration in ticks
   */
  int getReloadDurationTicks();

  /**
   * Get the {@link GunClient} associated with this gun. Will be null on dedicated server. <br>
   * <b>Always check <u>logical</u> side before accessing.
   * 
   * @return
   */
  GunClient getClient();

  AmmoProvider getAmmoProvider();

  void setAmmoProvider(AmmoProvider ammoProvider);

  Set<? extends Item> getAcceptedMagazines();

  ItemStack getDefaultMagazineStack();

  ItemStack getItemStack();

  enum SecondaryActionTrigger {
    HOLD, TOGGLE;
  }
}
