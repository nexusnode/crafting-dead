package com.craftingdead.mod.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGrenade extends Model {

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape5;
    ModelRenderer Shape4;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;

    public ModelGrenade() {
    	 super(RenderType::getEntityCutoutNoCull);
        textureWidth = 64;
        textureHeight = 32;

        Shape1 = new ModelRenderer(this, 17, 16);
        Shape1.addCuboid(0F, 0F, 0F, 3, 6, 3);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 32);
        Shape2 = new ModelRenderer(this, 17, 26);
        Shape2.addCuboid(0F, 0F, 0F, 4, 2, 4);
        Shape2.setRotationPoint(-0.5F, 6F, -0.5F);
        Shape2.setTextureSize(64, 32);
        Shape3 = new ModelRenderer(this, 17, 9);
        Shape3.addCuboid(0F, 0F, 0F, 4, 2, 4);
        Shape3.setRotationPoint(-0.5F, -2F, -0.5F);
        Shape3.setTextureSize(64, 32);
        Shape5 = new ModelRenderer(this, 17, 3);
        Shape5.addCuboid(0F, 0F, 0F, 2, 3, 2);
        Shape5.setRotationPoint(0.5F, -5F, 0.5F);
        Shape5.setTextureSize(64, 32);
        Shape4 = new ModelRenderer(this, 26, 4);
        Shape4.addCuboid(0F, 0F, 0F, 1, 2, 2);
        Shape4.setRotationPoint(2.5F, -5F, 0.5F);
        Shape4.setTextureSize(64, 32);
        Shape6 = new ModelRenderer(this, 36, 9);
        Shape6.addCuboid(0F, 0F, 0F, 4, 1, 2);
        Shape6.setRotationPoint(3.5F, -5F, 0.5F);
        Shape6.setTextureSize(64, 32);
        Shape7 = new ModelRenderer(this, 36, 13);
        Shape7.addCuboid(0F, 0F, 0F, 1, 5, 2);
        Shape7.setRotationPoint(4.45F, -1.5F, 0.5F);
        Shape7.setTextureSize(64, 32);
        Shape8 = new ModelRenderer(this, 36, 21);
        Shape8.addCuboid(0F, 0F, 0F, 1, 1, 2);
        Shape8.setRotationPoint(4F, 2.5F, 0.5F);
        Shape8.setTextureSize(64, 32);
        Shape9 = new ModelRenderer(this, 10, 3);
        Shape9.addCuboid(0F, 0F, 0F, 3, 3, 0);
        Shape9.setRotationPoint(0F, -5F, 0.5F);
        Shape9.setTextureSize(64, 32);
    }



	@Override
	public void render(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_,
			float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
		Shape1.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape2.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape3.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape5.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape4.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape6.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape7.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape8.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
        Shape9.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
	            p_225598_7_, p_225598_8_);
		
	}
}
