package com.craftingdead.mod.client.renderer.item;

import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;

@SuppressWarnings("deprecation")
public class GunRenderer extends ItemRenderer {

  private ClientDist client;

  private final ResourceLocation baseModelLocation;

  private IBakedModel baseModelBaked;

  public GunRenderer(ClientDist client, ResourceLocation baseModelLocation) {
    this.client = client;
    this.baseModelLocation = baseModelLocation;
  }

  @Override
  public void bakeModels(ModelBakery bakery,
      Function<ResourceLocation, TextureAtlasSprite> spriteGetter) {
    IUnbakedModel model = ModelLoaderRegistry.getModelOrMissing(this.baseModelLocation);
    this.baseModelBaked =
        model.bake(bakery, spriteGetter, ModelRotation.X0_Y0, DefaultVertexFormats.ITEM);
  }

  @Override
  public void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
    GlStateManager.translatef(0.5F, 0.5F, 0.5F);
    GlStateManager.pushMatrix();
    {
      switch (transformType) {
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          GunAnimation animation = this.client.getAnimationManager().getCurrentAnimation(itemStack);
          if (animation != null) {
            animation.render(itemStack, Minecraft.getInstance().getRenderPartialTicks());
          }
          break;
        default:
          break;
      }
      IBakedModel bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
          this.baseModelBaked, transformType,
          transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND
              || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
      Minecraft.getInstance().getItemRenderer().renderItem(itemStack, bakedModel);
    }
    GlStateManager.popMatrix();
  }

  // protected void applyHandTranslations(EnumHandSide handSide) {
  // GlStateManager.translate(-0.5, 0.15, 0);
  // switch (handSide) {
  // case LEFT:
  // GlStateManager.translate(1, 0, 0);
  // // Left/Right rotation
  // GlStateManager.rotate(145, 0, 1, 0);
  // // Up/Down rotation
  // GlStateManager.rotate(-70, 0, 0, 1);
  // break;
  // case RIGHT:
  // // Left/Right rotation
  // GlStateManager.rotate(-145, 0, 1, 0);
  // // Up/Down rotation
  // GlStateManager.rotate(70, 0, 0, 1);
  // break;
  // }
  // }

  // private void renderArm(EnumHandSide handSide) {
  // AbstractClientPlayer player = this.client.getPlayer().getEntity();
  // this.client.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());
  //
  // RenderPlayer renderplayer = (RenderPlayer) this.client.getMinecraft().getRenderManager()
  // .<AbstractClientPlayer>getEntityRenderObject(player);
  //
  // switch (handSide) {
  // case RIGHT:
  // renderplayer.renderRightArm(player);
  // break;
  // case LEFT:
  // renderplayer.renderLeftArm(player);
  // break;
  // }
  // }

  @Override
  public Collection<ResourceLocation> getDependencies() {
    return ImmutableList.of(this.baseModelLocation);
  }

  @Override
  public Collection<ResourceLocation> getTextures(
      Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors) {
    return ImmutableList.of();
  }
}
