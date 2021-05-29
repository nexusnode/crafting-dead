package com.craftingdead.core.client.renderer.entity.layers;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.model.ParachuteModel;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class ParachuteLayer<T extends LivingEntity, M extends EntityModel<T>>
    extends LayerRenderer<T, M> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/entity/parachute.png");
  private final ParachuteModel model = new ParachuteModel();

  public ParachuteLayer(IEntityRenderer<T, M> entityRenderer) {
    super(entityRenderer);
  }

  @Override
  public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageTicks,
      float headYaw, float headPitch) {
    if (livingEntity.hasEffect(ModMobEffects.PARACHUTE.get())) {
      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, 0.0D, 0.125D);
        this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageTicks, headYaw,
            headPitch);
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer,
            RenderType.armorCutoutNoCull(TEXTURE), false, false);
        this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight,
            OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      }
      matrixStack.popPose();
    }
  }
}
