package com.craftingdead.mod.client.renderer;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.entity.EntityCDZombie;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCDZombie extends RenderBiped<EntityCDZombie> {

	public RenderCDZombie(RenderManager renderManager, ModelBiped modelBiped, float shadowSize) {
		super(renderManager, modelBiped, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCDZombie entity) {
		ResourceLocation texture = new ResourceLocation(CraftingDead.MOD_ID,
				"textures/mobs/zombies/zombie" + entity.getTextureNumber() + ".png");
		return texture;
	}
}