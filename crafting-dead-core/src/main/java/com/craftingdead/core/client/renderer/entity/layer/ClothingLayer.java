package com.craftingdead.core.client.renderer.entity.layer;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
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
      int packedLight, T livingEntity, float p_225628_5_, float p_225628_6_,
      float partialTicks, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
    if (!livingEntity.isInvisible()) {
      livingEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
        String skinType = livingEntity instanceof ClientPlayerEntity
            ? ((ClientPlayerEntity) livingEntity).getSkinType()
            : "default";
        ItemStack clothingStack =
            living.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex());
        clothingStack
            .getCapability(ModCapabilities.CLOTHING)
            .ifPresent(clothing -> LayerRenderer
                .renderCutoutModel(this.getEntityModel(), clothing.getTexture(skinType), stack,
                    buffers,
                    packedLight, livingEntity, 1F, 1F, 1F));
      });
    }
  }
}
