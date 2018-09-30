package com.craftingdead.mod.client.model.gun;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMuzzleFlash extends ModelBase {

	private ModelRenderer box0;
	private ModelRenderer box1;
	private ModelRenderer box2;

	public ModelMuzzleFlash() {
		textureWidth = 64;
		textureHeight = 64;

		box0 = new ModelRenderer(this, 1, 1);
		box0.addBox(0F, 0F, 0F, 8, 8, 0);
		box0.setRotationPoint(-4F, -4F, 0F);
		box0.setTextureSize(64, 32);
		box0.mirror = true;
		setRotation(box0, 0F, 0F, 0F);
		box1 = new ModelRenderer(this, 9, 1);
		box1.addBox(-4F, 0F, 0F, 8, 0, 15);
		box1.setRotationPoint(0F, 0F, -15F);
		box1.setTextureSize(64, 32);
		box1.mirror = true;
		setRotation(box1, 0F, 0F, -0.7853982F);
		box2 = new ModelRenderer(this, 1, 17);
		box2.addBox(-4F, 0F, 0F, 8, 0, 15);
		box2.setRotationPoint(0F, 0F, -15F);
		box2.setTextureSize(64, 32);
		box2.mirror = true;
		setRotation(box2, 0F, 0F, -2.373648F);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		box0.render(scale);
		box1.render(scale);
		box2.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
