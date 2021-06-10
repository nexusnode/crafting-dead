package com.craftingdead.core.world.gun.type;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.AbstractGun;
import com.craftingdead.core.world.gun.FireMode;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.AttachmentLike;
import com.craftingdead.core.world.item.combatslot.CombatSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class TypedGun<T extends AbstractGunType> extends AbstractGun {

  private final T type;

  public <SELF extends TypedGun<T>> TypedGun(
      Function<SELF, ? extends TypedGunClient<? super SELF>> clientFactory,
      ItemStack itemStack, T type) {
    super(clientFactory, itemStack, type.getFireModes(), type.createAmmoProvider());
    this.type = type;
  }

  public T getType() {
    return this.type;
  }

  @Override
  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return (itemStack.getItem() instanceof AttachmentLike
        && this.type.getAcceptedAttachments()
            .contains(((AttachmentLike) itemStack.getItem()).asAttachment()))
        || this.type.getAcceptedPaints().contains(itemStack.getItem());
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
  public CombatSlotType getSlotType() {
    return this.type.getCombatSlotType();
  }

  @Override
  protected boolean canShoot(LivingExtension<?, ?> living) {
    return super.canShoot(living) && this.type.getTriggerPredicate().test(this);
  }

  @Override
  public float getAccuracy(LivingExtension<?, ?> living) {
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
