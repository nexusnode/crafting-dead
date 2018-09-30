package com.craftingdead.mod.client.renderer;

import java.util.List;
import java.util.UUID;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.util.PlayerResource;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;

public class RenderHelper extends net.minecraft.client.renderer.RenderHelper {

	public static void drawRectangle(double x, double y, double width, double height, int color, float alpha,
			boolean shadow) {
		if (shadow)
			drawRectangle(x - 1, y - 1, width + 2, height + 2, color, alpha * 0.3F, false);
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, alpha);
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) x, (double) y + height, 0.0D).endVertex();
		bufferbuilder.pos((double) x + width, (double) y + height, 0.0D).endVertex();
		bufferbuilder.pos((double) x + width, (double) y, 0.0D).endVertex();
		bufferbuilder.pos((double) x, (double) y, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawGradientRect(double x, double y, double width, double height, int startColor, int endColor,
			float alpha) {
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) x, (double) y + height, 0.0D).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) x + width, (double) y + height, 0.0D).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double) x + width, (double) y, 0.0D).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double) x, (double) y, 0.0D).color(f1, f2, f3, f).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawImage(double x, double y, ResourceLocation image, double width, double height, float alpha) {
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, 0.0D).tex(0.0D, 1.0D).endVertex();
		bufferbuilder.pos(x + width, y + height, 0.0D).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos(x + width, y, 0.0D).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos(x, y, 0.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
	}

	public static void drawTextScaled(String text, double x, double y, int color, boolean dropShadow, double scale) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glScaled(scale, scale, scale);
		Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, color, dropShadow);
		GL11.glPopMatrix();
	}

	public static ResourceLocation getPlayerAvatar(UUID playerUUID) {
		ResourceLocation resourceLocation = new ResourceLocation(CraftingDead.MOD_ID,
				"textures/avatars/" + playerUUID + ".png");
		ITextureObject object = Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation);
		if (object == null) {
			ThreadDownloadImageData imageData = new ThreadDownloadImageData(null,
					PlayerResource.AVATAR_URL.getUrl(playerUUID),
					new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/avatar.png"), null);
			Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, imageData);
		}
		return resourceLocation;
	}

	public static void drawItemStack(ItemStack stack, int x, int y) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
		itemRender.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = Minecraft.getMinecraft().fontRenderer;
		itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
		itemRender.zLevel = 0.0F;
	}

	public static void drawCenteredString(FontRenderer fontRenderer, String string, int x, int y, int color) {
		fontRenderer.drawStringWithShadow(string, (float) (x - fontRenderer.getStringWidth(string) / 2), (float) y,
				color);
	}

	public static void drawRightAlignedString(FontRenderer fontRenderer, String text, int x, int y, boolean dropShadow,
			int color) {
		fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text), y, color, dropShadow);
	}

	public static boolean isInBox(int x, int y, int width, int height, int checkX, int checkY) {
		return (checkX >= x) && (checkY >= y) && (checkX <= x + width) && (checkY <= y + height);
	}

	public static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform) {
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
		final IVertexConsumer consumer = new VertexTransformer(builder) {
			@Override
			public void put(int element, float... data) {
				VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
				switch (formatElement.getUsage()) {
				case POSITION: {
					float[] newData = new float[4];
					Vector4f vec = new Vector4f(data);
					transform.getMatrix().transform(vec);
					vec.get(newData);
					parent.put(element, newData);
					break;
				}
				default: {
					parent.put(element, data);
					break;
				}
				}
			}
		};
		quad.pipe(consumer);
		return builder.build();
	}

	private static void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack,
			ItemColors colors) {
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				k = colors.colorMultiplier(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	public static void renderModel(IBakedModel model, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, ItemStack.EMPTY,
					Minecraft.getMinecraft().getItemColors());
		}

		renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, ItemStack.EMPTY,
				Minecraft.getMinecraft().getItemColors());
		tessellator.draw();
	}

	public static void renderSuffocationOverlay(TextureAtlasSprite sprite) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
		GlStateManager.pushMatrix();
		float f6 = sprite.getMinU();
		float f7 = sprite.getMaxU();
		float f8 = sprite.getMinV();
		float f9 = sprite.getMaxV();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-1.0D, -1.0D, -0.5D).tex((double) f7, (double) f9).endVertex();
		bufferbuilder.pos(1.0D, -1.0D, -0.5D).tex((double) f6, (double) f9).endVertex();
		bufferbuilder.pos(1.0D, 1.0D, -0.5D).tex((double) f6, (double) f8).endVertex();
		bufferbuilder.pos(-1.0D, 1.0D, -0.5D).tex((double) f7, (double) f8).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
