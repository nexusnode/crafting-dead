package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.renderer.entity.model.ModelGrenade;
import com.craftingdead.mod.entity.GrenadeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class GrenadeRenderer  extends EntityRenderer<GrenadeEntity>{

	private ModelGrenade model = new ModelGrenade();
	
	
	public GrenadeRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		
		
	}
	
	@Override
	public void render(GrenadeEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_,
			IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
		//model.render(p_225623_4_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		super.render(entity, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
	}

	@Override
	public ResourceLocation getEntityTexture(GrenadeEntity entity) {
		switch(entity.getGraGrenadeType()) {
		case DECOY:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadedecoy.png");
		case FIRE:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadefire.png");
		case FLASH:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadeflash.png");
		case GAS:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadegas.png");
		case PIPEBOMB:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadepipe.png");
		case SMOKE:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadesmoke.png");
		default:
			return new ResourceLocation(CraftingDead.ID, "textures/entity/grenadefire.png");
		
		}
		
	}

}
