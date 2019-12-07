package com.craftingdead.mod.client.model.builtin;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

@SuppressWarnings("deprecation")
public class BakedBuiltinModel implements IBakedModel {

  private final IItemRenderer itemRenderer;

  public BakedBuiltinModel(IItemRenderer itemRenderer) {
    this.itemRenderer = itemRenderer;
  }

  public IItemRenderer getItemRenderer() {
    return this.itemRenderer;
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
  public Pair<IBakedModel, Matrix4f> handlePerspective(
      ItemCameraTransforms.TransformType cameraTransformType) {
    return Pair.of(this, this.itemRenderer.handlePerspective(cameraTransformType));
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
    return Minecraft.getInstance().getTextureMap().getSprite(null);
  }

  @Override
  public ItemOverrideList getOverrides() {
    return ItemOverrideList.EMPTY;
  }
}
