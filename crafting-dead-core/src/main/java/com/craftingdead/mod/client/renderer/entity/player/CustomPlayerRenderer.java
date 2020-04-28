package com.craftingdead.mod.client.renderer.entity.player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.item.ClothingItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomPlayerRenderer extends PlayerRenderer {

  public CustomPlayerRenderer(EntityRendererManager renderManager, boolean isSlim) {
    super(renderManager, isSlim);
  }

  @Override
  public void renderRightArm(MatrixStack matrix, IRenderTypeBuffer buffer, int p_229144_3_,
      AbstractClientPlayerEntity playerEntity) {

    // Normal render
    super.renderRightArm(matrix, buffer, p_229144_3_, playerEntity);

    // Render again, but using the skins
    this
        .renderArmsWithExtraSkins(matrix, buffer, p_229144_3_, playerEntity,
            this.entityModel.bipedRightArm, this.entityModel.bipedRightArmwear);
  }

  @Override
  public void renderLeftArm(MatrixStack matrix, IRenderTypeBuffer buffer, int p_229146_3_,
      AbstractClientPlayerEntity playerEntity) {

    // Normal render
    super.renderLeftArm(matrix, buffer, p_229146_3_, playerEntity);

    // Render again, but using the skins
    this
        .renderArmsWithExtraSkins(matrix, buffer, p_229146_3_, playerEntity,
            this.entityModel.bipedLeftArm, this.entityModel.bipedLeftArmwear);
  }

  private void renderArmsWithExtraSkins(MatrixStack matrix, IRenderTypeBuffer buffer,
      int p_229144_3_, AbstractClientPlayerEntity playerEntity, ModelRenderer firstLayerModel,
      ModelRenderer secondLayerModel) {
    playerEntity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> {
      String skinType = playerEntity.getSkinType();
      ItemStack clothingStack =
          player.getInventory().getStackInSlot(InventorySlotType.CLOTHING.getIndex());
      if (clothingStack.getItem() instanceof ClothingItem) {
        ClothingItem clothingItem = (ClothingItem) clothingStack.getItem();
        ResourceLocation clothingSkin = clothingItem.getClothingSkin(skinType);

        firstLayerModel.showModel = true;
        secondLayerModel.showModel = true;

        firstLayerModel
            .render(matrix, buffer.getBuffer(RenderType.getEntityTranslucent(clothingSkin)),
                p_229144_3_, OverlayTexture.DEFAULT_UV);
        secondLayerModel
            .render(matrix, buffer.getBuffer(RenderType.getEntityTranslucent(clothingSkin)),
                p_229144_3_, OverlayTexture.DEFAULT_UV);
      }
    });
  }

  public static void inject() throws Exception {
    EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();

    /* Detection of the target fields at EntityRendererManager class */

    Field fallbackPlayerRendererField = Arrays
        .stream(EntityRendererManager.class.getDeclaredFields())
        .filter(f -> f.getType() == PlayerRenderer.class)
        .findFirst()
        .orElseThrow(
            () -> new NoSuchElementException("PlayerRenderer fallback field was not found"));

    Field playerSkinsMapField =
        Arrays.stream(EntityRendererManager.class.getDeclaredFields()).filter(f -> {
          if (f.getType() == Map.class) {
            try {
              f.setAccessible(true);
              Map<?, ?> map = (Map<?, ?>) f.get(rendererManager);
              return map.containsKey("default") && map.containsKey("slim");
            } catch (Exception e) {
              throw new RuntimeException("Exception during a Reflection operation", e);
            }
          }
          return false;
        })
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("playerSkins Map field was not found"));

    /* Instantiation of our renderers */

    CustomPlayerRenderer defaultRenderer = new CustomPlayerRenderer(rendererManager, false);
    CustomPlayerRenderer slimRenderer = new CustomPlayerRenderer(rendererManager, true);

    /* Injection of our renderers */

    fallbackPlayerRendererField.setAccessible(true);
    fallbackPlayerRendererField.set(rendererManager, defaultRenderer);

    @SuppressWarnings("unchecked")
    Map<String, PlayerRenderer> playerSkinRendererMap =
        (Map<String, PlayerRenderer>) playerSkinsMapField.get(rendererManager);

    playerSkinRendererMap.put("default", defaultRenderer);
    playerSkinRendererMap.put("slim", slimRenderer);
  }
}
