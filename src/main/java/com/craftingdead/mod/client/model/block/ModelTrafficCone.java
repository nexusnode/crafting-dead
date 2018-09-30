package com.craftingdead.mod.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTrafficCone extends ModelBase {

	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;

	public ModelTrafficCone() {
		textureWidth = 256;
		textureHeight = 128;

		Shape1 = new ModelRenderer(this, 200, 0);
		Shape1.addBox(0F, 0F, 0F, 11, 1, 11);
		Shape1.setRotationPoint(-5.5F, 23F, -5.5F);
		Shape1.setTextureSize(256, 128);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 165, 0);
		Shape2.addBox(0F, 0F, 0F, 7, 8, 7);
		Shape2.setRotationPoint(-3.5F, 15.8F, -3.5F);
		Shape2.setTextureSize(256, 128);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 20);
		Shape3.addBox(0F, 0F, 0F, 5, 7, 5);
		Shape3.setRotationPoint(-2.5F, 9.5F, -2.5F);
		Shape3.setTextureSize(256, 128);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 140, 0);
		Shape4.addBox(0F, 0F, 0F, 3, 5, 3);
		Shape4.setRotationPoint(-1.5F, 4F, -1.5F);
		Shape4.setTextureSize(256, 128);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 110, 0);
		Shape5.addBox(0F, 0F, 0F, 5, 1, 5);
		Shape5.setRotationPoint(-2.5F, 8.5F, -2.5F);
		Shape5.setTextureSize(256, 128);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
