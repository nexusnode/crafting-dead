package com.craftingdead.core.capability.gun;

import java.util.Optional;
import java.util.Set;
import com.craftingdead.core.capability.animationprovider.IAnimationProvider;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimation;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimationController;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.item.AttachmentItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGun
    extends IAnimationProvider<GunAnimationController>, INBTSerializable<CompoundNBT> {

  void tick(ILiving<?> living, ItemStack itemStack);

  void setTriggerPressed(ILiving<?> living, ItemStack itemStack, boolean triggerPressed,
      boolean sendUpdate);

  boolean isTriggerPressed();

  void reload(ILiving<?> living);

  void removeMagazine(ILiving<?> living);

  float getAccuracy(ILiving<?> living, ItemStack itemStack);

  ItemStack getMagazineStack();

  void setMagazineStack(ItemStack magazineStack);

  int getMagazineSize();

  void setMagazineSize(int size);

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

  void setPaintStack(ItemStack paintStack);

  boolean isAcceptedPaintOrAttachment(ItemStack itemStack);

  void toggleFireMode(ILiving<?> living, boolean sendUpdate);

  boolean hasCrosshair();

  boolean isPerformingRightMouseAction();

  void toggleRightMouseAction(ILiving<?> living, boolean sendUpdate);

  RightMouseActionTriggerType getRightMouseActionTriggerType();

  Set<? extends Item> getAcceptedMagazines();

  Optional<SoundEvent> getReloadSound();

  int getReloadDurationTicks();

  boolean hasIronSight();

  Optional<GunAnimation> getAnimation(AnimationType animationType);

  int getShotCount();

  public static enum RightMouseActionTriggerType {
    HOLD, CLICK;
  }
}
