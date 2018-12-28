package com.craftingdead.mod.client.renderer.item;

import java.util.Collection;
import java.util.function.Function;

import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class GunRenderer extends ItemRenderer {

	private ClientDist client;

	private final ResourceLocation baseModelLocation;

	private IBakedModel baseModelBaked;

	public GunRenderer(ClientDist client, ResourceLocation baseModelLocation) {
		this.client = client;
		this.baseModelLocation = baseModelLocation;
	}

	@Override
	public void bakeModels(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		IModel model = ModelLoaderRegistry.getModelOrMissing(this.baseModelLocation);
		this.baseModelBaked = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, bakedTextureGetter);
	}

	@Override
	public void renderItem(ItemStack itemStack, TransformType transformType) {
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.pushMatrix();
		{
			switch (transformType) {
			case THIRD_PERSON_LEFT_HAND:
			case THIRD_PERSON_RIGHT_HAND:
			case FIRST_PERSON_LEFT_HAND:
			case FIRST_PERSON_RIGHT_HAND:
				GunAnimation animation = this.client.getAnimationManager().getCurrentAnimation(itemStack);
				if (animation != null)
					animation.render(itemStack, this.client.getMinecraft().getRenderPartialTicks());
				break;
			default:
				break;
			}
			IBakedModel bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
					this.baseModelBaked, transformType, transformType == TransformType.FIRST_PERSON_LEFT_HAND
							|| transformType == TransformType.THIRD_PERSON_LEFT_HAND);
			this.client.getMinecraft().getRenderItem().renderItem(itemStack, bakedModel);
		}
		GlStateManager.popMatrix();
	}

//	protected void applyHandTranslations(EnumHandSide handSide) {
//		GlStateManager.translate(-0.5, 0.15, 0);
//		switch (handSide) {
//		case LEFT:
//			GlStateManager.translate(1, 0, 0);
//			// Left/Right rotation
//			GlStateManager.rotate(145, 0, 1, 0);
//			// Up/Down rotation
//			GlStateManager.rotate(-70, 0, 0, 1);
//			break;
//		case RIGHT:
//			// Left/Right rotation
//			GlStateManager.rotate(-145, 0, 1, 0);
//			// Up/Down rotation
//			GlStateManager.rotate(70, 0, 0, 1);
//			break;
//		}
//	}

//	private void renderArm(EnumHandSide handSide) {
//		AbstractClientPlayer player = this.client.getPlayer().getEntity();
//		this.client.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());
//
//		RenderPlayer renderplayer = (RenderPlayer) this.client.getMinecraft().getRenderManager()
//				.<AbstractClientPlayer>getEntityRenderObject(player);
//
//		switch (handSide) {
//		case RIGHT:
//			renderplayer.renderRightArm(player);
//			break;
//		case LEFT:
//			renderplayer.renderLeftArm(player);
//			break;
//		}
//	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(this.baseModelLocation);
	}

}
