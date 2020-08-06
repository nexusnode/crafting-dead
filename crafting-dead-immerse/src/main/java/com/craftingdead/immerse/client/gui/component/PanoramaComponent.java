package com.craftingdead.immerse.client.gui.component;

import java.util.Objects;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.util.ResourceLocation;

public class PanoramaComponent extends Component<PanoramaComponent> {

  private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES =
      new ResourceLocation("textures/gui/title/background/panorama_overlay.png");

  private final RenderSkybox panorama;

  private final ResourceLocation panoramaTexture;

  public PanoramaComponent(ResourceLocation panoramaTexture) {
    Objects.requireNonNull(panoramaTexture, "Panorama texture cannot be null");
    this.panoramaTexture = panoramaTexture;
    this.panorama = new RenderSkybox(new RenderSkyboxCube(this.panoramaTexture));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.panorama.render(partialTicks, 1.0F);
    RenderUtil.bind(PANORAMA_OVERLAY_TEXTURES);
    RenderSystem.enableBlend();
    RenderSystem
        .blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderUtil
        .blit(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.0F, 0.0F, 16, 128, 16,
            128);
    RenderSystem.disableBlend();
  }
}
