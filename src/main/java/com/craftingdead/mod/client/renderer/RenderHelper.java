package com.craftingdead.mod.client.renderer;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.util.PlayerResource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderHelper {

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

}
