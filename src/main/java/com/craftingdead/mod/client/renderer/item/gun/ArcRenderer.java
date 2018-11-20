package com.craftingdead.mod.client.renderer.item.gun;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.client.renderer.RenderHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class ArcRenderer extends GunRenderer {

	public ArcRenderer(ClientMod client) {
		super(new ResourceLocation(CraftingDead.MOD_ID, "item/gun/base/arc"));
	}

	@Override
	protected void applyGunTransformations(TransformType transformType) {
		EnumHandSide handSide = RenderHelper.getHandSide(transformType);
		GlStateManager.translate(1, 0, 0);
		GlStateManager.scale(0.5, 0.5, 0.5);

		switch (transformType) {
		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
			GlStateManager.translate(-0.3, 0.5, -0.15);
			switch (handSide) {
			case RIGHT:
				// Backward translation
				GlStateManager.translate(-0.2, 0, 0.5);
				// Left/Right rotation
				GlStateManager.rotate(-90, 0, 1, 0);
				break;
			case LEFT:
				// Left/Right rotation
				GlStateManager.rotate(280, 0, 1, 0);
				break;
			}
			break;
		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
			GlStateManager.scale(2, 2, 2);
			switch (handSide) {
			case RIGHT:
				// Backward translation
				GlStateManager.translate(0, 0, 0.15);
				// Left/Right rotation
				GlStateManager.rotate(-100, 0, 1, 0);
				break;
			case LEFT:
				// Left/Right rotation
				GlStateManager.rotate(280, 0, 1, 0);
				break;
			}
			break;
		case GUI:

			GlStateManager.translate(-0.1, 0.95, 0);
			GlStateManager.scale(0.98, 0.98, 0.98);
			GlStateManager.rotate(41F, 0, 0, 1);
			GlStateManager.rotate(180, 0, 1, 0);

			break;
		default:
			break;
		}
	}

}
