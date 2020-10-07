/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.core.client.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import com.craftingdead.core.CraftingDead;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemModelGenerator;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;

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

  public static Optional<Vec2f> projectToPlayerView(double x, double y, double z,
      float partialTicks) {
    final ActiveRenderInfo activeRenderInfo = minecraft.gameRenderer.getActiveRenderInfo();
    final Vec3d cameraPos = activeRenderInfo.getProjectedView();
    final Quaternion cameraRotation = activeRenderInfo.getRotation().copy();
    cameraRotation.conjugate();

    final Vector3f result = new Vector3f((float) (cameraPos.x - x),
        (float) (cameraPos.y - y),
        (float) (cameraPos.z - z));

    result.transform(cameraRotation);

    if (minecraft.gameSettings.viewBobbing) {
      Entity renderViewEntity = minecraft.getRenderViewEntity();
      if (renderViewEntity instanceof PlayerEntity) {
        PlayerEntity playerentity = (PlayerEntity) renderViewEntity;
        float distanceWalkedModified = playerentity.distanceWalkedModified;

        float changeInDistance = distanceWalkedModified - playerentity.prevDistanceWalkedModified;
        float lerpDistance = -(distanceWalkedModified + changeInDistance * partialTicks);
        float lerpYaw =
            MathHelper.lerp(partialTicks, playerentity.prevCameraYaw, playerentity.cameraYaw);
        Quaternion q2 = new Quaternion(Vector3f.XP,
            Math.abs(MathHelper.cos(lerpDistance * (float) Math.PI - 0.2F) * lerpYaw) * 5.0F, true);
        q2.conjugate();
        result.transform(q2);

        Quaternion q1 =
            new Quaternion(Vector3f.ZP,
                MathHelper.sin(lerpDistance * (float) Math.PI) * lerpYaw * 3.0F,
                true);
        q1.conjugate();
        result.transform(q1);

        Vector3f bobTranslation =
            new Vector3f((MathHelper.sin(lerpDistance * (float) Math.PI) * lerpYaw * 0.5F),
                (-Math.abs(MathHelper.cos(lerpDistance * (float) Math.PI) * lerpYaw)), 0.0f);
        bobTranslation.setY(-bobTranslation.getY()); // this is weird but hey, if it works
        result.add(bobTranslation);
      }
    }

    final double fov = minecraft.gameRenderer.getFOVModifier(activeRenderInfo, partialTicks, true);
    final float halfHeight = (float) minecraft.getMainWindow().getScaledHeight() / 2;
    final float scale =
        halfHeight / (result.getZ() * (float) Math.tan(Math.toRadians(fov / 2.0D)));
    return result.getZ() > 0.0D ? Optional.empty()
        : Optional.of(new Vec2f(-result.getX() * scale, result.getY() * scale));
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

  public static void renderItemIntoGUI(ItemStack itemStack, int x, int y, int colour) {
    renderItemModelIntoGUI(itemStack, x, y, colour,
        minecraft.getItemRenderer().getItemModelWithOverrides(itemStack, null, null));
  }

  public static void renderItemModelIntoGUI(ItemStack itemStack, int x, int y, int colour,
      IBakedModel bakedModel) {
    RenderSystem.pushMatrix();
    minecraft.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    minecraft.textureManager.getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
        .setBlurMipmapDirect(false, false);
    RenderSystem.enableRescaleNormal();
    RenderSystem.enableAlphaTest();
    RenderSystem.defaultAlphaFunc();
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.translatef(x, y, 100.0F + minecraft.getItemRenderer().zLevel);
    RenderSystem.translatef(8.0F, 8.0F, 0.0F);
    RenderSystem.scalef(1.0F, -1.0F, 1.0F);
    RenderSystem.scalef(16.0F, 16.0F, 16.0F);
    MatrixStack matrixStack = new MatrixStack();
    IRenderTypeBuffer.Impl renderTypeBuffer =
        Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
    boolean diffuseLighting = !bakedModel.func_230044_c_();
    if (diffuseLighting) {
      RenderHelper.setupGuiFlatDiffuseLighting();
    }

    renderItem(itemStack, ItemCameraTransforms.TransformType.GUI, false, matrixStack,
        renderTypeBuffer, colour, 0xF000F0, OverlayTexture.NO_OVERLAY, bakedModel);
    renderTypeBuffer.finish();
    RenderSystem.enableDepthTest();
    if (diffuseLighting) {
      RenderHelper.setupGui3DDiffuseLighting();
    }

    RenderSystem.disableAlphaTest();
    RenderSystem.disableRescaleNormal();
    RenderSystem.popMatrix();
  }

  public static void renderItem(ItemStack itemStack,
      ItemCameraTransforms.TransformType transformType, boolean leftHand, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int colour, int packedLight, int packedOverlay,
      IBakedModel bakedModel) {
    if (!itemStack.isEmpty()) {
      matrixStack.push();
      boolean gui = transformType == ItemCameraTransforms.TransformType.GUI;
      boolean flat = gui || transformType == ItemCameraTransforms.TransformType.GROUND
          || transformType == ItemCameraTransforms.TransformType.FIXED;
      if (itemStack.getItem() == Items.TRIDENT && flat) {
        bakedModel = minecraft.getItemRenderer().getItemModelMesher().getModelManager()
            .getModel(new ModelResourceLocation("minecraft:trident#inventory"));
      }

      bakedModel =
          ForgeHooksClient.handleCameraTransforms(matrixStack, bakedModel, transformType, leftHand);
      matrixStack.translate(-0.5D, -0.5D, -0.5D);
      if (!bakedModel.isBuiltInRenderer() && (itemStack.getItem() != Items.TRIDENT || flat)) {
        RenderType renderType = RenderTypeLookup.getRenderType(itemStack);
        if (gui && Objects.equals(renderType, Atlases.getTranslucentBlockType())) {
          renderType = Atlases.getTranslucentCullBlockType();
        }

        IVertexBuilder vertexBuilder =
            ItemRenderer.getBuffer(renderTypeBuffer, renderType, true, itemStack.hasEffect());
        renderModel(bakedModel, colour, packedLight, packedOverlay, matrixStack, vertexBuilder);
      } else {
        itemStack.getItem().getItemStackTileEntityRenderer().render(itemStack, matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }

      matrixStack.pop();
    }
  }

  public static void renderModel(IBakedModel bakedModel, int colour, int packedLight,
      int packedOverlay,
      MatrixStack matrixStack, IVertexBuilder vertexBuilder) {
    final Random random = new Random();
    random.setSeed(42L);

    for (Direction direction : Direction.values()) {
      renderQuadsColour(matrixStack, vertexBuilder,
          bakedModel.getQuads(null, direction, random, EmptyModelData.INSTANCE), colour,
          packedLight, packedOverlay);
    }

    renderQuadsColour(matrixStack, vertexBuilder,
        bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE), colour, packedLight,
        packedOverlay);
  }

  private static void renderQuadsColour(MatrixStack matrixStack, IVertexBuilder vertexBuilder,
      List<BakedQuad> quads, int colour, int packedLight, int packedOverlay) {
    MatrixStack.Entry matrixStackEntry = matrixStack.getLast();
    for (BakedQuad bakedQuad : quads) {
      float r = (float) (colour >> 16 & 255) / 255.0F;
      float g = (float) (colour >> 8 & 255) / 255.0F;
      float b = (float) (colour & 255) / 255.0F;
      float a = (float) (colour >> 24 & 255) / 255.0F;
      vertexBuilder.addVertexData(matrixStackEntry, bakedQuad, r, g, b, a, packedLight,
          packedOverlay);
    }
  }
}
