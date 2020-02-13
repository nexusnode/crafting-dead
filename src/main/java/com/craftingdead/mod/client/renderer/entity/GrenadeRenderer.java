package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.renderer.entity.model.ModelGrenade;
import com.craftingdead.mod.entity.GrenadeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class GrenadeRenderer  extends EntityRenderer<GrenadeEntity>{

	private ModelGrenade model = new ModelGrenade();
	
	
	public GrenadeRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		
		
	}
	
	@Override
	public void render(GrenadeEntity entity, float entityYaw, float partialTicks,
		      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {
	
		matrixStack.scale(0.4f, 0.4f, 0.4f);
		
		IVertexBuilder vertexBuilder =
		        renderTypeBuffer.getBuffer(model.getLayer(this.getEntityTexture(entity)));
		model.render(matrixStack, vertexBuilder, p_225623_6_, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F,
	            1.0F, 0.15F);
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
