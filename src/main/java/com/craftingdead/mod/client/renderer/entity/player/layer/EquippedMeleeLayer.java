package com.craftingdead.mod.client.renderer.entity.player.layer;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.client.model.IEquipableModel;
import com.craftingdead.mod.client.util.RenderUtil;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Layer that renders melee items in a player's body.
 * As melees are not {@link IEquipableModel}s, they must be rendered this way.
 */
@SuppressWarnings("deprecation")
public class EquippedMeleeLayer
    extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

  public EquippedMeleeLayer(
      IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> entityRenderer) {
    super(entityRenderer);
  }

  @Override
  public void render(MatrixStack matrix, IRenderTypeBuffer buffers,
      int somethingThatSeemsToBeLightLevel, AbstractClientPlayerEntity playerEntity,
      float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_,
      float p_225628_10_) {

    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    playerEntity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> {

      ItemStack stackOnBack =
          player.getInventory().getStackInSlot(InventorySlotType.MELEE.getIndex());
      if (!stackOnBack.isEmpty()) {
        IBakedModel itemModel = itemRenderer.getItemModelWithOverrides(stackOnBack,
            playerEntity.world, (LivingEntity) null);

        matrix.push();

        // Applies crouching rotation if needed
        if (playerEntity.isCrouching()) {
          RenderUtil.applyPlayerCrouchRotation(matrix);
        }

        // Default item translation and rotation
        matrix.translate(0.325F, 0.1F, 0.15F);
        matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
        matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(10F));

        // Renders the item. Also note the TransformType.
        itemRenderer.renderItem(stackOnBack, TransformType.THIRD_PERSON_RIGHT_HAND, false, matrix,
            buffers, somethingThatSeemsToBeLightLevel, OverlayTexture.DEFAULT_UV, itemModel);

        matrix.pop();
      }
    });
  }
}
