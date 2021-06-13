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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.client.animation.GunAnimation;
import com.craftingdead.core.client.renderer.item.model.ModelMuzzleFlash;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.gun.GunClient;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.Attachments;
import com.craftingdead.core.world.gun.type.AbstractGunType;
import com.craftingdead.core.world.item.scope.Scope;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Either;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.data.EmptyModelData;

public abstract class GunRenderer implements CustomItemRenderer {

  private static final int FLASH_TEXTURE_CHANGE_TIMEOUT_MS = 250;

  private static final int SPRINT_TRANSITION_MS = 200;

  private static final Random random = new Random();

  protected Minecraft minecraft = Minecraft.getInstance();

  private final Map<Integer, IBakedModel> cachedModels = new HashMap<>();

  private final Model muzzleFlashModel = new ModelMuzzleFlash();

  private final ResourceLocation registryName;

  private final AbstractGunType gunType;

  protected float muzzleFlashX = 0.4F;
  protected float muzzleFlashY = 0.2F;
  protected float muzzleFlashZ = -1.8F;
  protected float muzzleScale = 2F;

  private long lastFlashTime = 0;

  private long sprintStartTimeMs;

  private boolean wasSprinting;

  public GunRenderer(ResourceLocation registryName, AbstractGunType gunType) {
    this.registryName = registryName;
    this.gunType = gunType;
  }

  public static int getColour(ItemStack itemStack) {
    // if (itemStack.getItem() instanceof IDyeableArmorItem) {
    // IDyeableArmorItem item = (IDyeableArmorItem) itemStack.getItem();
    // if (item.hasCustomColor(itemStack)) {
    // return item.getColor(itemStack);
    // }
    // }
    return 0xFFFFFFFF;
  }

  @Override
  public boolean handleRenderType(ItemStack item,
      ItemCameraTransforms.TransformType transformType) {
    switch (transformType) {
      case THIRD_PERSON_LEFT_HAND:
      case THIRD_PERSON_RIGHT_HAND:
      case FIRST_PERSON_LEFT_HAND:
      case FIRST_PERSON_RIGHT_HAND:
      case HEAD:
        return true;
      default:
        return false;
    }
  }

  @Override
  public void renderGeneric(ItemStack itemStack, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {

    final float partialTicks =
        this.minecraft.isPaused() ? 1.0F : this.minecraft.getFrameTime();

    final Gun gun = itemStack.getCapability(Capabilities.GUN)
        .orElseThrow(() -> new IllegalArgumentException("Gun expected"));

    matrixStack.pushPose();
    {
      matrixStack.scale(1.25F, 1.25F, 1.25F);
      matrixStack.mulPose(Vector3f.XP.rotation(3.1F));
      matrixStack.mulPose(Vector3f.YP.rotation(3.14F));

      this.applyGenericTransforms(gun, matrixStack);

      int colour = getColour(itemStack);

      this.renderGunModel(itemStack.hasFoil(), colour, gun,
          ItemCameraTransforms.TransformType.FIXED, matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);
      this.renderMainGunAmmo(gun, itemStack.hasFoil(), colour,
          ItemCameraTransforms.TransformType.FIXED,
          matrixStack, renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAttachments(gun, itemStack.hasFoil(), colour,
          ItemCameraTransforms.TransformType.FIXED,
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  @Override
  public void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType,
      LivingEntity entity, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {

    final float partialTicks =
        this.minecraft.isPaused() ? 1.0F : this.minecraft.getFrameTime();

    final Gun gun = itemStack.getCapability(Capabilities.GUN)
        .orElseThrow(() -> new IllegalArgumentException("Gun expected"));
    final GunClient gunClient = gun.getClient();

    final boolean scopeOverlayActive =
        gun instanceof Scope && ((Scope) gun).isAiming(entity)
            && ((Scope) gun).getOverlayTexture(entity).isPresent();
    if (scopeOverlayActive) {
      return;
    }

    final boolean leftHanded =
        transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
            || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;

    matrixStack.pushPose();
    {
      this.applyLegacyTransforms(matrixStack, transformType, leftHanded);
      switch (transformType) {
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          if (entity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
            this.renderFirstPerson(playerEntity, itemStack, gun, gunClient, transformType,
                partialTicks,
                matrixStack,
                renderTypeBuffer,
                packedLight, packedOverlay);
          }
          break;
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
          this.renderThirdPerson(entity, itemStack, gun, transformType, partialTicks, matrixStack,
              renderTypeBuffer, packedLight,
              packedOverlay);
          break;
        case HEAD:
          this.renderOnBack(itemStack.hasFoil(), getColour(itemStack), gun, partialTicks,
              matrixStack, renderTypeBuffer, packedLight, packedOverlay);
          break;
        default:
          break;
      }
    }
    matrixStack.popPose();
  }

  /**
   * Set up transformations to simulate the render state in Minecraft 1.6.4.
   * 
   * @param matrixStack - {@link MatrixStack} to transform
   * @param thirdPerson - if we are in third person or not
   * @param leftHanded - if the player is left handed
   */
  private final void applyLegacyTransforms(MatrixStack matrixStack,
      ItemCameraTransforms.TransformType transformType,
      boolean leftHanded) {

    final boolean thirdPerson =
        transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
            || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;

    // TODO Left hand support
    if (leftHanded && !thirdPerson) {
      matrixStack.translate(1.12F, 0, 0);
    }

    matrixStack.scale(0.5F, 0.5F, 0.5F);
    matrixStack.translate(-0.5, -0.5F, -0.5F);

    if (thirdPerson) {
      matrixStack.translate(-1F, 0, 0.5F);
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(50));
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(70));
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-35));
    }

    if (transformType == ItemCameraTransforms.TransformType.HEAD) {
      matrixStack.translate(-1F, 2.5F, 3.75F);


      matrixStack.mulPose(Vector3f.YP.rotationDegrees(270));
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(25F));


      matrixStack.scale(2F, 2F, 2F);
      matrixStack.translate(-2, -2F, -2F);
    }

    matrixStack.translate(0.3125F, 0.47625F, 0.8625F);

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(0.0625F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(95.0625F));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(335.0F));
    matrixStack.translate(-0.9375F, -0.0625F, 0.0F);
  }

  private final void renderFirstPersonArms(AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack, Gun gun, GunClient gunClient,
      ItemCameraTransforms.TransformType transformType,
      float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight) {
    final PlayerRenderer playerRenderer =
        (PlayerRenderer) this.minecraft.getEntityRenderDispatcher().getRenderer(playerEntity);

    // Right Hand
    matrixStack.pushPose();
    {
      gunClient.getAnimationController().applyTransforms(playerEntity, itemStack,
          GunAnimation.RIGHT_HAND, transformType, matrixStack, partialTicks);

      this.minecraft.getTextureManager().bind(playerEntity.getSkinTextureLocation());
      matrixStack.translate(1F, 1F, 0F);
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(125.0F));
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
      matrixStack.translate(0.19F, -1.4F, 0.5F);
      this.applyHandTransforms(gun, true, matrixStack);
      playerRenderer.renderRightHand(matrixStack, renderTypeBuffer, packedLight,
          playerEntity);

    }
    matrixStack.popPose();

    // Left Hand
    matrixStack.pushPose();
    {
      gunClient.getAnimationController().applyTransforms(playerEntity, itemStack,
          GunAnimation.LEFT_HAND, transformType, matrixStack, partialTicks);

      this.minecraft.getTextureManager().bind(playerEntity.getSkinTextureLocation());
      matrixStack.translate(1.5F, 0.0F, 0.0F);
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(120.0F));
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(120.0F));
      matrixStack.translate(0.3F, -1.5F, -0.12F);
      this.applyHandTransforms(gun, false, matrixStack);
      matrixStack.scale(1.0F, 1.2F, 1.0F);
      playerRenderer.renderLeftHand(matrixStack, renderTypeBuffer, packedLight,
          playerEntity);
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

      // Every x milliseconds, change the muzzle flash texture
      texture = (int) ((deltaTime / 100) + 1);

      float scale = (random.nextInt(5) + 3) / 10.0F;
      matrixStack.mulPose(new Vector3f(-0.08F, 1.0F, 0.0F).rotationDegrees(-85));
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(30));
      matrixStack.scale(this.muzzleScale + scale, this.muzzleScale + scale,
          this.muzzleScale + scale);
      if (gun.getAttachments().contains(Attachments.SUPPRESSOR.get())) {
        this.muzzleFlashZ -= 0.4;
      }
      matrixStack.translate(this.muzzleFlashX - scale * this.muzzleFlashX / 2,
          this.muzzleFlashY - scale * this.muzzleFlashY / 2,
          this.muzzleFlashZ - scale * this.muzzleFlashZ / 2);

      IVertexBuilder flashVertexBuilder = renderTypeBuffer
          .getBuffer(this.muzzleFlashModel.renderType(new ResourceLocation(CraftingDead.ID,
              "textures/flash/flash" + texture + ".png")));
      this.muzzleFlashModel.renderToBuffer(matrixStack, flashVertexBuilder, RenderUtil.FULL_LIGHT,
          packedOverlay, 1.0F, 1.0F, 1.0F, scale + 0.5F);
    }
    matrixStack.popPose();
  }

  private final void renderFirstPerson(AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack, Gun gun, GunClient gunClient,
      ItemCameraTransforms.TransformType transformType,
      float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {

    final boolean aiming =
        gun instanceof Scope && ((Scope) gun).isAiming(playerEntity);
    final boolean flash = gunClient.isFlashing();
    if (flash) {
      packedLight = RenderUtil.FULL_LIGHT;
    }

    matrixStack.pushPose();
    {
      if (aiming) {
        this.applyAimingTransforms(gun, matrixStack);
      } else {
        if (!playerEntity.isSprinting()) {
          this.renderFirstPersonArms(playerEntity, itemStack, gun, gunClient, transformType,
              partialTicks, matrixStack, renderTypeBuffer, packedLight);
        }

        if (flash) {
          this.renderFlash(gun, matrixStack, renderTypeBuffer, packedOverlay);
        }

        this.applyFirstPersonTransforms(gun, matrixStack);
      }

      if (this.wasSprinting != playerEntity.isSprinting()) {
        this.sprintStartTimeMs = Util.getMillis();
        this.wasSprinting = playerEntity.isSprinting();
      }

      float zeroToOneFadePct = MathHelper.clamp(
          (float) (Util.getMillis() - this.sprintStartTimeMs) / SPRINT_TRANSITION_MS,
          0.0F, 1.0F);
      if (!playerEntity.isSprinting()) {
        zeroToOneFadePct = 1.0F - zeroToOneFadePct;
      }
      this.applySprintingTransforms(matrixStack, RenderUtil.easeInOutSine(zeroToOneFadePct));

      gunClient.getAnimationController().applyTransforms(playerEntity, itemStack,
          GunAnimation.BODY, transformType, matrixStack, partialTicks);

      int colour = getColour(itemStack);

      this.renderGunModel(itemStack.hasFoil(), colour, gun, transformType, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);

      matrixStack.pushPose();
      {
        gunClient.getAnimationController().applyTransforms(playerEntity, itemStack,
            GunAnimation.MAGAZINE, transformType, matrixStack, partialTicks);

        this.renderMainGunAmmo(gun, itemStack.hasFoil(), colour, transformType, matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.popPose();

      this.renderMainGunAttachments(gun, itemStack.hasFoil(), colour, transformType,
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  private final void renderThirdPerson(LivingEntity livingEntity, ItemStack itemStack, Gun gun,
      ItemCameraTransforms.TransformType transformType, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    matrixStack.pushPose();
    {
      int colour = getColour(itemStack);
      this.applyThirdPersonTransforms(gun, matrixStack);
      gun.getClient().getAnimationController().applyTransforms(livingEntity, itemStack,
          GunAnimation.BODY, transformType, matrixStack, this.minecraft.getFrameTime());
      this.renderGunModel(itemStack.hasFoil(),
          colour, gun, transformType, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAmmo(gun, itemStack.hasFoil(), colour, transformType, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAttachments(gun, itemStack.hasFoil(), colour, transformType,
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  public void renderOnBack(boolean glint, int colour,
      Gun gun, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      this.applyWearingTransforms(gun, matrixStack);
      this.renderGunModel(glint, colour, gun, ItemCameraTransforms.TransformType.HEAD,
          matrixStack, renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAmmo(gun, glint, colour, ItemCameraTransforms.TransformType.HEAD,
          matrixStack, renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAttachments(gun, glint, colour, ItemCameraTransforms.TransformType.HEAD,
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  protected final void renderBakedModel(IBakedModel bakedModel, boolean glint, int colour,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
      matrixStack.translate(0, -1.45F, 0);

      bakedModel = bakedModel.handlePerspective(transformType, matrixStack);
      IVertexBuilder vertexBuilder =
          ItemRenderer.getFoilBuffer(renderTypeBuffer, Atlases.translucentCullBlockSheet(), true,
              glint);
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

  private final void renderGunModel(boolean glint, int colour, Gun gun,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      matrixStack.translate(0.0D, 0.05D, 0.0D);
      IBakedModel bakedModel = this.getBakedModel(this.getGunModelLocation(),
          gun.getPaintStack().isEmpty()
              ? null
              : ImmutableMap.of("base", Either.left(new RenderMaterial(PlayerContainer.BLOCK_ATLAS,
                  this.getPaintSkinLocation(gun.getPaintStack().getItem().getRegistryName())))));
      this.renderBakedModel(bakedModel, glint, colour,
          transformType, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.popPose();
  }

  protected final RenderMaterial getGunMaterial(Gun gun) {
    IUnbakedModel unbakedModel =
        ModelLoader.instance().getModelOrMissing(this.getGunModelLocation());
    ResourceLocation skin =
        gun.getPaintStack().isEmpty() ? null : gun.getPaintStack().getItem().getRegistryName();
    if (unbakedModel instanceof BlockModel) {
      if (skin != null) {
        return new RenderMaterial(PlayerContainer.BLOCK_ATLAS,
            new ResourceLocation(skin.getNamespace(),
                "gun/" + this.registryName.getPath() + "_" + skin.getPath()));
      }
      return ((BlockModel) unbakedModel).getMaterial("base");
    }
    return new RenderMaterial(PlayerContainer.BLOCK_ATLAS,
        MissingTextureSprite.getLocation());
  }

  protected final IBakedModel getBakedModel(ResourceLocation modelLocation,
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

  private void renderMainGunAttachments(Gun gun, boolean glint, int colour,
      ItemCameraTransforms.TransformType transformType, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    if (gun.hasIronSight()) {
      matrixStack.pushPose();
      {
        this.renderAdditionalParts(gun, partialTicks, matrixStack, renderTypeBuffer,
            packedLight, packedOverlay);
      }
      matrixStack.popPose();
    }
    matrixStack.pushPose();
    {
      float scale = 0.1F;
      matrixStack.scale(scale, scale, scale);
      for (Attachment attachment : gun.getAttachments()) {
        matrixStack.pushPose();
        {
          this.applyAttachmentTransforms(attachment, matrixStack);
          scale = 10F;
          matrixStack.scale(scale, scale, scale);

          final IBakedModel bakedModel = this.getBakedModel(
              this.getAttachmentModelLocation(attachment.getRegistryName()), null);
          this.renderBakedModel(bakedModel, glint, colour,
              transformType, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
        }
        matrixStack.popPose();
      }
    }
    matrixStack.popPose();
  }

  private void renderMainGunAmmo(Gun gun, boolean glint, int colour,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    ItemStack magazineStack = gun.getAmmoProvider().getMagazineStack();
    if (!magazineStack.isEmpty()) {
      matrixStack.pushPose();
      {
        float scale = 0.1F;
        matrixStack.scale(scale, scale, scale);

        this.applyMagazineTransforms(magazineStack, matrixStack);

        scale = 10F;
        matrixStack.scale(scale, scale, scale);

        final ResourceLocation modelLocation =
            this.getMagazineModelLocation(magazineStack.getItem().getRegistryName());
        final IBakedModel magazineBakedModel;
        if (gun.getAmmoProvider().getExpectedMagazine().hasCustomTexture()) {
          magazineBakedModel = this.getBakedModel(modelLocation, null);
        } else {
          magazineBakedModel = this.getBakedModel(modelLocation,
              ImmutableMap.of("base", Either.left(this.getGunMaterial(gun))));
        }

        this.renderBakedModel(magazineBakedModel, glint, colour,
            transformType, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.popPose();
    }
  }

  protected abstract void renderAdditionalParts(Gun gun,
      float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay);

  protected abstract void applyGenericTransforms(Gun gun, MatrixStack matrixStack);

  protected abstract void applyThirdPersonTransforms(Gun gun, MatrixStack matrixStack);

  protected abstract void applyFirstPersonTransforms(Gun gun, MatrixStack matrixStack);

  protected abstract void applyAimingTransforms(Gun gun, MatrixStack matrixStack);

  protected abstract void applyWearingTransforms(Gun gun, MatrixStack matrixStack);

  protected abstract void applyMagazineTransforms(ItemStack magazineStack, MatrixStack matrixStack);

  protected abstract void applyAttachmentTransforms(Attachment attachment, MatrixStack matrixStack);

  protected abstract void applyHandTransforms(Gun gun, boolean rightHanded,
      MatrixStack matrixStack);

  protected void applySprintingTransforms(MatrixStack matrixStack, float pct) {
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(pct * -50));
    matrixStack.translate(pct * 0.3F, 0.0F, pct * 0.3F);
  }

  protected ResourceLocation getGunModelLocation() {
    return new ResourceLocation(this.registryName.getNamespace(),
        "gun/" + this.registryName.getPath());
  }

  protected ResourceLocation getAttachmentModelLocation(ResourceLocation attachmentName) {
    return new ResourceLocation(attachmentName.getNamespace(),
        "attachment/" + attachmentName.getPath());
  }

  protected ResourceLocation getMagazineModelLocation(ResourceLocation magazineName) {
    return new ResourceLocation(magazineName.getNamespace(), "magazine/" + magazineName.getPath());
  }

  protected ResourceLocation getPaintSkinLocation(ResourceLocation paintName) {
    return new ResourceLocation(paintName.getNamespace(),
        "gun/" + this.registryName.getPath() + "_" + paintName.getPath());
  }

  @Override
  public Collection<ResourceLocation> getModelDependencies() {
    final Set<ResourceLocation> dependencies = new HashSet<>();
    dependencies.addAll(this.gunType.getAcceptedAttachments().stream()
        .map(Attachment::getRegistryName)
        .map(this::getAttachmentModelLocation)
        .collect(Collectors.toSet()));
    dependencies.addAll(this.gunType.getAcceptedMagazines().stream()
        .map(Item::getRegistryName)
        .map(this::getMagazineModelLocation)
        .collect(Collectors.toSet()));
    dependencies.add(this.getGunModelLocation());
    return dependencies;
  }

  @Override
  public Collection<ResourceLocation> getAdditionalModelTextures() {
    final Set<ResourceLocation> textures = new HashSet<>();
    textures.addAll(this.gunType.getAcceptedPaints().stream()
        .map(Item::getRegistryName)
        .map(this::getPaintSkinLocation)
        .collect(Collectors.toSet()));
    return textures;
  }

  @Override
  public void refreshCachedModels() {
    this.cachedModels.clear();
  }
}
