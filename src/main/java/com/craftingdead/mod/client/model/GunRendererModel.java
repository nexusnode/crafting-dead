package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.function.Function;

import com.craftingdead.mod.client.renderer.item.GunRenderer;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

public class GunRendererModel implements IModel {

	private final GunRenderer renderer;

	public GunRendererModel(GunRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return renderer;
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return renderer.getTextures();
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return renderer.getDependencies();
	}

}
