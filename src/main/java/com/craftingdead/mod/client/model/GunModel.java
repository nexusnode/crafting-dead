package com.craftingdead.mod.client.model;

import java.util.ArrayList;
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
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.PaintItem;
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

  private ItemOverrideList overrides;

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
    if (this.overrides == null) {
      this.overrides = new AttachmentOverrideHandler(bakery);
    }
    IBakedModel bakedModel = this.baseModel
        .bake(bakery, this.baseModel, spriteGetter, modelTransform, modelLocation, true);
    final Random random = new Random();
    random.setSeed(42L);
    return new BakedGunModel(bakedModel,
        bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE), this.overrides,
        modelTransform);
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors) {
    return this.baseModel.getTextureDependencies(modelGetter, missingTextureErrors);
  }

  public static class BakedGunModel implements IBakedModel {

    private final IBakedModel baseModel;
    private final List<BakedQuad> quads;
    private final ItemOverrideList itemOverrideList;
    private final IModelTransform transform;

    public BakedGunModel(IBakedModel model, List<BakedQuad> quads,
        ItemOverrideList itemOverrideList, IModelTransform transform) {
      this.baseModel = model;
      this.quads = quads;
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
      return this.quads;
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
      this.baseModel.handlePerspective(cameraTransformType, mat);
      return this;
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

    public AttachmentOverrideHandler(ModelBakery bakery) {
      this.bakery = bakery;
    }

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      final BakedGunModel gunModel = (BakedGunModel) originalModel;

      return itemStack.getCapability(ModCapabilities.GUN_CONTROLLER).map(gunController -> {
        final Set<AttachmentItem> attachments = gunController.getAttachments();
        final Optional<PaintItem> paint = gunController.getPaint();
        final int hash =
            attachments.hashCode() + paint.map(p -> p.getRegistryName().hashCode()).orElse(0);

        final Random random = new Random();
        random.setSeed(42L);

        return this.cachedModels.computeIfAbsent(hash, key -> {
          ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

          for (AttachmentItem attachment : attachments) {
            IBakedModel attachmentModel = Minecraft
                .getInstance()
                .getItemRenderer()
                .getItemModelWithOverrides(new ItemStack(attachment), world, entity);
            QuadTransformer quadTransformer = GunModel.this.attachmentTransforms
                .getOrDefault(attachment.getRegistryName(),
                    new QuadTransformer(TransformationMatrix.identity()));
            builder
                .addAll(quadTransformer
                    .processMany(
                        attachmentModel.getQuads(null, null, random, EmptyModelData.INSTANCE)));
          }

          IBakedModel bakedModel = paint.map(p -> {
            BlockModel paintedModel = new BlockModel(null, new ArrayList<>(),
                ImmutableMap
                    .of("base",
                        Either
                            .left(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
                                new ResourceLocation(p.getRegistryName().getNamespace(),
                                    "paint/" + p.getRegistryName().getPath())))),
                false, null, ItemCameraTransforms.DEFAULT, new ArrayList<>());
            paintedModel.parent = GunModel.this.baseModel.parent;
            return paintedModel
                .bake(this.bakery, paintedModel, ModelLoader.defaultTextureGetter(),
                    gunModel.transform, new ResourceLocation(CraftingDead.ID, "generated"), true);
          }).orElse(gunModel.baseModel);

          builder.addAll(bakedModel.getQuads(null, null, random, EmptyModelData.INSTANCE));

          return new BakedGunModel(bakedModel, builder.build(), this, gunModel.transform);
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
              .deserialize(JSONUtils.getJsonObject(modelContents, "model"), BlockModel.class),
          attachmentTransforms);
    }
  }
}
