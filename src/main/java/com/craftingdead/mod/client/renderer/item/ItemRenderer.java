package com.craftingdead.mod.client.renderer.item;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

@SuppressWarnings("deprecation")
public abstract class ItemRenderer extends ItemStackTileEntityRenderer
    implements IUnbakedModel, IBakedModel {

  private ItemCameraTransforms.TransformType transformType;

  @Override
  public void renderByItem(ItemStack itemStack) {
    this.renderItem(itemStack, this.transformType);
  }

  protected abstract void renderItem(ItemStack stack,
      ItemCameraTransforms.TransformType transformType);

  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
      ItemCameraTransforms.TransformType cameraTransformType) {
    this.transformType = cameraTransformType;
    return ForgeHooksClient.handlePerspective(getBakedModel(), cameraTransformType);
  }

  @Override
  public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
    return Collections.emptyList();
  }

  @Override
  public boolean isAmbientOcclusion() {
    return false;
  }

  @Override
  public boolean isGui3d() {
    return false;
  }

  @Override
  public boolean isBuiltInRenderer() {
    return true;
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return null;
  }

  @Override
  public ItemOverrideList getOverrides() {
    return ItemOverrideList.EMPTY;
  }

  @Override
  public IBakedModel bake(ModelBakery bakery,
      Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite,
      VertexFormat format) {
    this.bakeModels(bakery, spriteGetter);
    return this;
  }

  protected abstract void bakeModels(ModelBakery bakery,
      Function<ResourceLocation, TextureAtlasSprite> spriteGetter);
}
