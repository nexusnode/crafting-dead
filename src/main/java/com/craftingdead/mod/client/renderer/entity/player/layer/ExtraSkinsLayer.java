package com.craftingdead.mod.client.renderer.entity.player.layer;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.client.renderer.entity.player.ExtraSkinProviders;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;

/**
 * A layer that renders every provider registered under {@link ExtraSkinProviders}.
 */
public class ExtraSkinsLayer
    extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

  private final ExtraSkinProviders extraSkinProviders;

  public ExtraSkinsLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer,
      ExtraSkinProviders extraSkinProviders) {
    super(renderer);
    this.extraSkinProviders = extraSkinProviders;
  }

  @Override
  public void render(MatrixStack stack, IRenderTypeBuffer buffers,
      int somethingThatSeemsToBeLightLevel, AbstractClientPlayerEntity playerEntity,
      float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_,
      float p_225628_10_) {
    playerEntity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> {

      String skinType = playerEntity.getSkinType();
      this.extraSkinProviders.getAvailableExtraSkins(player, skinType).forEach(skinResource -> {

        // Forces to be shown, ignoring the player's skin options.
        this.getEntityModel().setVisible(true);

        // Renders the player's body again, but using the clothing texture instead.
        LayerRenderer.renderModel(this.getEntityModel(), skinResource, stack, buffers,
            somethingThatSeemsToBeLightLevel, playerEntity, 1F, 1F, 1F);

      });
    });
  }
}
