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

package com.craftingdead.core.client.renderer.item;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.client.model.geom.ModModelLayers;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.util.EasingFunction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.GunCraftSlotType;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.core.world.item.gun.skin.Paint;
import com.craftingdead.core.world.item.scope.Scope;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.model.TransformationHelper;

public class GunRenderer implements CombatSlotItemRenderer {

  private static final Pair<Transformation, Transformation> IDENTITY_HAND_TRANSFORMS =
      Pair.of(Transformation.identity(), Transformation.identity());

  private static final String GUN_TEXTURE_REFERENCE = "gun";

  private static final int FLASH_TEXTURE_CHANGE_TIMEOUT_MS = 250;

  private static final int SPRINT_TRANSITION_MS = 300;

  private static final int AIMING_TRANSITION_MS = 200;

  private static final Random random = new Random();

  private final Minecraft minecraft = Minecraft.getInstance();

  private final Map<Integer, BakedModel> cachedModels = new Int2ObjectOpenHashMap<>();

  private final GunItem item;

  private final GunRendererProperties properties;

  private ModelPart muzzleFlashModel;

  private long lastFlashTime = 0;

  private long sprintStartTimeMs;
  private boolean wasSprinting;

  private long aimingStartTimeMs;
  private boolean wasAiming;

  public GunRenderer(GunItem item, GunRendererProperties properties) {
    this.item = item;
    this.properties = properties;
  }

  @Override
  public void renderInCombatSlot(ItemStack itemStack, PoseStack poseStack, float partialTick,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    var gun = CapabilityUtil.getOrThrow(Gun.CAPABILITY, itemStack, Gun.class);
    this.renderGun(gun, false, true, itemStack.hasFoil(), 0.0F,
        ItemTransforms.TransformType.GUI, partialTick, poseStack, bufferSource, packedLight,
        packedOverlay);
  }

  @Override
  public void rotateCamera(ItemStack itemStack, LivingEntity livingEntity, float partialTick,
      Vector3f rotations) {
    var gun = CapabilityUtil.getOrThrow(Gun.CAPABILITY, itemStack, Gun.class);
    gun.getClient().getAnimationController().applyCamera(partialTick, rotations);
  }

  @Override
  public boolean handlePerspective(ItemStack item, ItemTransforms.TransformType transformType) {
    return switch (transformType) {
      case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND,
          FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND,
          FIXED, HEAD -> true;
      default -> false;
    };
  }

  @Override
  public void render(
      ItemStack itemStack,
      ItemTransforms.TransformType transformType,
      @Nullable LivingExtension<?, ?> living,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight,
      int packedOverlay) {

    var scope = itemStack.getCapability(Scope.CAPABILITY).orElse(null);
    var scoping = scope != null && living != null && scope.isScoping(living);
    if (scoping && scope.getOverlayTexture(living).isPresent()) {
      return;
    }

    var partialTick = this.minecraft.isPaused() ? 1.0F : this.minecraft.getFrameTime();

    var gun = CapabilityUtil.getOrThrow(Gun.CAPABILITY, itemStack, Gun.class);

    poseStack.pushPose();
    {
      switch (transformType) {
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          if (living != null && living.entity() instanceof AbstractClientPlayer player) {
            this.renderFirstPerson(player, itemStack, gun, scoping, transformType,
                partialTick, poseStack, bufferSource, packedLight, packedOverlay);
          }
          break;
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
          gun.getClient().getAnimationController().apply(partialTick, poseStack);
          this.renderGun(gun, true, false, itemStack.hasFoil(), 0.0F,
              transformType, partialTick, poseStack, bufferSource, packedLight,
              packedOverlay);
          break;
        case HEAD:
          this.properties.backTransform().push(poseStack);
          this.renderGun(gun, true, false, itemStack.hasFoil(), 0.0F,
              transformType, partialTick, poseStack, bufferSource, packedLight,
              packedOverlay);
          poseStack.popPose();
          break;
        case FIXED:
          poseStack.scale(1.5F, 1.5F, 1.5F);
          poseStack.translate(-0.25F, 0, 0);
          poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
          poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        default:
          this.renderGun(gun, true, false, itemStack.hasFoil(), 0.0F,
              transformType, partialTick, poseStack, bufferSource, packedLight,
              packedOverlay);
          break;
      }
    }
    poseStack.popPose();
  }

  private void renderFirstPersonArms(
      AbstractClientPlayer playerEntity,
      ItemStack itemStack,
      Gun gun,
      float aimingPct,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight) {

    final var playerRenderer =
        (PlayerRenderer) this.minecraft.getEntityRenderDispatcher().getRenderer(playerEntity);

    var mainArm = playerEntity.getMainArm();

    var heldTransforms = this.properties.handTransforms().getOrDefault(
        mainArm == HumanoidArm.RIGHT
            ? GunRendererProperties.HandTransform.HELD_RIGHT_HANDED
            : GunRendererProperties.HandTransform.HELD_LEFT_HANDED,
        IDENTITY_HAND_TRANSFORMS);

    Transformation rightHandTransforms;
    Transformation leftHandTransforms;
    if (aimingPct > 0) {
      var aimingTransforms = this.properties.handTransforms().getOrDefault(
          mainArm == HumanoidArm.RIGHT
              ? GunRendererProperties.HandTransform.AIMING_RIGHT_HANDED
              : GunRendererProperties.HandTransform.AIMING_LEFT_HANDED,
          IDENTITY_HAND_TRANSFORMS);
      rightHandTransforms = TransformationHelper.slerp(heldTransforms.getFirst(),
          aimingTransforms.getFirst(), aimingPct);
      leftHandTransforms = TransformationHelper.slerp(heldTransforms.getSecond(),
          aimingTransforms.getSecond(), aimingPct);
    } else {
      rightHandTransforms = heldTransforms.getFirst();
      leftHandTransforms = heldTransforms.getSecond();
    }

    // Right Arm
    poseStack.pushPose();
    {
      gun.getClient().getAnimationController().applyArm(
          playerEntity.getMainArm() == HumanoidArm.RIGHT
              ? InteractionHand.MAIN_HAND
              : InteractionHand.OFF_HAND,
          HumanoidArm.RIGHT,
          partialTicks, poseStack);
      poseStack.translate(0.75F, -0.75F, -0.6F);

      rightHandTransforms.push(poseStack);
      {
        poseStack.mulPose(Vector3f.YP.rotationDegrees(95.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(100.0F));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

        poseStack.translate(0.9F, -0.75F, 0.5F);

        playerRenderer.renderRightHand(poseStack, bufferSource, packedLight, playerEntity);
      }
      poseStack.popPose();
    }
    poseStack.popPose();

    // Left Arm
    poseStack.pushPose();
    {
      gun.getClient().getAnimationController().applyArm(
          playerEntity.getMainArm() == HumanoidArm.LEFT ? InteractionHand.MAIN_HAND
              : InteractionHand.OFF_HAND,
          HumanoidArm.LEFT,
          partialTicks, poseStack);

      poseStack.translate(0.75F, -0.75F, -0.75F);

      leftHandTransforms.push(poseStack);
      {
        poseStack.mulPose(Vector3f.YP.rotationDegrees(95.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(95.0F));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(120.0F));

        poseStack.translate(0.05F, -1.05F, 0.30F);

        playerRenderer.renderLeftHand(poseStack, bufferSource, packedLight, playerEntity);
      }
      poseStack.popPose();
    }
    poseStack.popPose();
  }

  private void renderFlash(Gun gun, PoseStack matrixStack, MultiBufferSource bufferSource,
      int packedOverlay) {
    matrixStack.pushPose();
    {
      var currentTime = Util.getMillis();
      var deltaTime = currentTime - this.lastFlashTime;

      int texture;
      if (deltaTime > FLASH_TEXTURE_CHANGE_TIMEOUT_MS) {
        this.lastFlashTime = currentTime;
        deltaTime = FLASH_TEXTURE_CHANGE_TIMEOUT_MS;
      }

      // Every n milliseconds, change the muzzle flash texture
      texture = (int) ((deltaTime / 100) + 1);
      var randomScale = (random.nextInt(8) + 3) / 10.0F;

      var transform = this.properties.muzzleFlashTransform();

      var scale = transform.getScale();
      matrixStack.scale(
          scale.x() + randomScale,
          scale.y() + randomScale,
          scale.z() + randomScale);

      var x = transform.getTranslation().x();
      var y = transform.getTranslation().y();
      var z = transform.getTranslation().z();
      if (gun.getAttachments().containsValue(Attachments.SUPPRESSOR.get())) {
        z -= 0.4;
      }

      matrixStack.translate(
          x - randomScale * x / 2,
          y - randomScale * y / 2,
          z - randomScale * z / 2);

      var flashBuffer = bufferSource.getBuffer(RenderType.beaconBeam(
          new ResourceLocation(CraftingDead.ID, "textures/flash/flash" + texture + ".png"),
          true));
      this.muzzleFlashModel.render(matrixStack, flashBuffer, RenderUtil.FULL_LIGHT,
          packedOverlay, 1.0F, 1.0F, 1.0F, randomScale + 0.5F);
    }
    matrixStack.popPose();
  }

  private void renderFirstPerson(
      AbstractClientPlayer playerEntity,
      ItemStack itemStack,
      Gun gun,
      boolean aiming,
      ItemTransforms.TransformType transformType,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    final var flash = gun.getClient().isFlashing();
    if (flash) {
      packedLight = RenderUtil.FULL_LIGHT;
    }

    // ============== Animations ==============

    if (this.wasSprinting != playerEntity.isSprinting()) {
      this.sprintStartTimeMs = Util.getMillis();
      this.wasSprinting = playerEntity.isSprinting();
    }

    var sprintingPct = Mth.clamp(
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

    var aimingPct = Mth.clamp(
        (float) (Util.getMillis() - this.aimingStartTimeMs) / AIMING_TRANSITION_MS,
        0.0F, 1.0F);
    if (!aiming) {
      aimingPct = 1.0F - aimingPct;
    }

    aimingPct = EasingFunction.SINE_IN_OUT.apply(aimingPct);

    // ============== Animations End ==============

    if (sprintingPct > 0) {
      TransformationHelper
          .slerp(Transformation.identity(), this.properties.sprintingTransform(), sprintingPct)
          .push(poseStack);
    } else {
      poseStack.pushPose();
    }
    {
      if (!aiming && flash) {
        this.renderFlash(gun, poseStack, renderTypeBuffer, packedOverlay);
      }

      this.renderFirstPersonArms(playerEntity, itemStack, gun, aimingPct, partialTicks,
          poseStack, renderTypeBuffer, packedLight);

      gun.getClient().getAnimationController().apply(partialTicks, poseStack);

      this.renderGun(gun, true, false, itemStack.hasFoil(), aimingPct, transformType,
          partialTicks, poseStack, renderTypeBuffer, packedLight, packedOverlay);
    }
    poseStack.popPose();
  }

  private void renderGun(
      Gun gun,
      boolean renderAttachments,
      boolean renderDefaultMagazine,
      boolean foil,
      float aimingPct,
      ItemTransforms.TransformType transformType,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight,
      int packedOverlay) {

    var skinTextureLocation = gun.getSkin() == null
        ? null
        : gun.getSkin().getTextureLocation(gun.getItemStack().getItem().getRegistryName());

    final var color = gun.getPaintStack().getCapability(Paint.CAPABILITY)
        .resolve()
        .map(paint -> paint.getColor().orElse(0xFFFFFFFF))
        .orElse(0xFFFFFFFF);

    Map<String, Either<Material, String>> textures = null;
    if (skinTextureLocation != null) {
      textures = Map.of(GUN_TEXTURE_REFERENCE,
          Either.left(new Material(InventoryMenu.BLOCK_ATLAS, skinTextureLocation)));
    }
    var bakedModel = this.getBakedModel(this.properties.modelLocation(), textures);

    var tempPoseStack = new PoseStack();
    bakedModel = bakedModel.handlePerspective(transformType, tempPoseStack);
    var normalTransform = new Transformation(tempPoseStack.last().pose());

    var perspectiveTransform = aimingPct > 0
        ? TransformationHelper.slerp(normalTransform,
            gun.hasIronSight()
                ? this.properties.aimTransform()
                : this.properties.scopeAimTransform().getOrDefault(gun.getAttachments()
                    .get(GunCraftSlotType.OVERBARREL_ATTACHMENT).getRegistryName(), this.properties.aimTransform()),
            aimingPct)
        : normalTransform;

    perspectiveTransform.push(poseStack);
    {
      this.renderBakedModel(bakedModel, foil, color, transformType,
          poseStack, bufferSource, packedLight, packedOverlay);

      var magazineStack = renderDefaultMagazine
          ? gun.getDefaultMagazineStack()
          : gun.getAmmoProvider().getMagazineStack();
      if (!magazineStack.isEmpty()) {
        this.renderMagazine(magazineStack, skinTextureLocation, color, foil, transformType,
            poseStack, bufferSource, packedLight, packedOverlay);
      }

      if (renderAttachments) {
        this.renderAttachments(gun, skinTextureLocation, color, foil, transformType,
            partialTick, poseStack, bufferSource, packedLight, packedOverlay);
      }
    }
    poseStack.popPose();
  }

  protected final void renderBakedModel(BakedModel bakedModel, boolean foil, int colour,
      ItemTransforms.TransformType transformType, PoseStack poseStack,
      MultiBufferSource renderTypeBuffer, int packedLight, int packedOverlay) {
    poseStack.pushPose();
    {
      poseStack.translate(-0.5D, -0.5D, -0.5D);
      VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer,
          Sheets.translucentCullBlockSheet(), true, foil);
      var bakedQuads = bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE);
      var pose = poseStack.last();
      for (var bakedQuad : bakedQuads) {
        var red = (colour >> 16 & 255) / 255.0F;
        var green = (colour >> 8 & 255) / 255.0F;
        var blue = (colour & 255) / 255.0F;
        vertexBuilder.putBulkData(pose, bakedQuad, red, green, blue, packedLight,
            packedOverlay, true);
      }
    }
    poseStack.popPose();
  }

  private BakedModel getBakedModel(ResourceLocation modelLocation,
      @Nullable Map<String, Either<Material, String>> textures) {
    return this.cachedModels.computeIfAbsent(
        modelLocation.hashCode() + (textures == null ? 0 : textures.hashCode()), key -> {
          if (textures != null) {
            var model = ForgeModelBakery.instance().getModelOrMissing(modelLocation);
            if (model instanceof BlockModel blockModel) {
              BlockModel overriddenModel = new BlockModel(null, List.of(), textures, false,
                  null, ItemTransforms.NO_TRANSFORMS, List.of());
              overriddenModel.parent = blockModel;
              return overriddenModel.bake(ForgeModelBakery.instance(), overriddenModel,
                  ForgeModelBakery.defaultTextureGetter(), SimpleModelState.IDENTITY, modelLocation,
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
      ItemTransforms.TransformType transformType,
      float partialTicks,
      PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer,
      int packedLight,
      int packedOverlay) {

    if (gun.hasIronSight()) {
      for (var ironSight : this.properties.ironSights()) {
        ironSight.getSecond().push(matrixStack);
        {
          var bakedModel = this.getBakedModel(ironSight.getFirst(),
              Map.of(GUN_TEXTURE_REFERENCE, Either.left(skinTextureLocation == null
                  ? this.getGunRenderMaterial()
                  : new Material(InventoryMenu.BLOCK_ATLAS, skinTextureLocation))));
          this.renderBakedModel(bakedModel, foil, colour, transformType, matrixStack,
              renderTypeBuffer, packedLight, packedOverlay);
        }
        matrixStack.popPose();
      }
    }

    for (var attachment : gun.getAttachments().values()) {
      var transform = this.properties.attachmentTransforms()
          .getOrDefault(attachment.getRegistryName(), Transformation.identity());
      transform.push(matrixStack);
      {
        var bakedModel =
            this.getBakedModel(getAttachmentModelLocation(attachment.getRegistryName()), null);
        this.renderBakedModel(bakedModel, foil, colour, transformType, matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.popPose();
    }
  }

  private void renderMagazine(
      ItemStack magazineStack,
      @Nullable ResourceLocation skinTextureLocation,
      int color,
      boolean foil,
      ItemTransforms.TransformType transformType,
      PoseStack poseStack,
      MultiBufferSource renderTypeBuffer,
      int packedLight,
      int packedOverlay) {
    var transform = this.properties.magazineTransforms()
        .getOrDefault(magazineStack.getItem().getRegistryName(), Transformation.identity());
    transform.push(poseStack);
    {
      var modelLocation = getMagazineModelLocation(magazineStack.getItem().getRegistryName());

      var magazineBakedModel = this.getBakedModel(modelLocation,
          Map.of(GUN_TEXTURE_REFERENCE, Either.left(skinTextureLocation == null
              ? this.getGunRenderMaterial()
              : new Material(InventoryMenu.BLOCK_ATLAS, skinTextureLocation))));

      this.renderBakedModel(magazineBakedModel, foil, color, transformType, poseStack,
          renderTypeBuffer, packedLight, packedOverlay);
    }
    poseStack.popPose();
  }

  private Material getGunRenderMaterial() {
    var unbakedModel =
        ForgeModelBakery.instance().getModelOrMissing(this.properties.modelLocation());
    return unbakedModel instanceof BlockModel model
        ? model.getMaterial(GUN_TEXTURE_REFERENCE)
        : new Material(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation());
  }

  @Override
  public Collection<ResourceLocation> getModelDependencies() {
    final Set<ResourceLocation> dependencies = new HashSet<>();
    dependencies.addAll(this.properties.ironSights().stream()
        .map(Pair::getFirst)
        .collect(Collectors.toSet()));
    dependencies.addAll(this.item.getAcceptedAttachments().stream()
        .map(Attachment::getRegistryName)
        .map(GunRenderer::getAttachmentModelLocation)
        .collect(Collectors.toSet()));
    dependencies.addAll(this.item.getAcceptedMagazines().stream()
        .map(Item::getRegistryName)
        .map(GunRenderer::getMagazineModelLocation)
        .collect(Collectors.toSet()));
    dependencies.add(this.properties.modelLocation());
    return dependencies;
  }

  @Override
  public Collection<Material> getMaterials() {
    return Collections.emptySet();
  }

  @Override
  public void refreshCachedModels(Function<ModelLayerLocation, ModelPart> modelBaker) {
    this.cachedModels.clear();
    this.muzzleFlashModel = modelBaker.apply(ModModelLayers.MUZZLE_FLASH);
  }

  @Override
  public ItemRendererType<?, ?> getType() {
    return ItemRendererTypes.GUN;
  }

  private static ResourceLocation getAttachmentModelLocation(ResourceLocation attachmentName) {
    return new ResourceLocation(attachmentName.getNamespace(),
        "attachment/" + attachmentName.getPath());
  }

  private static ResourceLocation getMagazineModelLocation(ResourceLocation magazineName) {
    return new ResourceLocation(magazineName.getNamespace(), "magazine/" + magazineName.getPath());
  }

  public static LayerDefinition createMuzzleFlashBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    root.addOrReplaceChild("box0",
        CubeListBuilder.create()
            .mirror()
            .texOffs(1, 1)
            .addBox(0F, 0F, 0F, 8, 8, 0)
            .mirror(false),
        PartPose.offset(-4F, -4F, 0F));
    root.addOrReplaceChild("box1",
        CubeListBuilder.create()
            .mirror()
            .texOffs(9, 1)
            .addBox(-4F, 0F, 0F, 8, 0, 15)
            .mirror(false),
        PartPose.offsetAndRotation(0F, 0F, -15F, 0F, 0F, -0.7853982F));
    root.addOrReplaceChild("box2",
        CubeListBuilder.create()
            .mirror()
            .texOffs(1, 17)
            .addBox(-4F, 0F, 0F, 8, 0, 15)
            .mirror(false),
        PartPose.offsetAndRotation(0F, 0F, -15F, 0F, 0F, -2.373648F));
    return LayerDefinition.create(mesh, 64, 64);
  }

  public GunRendererProperties getProperties() {
    return this.properties;
  }
}
