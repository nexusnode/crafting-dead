package com.craftingdead.core.capability.gun;

import java.util.Optional;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AimableGun extends DefaultGun implements IScope {

  public AimableGun(GunItem gunItem) {
    super(gunItem);
  }

  @Override
  public boolean isAiming(Entity entity, ItemStack itemStack) {
    return this.isPerformingRightMouseAction();
  }

  @Override
  public float getZoomMultiplier(Entity entity, ItemStack itemStack) {
    return this.hasIronSight() ? 2.0F
        : this.getAttachmentMultiplier(AttachmentItem.MultiplierType.ZOOM);
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity, ItemStack itemStack) {
    for (AttachmentItem attachmentItem : this.getAttachments()) {
      if (attachmentItem.isScope()) {
        return Optional.of(new ResourceLocation(attachmentItem.getRegistryName().getNamespace(),
            "textures/scope/" + attachmentItem.getRegistryName().getPath() + ".png"));
      }
    }
    return Optional.empty();
  }

  @Override
  public int getOverlayTextureWidth() {
    return 2048;
  }

  @Override
  public int getOverlayTextureHeight() {
    return 512;
  }
}
