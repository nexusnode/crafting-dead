/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun;

import java.util.Optional;
import java.util.Set;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.ServerConfig;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class TypedGun extends AbstractGun {

  private final GunItem item;
  private GunConfiguration configuration;

  public TypedGun(ItemStack itemStack, GunItem item) {
    super(itemStack);
    this.item = item;
    this.initialize();
  }

  public GunItem getItem() {
    return this.item;
  }

  public GunConfiguration getConfiguration() {
    this.checkInitialized();
    return this.configuration;
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void initialize() {
    this.configuration = this.item.getConfiguration(
        CraftingDead.getInstance().getModDist().registryAccess());
    super.initialize();
  }

  @Override
  public boolean isAcceptedAttachment(ItemStack itemStack) {
    return ServerConfig.instance.scopeAttachmentsAllowed.get() &&
        (itemStack.getItem() instanceof AttachmentLike
            && this.item.getAcceptedAttachments()
                .contains(((AttachmentLike) itemStack.getItem()).asAttachment()));
  }

  @Override
  public SecondaryActionTrigger getSecondaryActionTrigger() {
    return this.configuration.getSecondaryActionTrigger();
  }

  @Override
  public Optional<SoundEvent> getReloadSound() {
    return this.configuration.getReloadSound();
  }

  @Override
  public int getReloadDurationTicks() {
    return this.configuration.getReloadDurationTicks();
  }

  @Override
  public Set<? extends Item> getAcceptedMagazines() {
    return this.item.getAcceptedMagazines();
  }

  @Override
  public ItemStack getDefaultMagazineStack() {
    return this.item.getDefaultMagazine().getDefaultInstance();
  }

  @Override
  public CombatSlot getCombatSlot() {
    return this.item.getCombatSlot();
  }

  @Override
  protected boolean canShoot(LivingExtension<?, ?> living) {
    return super.canShoot(living) && this.item.getTriggerPredicate().test(this);
  }

  @Override
  protected float getAccuracy() {
    return this.configuration.getAccuracyPercent();
  }

  @Override
  protected Set<FireMode> getFireModes() {
    return this.configuration.getFireModes();
  }

  @Override
  protected AmmoProvider createAmmoProvider() {
    return this.item.createAmmoProvider();
  }

  @Override
  protected double getRange() {
    return this.configuration.getRange();
  }

  @Override
  protected long getFireDelayMs() {
    return this.configuration.getFireDelayMs();
  }

  @Override
  protected int getRoundsPerShot() {
    return this.configuration.getRoundsPerShot();
  }

  @Override
  protected float getDamage() {
    return this.configuration.getDamage();
  }
}
