package com.craftingdead.mod.client.renderer.item;

import java.util.function.Function;

import codechicken.lib.render.item.IItemRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class ItemRenderer implements IModel, IItemRenderer {

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		this.bakeModels(bakedTextureGetter);
		return this;
	}

	protected abstract void bakeModels(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter);

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public IModelState getTransforms() {
		return TRSRTransformation.identity();
	}

}