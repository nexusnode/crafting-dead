/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.core.network.Synched;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.Attachment.MultiplierType;
import com.craftingdead.core.world.item.gun.skin.Skin;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public interface Gun extends CombatSlotProvider, Synched {

  Capability<Gun> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

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
  void validatePendingHit(PlayerExtension<ServerPlayer> player,
      LivingExtension<?, ?> hitLiving, PendingHit pendingHit);

  /**
   * Get current accuracy percentage of the gun.
   * 
   * @param living - the entity holding the gun
   * @param random - a {@link Random} instance
   * @return the accuracy percentage
   */
  float getAccuracy(LivingExtension<?, ?> living, Random random);

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

  @Nullable
  Skin getSkin();

  void setSkin(@Nullable Holder<Skin> skin);

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
  boolean isAcceptedAttachment(ItemStack itemStack);

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
