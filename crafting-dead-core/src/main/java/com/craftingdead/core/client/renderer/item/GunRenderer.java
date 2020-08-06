package com.craftingdead.core.client.renderer.item;


import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimation;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.capability.paint.IPaint;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.client.renderer.item.model.ModelMuzzleFlash;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.item.MagazineItem;
import com.craftingdead.core.item.ModItems;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.data.EmptyModelData;

public abstract class GunRenderer implements IItemRenderer {

  private static final int FULL_LIGHT = 0xF000F0;

  private static final Random random = new Random();

  protected Minecraft mc = Minecraft.getInstance();

  private final Map<Integer, IBakedModel> cachedModels = new HashMap<>();

  private final Model muzzleFlashModel = new ModelMuzzleFlash();

  private final Supplier<? extends GunItem> gunItem;

  protected float muzzleFlashX = 0.4F;
  protected float muzzleFlashY = 0.2F;
  protected float muzzleFlashZ = -1.8F;
  protected float muzzleScale = 2F;

  public GunRenderer(Supplier<? extends GunItem> gunItem) {
    this.gunItem = gunItem;
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
  public void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType,
      LivingEntity entity, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {

    final IGun gun = itemStack.getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new InvalidParameterException("Gun expected"));

    final IScope scope = itemStack.getCapability(ModCapabilities.SCOPE).orElse(null);
    final boolean scopeOverlayActive = scope != null && scope.isAiming(entity, itemStack)
        && scope.getOverlayTexture(entity, itemStack).isPresent();
    if (scopeOverlayActive) {
      return;
    }

    final boolean leftHanded =
        transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
            || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;

    matrixStack.push();
    {
      this.applyLegacyTransforms(matrixStack, transformType, leftHanded);
      switch (transformType) {
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          if (entity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
            this.renderFirstPerson(playerEntity, itemStack, gun, scope, transformType, matrixStack,
                renderTypeBuffer,
                packedLight, packedOverlay);
          }
          break;
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
          this.renderThirdPerson(entity, itemStack, gun, transformType, matrixStack,
              renderTypeBuffer, packedLight,
              packedOverlay);
          break;
        case HEAD:
          this.renderOnBack(entity, itemStack, gun, matrixStack, renderTypeBuffer, packedLight,
              packedOverlay);
          break;
        default:
          break;
      }
    }
    matrixStack.pop();
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
      matrixStack.rotate(Vector3f.XP.rotationDegrees(50));
      matrixStack.rotate(Vector3f.YP.rotationDegrees(70));
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(-35));
    }

    if (transformType == ItemCameraTransforms.TransformType.HEAD) {
      matrixStack.translate(-1F, 2.5F, 3.75F);


      matrixStack.rotate(Vector3f.YP.rotationDegrees(270));
      matrixStack.rotate(Vector3f.XP.rotationDegrees(25F));


      matrixStack.scale(2F, 2F, 2F);
      matrixStack.translate(-2, -2F, -2F);
    }

    matrixStack.translate(0.3135F, 0.4765F, 0.8625F);

    matrixStack.rotate(Vector3f.XP.rotationDegrees(0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(95));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(335.0F));
    matrixStack.translate(-0.9375F, -0.0625F, 0.0F);
  }

  private final void renderFirstPersonArms(AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack, IGun gun, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight) {
    final PlayerRenderer playerRenderer =
        (PlayerRenderer) this.mc.getRenderManager().getRenderer(playerEntity);

    // Right Hand
    matrixStack.push();
    {
      gun.getAnimationController().applyTransforms(playerEntity, itemStack, GunAnimation.RIGHT_HAND,
          matrixStack);

      this.mc.getTextureManager().bindTexture(playerEntity.getLocationSkin());
      matrixStack.translate(1F, 1F, 0F);
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(125.0F));
      matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
      matrixStack.translate(0.19F, -1.4F, 0.5F);
      this.applyHandTransforms(playerEntity, gun, true, matrixStack);
      playerRenderer.renderRightArm(matrixStack, renderTypeBuffer, packedLight,
          playerEntity);

    }
    matrixStack.pop();

    // Left Hand
    matrixStack.push();
    {
      gun.getAnimationController().applyTransforms(playerEntity, itemStack, GunAnimation.LEFT_HAND,
          matrixStack);

      this.mc.getTextureManager().bindTexture(playerEntity.getLocationSkin());
      matrixStack.translate(1.5F, 0.0F, 0.0F);
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(120.0F));
      matrixStack.rotate(Vector3f.XP.rotationDegrees(120.0F));
      matrixStack.translate(0.3F, -1.5F, -0.12F);
      this.applyHandTransforms(playerEntity, gun, false, matrixStack);
      matrixStack.scale(1.0F, 1.2F, 1.0F);
      playerRenderer.renderLeftArm(matrixStack, renderTypeBuffer, packedLight,
          playerEntity);
    }
    matrixStack.pop();
  }

  private void renderFlash(IGun gun, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.rotate(new Vector3f(-0.08F, 1.0F, 0.0F).rotationDegrees(-85));
      matrixStack.rotate(Vector3f.XP.rotationDegrees(30));
      matrixStack.scale(muzzleScale, muzzleScale, muzzleScale);
      if (gun.getAttachments().contains(ModItems.SUPPRESSOR.get())) {
        muzzleFlashZ -= 0.4;
      }
      matrixStack.translate(muzzleFlashX, muzzleFlashY, muzzleFlashZ);
      IVertexBuilder flashVertexBuilder = renderTypeBuffer
          .getBuffer(this.muzzleFlashModel.getRenderType(new ResourceLocation(CraftingDead.ID,
              "textures/flash/flash"
                  + (random.nextInt(3) + 1) + ".png")));
      this.muzzleFlashModel.render(matrixStack, flashVertexBuilder, FULL_LIGHT,
          packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private final void renderFirstPerson(AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack, IGun gun, IScope scope, ItemCameraTransforms.TransformType transformType,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay) {

    final boolean aiming = scope != null & scope.isAiming(playerEntity, itemStack);
    if (gun.hasShotCountChanged() && !aiming) {
      packedLight = FULL_LIGHT;
    }

    matrixStack.push();
    {
      if (aiming) {
        this.applyAimingTransforms(playerEntity, gun, matrixStack);
      } else {
        if (!playerEntity.isSprinting()) {
          this.renderFirstPersonArms(playerEntity, itemStack, gun, matrixStack, renderTypeBuffer,
              packedLight);
        }

        if (gun.hasShotCountChanged()) {
          this.renderFlash(gun, matrixStack, renderTypeBuffer, packedOverlay);
        }

        this.applyFirstPersonTransforms(playerEntity, gun, matrixStack);
      }

      if (playerEntity.isSprinting()) {
        this.applySprintingTransforms(matrixStack);
      }

      gun.getAnimationController().applyTransforms(playerEntity, itemStack, GunAnimation.BODY,
          matrixStack);

      this.renderGun(itemStack, gun, transformType, matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);

      matrixStack.push();
      {
        gun.getAnimationController().applyTransforms(playerEntity, itemStack, GunAnimation.MAGAZINE,
            matrixStack);

        this.renderMainGunAmmo(playerEntity, itemStack.getItem().getRegistryName(), gun,
            transformType,
            matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.pop();

      this.renderMainGunAttachments(playerEntity, itemStack.getItem().getRegistryName(), gun,
          transformType,
          matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);
    }
    matrixStack.pop();
  }

  private final void renderThirdPerson(LivingEntity livingEntity, ItemStack itemStack, IGun gun,
      ItemCameraTransforms.TransformType transformType,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      this.applyThirdPersonTransforms(livingEntity, gun, matrixStack);

      gun.getAnimationController().applyTransforms(livingEntity, itemStack, GunAnimation.BODY,
          matrixStack);


      this.renderGun(itemStack, gun, transformType, matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);

      this.renderMainGunAmmo(livingEntity, itemStack.getItem().getRegistryName(), gun,
          transformType, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAttachments(livingEntity, itemStack.getItem().getRegistryName(), gun,
          transformType,
          matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.pop();
  }

  public void renderOnBack(LivingEntity entity, ItemStack itemStack,
      IGun gun,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    matrixStack.push();
    {
      this.applyWearingTransforms(entity, gun, matrixStack);

      this.renderGun(itemStack, gun, ItemCameraTransforms.TransformType.HEAD, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);

      this.renderMainGunAmmo(entity, itemStack.getItem().getRegistryName(), gun,
          ItemCameraTransforms.TransformType.HEAD, matrixStack,
          renderTypeBuffer, packedLight,
          packedOverlay);
      this.renderMainGunAttachments(entity, itemStack.getItem().getRegistryName(), gun,
          ItemCameraTransforms.TransformType.HEAD, matrixStack,
          renderTypeBuffer, packedLight,
          packedOverlay);
    }
    matrixStack.pop();
  }

  protected void renderGun(ItemStack itemStack, IGun gun,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
      matrixStack.translate(0, -1.45F, 0);

      this.renderBakedModel(this.getPaintedBakedModel(this.getGunModelLocation(), gun), itemStack,
          transformType,
          matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);
    }
    matrixStack.pop();
  }

  protected final void renderBakedModel(IBakedModel bakedModel, ItemStack itemStack,
      ItemCameraTransforms.TransformType transformType,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    bakedModel = bakedModel.handlePerspective(transformType, matrixStack);
    this.mc.getItemRenderer().renderQuads(matrixStack,
        ItemRenderer.getBuffer(renderTypeBuffer,
            Atlases.getTranslucentBlockType(), true, itemStack.hasEffect()),
        bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE), itemStack, packedLight,
        packedOverlay);
  }

  protected final IBakedModel getPaintedBakedModel(ResourceLocation modelLocation, IGun gun) {
    ResourceLocation skin = gun.getPaintStack().getCapability(ModCapabilities.PAINT)
        .map(IPaint::getSkin).orElse(Optional.empty()).orElse(null);
    return this.getBakedModel(modelLocation,
        skin == null ? null
            : ImmutableMap.of("base",
                Either.left(new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE,
                    new ResourceLocation(skin.getNamespace(),
                        "gun/" + this.gunItem.get().getRegistryName().getPath() + "_"
                            + skin.getPath())))));
  }

  protected final Material getGunMaterial(IGun gun) {
    IUnbakedModel unbakedModel =
        ModelLoader.instance().getModelOrMissing(this.getGunModelLocation());
    ResourceLocation skin = gun.getPaintStack().getCapability(ModCapabilities.PAINT)
        .map(IPaint::getSkin).orElse(Optional.empty()).orElse(null);
    if (unbakedModel instanceof BlockModel) {
      if (skin != null) {
        return new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE,
            new ResourceLocation(skin.getNamespace(),
                "gun/" + this.gunItem.get().getRegistryName().getPath() + "_"
                    + skin.getPath()));
      }
      return ((BlockModel) unbakedModel).resolveTextureName("base");
    }
    return new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE,
        MissingTextureSprite.getLocation());
  }

  protected final IBakedModel getBakedModel(ResourceLocation modelLocation,
      @Nullable Map<String, Either<Material, String>> textures) {
    this.cachedModels.clear();
    return this.cachedModels.computeIfAbsent(
        modelLocation.hashCode() + (textures == null ? 0 : textures.hashCode()), key -> {
          if (textures != null) {
            IUnbakedModel model = ModelLoader.instance().getModelOrMissing(modelLocation);
            if (model instanceof BlockModel) {
              BlockModel blockModel = (BlockModel) model;
              BlockModel overriddenModel = new BlockModel(null, new ArrayList<>(), textures, false,
                  null, ItemCameraTransforms.DEFAULT, new ArrayList<>());
              overriddenModel.parent = blockModel;
              return overriddenModel.bakeModel(ModelLoader.instance(), overriddenModel,
                  ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, modelLocation,
                  false);
            }
          }
          return this.mc.getModelManager().getModel(modelLocation);
        });
  }

  private void renderMainGunAttachments(LivingEntity livingEntity, ResourceLocation registryName,
      IGun gun, ItemCameraTransforms.TransformType transformType,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    if (gun.hasIronSight()) {
      matrixStack.push();
      {
        this.renderAdditionalParts(livingEntity, gun, matrixStack, renderTypeBuffer, packedLight,
            packedOverlay);
      }
      matrixStack.pop();
    }
    matrixStack.push();
    {
      float scale = 0.1F;
      matrixStack.scale(scale, scale, scale);
      for (AttachmentItem attachmentItem : gun.getAttachments()) {
        matrixStack.push();
        {
          this.applyAttachmentTransforms(livingEntity, attachmentItem, matrixStack);
          float scale2 = 10F;
          matrixStack.scale(scale2, scale2, scale2);

          matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
          matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
          matrixStack.translate(0, -1.45F, 0);

          final IBakedModel bakedModel =
              this.getBakedModel(
                  this.getAttachmentModelLocation(attachmentItem.getRegistryName()), null);
          this.renderBakedModel(bakedModel, ItemStack.EMPTY, transformType, matrixStack,
              renderTypeBuffer,
              packedLight,
              packedOverlay);
        }
        matrixStack.pop();
      }

    }
    matrixStack.pop();
  }

  private void renderMainGunAmmo(LivingEntity livingEntity, ResourceLocation texture, IGun gun,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    IMagazine magazine =
        gun.getMagazineStack().getCapability(ModCapabilities.MAGAZINE).orElse(null);
    if (magazine != null) {
      matrixStack.push();
      {
        float scale = 0.825F;
        matrixStack.scale(scale, scale, scale);

        this.applyMagazineTransforms(livingEntity, gun.getMagazineStack(), matrixStack);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrixStack.translate(0, -1.45F, 0);

        final ResourceLocation modelLocation =
            this.getMagazineModelLocation(gun.getMagazineStack().getItem().getRegistryName());
        final IBakedModel magazineBakedModel;
        if (magazine.hasCustomTexture()) {
          magazineBakedModel = this.getBakedModel(modelLocation, null);
        } else {
          magazineBakedModel = this.getBakedModel(modelLocation,
              ImmutableMap.of("base", Either.left(this.getGunMaterial(gun))));
        }

        this.renderBakedModel(magazineBakedModel,
            gun.getMagazineStack(), transformType, matrixStack, renderTypeBuffer, packedLight,
            packedOverlay);
      }
      matrixStack.pop();
    }
  }

  protected abstract void renderAdditionalParts(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay);

  protected abstract void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack);

  protected abstract void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack);

  protected abstract void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack);

  protected abstract void applyWearingTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack);

  protected abstract void applyMagazineTransforms(LivingEntity livingEntity,
      ItemStack magazineStack, MatrixStack matrixStack);

  protected abstract void applyAttachmentTransforms(LivingEntity livingEntity,
      AttachmentItem attachment, MatrixStack matrixStack);

  protected abstract void applyHandTransforms(PlayerEntity playerEntity, IGun gun,
      boolean rightHanded, MatrixStack matrixStack);

  protected void applySprintingTransforms(MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-70));
    matrixStack.translate(0.7F, 0.0F, 0.2F);
  }

  protected ResourceLocation getGunModelLocation() {
    return new ResourceLocation(this.gunItem.get().getRegistryName().getNamespace(),
        "gun/" + this.gunItem.get().getRegistryName().getPath());
  }

  protected ResourceLocation getAttachmentModelLocation(ResourceLocation attachmentName) {
    return new ResourceLocation(attachmentName.getNamespace(),
        "attachment/" + attachmentName.getPath());
  }

  protected ResourceLocation getMagazineModelLocation(ResourceLocation magazineName) {
    return new ResourceLocation(magazineName.getNamespace(), "magazine/" + magazineName.getPath());
  }

  @Override
  public Collection<ResourceLocation> getModelDependencies() {
    final Set<ResourceLocation> dependencies = new HashSet<>();
    dependencies
        .addAll(this.gunItem.get().getAcceptedAttachments().stream().map(Item::getRegistryName)
            .map(this::getAttachmentModelLocation).collect(Collectors.toSet()));
    dependencies
        .addAll(this.gunItem.get().getAcceptedMagazines().stream()
            .filter(MagazineItem::hasCustomTexture).map(Item::getRegistryName)
            .map(this::getMagazineModelLocation).collect(Collectors.toSet()));
    dependencies.add(this.getGunModelLocation());
    return dependencies;
  }

  @Override
  public Collection<ResourceLocation> getAdditionalModelTextures() {
    final Set<ResourceLocation> textures = new HashSet<>();
    textures.addAll(this.gunItem.get().getAcceptedPaints().stream()
        .map(paintItem -> new ResourceLocation(paintItem.getRegistryName().getNamespace(),
            "gun/" + this.gunItem.get().getRegistryName().getPath() + "_"
                + paintItem.getRegistryName().getPath()))
        .collect(Collectors.toSet()));
    return textures;
  }

  @Override
  public void refreshCachedModels() {
    this.cachedModels.clear();
  }
}
