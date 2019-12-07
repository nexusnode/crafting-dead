package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.function.Function;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.animation.IGunAnimation;
import com.craftingdead.mod.client.model.builtin.IItemRenderer;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;

@SuppressWarnings("deprecation")
public class GunRenderer implements IItemRenderer {

  private ItemCameraTransforms.TransformType cameraTransformType =
      ItemCameraTransforms.TransformType.NONE;

  private ResourceLocation mainModelLocation;
  private IBakedModel bakedMainModel;

  public GunRenderer(ResourceLocation mainModelLocation) {
    this.mainModelLocation = mainModelLocation;
  }

  @Override
  public void bakeModels(ModelBakery bakery,
      Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite,
      VertexFormat format) {
    IUnbakedModel model = ModelLoaderRegistry
        .getModelOrLogError(this.mainModelLocation, "An exception occurred while loading model");
    this.bakedMainModel = model.bake(bakery, spriteGetter, sprite, format);
  }

  @Override
  public void renderItem(ItemStack itemStack) {
    IBakedModel bakedModel = net.minecraftforge.client.ForgeHooksClient
        .handleCameraTransforms(this.bakedMainModel, this.cameraTransformType,
            this.cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND
                || this.cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND);
    Minecraft.getInstance().getItemRenderer().renderItem(itemStack, bakedModel);
  }

  @Override
  public Matrix4f handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
    this.cameraTransformType = cameraTransformType;

    Matrix4f matrix = new Matrix4f();
    matrix.setIdentity();
    matrix.setTranslation(new Vector3f(0.5F, 0.5F, 0.5F));

    Matrix4f transformation = new Matrix4f();
    transformation.setIdentity();

    switch (this.cameraTransformType) {
      case THIRD_PERSON_LEFT_HAND:
      case THIRD_PERSON_RIGHT_HAND:
      case FIRST_PERSON_LEFT_HAND:
      case FIRST_PERSON_RIGHT_HAND:
        IGunAnimation animation = ((ClientDist) CraftingDead.getInstance().getModDist())
            .getAnimationManager()
            .getCurrentAnimation(Minecraft.getInstance().player.getHeldItemMainhand());
        if (animation != null) {
          animation.apply(transformation, Minecraft.getInstance().getRenderPartialTicks());
        }
        break;
      default:
        break;
    }

    matrix.mul(transformation);
    return matrix;
  }

  @Override
  public Collection<ResourceLocation> getDependencies() {
    return ImmutableSet.of(this.mainModelLocation);
  }
}
