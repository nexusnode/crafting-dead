package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.mod.client.util.RenderUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
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
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

/**
 * A simple model wrapper that implements the {@link IEquipableModel} interface on
 * its baked model.
 */
@SuppressWarnings("deprecation")
public class EquipableModel implements IModelGeometry<EquipableModel> {

  /**
   * The model to be shown when equipped.
   */
  private final IUnbakedModel equippedModel;

  /**
   * The model to be shown when not equipped.
   */
  private final IUnbakedModel defaultModel;

  public EquipableModel(IUnbakedModel equippedModel, IUnbakedModel defaultModel) {
    this.equippedModel = equippedModel;
    this.defaultModel = defaultModel;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {

    IBakedModel bakedDefaultModel = null;

    if (this.defaultModel instanceof BlockModel) {
      BlockModel blockModel = (BlockModel) this.defaultModel;

      // Generates a default 2D model if the model has a generation marker
      if (RenderUtil.hasGenerationMarker(blockModel)) {
        bakedDefaultModel = RenderUtil.generateSpriteModel(blockModel, bakery, spriteGetter,
            modelTransform, modelLocation);
      }
    }

    if (bakedDefaultModel == null) {
      bakedDefaultModel = this.defaultModel.bake(bakery, spriteGetter, modelTransform, modelLocation);
    }

    IBakedModel equippedModel =
        this.equippedModel.bake(bakery, spriteGetter, modelTransform, modelLocation);

    return new BakedEquipableModel(equippedModel, bakedDefaultModel);
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<Pair<String, String>> missingTextureErrors) {
    Set<Material> materials = new HashSet<>();
    materials.add(owner.resolveTexture("particle"));
    materials.addAll(this.equippedModel.getTextureDependencies(modelGetter, missingTextureErrors));
    materials.addAll(this.defaultModel.getTextureDependencies(modelGetter, missingTextureErrors));
    return materials;
  }

  /**
   * Wraps a default {@link IBakedModel} and implements {@link IEquipableModel} interface.
   */
  public static class BakedEquipableModel implements IEquipableModel {

    private static final ItemOverrideList OVERRIDE_LIST = new ModelOverrideHandler();

    /**
     * The model to be shown when equipped.
     */
    private final IBakedModel equippedModel;

    /**
     * The model to be shown when not equipped.
     */
    private final IBakedModel defaultModel;

    public BakedEquipableModel(IBakedModel equippedModel, IBakedModel defaultModel) {
      this.equippedModel = equippedModel;
      this.defaultModel = defaultModel;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
      return this.defaultModel.getParticleTexture();
    }

    @Override
    public IBakedModel getEquippedModel() {
      return this.equippedModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        Random rand) {
      return this.defaultModel.getQuads(state, side, rand);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        @Nonnull Random rand, @Nonnull IModelData extraData) {
      return this.defaultModel.getQuads(state, side, rand, extraData);
    }

    @Override
    public boolean isAmbientOcclusion() {
      return this.defaultModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
      return this.defaultModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
      return this.defaultModel.isSideLit();
    }

    @Override
    public boolean isBuiltInRenderer() {
      return this.defaultModel.isBuiltInRenderer();
    }

    @Override
    public boolean doesHandlePerspectives() {
      return this.defaultModel.doesHandlePerspectives();
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType,
        MatrixStack mat) {
      return this.defaultModel.handlePerspective(cameraTransformType, mat);
    }

    @Override
    public ItemOverrideList getOverrides() {
      return OVERRIDE_LIST;
    }
  }

  private static final class ModelOverrideHandler extends ItemOverrideList {

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      BakedEquipableModel model = (BakedEquipableModel) originalModel;
      return new BakedEquipableModel(
          model.equippedModel
              .getOverrides()
              .getModelWithOverrides(model.equippedModel, itemStack, world, entity),
          model.defaultModel
              .getOverrides()
              .getModelWithOverrides(model.defaultModel, itemStack, world, entity));
    }
  }

  public static final class Loader implements IModelLoader<EquipableModel> {

    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public EquipableModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      IUnbakedModel equippedModel = deserializationContext
          .deserialize(JSONUtils.getJsonObject(modelContents, "equipped"), BlockModel.class);
      IUnbakedModel defaultModel = deserializationContext
          .deserialize(JSONUtils.getJsonObject(modelContents, "default"), BlockModel.class);

      return new EquipableModel(equippedModel, defaultModel);
    }
  }
}
