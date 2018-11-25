package com.craftingdead.mod.client.renderer.item.gun;

import java.util.Collection;
import java.util.function.Function;

import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.client.renderer.Graphics;
import com.craftingdead.mod.client.renderer.item.ItemRenderer;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public abstract class GunRenderer extends ItemRenderer {

	private ClientMod client;
	
	private final ResourceLocation baseModelLocation;

	private IBakedModel baseModelBaked;

	public GunRenderer(ClientMod client, ResourceLocation baseModelLocation) {
		this.client = client;
		this.baseModelLocation = baseModelLocation;
	}

	@Override
	public void bakeModels(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		IModel model = ModelLoaderRegistry.getModelOrMissing(baseModelLocation);
		this.baseModelBaked = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, bakedTextureGetter);
	}

	@Override
	public void renderItem(ItemStack itemStack, TransformType transformType) {
		GlStateManager.pushMatrix();
		this.applyGunTransformations(transformType);
		Graphics.renderModel(baseModelBaked, DefaultVertexFormats.ITEM);
		GlStateManager.popMatrix();

		switch (transformType) {
		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
			EnumHandSide handSide = Graphics.getHandSide(transformType);
			GlStateManager.pushMatrix();
			this.applyHandTranslations(handSide);
			this.renderArm(handSide);
			GlStateManager.popMatrix();
			break;
		default:
			break;
		}
	}

	protected void applyHandTranslations(EnumHandSide handSide) {
		GlStateManager.translate(0, 0.65, 0.5);
		switch (handSide) {
		case LEFT:
			GlStateManager.translate(1, 0, 0);
			// Left/Right rotation
			GlStateManager.rotate(145, 0, 1, 0);
			// Up/Down rotation
			GlStateManager.rotate(-70, 0, 0, 1);
			break;
		case RIGHT:
			// Left/Right rotation
			GlStateManager.rotate(-145, 0, 1, 0);
			// Up/Down rotation
			GlStateManager.rotate(70, 0, 0, 1);
			break;
		}
	}

	protected abstract void applyGunTransformations(TransformType transformType);

	private void renderArm(EnumHandSide handSide) {
		AbstractClientPlayer player = this.client.getPlayer().getEntity();
		this.client.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());

		RenderPlayer renderplayer = (RenderPlayer) this.client.getMinecraft().getRenderManager()
				.<AbstractClientPlayer>getEntityRenderObject(player);

		switch (handSide) {
		case RIGHT:
			renderplayer.renderRightArm(player);
			break;
		case LEFT:
			renderplayer.renderLeftArm(player);
			break;
		}
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(this.baseModelLocation);
	}

}
