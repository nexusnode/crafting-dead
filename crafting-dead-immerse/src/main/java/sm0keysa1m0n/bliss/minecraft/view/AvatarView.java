package sm0keysa1m0n.bliss.minecraft.view;

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.view.View;

public class AvatarView extends View {

  private ResourceLocation textureLocation;

  @SuppressWarnings("removal")
  public AvatarView(Properties properties, GameProfile gameProfile) {
    super(properties);
    this.textureLocation = DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
    this.minecraft.getSkinManager().registerSkins(gameProfile,
        (type, textureLocation, texture) -> {
          if (type == MinecraftProfileTexture.Type.SKIN) {
            this.textureLocation = textureLocation;
          }
        }, true);
  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTick) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTick);
    this.graphicsContext.exitManaged();
    {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
      RenderUtil.blitAvatar(this.textureLocation, matrixStack,
          this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledContentWidth(), this.getScaledContentHeight());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    this.graphicsContext.enterManaged();
  }
}
