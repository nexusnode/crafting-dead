/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.client.util;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class RenderUtil {

  public static final Codec<Vector3f> VECTOR_3F_CODEC =
      Codec.FLOAT.listOf().comapFlatMap((list) -> {
        if (list.size() > 3) {
          return DataResult.error("Input is not a list of 3 floats");
        } else {
          return DataResult
              .success(new Vector3f(list.get(0), list.get(1), list.get(2)));
        }
      }, (vector) -> Lists.newArrayList(vector.x(), vector.y(), vector.z()));

  public static final Codec<Quaternion> QUATERNION_CODEC = VECTOR_3F_CODEC
      .xmap(t -> new Quaternion(t.x(), t.y(), t.z(), true), RenderUtil::toEulerAngles);

  public static final Codec<TransformationMatrix> TRANSFORMATION_MATRIX_CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              VECTOR_3F_CODEC
                  .optionalFieldOf("translation", new Vector3f())
                  .xmap(vec -> {
                    Vector3f scaledVec = vec.copy();
                    scaledVec.mul(1.0F / 16.0F);
                    return scaledVec;
                  }, scaledVec -> {
                    Vector3f vec = scaledVec.copy();
                    vec.mul(16.0F);
                    return vec;
                  })
                  .forGetter(TransformationMatrix::getTranslation),
              QUATERNION_CODEC.optionalFieldOf("rotation", Quaternion.ONE.copy())
                  .forGetter(TransformationMatrix::getLeftRotation),
              VECTOR_3F_CODEC.optionalFieldOf("scale", new Vector3f(1.0F, 1.0F, 1.0F))
                  .forGetter(TransformationMatrix::getScale),
              QUATERNION_CODEC.optionalFieldOf("post-rotation", Quaternion.ONE.copy())
                  .forGetter(TransformationMatrix::getRightRot))
          .apply(instance, TransformationMatrix::new));

  public static final int FULL_LIGHT = 0xF000F0;

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void updateUniform(String name, float value, ShaderGroup shaderGroup) {
    ShaderGroup sg = minecraft.gameRenderer.currentEffect();
    if (sg != null) {
      for (Shader shader : sg.passes) {
        ShaderDefault variable = shader.getEffect().safeGetUniform(name);
        if (variable != null) {
          variable.set(value);
        }
      }
    }
  }

  public static void updateUniform(String name, float[] value, ShaderGroup shaderGroup) {
    ShaderGroup sg = minecraft.gameRenderer.currentEffect();
    if (sg != null) {
      for (Shader shader : sg.passes) {
        ShaderDefault variable = shader.getEffect().safeGetUniform(name);
        if (variable != null) {
          variable.set(value);
        }
      }
    }
  }

  public static Vector3f toEulerAngles(Quaternion quat) {
    // roll (x-axis rotation)
    float sinRollCosPitch = 2 * (quat.i() * quat.j() + quat.k() * quat.r());
    float cosRollCosPitch = 1 - 2 * (quat.j() * quat.j() + quat.k() * quat.k());
    float roll = (float) MathHelper.atan2(sinRollCosPitch, cosRollCosPitch);

    // pitch (y-axis rotation)
    float sinPitch = 2 * (quat.i() * quat.k() - quat.r() * quat.j());
    float pitch;
    if (MathHelper.abs(sinPitch) >= 1) {
      pitch = (float) Math.copySign(Math.PI / 2, sinPitch); // use 90 degrees if out of range
    } else {
      pitch = (float) Math.asin(sinPitch);
    }

    // yaw (z-axis rotation)
    float sinYawCosPitch = 2 * (quat.i() * quat.r() + quat.j() * quat.k());
    float cosYawCosPitch = 1 - 2 * (quat.k() * quat.k() + quat.r() * quat.r());
    float yaw = (float) MathHelper.atan2(sinYawCosPitch, cosYawCosPitch);

    return new Vector3f(roll, pitch, yaw);
  }

  public static Optional<Vector2f> projectToPlayerView(double x, double y, double z,
      float partialTicks) {
    final ActiveRenderInfo activeRenderInfo = minecraft.gameRenderer.getMainCamera();
    final Vector3d cameraPos = activeRenderInfo.getPosition();
    final Quaternion cameraRotation = activeRenderInfo.rotation().copy();
    cameraRotation.conj();

    final Vector3f result = new Vector3f((float) (cameraPos.x - x),
        (float) (cameraPos.y - y),
        (float) (cameraPos.z - z));

    result.transform(cameraRotation);

    if (minecraft.options.bobView) {
      Entity renderViewEntity = minecraft.getCameraEntity();
      if (renderViewEntity instanceof PlayerEntity) {
        PlayerEntity playerentity = (PlayerEntity) renderViewEntity;
        float distanceWalkedModified = playerentity.walkDist;

        float changeInDistance = distanceWalkedModified - playerentity.walkDistO;
        float lerpDistance = -(distanceWalkedModified + changeInDistance * partialTicks);
        float lerpYaw =
            MathHelper.lerp(partialTicks, playerentity.oBob, playerentity.bob);
        Quaternion q2 = new Quaternion(Vector3f.XP,
            Math.abs(MathHelper.cos(lerpDistance * (float) Math.PI - 0.2F) * lerpYaw) * 5.0F, true);
        q2.conj();
        result.transform(q2);

        Quaternion q1 =
            new Quaternion(Vector3f.ZP,
                MathHelper.sin(lerpDistance * (float) Math.PI) * lerpYaw * 3.0F,
                true);
        q1.conj();
        result.transform(q1);

        Vector3f bobTranslation =
            new Vector3f((MathHelper.sin(lerpDistance * (float) Math.PI) * lerpYaw * 0.5F),
                (-Math.abs(MathHelper.cos(lerpDistance * (float) Math.PI) * lerpYaw)), 0.0f);
        bobTranslation.setY(-bobTranslation.y()); // this is weird but hey, if it works
        result.add(bobTranslation);
      }
    }

    final double fov = minecraft.gameRenderer.getFov(activeRenderInfo, partialTicks, true);
    final float halfHeight = (float) minecraft.getWindow().getGuiScaledHeight() / 2;
    final float scale =
        halfHeight / (result.z() * (float) Math.tan(Math.toRadians(fov / 2.0D)));
    return result.z() > 0.0D ? Optional.empty()
        : Optional.of(new Vector2f(-result.x() * scale, result.y() * scale));
  }

  /**
   * Checks whether the entity is inside the viewing frustum. The size of the game window is also
   * considered. Blocks in front of player's view are not considered.
   *
   * @param target - the entity to test for
   * @return <code>true</code> if the entity is visible, <code>false</code> otherwise.
   */
  public static boolean isInsideFrustum(Entity target, boolean firstPerson) {
    ActiveRenderInfo camera = minecraft.gameRenderer.getMainCamera();
    Vector3d projectedViewVec3d = firstPerson
        ? target.position().add(0, target.getEyeHeight(), 0)
        : camera.getPosition();
    double viewerX = projectedViewVec3d.x();
    double viewerY = projectedViewVec3d.y();
    double viewerZ = projectedViewVec3d.z();

    if (!target.shouldRender(viewerX, viewerY, viewerZ)) {
      return false;
    }

    AxisAlignedBB renderBoundingBox = target.getBoundingBoxForCulling();

    // Validation from Vanilla.
    // Generates a render bounding box if it is needed.
    if (renderBoundingBox.hasNaN() || renderBoundingBox.getSize() == 0.0D) {
      renderBoundingBox = new AxisAlignedBB(target.getX() - 2.0D, target.getY() - 2.0D,
          target.getZ() - 2.0D, target.getX() + 2.0D, target.getY() + 2.0D,
          target.getZ() + 2.0D);
    }

    return RenderUtil.createFrustum(1.0F, firstPerson).isVisible(renderBoundingBox);
  }

  public static ClippingHelper createFrustum(float partialTicks, boolean firstPerson) {
    GameRenderer gameRenderer = minecraft.gameRenderer;
    ActiveRenderInfo camera = minecraft.gameRenderer.getMainCamera();
    Vector3d projectedViewVec3d = firstPerson
        ? minecraft.player.position().add(0, minecraft.player.getEyeHeight(), 0)
        : camera.getPosition();
    double viewerX = projectedViewVec3d.x();
    double viewerY = projectedViewVec3d.y();
    double viewerZ = projectedViewVec3d.z();
    float pitch = firstPerson ? minecraft.player.xRot : camera.getXRot();
    float yaw = firstPerson ? minecraft.player.yRot : camera.getYRot();

    MatrixStack poseStack = new MatrixStack();
    // As of writing this, Camera does not contain a roll
    // cameraRotationStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rollHere));
    poseStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
    poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw + 180.0F));

    Matrix4f projectionMatrix = new Matrix4f();
    projectionMatrix.setIdentity();

    projectionMatrix
        .multiply(gameRenderer.getProjectionMatrix(camera, partialTicks, true));

    ClippingHelper frustum =
        new ClippingHelper(poseStack.last().pose(), projectionMatrix);
    frustum.prepare(viewerX, viewerY, viewerZ);

    return frustum;
  }

  public static void fill(MatrixStack matrixStack, int x, int y, int width, int height,
      int colour) {
    AbstractGui.fill(matrixStack, x, y, x + width, y + height, colour);
  }

  @SuppressWarnings("deprecation")
  public static void drawGradientRectangle(double x, double y, double x2, double y2, int startColor,
      int endColor) {
    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.disableAlphaTest();
    RenderSystem.defaultBlendFunc();
    RenderSystem.shadeModel(GL11.GL_SMOOTH);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuilder();
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

    float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
    float startRed = (float) (startColor >> 16 & 255) / 255.0F;
    float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
    float startBlue = (float) (startColor & 255) / 255.0F;

    float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
    float endRed = (float) (endColor >> 16 & 255) / 255.0F;
    float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
    float endBlue = (float) (endColor & 255) / 255.0F;

    buffer.vertex(x, y2, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    buffer.vertex(x2, y2, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.vertex(x2, y, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.vertex(x, y, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    tessellator.end();

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
    matrix.mulPose(Vector3f.XP.rotation(0.5F));

    // Fix XYZ position
    matrix.translate(0F, 0.2F, -0.1F);
  }

  public static void blit(double x, double y, float width, float height,
      float textureX, float textureY) {
    blit(x, y, x + width, y + height, textureX, textureY, textureX + width,
        textureY + height, 256, 256);
  }

  public static void blit(double x, double y, double x2, double y2, float textureX,
      float textureY, float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    blit(x, y, x2, y2, u, v, u2, v2);
  }

  public static void blit(double x, double y, double width, double height) {
    blit(x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void blit(double x, double y, double x2, double y2, float u,
      float v, float u2, float v2) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuilder();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.vertex(x, y2, 0.0D).uv(u, v).endVertex();
    bufferbuilder.vertex(x2, y2, 0.0D).uv(u2, v).endVertex();
    bufferbuilder.vertex(x2, y, 0.0D).uv(u2, v2).endVertex();
    bufferbuilder.vertex(x, y, 0.0D).uv(u, v2).endVertex();
    tessellator.end();
  }

  public static void bind(ResourceLocation resourceLocation) {
    minecraft.getTextureManager().bind(resourceLocation);
  }

  public static double getFitScale(final double imageWidth, final double imageHeight) {
    double widthScale = minecraft.getWindow().getScreenWidth() / imageWidth;
    double heightScale = minecraft.getWindow().getScreenHeight() / imageHeight;
    final double scale =
        imageHeight * widthScale < minecraft.getWindow().getScreenHeight() ? heightScale
            : widthScale;
    return scale / minecraft.getWindow().getGuiScale();
  }

  public static void renderGuiItem(ItemStack itemStack, int x, int y, int colour) {
    renderGuiItem(itemStack, x, y, colour,
        minecraft.getItemRenderer().getModel(itemStack, null, null),
        ItemCameraTransforms.TransformType.GUI);
  }

  public static void renderGuiItem(ItemStack itemStack, int x, int y, int colour,
      ItemCameraTransforms.TransformType transformType) {
    renderGuiItem(itemStack, x, y, colour,
        minecraft.getItemRenderer().getModel(itemStack, null, null), transformType);
  }

  /**
   * Copied from {@link ItemRenderer#renderItemModelIntoGUI} with the ability to customise the
   * colour.
   */
  @SuppressWarnings("deprecation")
  public static void renderGuiItem(ItemStack itemStack, int x, int y,
      int colour, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transformType) {
    RenderSystem.pushMatrix();
    minecraft.textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
    minecraft.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(
        false, false);
    RenderSystem.enableRescaleNormal();
    RenderSystem.enableAlphaTest();
    RenderSystem.defaultAlphaFunc();
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.translatef((float) x, (float) y, 100.0F + minecraft.getItemRenderer().blitOffset);
    RenderSystem.translatef(8.0F, 8.0F, 0.0F);
    RenderSystem.scalef(1.0F, -1.0F, 1.0F);
    RenderSystem.scalef(16.0F, 16.0F, 16.0F);
    IRenderTypeBuffer.Impl renderTypeBuffer =
        Minecraft.getInstance().renderBuffers().bufferSource();
    boolean enable3dLight = !bakedmodel.usesBlockLight();
    if (enable3dLight) {
      RenderHelper.setupForFlatItems();
    }

    if (colour > -1) {
      renderItemColour(itemStack, transformType, false, new MatrixStack(),
          renderTypeBuffer, colour, FULL_LIGHT, OverlayTexture.NO_OVERLAY, bakedmodel);
    } else {
      minecraft.getItemRenderer().render(itemStack, transformType, false, new MatrixStack(),
          renderTypeBuffer, FULL_LIGHT, OverlayTexture.NO_OVERLAY, bakedmodel);
    }

    renderTypeBuffer.endBatch();
    RenderSystem.enableDepthTest();
    if (enable3dLight) {
      RenderHelper.setupFor3DItems();
    }

    RenderSystem.disableAlphaTest();
    RenderSystem.disableRescaleNormal();
    RenderSystem.popMatrix();
  }


  /**
   * Copied from
   * {@link ItemRenderer#renderItem(ItemStack, net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType, boolean, MatrixStack, IRenderTypeBuffer, int, int, IBakedModel)}
   * with the ability to customise the colour.
   */
  public static void renderItemColour(ItemStack itemStackIn,
      ItemCameraTransforms.TransformType transformTypeIn,
      boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int colour,
      int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn) {
    if (!itemStackIn.isEmpty()) {
      matrixStackIn.pushPose();
      boolean flag = transformTypeIn == ItemCameraTransforms.TransformType.GUI
          || transformTypeIn == ItemCameraTransforms.TransformType.GROUND
          || transformTypeIn == ItemCameraTransforms.TransformType.FIXED;
      if (itemStackIn.getItem() == Items.TRIDENT && flag) {
        modelIn = minecraft.getItemRenderer().getItemModelShaper().getModelManager()
            .getModel(new ModelResourceLocation("minecraft:trident#inventory"));
      }

      modelIn = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStackIn,
          modelIn, transformTypeIn, leftHand);
      matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
      if (!modelIn.isCustomRenderer() && (itemStackIn.getItem() != Items.TRIDENT || flag)) {
        boolean flag1;
        if (transformTypeIn != ItemCameraTransforms.TransformType.GUI
            && !transformTypeIn.firstPerson() && itemStackIn.getItem() instanceof BlockItem) {
          Block block = ((BlockItem) itemStackIn.getItem()).getBlock();
          flag1 = !(block instanceof BreakableBlock) && !(block instanceof StainedGlassPaneBlock);
        } else {
          flag1 = true;
        }
        if (modelIn.isLayered()) {
          net.minecraftforge.client.ForgeHooksClient.drawItemLayered(minecraft.getItemRenderer(),
              modelIn, itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn,
              flag1);
        } else {
          RenderType rendertype = RenderTypeLookup.getRenderType(itemStackIn, flag1);
          IVertexBuilder ivertexbuilder;
          if (itemStackIn.getItem() == Items.COMPASS && itemStackIn.hasFoil()) {
            matrixStackIn.pushPose();
            MatrixStack.Entry matrixstack$entry = matrixStackIn.last();
            if (transformTypeIn == ItemCameraTransforms.TransformType.GUI) {
              matrixstack$entry.pose().multiply(0.5F);
            } else if (transformTypeIn.firstPerson()) {
              matrixstack$entry.pose().multiply(0.75F);
            }

            if (flag1) {
              ivertexbuilder =
                  ItemRenderer.getCompassFoilBufferDirect(bufferIn, rendertype, matrixstack$entry);
            } else {
              ivertexbuilder =
                  ItemRenderer.getCompassFoilBuffer(bufferIn, rendertype, matrixstack$entry);
            }

            matrixStackIn.popPose();
          } else if (flag1) {
            ivertexbuilder = ItemRenderer.getFoilBufferDirect(bufferIn, rendertype, true,
                itemStackIn.hasFoil());
          } else {
            ivertexbuilder =
                ItemRenderer.getFoilBuffer(bufferIn, rendertype, true, itemStackIn.hasFoil());
          }

          renderModelColour(modelIn, colour, combinedLightIn, combinedOverlayIn, matrixStackIn,
              ivertexbuilder);
        }
      } else {
        itemStackIn.getItem().getItemStackTileEntityRenderer().renderByItem(itemStackIn,
            transformTypeIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
      }

      matrixStackIn.popPose();
    }
  }

  public static void renderModelColour(IBakedModel bakedModel, int colour, int packedLight,
      int packedOverlay, MatrixStack matrixStack, IVertexBuilder vertexBuilder) {
    final Random random = new Random();

    for (Direction direction : Direction.values()) {
      random.setSeed(42L);
      renderQuadsColour(matrixStack, vertexBuilder,
          bakedModel.getQuads(null, direction, random, EmptyModelData.INSTANCE), colour,
          packedLight, packedOverlay);
    }

    random.setSeed(42L);
    renderQuadsColour(matrixStack, vertexBuilder,
        bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE), colour, packedLight,
        packedOverlay);
  }

  private static void renderQuadsColour(MatrixStack matrixStack, IVertexBuilder vertexBuilder,
      List<BakedQuad> quads, int colour, int packedLight, int packedOverlay) {
    MatrixStack.Entry matrixStackEntry = matrixStack.last();
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
