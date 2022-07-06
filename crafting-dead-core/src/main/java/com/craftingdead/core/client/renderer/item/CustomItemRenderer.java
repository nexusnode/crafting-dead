/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.renderer.item;

import java.util.Collection;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Provides custom {@link Item} rendering abilities.
 */
public interface CustomItemRenderer {

  /**
   * Checks if this renderer should handle a specific item's render type.
   * 
   * @param itemStack - {@link ItemStack} being rendered
   * @param transformType - the perspective
   * @return true if this renderer should handle the given render type, otherwise false
   */
  boolean handlePerspective(ItemStack itemStack, ItemTransforms.TransformType transformType);

  /**
   * Called to do the actual rendering.
   * 
   * @param transformType - the perspective
   * @param itemStack - {@link ItemStack} being rendered
   */
  void render(ItemStack itemStack, ItemTransforms.TransformType transformType,
      @Nullable LivingExtension<?, ?> living, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay);

  void rotateCamera(ItemStack itemStack, LivingEntity livingEntity, float partialTicks,
      Vector3f rotations);

  /**
   * Get all models this {@link CustomItemRenderer} depends on. This is used to resolve and stitch
   * model texture dependencies as well as auto baking models upon resource reload.
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
  Collection<Material> getMaterials();

  /**
   * Clear any cached models this {@link CustomItemRenderer} may of stored.
   */
  void refreshCachedModels(Function<ModelLayerLocation, ModelPart> modelBaker);

  ItemRendererType<?, ?> getType();
}
