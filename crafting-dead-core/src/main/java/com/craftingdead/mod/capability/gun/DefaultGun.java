package com.craftingdead.mod.capability.gun;

import java.util.HashSet;
import java.util.Set;
import com.craftingdead.mod.capability.animation.DefaultAnimationController;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class DefaultGun extends DefaultAnimationController implements IGun {

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}

  @Override
  public void tick(Entity entity, ItemStack itemStack) {}

  @Override
  public void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed,
      boolean sendUpdate) {}

  @Override
  public boolean isTriggerPressed() {
    return false;
  }

  @Override
  public boolean isReloading() {
    return false;
  }

  @Override
  public int getTotalReloadDurationTicks() {
    return 0;
  }

  @Override
  public int getReloadDurationTicks() {
    return 0;
  }

  @Override
  public void cancelActions(Entity entity, ItemStack itemStack) {}

  @Override
  public void reload(Entity entity, ItemStack itemStack, boolean sendUpdate) {}

  @Override
  public float getAccuracy(Entity entity, ItemStack itemStack) {
    return 0;
  }

  @Override
  public ItemStack getMagazineStack() {
    return ItemStack.EMPTY;
  }

  @Override
  public int getMagazineSize() {
    return 0;
  }

  @Override
  public void setMagazineSize(int ammo) {}

  @Override
  public Set<AttachmentItem> getAttachments() {
    return new HashSet<>();
  }

  @Override
  public float getAttachmentMultiplier(MultiplierType type) {
    return 1.0F;
  }

  @Override
  public void setAttachments(Set<AttachmentItem> attachments) {}

  @Override
  public ItemStack getPaintStack() {
    return ItemStack.EMPTY;
  }

  @Override
  public void setPaintStack(ItemStack paintStack) {}

  @Override
  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return false;
  }

  @Override
  public void toggleFireMode(Entity entity, boolean sendUpdate) {}

  @Override
  public void setMagazineStack(ItemStack stack) {}

  @Override
  public boolean hasCrosshair() {
    return false;
  }
}
