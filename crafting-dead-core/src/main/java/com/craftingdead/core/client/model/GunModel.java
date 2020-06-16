package com.craftingdead.core.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.item.AttachmentItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
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

  private final BlockModel baseModel;
  private final Map<ResourceLocation, QuadTransformer> attachmentTransforms;

  public GunModel(BlockModel baseModel,
      Map<ResourceLocation, QuadTransformer> attachmentTransforms) {
    this.baseModel = baseModel;
    this.attachmentTransforms = attachmentTransforms;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {
    IBakedModel bakedModel = this.baseModel
        .bake(bakery, this.baseModel, spriteGetter, modelTransform, modelLocation, true);
    final Random random = new Random();
    random.setSeed(42L);
    return new BakedGunModel(bakedModel, ImmutableMap.of(),
        new AttachmentOverrideHandler(bakery, overrides), owner.getCombinedTransform());
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors) {
    return this.baseModel.getTextureDependencies(modelGetter, missingTextureErrors);
  }

  public static class BakedGunModel implements IBakedModel {

    private final IBakedModel baseModel;
    private final Map<IBakedModel, QuadTransformer> attachments;
    private final ItemOverrideList itemOverrideList;
    private final IModelTransform transform;

    public BakedGunModel(IBakedModel model, Map<IBakedModel, QuadTransformer> attachments,
        ItemOverrideList itemOverrideList, IModelTransform transform) {
      this.baseModel = model;
      this.attachments = attachments;
      this.itemOverrideList = itemOverrideList;
      this.transform = transform;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
      return this.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        @Nonnull Random rand, @Nonnull IModelData extraData) {
      ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
      for (Map.Entry<IBakedModel, QuadTransformer> entry : this.attachments.entrySet()) {
        builder
            .addAll(entry
                .getValue()
                .processMany(entry.getKey().getQuads(state, side, rand, EmptyModelData.INSTANCE)));
      }
      builder.addAll(this.baseModel.getQuads(state, side, rand, EmptyModelData.INSTANCE));
      return builder.build();
    }

    @Override
    public boolean isAmbientOcclusion() {
      return this.baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
      return this.baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
      return this.baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
      return this.getParticleTexture(EmptyModelData.INSTANCE);
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IModelData modelData) {
      return this.baseModel.getParticleTexture(modelData);
    }

    @Override
    public boolean doesHandlePerspectives() {
      return true;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType,
        MatrixStack mat) {
      this.transform.getPartTransformation(cameraTransformType).push(mat);
      ImmutableMap.Builder<IBakedModel, QuadTransformer> transformedAttachments =
          ImmutableMap.builder();
      for (Map.Entry<IBakedModel, QuadTransformer> entry : this.attachments.entrySet()) {
        transformedAttachments
            .put(entry.getKey() instanceof PerspectiveAwareModel.BakedPerspectiveAwareModel
                ? ((PerspectiveAwareModel.BakedPerspectiveAwareModel) entry.getKey())
                    .getModelForPerspective(cameraTransformType)
                : entry.getKey(), entry.getValue());
      }
      return new BakedGunModel(this.baseModel, transformedAttachments.build(),
          this.itemOverrideList, this.transform);
    }

    @Override
    public ItemOverrideList getOverrides() {
      return this.itemOverrideList;
    }

    @Override
    public boolean isSideLit() {
      return this.baseModel.isSideLit();
    }
  }

  private final class AttachmentOverrideHandler extends ItemOverrideList {

    private final Map<Integer, BakedGunModel> cachedModels = new HashMap<>();
    private final ModelBakery bakery;
    private final ItemOverrideList overrides;

    public AttachmentOverrideHandler(ModelBakery bakery, ItemOverrideList overrides) {
      this.bakery = bakery;
      this.overrides = overrides;
    }

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      final BakedGunModel gunModel = (BakedGunModel) originalModel;

      IBakedModel override =
          this.overrides.getModelWithOverrides(originalModel, itemStack, world, entity);
      if (override.getOverrides() != this) {
        return override.getOverrides().getModelWithOverrides(override, itemStack, world, entity);
      }

      return itemStack.getCapability(ModCapabilities.GUN).map(gunController -> {
        final Set<AttachmentItem> attachments = gunController.getAttachments();
        final int hash = attachments.hashCode()
            + gunController.getPaintStack().getItem().getRegistryName().hashCode();

        final Random random = new Random();
        random.setSeed(42L);

        return this.cachedModels.computeIfAbsent(hash, key -> {

          ImmutableMap.Builder<IBakedModel, QuadTransformer> attachmentModels =
              ImmutableMap.builder();

          for (AttachmentItem attachment : attachments) {
            IBakedModel attachmentModel = Minecraft
                .getInstance()
                .getItemRenderer()
                .getItemModelWithOverrides(new ItemStack(attachment), world, entity);
            QuadTransformer quadTransformer = GunModel.this.attachmentTransforms
                .getOrDefault(attachment.getRegistryName(),
                    new QuadTransformer(TransformationMatrix.identity()));
            attachmentModels.put(attachmentModel, quadTransformer);
          }

          IBakedModel bakedModel = gunController
              .getPaintStack()
              .getCapability(ModCapabilities.PAINT)
              .filter(paint -> paint.getSkin().isPresent())
              .map(paint -> {
                ResourceLocation skin = paint.getSkin().get();
                ResourceLocation textureLocation =
                    new ResourceLocation(skin.getNamespace(), "models/guns/"
                        + itemStack.getItem().getRegistryName().getPath() + "_" + skin.getPath());
                BlockModel paintedModel = new BlockModel(null, new ArrayList<>(), ImmutableMap
                    .of("base", Either
                        .left(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, textureLocation))),
                    false, null, ItemCameraTransforms.DEFAULT, new ArrayList<>());
                paintedModel.parent = GunModel.this.baseModel;
                return paintedModel
                    .bake(this.bakery, paintedModel, ModelLoader.defaultTextureGetter(),
                        gunModel.transform, new ResourceLocation(CraftingDead.ID, "generated"),
                        true);
              })
              .orElse(gunModel.baseModel);
          return new BakedGunModel(bakedModel, attachmentModels.build(), this, gunModel.transform);
        });
      }).orElse(gunModel);
    }
  }

  public static class Loader implements IModelLoader<GunModel> {

    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public GunModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      Map<ResourceLocation, QuadTransformer> attachmentTransforms = new HashMap<>();
      if (modelContents.has("attachment_transforms")) {
        modelContents.getAsJsonObject("attachment_transforms").entrySet().forEach(entry -> {
          attachmentTransforms
              .put(new ResourceLocation(entry.getKey()), new QuadTransformer(deserializationContext
                  .deserialize(entry.getValue(), TransformationMatrix.class)));
        });
      }

      return new GunModel(
          deserializationContext
              .deserialize(JSONUtils.getJsonObject(modelContents, "base_model"), BlockModel.class),
          attachmentTransforms);
    }
  }
}
