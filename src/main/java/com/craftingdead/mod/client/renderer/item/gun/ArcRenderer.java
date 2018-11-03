package com.craftingdead.mod.client.renderer.item.gun;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.client.renderer.RenderHelper;
import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class ArcRenderer extends GunRenderer {

	public ArcRenderer(ClientProxy client) {
		super(client, new ResourceLocation(CraftingDead.MOD_ID, "item/gun/base/arc"));
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
			GlStateManager.scale(0.9, 0.9, 0.9);
			GlStateManager.translate(-0.3, 1, 0);
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.rotate(-40, 0, 0, 1);
			break;
		default:
			break;
		}
	}

}
