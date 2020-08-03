package com.craftingdead.core.client.util;

import java.util.function.Function;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import com.craftingdead.core.CraftingDead;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.ItemModelGenerator;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class RenderUtil {

  private static final ItemModelGenerator MODEL_GENERATOR = new ItemModelGenerator();

  public static final ResourceLocation ICONS =
      new ResourceLocation(CraftingDead.ID, "textures/gui/icons.png");

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void updateUniform(String name, float value, ShaderGroup shaderGroup) {
    ShaderGroup sg = minecraft.gameRenderer.getShaderGroup();
    if (sg != null) {
      for (Shader shader : sg.listShaders) {
        ShaderDefault variable = shader.getShaderManager().getShaderUniform(name);
        if (variable != null) {
          variable.set(value);
        }
      }
    }
  }

  public static void updateUniform(String name, float[] value, ShaderGroup shaderGroup) {
    ShaderGroup sg = minecraft.gameRenderer.getShaderGroup();
    if (sg != null) {
      for (Shader shader : sg.listShaders) {
        ShaderDefault variable = shader.getShaderManager().getShaderUniform(name);
        if (variable != null) {
          variable.set(value);
        }
      }
    }
  }

  /**
   * Checks whether the entity is inside the FOV of the game client. The size of the game window is
   * also considered. Blocks in front of player's view are not considered.
   *
   * @param target - The entity to test from the player's view
   * @return <code>true</code> if the entity can be directly seen. <code>false</code> otherwise.
   */
  public static boolean isInsideGameFOV(Entity target, boolean firstPerson) {
    ActiveRenderInfo activerenderinfo = minecraft.gameRenderer.getActiveRenderInfo();
    Vec3d projectedViewVec3d =
        firstPerson ? target.getPositionVec().add(0, target.getEyeHeight(), 0)
            : activerenderinfo.getProjectedView();
    double viewerX = projectedViewVec3d.getX();
    double viewerY = projectedViewVec3d.getY();
    double viewerZ = projectedViewVec3d.getZ();

    if (!target.isInRangeToRender3d(viewerX, viewerY, viewerZ)) {
      return false;
    }

    AxisAlignedBB renderBoundingBox = target.getRenderBoundingBox();

    // Validation from Vanilla.
    // Generates a render bounding box if it is needed.
    if (renderBoundingBox.hasNaN() || renderBoundingBox.getAverageEdgeLength() == 0.0D) {
      renderBoundingBox = new AxisAlignedBB(target.getPosX() - 2.0D, target.getPosY() - 2.0D,
          target.getPosZ() - 2.0D, target.getPosX() + 2.0D, target.getPosY() + 2.0D,
          target.getPosZ() + 2.0D);
    }

    return RenderUtil.createClippingHelper(1F, firstPerson)
        .isBoundingBoxInFrustum(renderBoundingBox);
  }

  public static ClippingHelperImpl createClippingHelper(float partialTicks, boolean firstPerson) {
    GameRenderer gameRenderer = minecraft.gameRenderer;
    ActiveRenderInfo activerenderinfo = minecraft.gameRenderer.getActiveRenderInfo();
    Vec3d projectedViewVec3d =
        firstPerson ? minecraft.player.getPositionVec().add(0, minecraft.player.getEyeHeight(), 0)
            : activerenderinfo.getProjectedView();
    double viewerX = projectedViewVec3d.getX();
    double viewerY = projectedViewVec3d.getY();
    double viewerZ = projectedViewVec3d.getZ();
    float pitch = firstPerson ? minecraft.player.rotationPitch : activerenderinfo.getPitch();
    float yaw = firstPerson ? minecraft.player.rotationYaw : activerenderinfo.getYaw();

    MatrixStack cameraRotationStack = new MatrixStack();
    // Reminder: At 1.15.2, roll cannot be get from ActiveRenderInfo
    // cameraRotationStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rollHere));
    cameraRotationStack.rotate(Vector3f.XP.rotationDegrees(pitch));
    cameraRotationStack.rotate(Vector3f.YP.rotationDegrees(yaw + 180.0F));

    Matrix4f fovAndWindowMatrix = new MatrixStack().getLast().getMatrix();
    fovAndWindowMatrix.mul(gameRenderer.getProjectionMatrix(activerenderinfo, partialTicks, true));

    ClippingHelperImpl clippingHelper =
        new ClippingHelperImpl(cameraRotationStack.getLast().getMatrix(), fovAndWindowMatrix);
    clippingHelper.setCameraPosition(viewerX, viewerY, viewerZ);

    return clippingHelper;
  }

  public static void drawGradientRectangle(double x, double y, double x2, double y2, int startColor,
      int endColor) {
    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.disableAlphaTest();
    RenderSystem.defaultBlendFunc();
    RenderSystem.shadeModel(GL11.GL_SMOOTH);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

    float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
    float startRed = (float) (startColor >> 16 & 255) / 255.0F;
    float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
    float startBlue = (float) (startColor & 255) / 255.0F;

    float endAlpha = (float) (startColor >> 24 & 255) / 255.0F;
    float endRed = (float) (endColor >> 16 & 255) / 255.0F;
    float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
    float endBlue = (float) (endColor & 255) / 255.0F;

    buffer.pos(x, y2, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    buffer.pos(x2, y2, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.pos(x2, y, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.pos(x, y, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    tessellator.draw();

    RenderSystem.shadeModel(GL11.GL_FLAT);
    RenderSystem.enableAlphaTest();
    RenderSystem.disableBlend();
    RenderSystem.enableTexture();
  }

  /**
   * Applies the same body rotation of a vanilla crouching player.
   */
  public static void applyPlayerCrouchRotation(MatrixStack matrix) {
    // Fix X rotation
    matrix.rotate(Vector3f.XP.rotation(0.5F));

    // Fix XYZ position
    matrix.translate(0F, 0.2F, -0.1F);
  }

  /**
   * Checks whether the model has a generation marker. This means the model may be waiting to be
   * generated.
   */
  public static boolean hasGenerationMarker(BlockModel blockModel) {
    return blockModel.getRootModel().name.equals("generation marker");
  }

  /**
   * Generates a model for the sprite (texture) of the item. This can be used to generate simple
   * item models.
   *
   * @param blockModel The unbaked model of the item
   * @throws IllegalArgumentException Throws if the model does not have a generation marker. It is a
   *         good practice to check if the item has a generation marker before trying to generating
   *         the model.
   * @return The {@link IBakedModel} of this item, ready to be rendered.
   */
  public static IBakedModel generateSpriteModel(BlockModel blockModel, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ResourceLocation modelLocation) throws IllegalArgumentException {

    // It is a good practice check if the model can be generated before trying to generate.
    Validate.isTrue(hasGenerationMarker(blockModel), "The model does not have a generation marker");

    return MODEL_GENERATOR
        .makeItemModel(spriteGetter, blockModel)
        .bakeModel(bakery, blockModel, spriteGetter, modelTransform, modelLocation, false);
  }

  public static void drawTexturedRectangle(double x, double y, float width, float height,
      float textureX, float textureY) {
    drawTexturedRectangle(x, y, x + width, y + height, textureX, textureY, textureX + width,
        textureY + height, 256, 256);
  }

  public static void drawTexturedRectangle(double x, double y, double x2, double y2, float textureX,
      float textureY, float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    drawTexturedRectangle(x, y, x2, y2, u, v, u2, v2);
  }

  public static void drawTexturedRectangle(double x, double y, double width, double height) {
    drawTexturedRectangle(x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void drawTexturedRectangle(double x, double y, double x2, double y2, float u,
      float v, float u2, float v2) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(x, y2, 0.0D).tex(u, v).endVertex();
    bufferbuilder.pos(x2, y2, 0.0D).tex(u2, v).endVertex();
    bufferbuilder.pos(x2, y, 0.0D).tex(u2, v2).endVertex();
    bufferbuilder.pos(x, y, 0.0D).tex(u, v2).endVertex();
    tessellator.draw();
  }

  public static void bind(ResourceLocation resourceLocation) {
    minecraft.getTextureManager().bindTexture(resourceLocation);
  }

  public static double getFitScale(final double imageWidth, final double imageHeight) {
    double widthScale = minecraft.getMainWindow().getWidth() / imageWidth;
    double heightScale = minecraft.getMainWindow().getHeight() / imageHeight;
    final double scale =
        imageHeight * widthScale < minecraft.getMainWindow().getHeight() ? heightScale : widthScale;
    return scale / minecraft.getMainWindow().getGuiScaleFactor();
  }
}
