package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.entity.EntityCorpse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class RenderCorpse extends Render<EntityCorpse> {

	private ModelBiped mainModel;

	public RenderCorpse(RenderManager renderManager) {
		super(renderManager);
		this.mainModel = new ModelPlayer(0.0F, false);
	}
 
	@Override
	public void doRender(EntityCorpse entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.bindTexture(this.getEntityTexture(entity));

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 0.2F, z - 0.4F);
		double scale = 0.1D;
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
		float par7 = 0.625F;
		int limbCount = entity.getLimbCount();

		this.mainModel.setRotationAngles(0F, 0F, 0F, 0F, 0F, 0F, entity);
		this.mainModel.bipedHead.render(par7);
		this.mainModel.bipedBody.render(par7);

		if (limbCount > 3) {
			this.mainModel.bipedRightArm.render(par7);
		}

		if (limbCount > 2) {
			this.mainModel.bipedLeftArm.render(par7);
		}

		if (limbCount > 1) {
			this.mainModel.bipedRightLeg.render(par7);
		}

		if (limbCount > 0) {
			this.mainModel.bipedLeftLeg.render(par7);
		}

		this.mainModel.bipedHeadwear.render(par7);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCorpse entity) {
		NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getConnection()
				.getPlayerInfo(entity.getPlayerUUID());
		return networkPlayerInfo == null ? DefaultPlayerSkin.getDefaultSkin(entity.getPlayerUUID())
				: networkPlayerInfo.getLocationSkin();
	}

}
