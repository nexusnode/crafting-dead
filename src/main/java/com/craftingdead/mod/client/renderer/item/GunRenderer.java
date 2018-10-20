package com.craftingdead.mod.client.renderer.item;

import java.util.Collection;
import java.util.List;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.client.model.ModelHandle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import codechicken.lib.render.item.IItemRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class GunRenderer implements IItemRenderer {

	protected final ClientProxy client;

	private final ModelHandle modelHandle;

	public GunRenderer(ClientProxy client, ModelHandle modelHandle) {
		this.client = client;
		this.modelHandle = modelHandle;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public void renderItem(ItemStack itemStack, TransformType transformType) {
		TextureManager textureManager = client.getMinecraft().getTextureManager();
		EnumHandSide hand = (transformType == TransformType.FIRST_PERSON_LEFT_HAND
				|| transformType == TransformType.THIRD_PERSON_LEFT_HAND) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;

		GlStateManager.pushMatrix();
		textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);

		int handTranslation = hand == EnumHandSide.RIGHT ? 1 : -1;

		GlStateManager.translate((handTranslation * 0.5F) + 0.5, 0, 0);

		ForgeHooksClient.handleCameraTransforms(modelHandle.get(), transformType, hand == EnumHandSide.LEFT);

		modelHandle.render();
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.disableBlend();
		textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.9, 0, -0.6);
		GlStateManager.rotate(-20, 0, 1, 0);
		this.renderArmFirstPerson(0, 0, hand);
		GlStateManager.popMatrix();

	}

	private void renderArmFirstPerson(float equipProgress, float swing, EnumHandSide handSide) {
		boolean flag = handSide != EnumHandSide.LEFT;
		float f = flag ? 1.0F : -1.0F;

		GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
		AbstractClientPlayer abstractclientplayer = this.client.getPlayer().getVanillaEntity();
		this.client.getMinecraft().getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());

		GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
		GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);

		RenderPlayer renderplayer = (RenderPlayer) this.client.getMinecraft().getRenderManager()
				.<AbstractClientPlayer>getEntityRenderObject(abstractclientplayer);
		GlStateManager.disableCull();

		if (flag) {
			renderplayer.renderRightArm(abstractclientplayer);
		} else {
			renderplayer.renderLeftArm(abstractclientplayer);
		}

		GlStateManager.enableCull();
	}

	@Override
	public IModelState getTransforms() {
		return TRSRTransformation.identity();
	}

	public List<ResourceLocation> getTextures() {
		List<ResourceLocation> textures = Lists.newArrayList();
		textures.addAll(ModelLoaderRegistry.getModelOrMissing(this.modelHandle.getModel()).getTextures());
		return textures;
	}

	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(modelHandle.getModel());
	}

}
