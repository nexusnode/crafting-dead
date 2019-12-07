package com.craftingdead.mod.client.model.builtin;

import java.util.Collection;
import java.util.function.Function;
import javax.vecmath.Matrix4f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("deprecation")
public interface IItemRenderer {

  void bakeModels(ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter,
      ISprite sprite, VertexFormat format);

  void renderItem(ItemStack itemStack);

  Matrix4f handlePerspective(ItemCameraTransforms.TransformType cameraTransformType);

  Collection<ResourceLocation> getDependencies();
}
