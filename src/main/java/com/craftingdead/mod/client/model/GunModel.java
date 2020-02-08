package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.GunItem;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

@SuppressWarnings("deprecation")
public class GunModel implements IModelGeometry<GunModel> {

  private final IUnbakedModel base;
  private final Map<ResourceLocation, TransformationMatrix> attachmentTransforms;

  public GunModel(IUnbakedModel base,
      Map<ResourceLocation, TransformationMatrix> attachmentTransforms) {
    this.base = base;
    this.attachmentTransforms = attachmentTransforms;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {
    return new BakedGunModel(this.base.bake(bakery, spriteGetter, modelTransform, modelLocation),
        new AttachmentOverrideHandler(this.attachmentTransforms));
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors) {
    return this.base.getTextureDependencies(modelGetter, missingTextureErrors);
  }

  public static class BakedGunModel implements IBakedModel {

    private final IBakedModel base;
    private final Map<IBakedModel, QuadTransformer> attachments = new HashMap<>();
    private final ItemOverrideList itemOverrideList;

    private Optional<IAnimationController> animationController = Optional.empty();

    public BakedGunModel(IBakedModel base, ItemOverrideList itemOverrideList) {
      this.base = base;
      this.itemOverrideList = itemOverrideList;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
      return this.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        @Nonnull Random rand, @Nonnull IModelData extraData) {
      ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
      builder.addAll(this.base.getQuads(state, side, rand, extraData));
      for (Map.Entry<IBakedModel, QuadTransformer> entry : this.attachments.entrySet()) {
        builder
            .addAll(entry
                .getValue()
                .processMany(entry.getKey().getQuads(state, side, rand, extraData)));
      }
      return builder.build();
    }

    @Override
    public boolean isAmbientOcclusion() {
      return true;
    }

    @Override
    public boolean isGui3d() {
      return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
      return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
      return this.base.getParticleTexture();
    }

    @Override
    public boolean doesHandlePerspectives() {
      return true;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType,
        MatrixStack mat) {
      if (isHeld(cameraTransformType)) {
        this.animationController.ifPresent(controller -> controller.apply(mat));
      }
      return this.base.handlePerspective(cameraTransformType, mat);
    }

    @Override
    public ItemOverrideList getOverrides() {
      return this.itemOverrideList;
    }

    private static boolean isHeld(ItemCameraTransforms.TransformType cameraTransformType) {
      switch (cameraTransformType) {
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          return true;
        default:
          return false;
      }
    }

    @Override
    public boolean isSideLit() {
      return false;
    }
  }

  public static class Loader implements IModelLoader<GunModel> {

    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public GunModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      if (!modelContents.has("base")) {
        throw new RuntimeException("Gun model requires 'base' key that points to a model.");
      }
      IUnbakedModel base = ModelLoader
          .instance()
          .getModelOrMissing(new ResourceLocation(modelContents.get("base").getAsString()));

      Map<ResourceLocation, TransformationMatrix> attachmentTransforms = new HashMap<>();
      if (modelContents.has("attachment_transforms")) {
        modelContents.getAsJsonObject("attachment_transforms").entrySet().forEach(entry -> {
          attachmentTransforms
              .put(new ResourceLocation(entry.getKey()),
                  deserializationContext.deserialize(entry.getValue(), TransformationMatrix.class));
        });
      }

      return new GunModel(base, attachmentTransforms);
    }
  }

  private static final class AttachmentOverrideHandler extends ItemOverrideList {

    private final Map<ResourceLocation, TransformationMatrix> attachmentTransforms;

    public AttachmentOverrideHandler(
        Map<ResourceLocation, TransformationMatrix> attachmentTransforms) {
      this.attachmentTransforms = attachmentTransforms;
    }

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      if (itemStack.getItem() instanceof GunItem) {
        GunItem gunItem = (GunItem) itemStack.getItem();
        BakedGunModel model = (BakedGunModel) originalModel;
        model.attachments.clear();
        for (AttachmentItem attachment : gunItem.getAttachments(itemStack).values()) {
          model.attachments
              .put(
                  Minecraft
                      .getInstance()
                      .getItemRenderer()
                      .getItemModelWithOverrides(new ItemStack(attachment), world, entity),
                  new QuadTransformer(this.attachmentTransforms
                      .getOrDefault(attachment.getRegistryName(),
                          TransformationMatrix.identity())));
        }
        model.animationController = Optional
            .ofNullable(itemStack.getCapability(ModCapabilities.ANIMATION_CONTROLLER).orElse(null));
      }
      return originalModel;
    }
  }
}
