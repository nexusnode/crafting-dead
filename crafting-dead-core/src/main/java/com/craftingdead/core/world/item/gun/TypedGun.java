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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TypedGun<T extends GunItem> extends AbstractGun {

  private final T type;

  public static <T extends GunItem> TypedGun<T> create(
      Function<TypedGun<T>, ? extends TypedGunClient<? super TypedGun<T>>> clientFactory,
      ItemStack itemStack, T type) {
    TypedGun<T> gun = new TypedGun<>(clientFactory, itemStack, type);
    gun.initialize();
    return gun;
  }

  protected <SELF extends TypedGun<T>> TypedGun(
      Function<SELF, ? extends TypedGunClient<? super SELF>> clientFactory,
      ItemStack itemStack, T type) {
    super(clientFactory, itemStack, type.getFireModes());
    this.type = type;
  }

  public T getType() {
    return this.type;
  }

  @Override
  public boolean isAcceptedAttachment(ItemStack itemStack) {
    return CraftingDead.serverConfig.scopeAttachmentsAllowed.get() &&
        (itemStack.getItem() instanceof AttachmentLike
        && this.type.getAcceptedAttachments()
            .contains(((AttachmentLike) itemStack.getItem()).asAttachment()));
  }

  @Override
  public SecondaryActionTrigger getSecondaryActionTrigger() {
    return this.type.getSecondaryActionTrigger();
  }

  @Override
  public Optional<SoundEvent> getReloadSound() {
    return this.type.getReloadSound();
  }

  @Override
  public int getReloadDurationTicks() {
    return this.type.getReloadDurationTicks();
  }

  @Override
  public Set<? extends Item> getAcceptedMagazines() {
    return this.type.getAcceptedMagazines();
  }

  @Override
  public ItemStack getDefaultMagazineStack() {
    return this.type.getDefaultMagazine().getDefaultInstance();
  }

  @Override
  public CombatSlot getCombatSlot() {
    return this.type.getCombatSlot();
  }

  @Override
  protected boolean canShoot(LivingExtension<?, ?> living) {
    return super.canShoot(living) && this.type.getTriggerPredicate().test(this);
  }

  @Override
  public float getAccuracy(LivingExtension<?, ?> living, Random random) {
    float accuracy = this.type.getAccuracyPct()
        * this.getAttachmentMultiplier(Attachment.MultiplierType.ACCURACY);
    return Math.min(living.getModifiedAccuracy(accuracy, random), 1.0F);
  }

  @Override
  protected Set<FireMode> getFireModes() {
    return this.type.getFireModes();
  }

  @Override
  protected AmmoProvider createAmmoProvider() {
    return this.type.createAmmoProvider();
  }

  @Override
  protected double getRange() {
    return this.type.getRange();
  }

  @Override
  protected long getFireDelayMs() {
    return this.type.getFireDelayMs();
  }

  @Override
  protected int getRoundsPerShot() {
    return this.type.getRoundsPerShot();
  }

  @Override
  protected float getDamage() {
    return this.type.getDamage();
  }
}
