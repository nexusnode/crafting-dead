package com.craftingdead.mod.client.renderer.item;

import com.craftingdead.mod.client.ClientProxy;

import codechicken.lib.render.item.IItemRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class GunRenderer implements IItemRenderer {

	protected final ClientProxy client;

	protected abstract IBakedModel getModel();

	protected abstract ResourceLocation getTexture();

	public GunRenderer(ClientProxy client) {
		this.client = client;
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
	public void renderItem(ItemStack itemStack, TransformType transformType) {
		// TODO: Render the item
	}

	@Override
	public IModelState getTransforms() {
		return TRSRTransformation.identity();
	}

}
