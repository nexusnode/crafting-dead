package com.craftingdead.mod.capability.gun;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
import com.craftingdead.mod.item.PaintItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class DefaultGunController implements IGunController {

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}

  @Override
  public void tick(Entity entity, ItemStack itemStack) {}

  @Override
  public void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed) {}

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
  public void stopReloading() {}

  @Override
  public void startReloading(Entity entity, ItemStack itemStack) {}

  @Override
  public float getAccuracy(Entity entity, ItemStack itemStack) {
    return 0;
  }

  @Override
  public int getAmmo() {
    return 0;
  }

  @Override
  public void setAmmo(int ammo) {}

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
  public Optional<PaintItem> getPaint() {
    return Optional.empty();
  }

  @Override
  public void setPaint(Optional<PaintItem> paint) {}

  @Override
  public boolean isAcceptedPaintOrAttachment(ItemStack itemStack) {
    return false;
  }

  @Override
  public void toggleFireMode(Entity entity) {}
}
