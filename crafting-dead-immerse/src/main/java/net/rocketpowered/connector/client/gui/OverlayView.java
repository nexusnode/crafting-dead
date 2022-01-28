package net.rocketpowered.connector.client.gui;

import java.util.EnumMap;
import java.util.Map;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OverlayView extends ParentView<OverlayView, OverlayManager, YogaLayout> {

  private final Map<MinecraftProfileTexture.Type, ResourceLocation> textureLocations =
      new EnumMap<>(MinecraftProfileTexture.Type.class);


  private OverlayView(OverlayManager layout, LayoutParent<YogaLayout> layoutParent) {
    super(layout, layoutParent);
    this.minecraft.getSkinManager().registerSkins(this.minecraft.getUser().getGameProfile(),
        (type, textureLocation, texture) -> this.textureLocations.put(type, textureLocation), true);

    // this.setBackgroundColour(new Colour(0.25F, 0.25F, 0.25F, 0.6F));

  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    int opacity = Mth.ceil(this.getAlpha() * 255.0F) << 24;
    if ((opacity & 0xFC000000) != 0) {

      fill(matrixStack, 0, 0,
          (int) this.getScaledContentWidth(),
          (int) this.getScaledContentHeight(),
          0x505050 | (Mth.ceil(this.getAlpha() * 96.0F) << 24));

      this.fillGradient(matrixStack, 0, 0,
          (int) this.getScaledContentWidth(),
          (int) this.getScaledContentHeight(), 0,
          0x101010 | opacity);

      RenderSystem.setShaderTexture(0, new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/rocket.png"));

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
      RenderSystem.enableBlend();

      matrixStack.pushPose();
      {
        matrixStack.translate(this.getScaledContentWidth() / 2 - 150,
            this.getScaledContentHeight() - 40 - 20, 0.0D);

        matrixStack.scale(0.5F, 0.5F, 0.5F);
        RenderUtil.blit(matrixStack, 0, 0, 243, 80);
      }
      matrixStack.popPose();


      RenderUtil.blitAvatar(
          this.textureLocations.getOrDefault(MinecraftProfileTexture.Type.SKIN,
              DefaultPlayerSkin.getDefaultSkin(this.minecraft.getUser().getGameProfile().getId())),
          matrixStack, (int) this.getScaledContentWidth() / 2 + 48,
          (int) this.getScaledContentHeight() - 48, 16, 16);
      RenderSystem.disableBlend();


      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      this.minecraft.font.drawShadow(matrixStack, this.minecraft.getUser().getName(),
          this.getScaledContentWidth() / 2 + 20, this.getScaledContentHeight() - 48,
          0xFFFFFF | opacity);

      super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

    }

  }

  public static OverlayView create(OverlayManager layout) {
    LayoutParent<YogaLayout> layoutParent = new YogaLayoutParent();
    return new OverlayView(layout, layoutParent);
  }
}
