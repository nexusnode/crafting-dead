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

package com.craftingdead.core.client.renderer.item;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.client.renderer.item.model.MuzzleFlashModel;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.util.EasingFunction;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunItem;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.core.world.item.gun.skin.Paint;
import com.craftingdead.core.world.item.scope.Scope;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.model.TransformationHelper;

public class GunRenderer implements CustomItemRenderer {

  private static final TransformationMatrix DEFAULT_SPRINTING_TRANSFORM =
      new TransformationMatrix(
          new Vector3f(-0.3F, 0.0F, 0.15F),
          new Quaternion(-20, 40, 0, true),
          new Vector3f(1.0F, 1.0F, 1.0F),
          null);

  public static final Codec<GunRenderer> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          ResourceLocation.CODEC
              .fieldOf("model")
              .forGetter(t -> t.modelLocation),
          RenderUtil.TRANSFORMATION_MATRIX_CODEC
              .fieldOf("muzzle_flash_transform")
              .forGetter(t -> t.muzzleFlashTransform),
          RenderUtil.TRANSFORMATION_MATRIX_CODEC
              .optionalFieldOf("aiming_transform", TransformationMatrix.identity())
              .forGetter(t -> t.aimingTransform),
          RenderUtil.TRANSFORMATION_MATRIX_CODEC
              .optionalFieldOf("sprinting_transform", DEFAULT_SPRINTING_TRANSFORM)
              .forGetter(t -> t.sprintingTransform),
          Codec.unboundedMap(ResourceLocation.CODEC, RenderUtil.TRANSFORMATION_MATRIX_CODEC)
              .optionalFieldOf("attachement_transforms", Collections.emptyMap())
              .forGetter(t -> t.attachementTransforms),
          Codec.unboundedMap(ResourceLocation.CODEC, RenderUtil.TRANSFORMATION_MATRIX_CODEC)
              .optionalFieldOf("magazine_transforms", Collections.emptyMap())
              .forGetter(t -> t.magazineTransforms),
          Codec
              .unboundedMap(HandTransform.CODEC,
                  Codec.mapPair(
                      RenderUtil.TRANSFORMATION_MATRIX_CODEC.optionalFieldOf("right_hand",
                          TransformationMatrix.identity()),
                      RenderUtil.TRANSFORMATION_MATRIX_CODEC.optionalFieldOf("left_hand",
                          TransformationMatrix.identity()))
                      .codec())
              .optionalFieldOf("hand_transforms", Collections.emptyMap())
              .forGetter(t -> t.handTransforms),
          Codec.compoundList(ResourceLocation.CODEC, RenderUtil.TRANSFORMATION_MATRIX_CODEC)
              .optionalFieldOf("iron_sights", Collections.emptyList())
              .forGetter(t -> t.ironSights))
      .apply(instance, GunRenderer::new));

  private static final Pair<TransformationMatrix, TransformationMatrix> IDENTITY_HAND_TRANSFORMS =
      Pair.of(TransformationMatrix.identity(), TransformationMatrix.identity());

  private static final String GUN_TEXTURE_REFERENCE = "gun";

  private static final int FLASH_TEXTURE_CHANGE_TIMEOUT_MS = 250;

  private static final int SPRINT_TRANSITION_MS = 300;

  private static final int AIMING_TRANSITION_MS = 200;

  private static final Model muzzleFlashModel = new MuzzleFlashModel();

  private static final Random random = new Random();

  private final Minecraft minecraft = Minecraft.getInstance();

  private final Map<Integer, IBakedModel> cachedModels = new Int2ObjectOpenHashMap<>();

  private final ResourceLocation modelLocation;

  private final TransformationMatrix muzzleFlashTransform;

  private final TransformationMatrix aimingTransform;

  private final TransformationMatrix sprintingTransform;

  private final Map<ResourceLocation, TransformationMatrix> attachementTransforms;

  private final Map<ResourceLocation, TransformationMatrix> magazineTransforms;

  private final Map<HandTransform, Pair<TransformationMatrix, TransformationMatrix>> handTransforms;

  private final List<Pair<ResourceLocation, TransformationMatrix>> ironSights;

  private long lastFlashTime = 0;

  private long sprintStartTimeMs;
  private boolean wasSprinting;

  private long aimingStartTimeMs;
  private boolean wasAiming;

  public GunRenderer(ResourceLocation model,
      TransformationMatrix muzzleFlashTransform,
      TransformationMatrix aimingTransform,
      TransformationMatrix sprintingTransform,
      Map<ResourceLocation, TransformationMatrix> attachementTransforms,
      Map<ResourceLocation, TransformationMatrix> magazineTransforms,
      Map<HandTransform, Pair<TransformationMatrix, TransformationMatrix>> handTransforms,
      List<Pair<ResourceLocation, TransformationMatrix>> ironSights) {
    this.modelLocation = model;
    this.muzzleFlashTransform = muzzleFlashTransform;
    this.aimingTransform = aimingTransform;
    this.sprintingTransform = sprintingTransform;
    this.attachementTransforms = attachementTransforms;
    this.magazineTransforms = magazineTransforms;
    this.handTransforms = handTransforms;
    this.ironSights = ironSights;
  }

  @Override
  public void rotateCamera(ItemStack itemStack, LivingEntity livingEntity, float partialTicks,
      Vector3f rotations) {
    Gun gun = Capabilities.getOrThrow(Capabilities.GUN, itemStack, Gun.class);
    gun.getClient().getAnimationController().applyCamera(partialTicks, rotations);
  }

  @Override
  public boolean handlePerspective(ItemStack item,
      ItemCameraTransforms.TransformType transformType) {
    switch (transformType) {
      case THIRD_PERSON_LEFT_HAND:
      case THIRD_PERSON_RIGHT_HAND:
      case FIRST_PERSON_LEFT_HAND:
      case FIRST_PERSON_RIGHT_HAND:
      case FIXED:
      case HEAD:
        return true;
      default:
        return false;
    }
  }

  @Override
  public void renderItem(
      ItemStack itemStack,
      ItemCameraTransforms.TransformType transformType,
      LivingEntity entity,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    Scope scope = itemStack.getCapability(Capabilities.SCOPE).orElse(null);
    boolean aiming = scope != null && scope.isAiming(entity);
    if (aiming && scope.getOverlayTexture(entity).isPresent()) {
      return;
    }

    float partialTicks = this.minecraft.isPaused() ? 1.0F : this.minecraft.getFrameTime();

    Gun gun = Capabilities.getOrThrow(Capabilities.GUN, itemStack, Gun.class);

    matrixStack.pushPose();
    {
      switch (transformType) {
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          if (entity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
            this.renderFirstPerson(playerEntity, itemStack, gun, aiming, transformType,
                partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
          }
          break;
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
          gun.getClient().getAnimationController().apply(partialTicks, matrixStack);
          this.renderGunWithAttachements(itemStack.hasFoil(), gun, 0.0F,
              transformType, partialTicks, matrixStack, renderTypeBuffer, packedLight,
              packedOverlay);
          break;
        case FIXED:
          matrixStack.scale(1.5F, 1.5F, 1.5F);
          matrixStack.translate(-0.25F, 0, 0);
          matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
          matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        default:
          this.renderGunWithAttachements(itemStack.hasFoil(), gun, 0.0F,
              transformType, partialTicks, matrixStack, renderTypeBuffer, packedLight,
              packedOverlay);
          break;
      }
    }
    matrixStack.popPose();
  }

  private void renderFirstPersonHands(
      AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack,
      Gun gun,
      float aimingPct,
      float partialTicks,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight) {

    final PlayerRenderer playerRenderer =
        (PlayerRenderer) this.minecraft.getEntityRenderDispatcher().getRenderer(playerEntity);

    HandSide handSide = playerEntity.getMainArm();

    Pair<TransformationMatrix, TransformationMatrix> heldTransforms =
        this.handTransforms.getOrDefault(
            handSide == HandSide.RIGHT
                ? HandTransform.HELD_RIGHT_HANDED
                : HandTransform.HELD_LEFT_HANDED,
            IDENTITY_HAND_TRANSFORMS);

    TransformationMatrix rightHandTransforms;
    TransformationMatrix leftHandTransforms;
    if (aimingPct > 0) {
      Pair<TransformationMatrix, TransformationMatrix> aimingTransforms =
          this.handTransforms.getOrDefault(
              handSide == HandSide.RIGHT
                  ? HandTransform.AIMING_RIGHT_HANDED
                  : HandTransform.AIMING_LEFT_HANDED,
              IDENTITY_HAND_TRANSFORMS);
      rightHandTransforms = TransformationHelper.slerp(heldTransforms.getFirst(),
          aimingTransforms.getFirst(), aimingPct);
      leftHandTransforms = TransformationHelper.slerp(heldTransforms.getSecond(),
          aimingTransforms.getSecond(), aimingPct);
    } else {
      rightHandTransforms = heldTransforms.getFirst();
      leftHandTransforms = heldTransforms.getSecond();
    }

    // Right Hand
    matrixStack.pushPose();
    {
      gun.getClient().getAnimationController().applyHand(
          playerEntity.getMainArm() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND,
          HandSide.RIGHT,
          partialTicks, matrixStack);
      matrixStack.translate(0.75F, -0.75F, -0.6F);

      rightHandTransforms.push(matrixStack);
      {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(95.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(100.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

        matrixStack.translate(0.9F, -0.75F, 0.5F);

        playerRenderer.renderRightHand(matrixStack, renderTypeBuffer, packedLight, playerEntity);
      }
      matrixStack.popPose();
    }
    matrixStack.popPose();

    // Left Hand
    matrixStack.pushPose();
    {
      gun.getClient().getAnimationController().applyHand(
          playerEntity.getMainArm() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND,
          HandSide.LEFT,
          partialTicks, matrixStack);

      matrixStack.translate(0.75F, -0.75F, -0.75F);

      leftHandTransforms.push(matrixStack);
      {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(95.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(95.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(120.0F));

        matrixStack.translate(0.05F, -1.05F, 0.30F);

        playerRenderer.renderLeftHand(matrixStack, renderTypeBuffer, packedLight, playerEntity);
      }
      matrixStack.popPose();
    }
    matrixStack.popPose();
  }

  private void renderFlash(Gun gun, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedOverlay) {
    matrixStack.pushPose();
    {
      long currentTime = Util.getMillis();
      long deltaTime = currentTime - this.lastFlashTime;

      int texture;
      if (deltaTime > FLASH_TEXTURE_CHANGE_TIMEOUT_MS) {
        this.lastFlashTime = currentTime;
        deltaTime = FLASH_TEXTURE_CHANGE_TIMEOUT_MS;
      }

      // Every n milliseconds, change the muzzle flash texture
      texture = (int) ((deltaTime / 100) + 1);
      float scale = (random.nextInt(8) + 3) / 10.0F;

      float x = this.muzzleFlashTransform.getTranslation().x();
      float y = this.muzzleFlashTransform.getTranslation().y();
      float z = this.muzzleFlashTransform.getTranslation().z();
      if (gun.getAttachments().contains(Attachments.SUPPRESSOR.get())) {
        z -= 0.4;
      }

      matrixStack.scale(this.muzzleFlashTransform.getScale().x() + scale,
          this.muzzleFlashTransform.getScale().y() + scale,
          this.muzzleFlashTransform.getScale().z() + scale);

      matrixStack.translate(x - scale * x / 2, y - scale * y / 2, z - scale * z / 2);

      IVertexBuilder flashVertexBuilder = renderTypeBuffer
          .getBuffer(muzzleFlashModel.renderType(new ResourceLocation(CraftingDead.ID,
              "textures/flash/flash" + texture + ".png")));
      muzzleFlashModel.renderToBuffer(matrixStack, flashVertexBuilder, RenderUtil.FULL_LIGHT,
          packedOverlay, 1.0F, 1.0F, 1.0F, scale + 0.5F);
    }
    matrixStack.popPose();
  }

  private void renderFirstPerson(
      AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack,
      Gun gun,
      boolean aiming,
      ItemCameraTransforms.TransformType transformType,
      float partialTicks,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    final boolean flash = gun.getClient().isFlashing();
    if (flash) {
      packedLight = RenderUtil.FULL_LIGHT;
    }

    // ============== Animations ==============

    if (this.wasSprinting != playerEntity.isSprinting()) {
      this.sprintStartTimeMs = Util.getMillis();
      this.wasSprinting = playerEntity.isSprinting();
    }

    float sprintingPct = MathHelper.clamp(
        (float) (Util.getMillis() - this.sprintStartTimeMs) / SPRINT_TRANSITION_MS,
        0.0F, 1.0F);
    if (!playerEntity.isSprinting()) {
      sprintingPct = 1.0F - sprintingPct;
    }

    sprintingPct = EasingFunction.SINE_IN_OUT.apply(sprintingPct);

    if (this.wasAiming != aiming) {
      this.aimingStartTimeMs = Util.getMillis();
      this.wasAiming = aiming;
    }

    float aimingPct = MathHelper.clamp(
        (float) (Util.getMillis() - this.aimingStartTimeMs) / AIMING_TRANSITION_MS,
        0.0F, 1.0F);
    if (!aiming) {
      aimingPct = 1.0F - aimingPct;
    }

    aimingPct = EasingFunction.SINE_IN_OUT.apply(aimingPct);

    // ============== Animations End ==============

    if (sprintingPct > 0) {
      TransformationHelper
          .slerp(TransformationMatrix.identity(), this.sprintingTransform, sprintingPct)
          .push(matrixStack);
    } else {
      matrixStack.pushPose();
    }
    {
      if (!aiming && flash) {
        this.renderFlash(gun, matrixStack, renderTypeBuffer, packedOverlay);
      }

      this.renderFirstPersonHands(playerEntity, itemStack, gun, aimingPct, partialTicks,
          matrixStack, renderTypeBuffer, packedLight);

      gun.getClient().getAnimationController().apply(partialTicks, matrixStack);

      this.renderGunWithAttachements(itemStack.hasFoil(), gun, aimingPct, transformType,
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  private void renderGunWithAttachements(
      boolean foil,
      Gun gun,
      float aimingPct,
      ItemCameraTransforms.TransformType transformType,
      float partialTicks,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    ResourceLocation skinTextureLocation = gun.getSkin() == null
        ? null
        : gun.getSkin().getTextureLocation(gun.getItemStack().getItem().getRegistryName());

    final int color = gun.getPaintStack().getCapability(Capabilities.PAINT)
        .resolve()
        .flatMap(Paint::getColor)
        .orElse(0xFFFFFFFF);

    Map<String, Either<RenderMaterial, String>> textures = null;
    if (skinTextureLocation != null) {
      textures = ImmutableMap.of(GUN_TEXTURE_REFERENCE,
          Either.left(new RenderMaterial(PlayerContainer.BLOCK_ATLAS, skinTextureLocation)));
    }
    IBakedModel bakedModel = this.getBakedModel(this.modelLocation, textures);

    MatrixStack tempStack = new MatrixStack();
    bakedModel = bakedModel.handlePerspective(transformType, tempStack);
    TransformationMatrix normalTransform = new TransformationMatrix(tempStack.last().pose());

    TransformationMatrix perspectiveTransform;
    if (aimingPct > 0) {
      perspectiveTransform =
          TransformationHelper.slerp(normalTransform, this.aimingTransform, aimingPct);
    } else {
      perspectiveTransform = normalTransform;
    }

    perspectiveTransform.push(matrixStack);
    {
      this.renderBakedModel(bakedModel, foil, color, transformType,
          matrixStack, renderTypeBuffer, packedLight, packedOverlay);

      matrixStack.pushPose();
      {
        this.renderMagazine(gun, skinTextureLocation, color, foil, transformType,
            matrixStack, renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.popPose();

      this.renderAttachments(gun, skinTextureLocation, color, foil, transformType,
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  protected final void renderBakedModel(IBakedModel bakedModel, boolean foil, int colour,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      matrixStack.translate(-0.5D, -0.5D, -0.5D);
      IVertexBuilder vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer,
          Atlases.translucentCullBlockSheet(), true, foil);
      List<BakedQuad> bakedQuads = bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE);
      MatrixStack.Entry matrixStackEntry = matrixStack.last();
      for (BakedQuad bakedQuad : bakedQuads) {
        float red = (colour >> 16 & 255) / 255.0F;
        float green = (colour >> 8 & 255) / 255.0F;
        float blue = (colour & 255) / 255.0F;
        vertexBuilder.addVertexData(matrixStackEntry, bakedQuad, red, green, blue, packedLight,
            packedOverlay, true);
      }
    }
    matrixStack.popPose();
  }

  private IBakedModel getBakedModel(ResourceLocation modelLocation,
      @Nullable Map<String, Either<RenderMaterial, String>> textures) {
    return this.cachedModels.computeIfAbsent(
        modelLocation.hashCode() + (textures == null ? 0 : textures.hashCode()), key -> {
          if (textures != null) {
            IUnbakedModel model = ModelLoader.instance().getModelOrMissing(modelLocation);
            if (model instanceof BlockModel) {
              BlockModel blockModel = (BlockModel) model;
              BlockModel overriddenModel = new BlockModel(null, new ArrayList<>(), textures, false,
                  null, ItemCameraTransforms.NO_TRANSFORMS, new ArrayList<>());
              overriddenModel.parent = blockModel;
              return overriddenModel.bake(ModelLoader.instance(), overriddenModel,
                  ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, modelLocation,
                  false);
            }
          }
          return this.minecraft.getModelManager().getModel(modelLocation);
        });
  }

  private void renderAttachments(
      Gun gun,
      @Nullable ResourceLocation skinTextureLocation,
      int colour,
      boolean foil,
      ItemCameraTransforms.TransformType transformType,
      float partialTicks,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    if (gun.hasIronSight()) {
      for (Pair<ResourceLocation, TransformationMatrix> ironSight : this.ironSights) {
        ironSight.getSecond().push(matrixStack);
        {
          IBakedModel bakedModel = this.getBakedModel(ironSight.getFirst(),
              ImmutableMap.of(GUN_TEXTURE_REFERENCE, Either.left(skinTextureLocation == null
                  ? this.getGunRenderMaterial()
                  : new RenderMaterial(PlayerContainer.BLOCK_ATLAS, skinTextureLocation))));
          this.renderBakedModel(bakedModel, foil, colour, transformType, matrixStack,
              renderTypeBuffer, packedLight, packedOverlay);
        }
        matrixStack.popPose();
      }
    }

    for (Attachment attachment : gun.getAttachments()) {
      TransformationMatrix transform =
          this.attachementTransforms.getOrDefault(attachment.getRegistryName(),
              TransformationMatrix.identity());
      transform.push(matrixStack);
      {
        IBakedModel bakedModel =
            this.getBakedModel(getAttachmentModelLocation(attachment.getRegistryName()), null);
        this.renderBakedModel(bakedModel, foil, colour, transformType, matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.popPose();
    }
  }

  private void renderMagazine(
      Gun gun,
      @Nullable ResourceLocation skinTextureLocation,
      int colour,
      boolean foil,
      ItemCameraTransforms.TransformType transformType,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    ItemStack magazineStack = gun.getAmmoProvider().getMagazineStack();
    if (!magazineStack.isEmpty()) {
      TransformationMatrix transform = this.magazineTransforms
          .getOrDefault(magazineStack.getItem().getRegistryName(), TransformationMatrix.identity());
      transform.push(matrixStack);
      {
        ResourceLocation modelLocation =
            getMagazineModelLocation(magazineStack.getItem().getRegistryName());

        IBakedModel magazineBakedModel = this.getBakedModel(modelLocation,
            ImmutableMap.of(GUN_TEXTURE_REFERENCE, Either.left(skinTextureLocation == null
                ? this.getGunRenderMaterial()
                : new RenderMaterial(PlayerContainer.BLOCK_ATLAS, skinTextureLocation))));

        this.renderBakedModel(magazineBakedModel, foil, colour, transformType, matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.popPose();
    }
  }

  private RenderMaterial getGunRenderMaterial() {
    IUnbakedModel unbakedModel = ModelLoader.instance().getModelOrMissing(this.modelLocation);
    return unbakedModel instanceof BlockModel
        ? ((BlockModel) unbakedModel).getMaterial(GUN_TEXTURE_REFERENCE)
        : new RenderMaterial(PlayerContainer.BLOCK_ATLAS, MissingTextureSprite.getLocation());
  }

  @Override
  public Collection<ResourceLocation> getModelDependencies(Item item) {
    final GunItem gun = (GunItem) item;
    final Set<ResourceLocation> dependencies = new HashSet<>();
    dependencies.addAll(this.ironSights.stream()
        .map(Pair::getFirst)
        .collect(Collectors.toSet()));
    dependencies.addAll(gun.getAcceptedAttachments().stream()
        .map(Attachment::getRegistryName)
        .map(GunRenderer::getAttachmentModelLocation)
        .collect(Collectors.toSet()));
    dependencies.addAll(gun.getAcceptedMagazines().stream()
        .map(Item::getRegistryName)
        .map(GunRenderer::getMagazineModelLocation)
        .collect(Collectors.toSet()));
    dependencies.add(this.modelLocation);
    return dependencies;
  }

  @Override
  public Collection<RenderMaterial> getMaterials() {
    return Collections.emptySet();
  }

  @Override
  public void refreshCachedModels() {
    this.cachedModels.clear();
  }

  @Override
  public ItemRendererType getType() {
    return ItemRendererTypes.GUN.get();
  }

  private static ResourceLocation getAttachmentModelLocation(ResourceLocation attachmentName) {
    return new ResourceLocation(attachmentName.getNamespace(),
        "attachment/" + attachmentName.getPath());
  }

  private static ResourceLocation getMagazineModelLocation(ResourceLocation magazineName) {
    return new ResourceLocation(magazineName.getNamespace(), "magazine/" + magazineName.getPath());
  }

  private enum HandTransform implements IStringSerializable {

    HELD_RIGHT_HANDED("held_right_handed"),
    HELD_LEFT_HANDED("held_left_handed"),
    AIMING_RIGHT_HANDED("aiming_right_handed"),
    AIMING_LEFT_HANDED("aiming_left_handed");

    public static final Codec<HandTransform> CODEC =
        IStringSerializable.fromEnum(HandTransform::values, HandTransform::byName);
    private static final Map<String, HandTransform> BY_NAME = Arrays.stream(values())
        .collect(Collectors.toMap(HandTransform::getSerializedName, Function.identity()));

    private final String name;

    private HandTransform(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static HandTransform byName(String name) {
      return BY_NAME.get(name);
    }
  }
}
