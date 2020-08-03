package com.craftingdead.core.client.renderer.item;

import java.util.Collection;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Provides custom {@link Item} rendering abilities through the use of a {@link IRendererProvider}.
 */
public interface IItemRenderer {

  /**
   * Checks if this renderer should handle a specific item's render type.
   * 
   * @param itemStack - {@link ItemStack} being rendered
   * @param transformType - the perspective
   * @return true if this renderer should handle the given render type, otherwise false
   */
  boolean handleRenderType(ItemStack itemStack, ItemCameraTransforms.TransformType transformType);

  /**
   * Called to do the actual rendering.
   * 
   * @param transformType - the perspective
   * @param itemStack - {@link ItemStack} being rendered
   */
  void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType,
      LivingEntity livingEntity, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay);

  /**
   * Get all models this {@link IItemRenderer} depends on. This is used to resolve and stitch model
   * texture dependencies as well as auto baking models upon resource reload.
   * 
   * @return a {@link Collection} of model locations.
   */
  Collection<ResourceLocation> getModelDependencies();

  /**
   * Get any additional textures that need to be stitched to the block texture atlas. Use this if
   * you want to re-bake models with different textures.
   * 
   * @return a {@link Collection} of texture locations.
   */
  Collection<ResourceLocation> getAdditionalModelTextures();

  /**
   * Clear any cached models this {@link IItemRenderer} may of stored.
   */
  void refreshCachedModels();
}
