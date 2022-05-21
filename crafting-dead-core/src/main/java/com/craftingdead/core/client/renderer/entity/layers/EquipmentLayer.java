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

package com.craftingdead.core.client.renderer.entity.layers;

import java.util.Objects;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Layer that renders {@link IEquipableModel}s attached to a player's body.
 */
public class EquipmentLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel>
    extends RenderLayer<T, M> {

  private final ModEquipmentSlot slot;

  /**
   * Whether this model should be rotated when the player is crouching.
   */
  private final boolean useCrouchOrientation;

  /**
   * Whether this model should be rotated accordingly to the player's head.
   */
  private final boolean useHeadOrientation;

  /**
   * Optional arbitrary transformation right before rendering the {@link ItemStack}.
   */
  @Nullable
  private final Consumer<PoseStack> transformation;

  private EquipmentLayer(Builder<T, M> builder) {
    super(builder.renderer);
    this.slot = builder.slot;
    this.useCrouchOrientation = builder.useCrouchOrientation;
    this.transformation = builder.tranformation;
    this.useHeadOrientation = builder.useHeadOrientation;
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource bufferSource,
      int packedLight, T livingEntity, float limbSwing, float limbSwingAmount,
      float partialTicks, float ageTicks, float headYaw, float headPitch) {
    final var minecraft = Minecraft.getInstance();
    final var invisible = livingEntity.isInvisible();
    final var partiallyVisible =
        livingEntity.isInvisible() && !livingEntity.isInvisibleTo(minecraft.player);
    if (partiallyVisible || !invisible) {

      final var itemRenderer = minecraft.getItemRenderer();

      livingEntity.getCapability(LivingExtension.CAPABILITY).ifPresent(living -> {

        final var itemStack = living.getItemHandler().getStackInSlot(this.slot.getIndex());

        if (!itemStack.isEmpty()) {
          var bakedModel = itemRenderer.getModel(itemStack, livingEntity.level, livingEntity, 0);

          poseStack.pushPose();

          // Applies crouching rotation is needed
          if (this.useCrouchOrientation && livingEntity.isCrouching()) {
            RenderUtil.applyPlayerCrouchRotation(poseStack);
          }

          // Applies the head orientation if needed
          if (this.useHeadOrientation) {
            // Vanilla's transformation for child entities, like baby zombies
            if (livingEntity.isBaby()) {
              poseStack.translate(0.0D, 0.03125D, 0.0D);
              poseStack.scale(0.7F, 0.7F, 0.7F);
              poseStack.translate(0.0D, 1.0D, 0.0D);
            }

            this.getParentModel().getHead().translateAndRotate(poseStack);
          }

          // Applies the arbitrary transformation if needed
          if (this.transformation != null) {
            this.transformation.accept(poseStack);
          }

          // Renders the item. Also note the TransformType.
          itemRenderer.render(itemStack, ItemTransforms.TransformType.HEAD, false,
              poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedModel);

          poseStack.popPose();
        }
      });
    }
  }

  public static <T extends LivingEntity, M extends EntityModel<T> & HeadedModel> Builder<T, M> builder(
      LivingEntityRenderer<T, M> renderer) {
    return new Builder<>(renderer);
  }

  public static class Builder<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> {

    private final LivingEntityRenderer<T, M> renderer;
    private ModEquipmentSlot slot;
    @Nullable
    private Consumer<PoseStack> tranformation;
    private boolean useCrouchOrientation;
    private boolean useHeadOrientation;

    private Builder(LivingEntityRenderer<T, M> renderer) {
      this.renderer = renderer;
    }

    public Builder<T, M> slot(ModEquipmentSlot slot) {
      this.slot = slot;
      return this;
    }

    public Builder<T, M> transformation(Consumer<PoseStack> transformation) {
      this.tranformation = transformation;
      return this;
    }

    public Builder<T, M> useCrouchOrientation(boolean useCrouchOrientation) {
      this.useCrouchOrientation = useCrouchOrientation;
      return this;
    }

    public Builder<T, M> useHeadOrientation(boolean useHeadOrientation) {
      this.useHeadOrientation = useHeadOrientation;
      return this;
    }

    public EquipmentLayer<T, M> build() {
      Objects.requireNonNull(this.renderer, "The renderer must not be null");
      Objects.requireNonNull(this.slot, "The slot must not be null");
      return new EquipmentLayer<>(this);
    }
  }
}
