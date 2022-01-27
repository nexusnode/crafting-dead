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

package com.craftingdead.core.world.item.gun;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.AttachmentLike;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

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
    return (itemStack.getItem() instanceof AttachmentLike
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
