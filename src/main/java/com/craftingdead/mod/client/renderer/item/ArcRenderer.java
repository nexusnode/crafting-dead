package com.craftingdead.mod.client.renderer.item;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ArcRenderer extends GunRenderer {

	public ArcRenderer(ClientProxy client) {
		super(client);
	}

	@Override
	protected IBakedModel getModel() {
		IModel model = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(CraftingDead.MOD_ID, "item/arc_base"));
		return model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
	}

	@Override
	protected ResourceLocation getTexture() {
		return new ResourceLocation(CraftingDead.MOD_ID, "textures/items/arc.png");
	}

}
