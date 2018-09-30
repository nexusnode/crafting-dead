package com.craftingdead.mod.client.gui;

import org.lwjgl.util.glu.Project;

import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiMainMenu extends GuiScreen {

	/**
	 * Timer used to rotate the panorama, increases every tick.
	 */
	private float panoramaTimer;
	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;

	private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[] {
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/title/background/panorama_0.png"),
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/title/background/panorama_1.png"),
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/title/background/panorama_2.png"),
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/title/background/panorama_3.png"),
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/title/background/panorama_4.png"),
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/title/background/panorama_5.png") };

	private ResourceLocation backgroundTexture;

	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background",
				this.viewportTexture);
		this.addButton(new GuiButtonStyled(0, this.width / 2 - 50, this.height / 2 - 25, "Play"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.panoramaTimer += partialTicks;
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int mouseX, int mouseY, float partialTicks) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);

		for (int j = 0; j < 64; ++j) {
			GlStateManager.pushMatrix();
			float f = ((float) (j % 8) / 8.0F - 0.5F) / 64.0F;
			float f1 = ((float) (j / 8) / 8.0F - 0.5F) / 64.0F;
			GlStateManager.translate(f, f1, 0.0F);
			GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-this.panoramaTimer * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k) {
				GlStateManager.pushMatrix();

				if (k == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[k]);
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				bufferbuilder.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				bufferbuilder.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				bufferbuilder.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox() {
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GlStateManager.glTexParameteri(3553, 10241, 9729);
		GlStateManager.glTexParameteri(3553, 10240, 9729);
		GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();

		for (int j = 0; j < 3; ++j) {
			float f = 1.0F / (float) (j + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (float) (j - 1) / 256.0F;
			bufferbuilder.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			bufferbuilder.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			bufferbuilder.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
			bufferbuilder.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D)
					.color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int mouseX, int mouseY, float partialTicks) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(mouseX, mouseY, partialTicks);
		this.rotateAndBlurSkybox();
		this.rotateAndBlurSkybox();
		this.rotateAndBlurSkybox();
		this.rotateAndBlurSkybox();
		this.rotateAndBlurSkybox();
		this.rotateAndBlurSkybox();
		this.rotateAndBlurSkybox();
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = 120.0F / (float) (this.width > this.height ? this.width : this.height);
		float f1 = (float) this.height * f / 256.0F;
		float f2 = (float) this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		bufferbuilder.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		bufferbuilder.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		bufferbuilder.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2))
				.color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

}
