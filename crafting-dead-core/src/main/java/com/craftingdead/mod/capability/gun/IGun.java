package com.craftingdead.mod.capability.gun;

import java.util.Set;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.item.AttachmentItem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGun extends IAction, IAnimationController, INBTSerializable<CompoundNBT> {

  void tick(Entity entity, ItemStack itemStack);

  void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed, boolean sendUpdate);
  
  boolean isTriggerPressed();

  boolean isReloading();

  int getTotalReloadDurationTicks();

  int getReloadDurationTicks();

  void cancelActions(Entity entity, ItemStack itemStack);

  void reload(Entity entity, ItemStack itemStack, boolean sendUpdate);

  float getAccuracy(Entity entity, ItemStack itemStack);

  ItemStack getMagazineStack();

  void setMagazineStack(ItemStack stack);

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

  void toggleFireMode(Entity entity, boolean sendUpdate);

  boolean hasCrosshair();

  @Override
  default boolean isActive(ClientPlayerEntity playerEntity) {
    return this.isReloading();
  }

  @Override
  default ITextComponent getText(ClientPlayerEntity playerEntity) {
    return this.getMagazineStack().isEmpty() ? new TranslationTextComponent("action.reload")
        : new TranslationTextComponent("action.unload");
  }

  @Override
  default float getProgress(ClientPlayerEntity playerEntity) {
    return (float) (this.getTotalReloadDurationTicks() - this.getReloadDurationTicks())
        / this.getTotalReloadDurationTicks();
  }
}
