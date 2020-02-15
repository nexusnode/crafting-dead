package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.mod.item.GunItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

@SuppressWarnings("deprecation")
public class GunModel implements IModelGeometry<GunModel> {

  private final IUnbakedModel model;
  private final Map<ResourceLocation, QuadTransformer> attachmentTransforms;

  public GunModel(IUnbakedModel model,
      Map<ResourceLocation, QuadTransformer> attachmentTransforms) {
    this.model = model;
    this.attachmentTransforms = attachmentTransforms;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {
    return new BakedGunModel(this.model.bake(bakery, spriteGetter, modelTransform, modelLocation),
        new AttachmentOverrideHandler(this.attachmentTransforms));
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors) {
    return this.model.getTextureDependencies(modelGetter, missingTextureErrors);
  }

  public static class BakedGunModel implements IBakedModel {

    private final IBakedModel base;
    private final Map<IBakedModel, QuadTransformer> attachments;
    private final ItemOverrideList itemOverrideList;

    public BakedGunModel(IBakedModel base, ItemOverrideList itemOverrideList) {
      this(base, ImmutableMap.of(), itemOverrideList);
    }

    public BakedGunModel(IBakedModel base, ImmutableMap<IBakedModel, QuadTransformer> attachments,
        ItemOverrideList itemOverrideList) {
      this.base = base;
      this.attachments = attachments;
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
      return this.base.handlePerspective(cameraTransformType, mat);
    }

    @Override
    public ItemOverrideList getOverrides() {
      return this.itemOverrideList;
    }

    @Override
    public boolean isSideLit() {
      return false;
    }
  }

  private static final class AttachmentOverrideHandler extends ItemOverrideList {

    private final Map<ResourceLocation, QuadTransformer> attachmentTransforms;
    private final Map<Integer, ImmutableMap<IBakedModel, QuadTransformer>> attachmentCache =
        new HashMap<>();

    public AttachmentOverrideHandler(Map<ResourceLocation, QuadTransformer> attachmentTransforms) {
      this.attachmentTransforms = attachmentTransforms;
    }

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      if (itemStack.getItem() instanceof GunItem) {
        GunItem gunItem = (GunItem) itemStack.getItem();
        BakedGunModel model = (BakedGunModel) originalModel;
        final int attachmentHash = gunItem.getAttachments(itemStack).hashCode();
        if (!this.attachmentCache.containsKey(attachmentHash)) {
          this.attachmentCache
              .put(attachmentHash, gunItem
                  .getAttachments(itemStack)
                  .stream()
                  .collect(ImmutableMap
                      .toImmutableMap(
                          attachment -> Minecraft
                              .getInstance()
                              .getItemRenderer()
                              .getItemModelWithOverrides(new ItemStack(attachment), world, entity),
                          attachment -> this.attachmentTransforms
                              .getOrDefault(attachment.getRegistryName(),
                                  new QuadTransformer(TransformationMatrix.identity())))));
        }

        return new BakedGunModel(
            model.base.getOverrides().getModelWithOverrides(model.base, itemStack, world, entity),
            this.attachmentCache.get(attachmentHash), this);
      }
      return originalModel;
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
