package com.craftingdead.mod.client.model.builtin;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class BuiltinModel implements IUnbakedModel {

  private final IItemRenderer itemRenderer;

  public BuiltinModel(IItemRenderer itemRenderer) {
    this.itemRenderer = itemRenderer;
  }

  @Override
  public IBakedModel bake(ModelBakery bakery,
      Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite,
      VertexFormat format) {
    this.itemRenderer.bakeModels(bakery, spriteGetter, sprite, format);
    return new BakedBuiltinModel(this.itemRenderer);
  }

  @Override
  public Collection<ResourceLocation> getDependencies() {
    return this.itemRenderer.getDependencies();
  }

  @Override
  public Collection<ResourceLocation> getTextures(
      Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors) {
    Set<ResourceLocation> textures =
        this.itemRenderer.getDependencies().stream().map(modelGetter::apply).flatMap((model) -> {
          return model.getTextures(modelGetter, missingTextureErrors).stream();
        }).collect(Collectors.toSet());
    return textures;
  }
}
