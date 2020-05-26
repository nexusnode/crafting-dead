package com.craftingdead.mod.client.renderer.entity.layer;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.item.ClothingItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ClothingLayer<T extends LivingEntity, M extends BipedModel<T>>
    extends LayerRenderer<T, M> {

  public ClothingLayer(IEntityRenderer<T, M> renderer) {
    super(renderer);
  }

  @Override
  public void render(MatrixStack stack, IRenderTypeBuffer buffers,
      int somethingThatSeemsToBeLightLevel, T livingEntity, float p_225628_5_, float p_225628_6_,
      float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
    livingEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      String skinType = livingEntity instanceof ClientPlayerEntity
          ? ((ClientPlayerEntity) livingEntity).getSkinType()
          : "default";
      ItemStack clothingStack = living.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
      if (clothingStack.getItem() instanceof ClothingItem && !livingEntity.isInvisible()) {
        ClothingItem clothingItem = (ClothingItem) clothingStack.getItem();
        LayerRenderer
            .renderModel(this.getEntityModel(), clothingItem.getClothingSkin(skinType), stack,
                buffers, somethingThatSeemsToBeLightLevel, livingEntity, 1F, 1F, 1F);
      }
    });
  }
}
