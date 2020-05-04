package com.craftingdead.mod.capability.gun;

import java.util.HashSet;
import java.util.Set;
import com.craftingdead.mod.capability.animation.IAnimation;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

@SuppressWarnings("deprecation")
public class DefaultGun implements IGun {

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
  public void cancelActions() {}

  @Override
  public void reload(Entity entity, ItemStack itemStack) {}

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
  public void toggleFireMode(Entity entity) {}

  @Override
  public void setMagazineStack(ItemStack stack) {}

  @Override
  public boolean hasCrosshair() {
    return false;
  }

  @Override
  public void addAnimation(IAnimation animation) {}

  @Override
  public void cancelCurrentAnimation() {}

  @Override
  public void clearAnimations() {}

  @Override
  public void apply(MatrixStack matrixStack) {}

  @Override
  public boolean isAcceptedPerspective(ItemCameraTransforms.TransformType cameraTransformType) {
    return false;
  }
}
