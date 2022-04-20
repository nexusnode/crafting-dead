/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.util;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;

public class RenderUtil {

  public static final Codec<Quaternion> QUATERNION_CODEC = Vector3f.CODEC
      .xmap(Quaternion::fromXYZDegrees, Quaternion::toXYZDegrees);

  public static final Codec<Transformation> TRANSFORMATION_MATRIX_CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Vector3f.CODEC
                  .optionalFieldOf("translation", new Vector3f())
                  .xmap(vec -> {
                    var scaledVec = vec.copy();
                    scaledVec.mul(1.0F / 16.0F);
                    return scaledVec;
                  }, scaledVec -> {
                    var vec = scaledVec.copy();
                    vec.mul(16.0F);
                    return vec;
                  })
                  .forGetter(Transformation::getTranslation),
              QUATERNION_CODEC.optionalFieldOf("rotation", Quaternion.ONE.copy())
                  .forGetter(Transformation::getLeftRotation),
              Vector3f.CODEC.optionalFieldOf("scale", new Vector3f(1.0F, 1.0F, 1.0F))
                  .forGetter(Transformation::getScale),
              QUATERNION_CODEC.optionalFieldOf("post-rotation", Quaternion.ONE.copy())
                  .forGetter(Transformation::getRightRotation))
          .apply(instance, Transformation::new));

  public static final int FULL_LIGHT = 0xF000F0;

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void updateUniform(String name, float value, PostChain postChain) {
    for (var pass : postChain.passes) {
      pass.getEffect().safeGetUniform(name).set(value);
    }
  }

  public static void updateUniform(String name, float[] value, PostChain postChain) {
    for (var pass : postChain.passes) {
      pass.getEffect().safeGetUniform(name).set(value);
    }
  }

  @Nullable
  public static Vec2 projectToPlayerView(double x, double y, double z, float partialTicks) {
    final var activeRenderInfo = minecraft.gameRenderer.getMainCamera();
    final var cameraPos = activeRenderInfo.getPosition();
    final var cameraRotation = activeRenderInfo.rotation().copy();
    cameraRotation.conj();

    final var result = new Vector3f(
        (float) (cameraPos.x - x),
        (float) (cameraPos.y - y),
        (float) (cameraPos.z - z));

    result.transform(cameraRotation);

    if (minecraft.options.bobView) {
      final var renderViewEntity = minecraft.getCameraEntity();
      if (renderViewEntity instanceof Player player) {
        final var distanceWalkedModified = player.walkDist;

        final var changeInDistance = distanceWalkedModified - player.walkDistO;
        final var lerpDistance = -(distanceWalkedModified + changeInDistance * partialTicks);
        final var lerpYaw = Mth.lerp(partialTicks, player.oBob, player.bob);
        final var q2 = new Quaternion(Vector3f.XP,
            Math.abs(Mth.cos(lerpDistance * (float) Math.PI - 0.2F) * lerpYaw) * 5.0F, true);
        q2.conj();
        result.transform(q2);

        final var q1 = new Quaternion(Vector3f.ZP,
            Mth.sin(lerpDistance * (float) Math.PI) * lerpYaw * 3.0F,
            true);
        q1.conj();
        result.transform(q1);

        var bobTranslation = new Vector3f(
            Mth.sin(lerpDistance * (float) Math.PI) * lerpYaw * 0.5F,
            -Math.abs(Mth.cos(lerpDistance * (float) Math.PI) * lerpYaw),
            0.0F);
        bobTranslation.setY(-bobTranslation.y()); // this is weird but hey, if it works
        result.add(bobTranslation);
      }
    }

    final var fov = minecraft.gameRenderer.getFov(activeRenderInfo, partialTicks, true);
    final var halfHeight = minecraft.getWindow().getGuiScaledHeight() / 2.0F;
    final var scale = halfHeight / (result.z() * (float) Math.tan(Math.toRadians(fov / 2.0D)));
    return result.z() > 0.0D
        ? null
        : new Vec2(-result.x() * scale, result.y() * scale);
  }

  /**
   * Checks whether the entity is inside the viewing frustum. The size of the game window is also
   * considered. Blocks in front of player's view are not considered.
   *
   * @param target - the entity to test for
   * @return <code>true</code> if the entity is visible, <code>false</code> otherwise.
   */
  public static boolean isInsideFrustum(Entity target, boolean firstPerson) {
    final var camera = minecraft.gameRenderer.getMainCamera();
    final var projectedViewVec3d = firstPerson
        ? target.position().add(0, target.getEyeHeight(), 0)
        : camera.getPosition();
    final var viewerX = projectedViewVec3d.x();
    final var viewerY = projectedViewVec3d.y();
    final var viewerZ = projectedViewVec3d.z();

    if (!target.shouldRender(viewerX, viewerY, viewerZ)) {
      return false;
    }

    var renderBoundingBox = target.getBoundingBoxForCulling();

    // Validation from Vanilla.
    // Generates a render bounding box if it is needed.
    if (renderBoundingBox.hasNaN() || renderBoundingBox.getSize() == 0.0D) {
      renderBoundingBox = new AABB(
          target.getX() - 2.0D,
          target.getY() - 2.0D,
          target.getZ() - 2.0D,
          target.getX() + 2.0D,
          target.getY() + 2.0D,
          target.getZ() + 2.0D);
    }

    return RenderUtil.createFrustum(1.0F, firstPerson).isVisible(renderBoundingBox);
  }

  public static Frustum createFrustum(float partialTicks, boolean firstPerson) {
    final var gameRenderer = minecraft.gameRenderer;
    final var player = minecraft.player;
    final var camera = gameRenderer.getMainCamera();
    final var projectedViewVec3d = firstPerson
        ? player.position().add(0, player.getEyeHeight(), 0)
        : camera.getPosition();
    final var viewerX = projectedViewVec3d.x();
    final var viewerY = projectedViewVec3d.y();
    final var viewerZ = projectedViewVec3d.z();
    final var pitch = firstPerson ? player.getXRot() : camera.getXRot();
    final var yaw = firstPerson ? player.getYRot() : camera.getYRot();

    final var poseStack = new PoseStack();
    // As of writing this, Camera does not contain a roll
    // cameraRotationStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rollHere));
    poseStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
    poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw + 180.0F));

    final var projectionMatrix = new Matrix4f();
    projectionMatrix.setIdentity();

    projectionMatrix.multiply(
        gameRenderer.getProjectionMatrix(gameRenderer.getFov(camera, partialTicks, true)));

    final var frustum = new Frustum(poseStack.last().pose(), projectionMatrix);
    frustum.prepare(viewerX, viewerY, viewerZ);

    return frustum;
  }

  public static void fill(PoseStack poseStack, int x, int y, int width, int height, int colour) {
    GuiComponent.fill(poseStack, x, y, x + width, y + height, colour);
  }

  public static void fillGradient(PoseStack poseStack, float x, float y, float x2, float y2,
      int startColor, int endColor) {
    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    var startAlpha = (startColor >> 24 & 255) / 255.0F;
    var startRed = (startColor >> 16 & 255) / 255.0F;
    var startGreen = (startColor >> 8 & 255) / 255.0F;
    var startBlue = (startColor & 255) / 255.0F;

    var endAlpha = (endColor >> 24 & 255) / 255.0F;
    var endRed = (endColor >> 16 & 255) / 255.0F;
    var endGreen = (endColor >> 8 & 255) / 255.0F;
    var endBlue = (endColor & 255) / 255.0F;

    var pose = poseStack.last().pose();

    var tessellator = Tesselator.getInstance();
    var builder = tessellator.getBuilder();
    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    builder
        .vertex(pose, x, y2, 0.0F)
        .color(startRed, startGreen, startBlue, startAlpha)
        .endVertex();
    builder
        .vertex(pose, x2, y2, 0.0F)
        .color(endRed, endGreen, endBlue, endAlpha)
        .endVertex();
    builder
        .vertex(pose, x2, y, 0.0F)
        .color(endRed, endGreen, endBlue, endAlpha)
        .endVertex();
    builder
        .vertex(pose, x, y, 0.0F)
        .color(startRed, startGreen, startBlue, startAlpha)
        .endVertex();
    tessellator.end();

    RenderSystem.disableBlend();
    RenderSystem.enableTexture();
  }

  /**
   * Applies the same body rotation of a vanilla crouching player.
   */
  public static void applyPlayerCrouchRotation(PoseStack matrix) {
    // Fix X rotation
    matrix.mulPose(Vector3f.XP.rotation(0.5F));

    // Fix XYZ position
    matrix.translate(0F, 0.2F, -0.1F);
  }

  public static void blit(float x, float y, float width, float height,
      float textureX, float textureY) {
    blit(x, y, x + width, y + height, textureX, textureY, textureX + width,
        textureY + height, 256, 256);
  }

  public static void blit(float x, float y, float x2, float y2, float textureX,
      float textureY, float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    blit(x, y, x2, y2, u, v, u2, v2);
  }

  public static void blit(float x, float y, float width, float height) {
    blit(x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void blit(float x, float y, float x2, float y2, float u,
      float v, float u2, float v2) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    final var tessellator = Tesselator.getInstance();
    final var builder = tessellator.getBuilder();
    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    builder.vertex(x, y2, 0.0D).uv(u, v).endVertex();
    builder.vertex(x2, y2, 0.0D).uv(u2, v).endVertex();
    builder.vertex(x2, y, 0.0D).uv(u2, v2).endVertex();
    builder.vertex(x, y, 0.0D).uv(u, v2).endVertex();
    tessellator.end();
  }

  public static float getFitScale(final float imageWidth, final float imageHeight) {
    final var window = minecraft.getWindow();
    final var widthScale = window.getScreenWidth() / imageWidth;
    final var heightScale = window.getScreenHeight() / imageHeight;
    final var scale = imageHeight * widthScale < window.getScreenHeight()
        ? heightScale
        : widthScale;
    return scale / (float) minecraft.getWindow().getGuiScale();
  }

  public static void renderGuiItem(ItemStack itemStack, int x, int y, int colour) {
    renderGuiItem(itemStack, x, y, colour,
        minecraft.getItemRenderer().getModel(itemStack, null, null, 0),
        ItemTransforms.TransformType.GUI);
  }

  public static void renderGuiItem(ItemStack itemStack, int x, int y, int colour,
      ItemTransforms.TransformType transformType) {
    renderGuiItem(itemStack, x, y, colour,
        minecraft.getItemRenderer().getModel(itemStack, null, null, 0), transformType);
  }

  /**
   * Copied from {@link ItemRenderer#renderGuiItem} with the ability to customise the colour.
   */
  @SuppressWarnings("deprecation")
  public static void renderGuiItem(ItemStack itemStack, int x, int y,
      int colour, BakedModel bakedmodel, ItemTransforms.TransformType transformType) {
    minecraft.textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
    RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    var poseStack = RenderSystem.getModelViewStack();
    poseStack.pushPose();
    poseStack.translate(x, y, 100.0F + minecraft.getItemRenderer().blitOffset);
    poseStack.translate(8.0D, 8.0D, 0.0D);
    poseStack.scale(1.0F, -1.0F, 1.0F);
    poseStack.scale(16.0F, 16.0F, 16.0F);
    RenderSystem.applyModelViewMatrix();

    var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
    boolean enable3dLight = !bakedmodel.usesBlockLight();
    if (enable3dLight) {
      Lighting.setupForFlatItems();
    }

    if (colour > -1) {
      render(itemStack, transformType, false, new PoseStack(),
          bufferSource, colour, FULL_LIGHT, OverlayTexture.NO_OVERLAY, bakedmodel);
    } else {
      minecraft.getItemRenderer().render(itemStack, transformType, false, new PoseStack(),
          bufferSource, FULL_LIGHT, OverlayTexture.NO_OVERLAY, bakedmodel);
    }

    bufferSource.endBatch();
    RenderSystem.enableDepthTest();
    if (enable3dLight) {
      Lighting.setupFor3DItems();
    }

    poseStack.popPose();
    RenderSystem.applyModelViewMatrix();
  }

  /**
   * Copied from
   * {@link ItemRenderer#render(ItemStack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType, boolean, PoseStack, MultiBufferSource, int, int, BakedModel)
   * with the ability to customise the colour.
   */
  public static void render(ItemStack p_115144_, ItemTransforms.TransformType p_115145_,
      boolean p_115146_, PoseStack p_115147_, MultiBufferSource p_115148_, int color, int p_115149_,
      int p_115150_, BakedModel p_115151_) {
    if (!p_115144_.isEmpty()) {
      p_115147_.pushPose();
      boolean flag = p_115145_ == ItemTransforms.TransformType.GUI
          || p_115145_ == ItemTransforms.TransformType.GROUND
          || p_115145_ == ItemTransforms.TransformType.FIXED;
      if (flag) {
        if (p_115144_.is(Items.TRIDENT)) {
          p_115151_ = minecraft.getItemRenderer().getItemModelShaper().getModelManager()
              .getModel(new ModelResourceLocation("minecraft:trident#inventory"));
        } else if (p_115144_.is(Items.SPYGLASS)) {
          p_115151_ = minecraft.getItemRenderer().getItemModelShaper().getModelManager()
              .getModel(new ModelResourceLocation("minecraft:spyglass#inventory"));
        }
      }

      p_115151_ = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(p_115147_,
          p_115151_, p_115145_, p_115146_);
      p_115147_.translate(-0.5D, -0.5D, -0.5D);
      if (!p_115151_.isCustomRenderer() && (!p_115144_.is(Items.TRIDENT) || flag)) {
        boolean flag1;
        if (p_115145_ != ItemTransforms.TransformType.GUI && !p_115145_.firstPerson()
            && p_115144_.getItem() instanceof BlockItem) {
          Block block = ((BlockItem) p_115144_.getItem()).getBlock();
          flag1 =
              !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
        } else {
          flag1 = true;
        }
        if (p_115151_.isLayered()) {
          net.minecraftforge.client.ForgeHooksClient.drawItemLayered(minecraft.getItemRenderer(),
              p_115151_, p_115144_,
              p_115147_, p_115148_, p_115149_, p_115150_, flag1);
        } else {
          RenderType rendertype = ItemBlockRenderTypes.getRenderType(p_115144_, flag1);
          VertexConsumer vertexconsumer;
          if (p_115144_.is(Items.COMPASS) && p_115144_.hasFoil()) {
            p_115147_.pushPose();
            PoseStack.Pose posestack$pose = p_115147_.last();
            if (p_115145_ == ItemTransforms.TransformType.GUI) {
              posestack$pose.pose().multiply(0.5F);
            } else if (p_115145_.firstPerson()) {
              posestack$pose.pose().multiply(0.75F);
            }

            if (flag1) {
              vertexconsumer =
                  ItemRenderer.getCompassFoilBufferDirect(p_115148_, rendertype, posestack$pose);
            } else {
              vertexconsumer =
                  ItemRenderer.getCompassFoilBuffer(p_115148_, rendertype, posestack$pose);
            }

            p_115147_.popPose();
          } else if (flag1) {
            vertexconsumer =
                ItemRenderer.getFoilBufferDirect(p_115148_, rendertype, true, p_115144_.hasFoil());
          } else {
            vertexconsumer =
                ItemRenderer.getFoilBuffer(p_115148_, rendertype, true, p_115144_.hasFoil());
          }

          renderModelLists(p_115151_, p_115144_, p_115149_, p_115150_, p_115147_,
              vertexconsumer, color);
        }
      } else {
        net.minecraftforge.client.RenderProperties.get(p_115144_).getItemStackRenderer()
            .renderByItem(p_115144_, p_115145_, p_115147_, p_115148_, p_115149_, p_115150_);
      }

      p_115147_.popPose();
    }
  }

  @SuppressWarnings("deprecation")
  public static void renderModelLists(BakedModel p_115190_, ItemStack p_115191_,
      int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_, int color) {
    Random random = new Random();
    long i = 42L;

    for (Direction direction : Direction.values()) {
      random.setSeed(i);
      renderQuadList(p_115194_, p_115195_,
          p_115190_.getQuads((BlockState) null, direction, random), p_115191_, p_115192_,
          p_115193_, color);
    }

    random.setSeed(i);
    renderQuadList(p_115194_, p_115195_,
        p_115190_.getQuads((BlockState) null, (Direction) null, random), p_115191_, p_115192_,
        p_115193_, color);
  }


  public static void renderQuadList(PoseStack p_115163_, VertexConsumer p_115164_,
      List<BakedQuad> p_115165_, ItemStack p_115166_, int p_115167_, int p_115168_, int color) {
    boolean flag = !p_115166_.isEmpty();
    PoseStack.Pose posestack$pose = p_115163_.last();

    for (BakedQuad bakedquad : p_115165_) {
      int i = color;
      if (flag && bakedquad.isTinted()) {
        i = minecraft.getItemColors().getColor(p_115166_, bakedquad.getTintIndex());
      }

      float f = (float) (i >> 16 & 255) / 255.0F;
      float f1 = (float) (i >> 8 & 255) / 255.0F;
      float f2 = (float) (i & 255) / 255.0F;
      p_115164_.putBulkData(posestack$pose, bakedquad, f, f1, f2, p_115167_, p_115168_, true);
    }
  }
}
