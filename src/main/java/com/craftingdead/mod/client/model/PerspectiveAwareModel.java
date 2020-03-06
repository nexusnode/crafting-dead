package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.animation.IAnimationController;
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
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.common.util.LazyOptional;

/**
 * A model that can be rendered as a different model depending on the view perspective.
 */
@SuppressWarnings("deprecation")
public class PerspectiveAwareModel implements IModelGeometry<PerspectiveAwareModel> {

  /**
   * Model to be shown when held.
   */
  private final IUnbakedModel heldModel;

  /**
   * Model to be shown otherwise.
   */
  private final IUnbakedModel simpleModel;

  public PerspectiveAwareModel(IUnbakedModel unbakedHeldModel, IUnbakedModel unbakedSimpleModel) {
    this.heldModel = unbakedHeldModel;
    this.simpleModel = unbakedSimpleModel;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {

    IBakedModel bakedSimpleModel = null;

    if (this.simpleModel instanceof BlockModel) {
      BlockModel blockModel = (BlockModel) this.simpleModel;

      // Generates a default 2D model if the model has a generation marker
      if (RenderUtil.hasGenerationMarker(blockModel)) {
        bakedSimpleModel = RenderUtil.generateSpriteModel(blockModel, bakery, spriteGetter,
            modelTransform, modelLocation);
      }
    }

    if (bakedSimpleModel == null) {
      bakedSimpleModel = this.simpleModel.bake(bakery, spriteGetter, modelTransform, modelLocation);
    }

    return new BakedLegacyLikeModel(
        this.heldModel.bake(bakery, spriteGetter, modelTransform, modelLocation), bakedSimpleModel,
        spriteGetter.apply(owner.resolveTexture("particle")));
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<Pair<String, String>> missingTextureErrors) {
    Set<Material> materials = new HashSet<>();
    materials.add(owner.resolveTexture("particle"));
    materials.addAll(this.heldModel.getTextureDependencies(modelGetter, missingTextureErrors));
    materials.addAll(this.simpleModel.getTextureDependencies(modelGetter, missingTextureErrors));
    return materials;
  }

  public static class BakedLegacyLikeModel implements IBakedModel {

    private static final ItemOverrideList OVERRIDE_LIST = new ModelOverrideHandler();

    /**
     * Model to be shown when held.
     */
    private final IBakedModel heldModel;

    /**
     * Model to be shown otherwise.
     */
    private final IBakedModel simpleModel;
    private final TextureAtlasSprite particle;
    private final LazyOptional<IAnimationController> animationController;

    public BakedLegacyLikeModel(IBakedModel heldModel, IBakedModel simpleModel,
        TextureAtlasSprite particle) {
      this(heldModel, simpleModel, particle, LazyOptional.empty());
    }

    public BakedLegacyLikeModel(IBakedModel heldModel, IBakedModel simpleModel,
        TextureAtlasSprite particle, LazyOptional<IAnimationController> animationController) {
      this.heldModel = heldModel;
      this.simpleModel = simpleModel;
      this.particle = particle;
      this.animationController = animationController;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        Random rand) {
      return this.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        @Nonnull Random rand, @Nonnull IModelData extraData) {
      return this.simpleModel.getQuads(state, side, rand, extraData);
    }

    @Override
    public boolean isAmbientOcclusion() {
      return true;
    }

    @Override
    public boolean isGui3d() {
      return false;
    }

    @Override
    public boolean isSideLit() {
      return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
      return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
      return this.particle;
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
        return this.heldModel.handlePerspective(cameraTransformType, mat);
      } else {
        return this.simpleModel.handlePerspective(cameraTransformType, mat);
      }

    }

    @Override
    public ItemOverrideList getOverrides() {
      return OVERRIDE_LIST;
    }

    // TODO Will this actually be the only supported perspective?
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
  }

  private static final class ModelOverrideHandler extends ItemOverrideList {

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      BakedLegacyLikeModel model = (BakedLegacyLikeModel) originalModel;
      return new BakedLegacyLikeModel(
          model.heldModel
              .getOverrides()
              .getModelWithOverrides(model.heldModel, itemStack, world, entity),
          model.simpleModel, model.particle,
          itemStack.getCapability(ModCapabilities.ANIMATION_CONTROLLER));
    }
  }

  public static final class Loader implements IModelLoader<PerspectiveAwareModel> {

    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public PerspectiveAwareModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      IUnbakedModel unbakedHeldModel = deserializationContext
          .deserialize(JSONUtils.getJsonObject(modelContents, "held"), BlockModel.class);
      IUnbakedModel unbakedSimpleModel = deserializationContext
          .deserialize(JSONUtils.getJsonObject(modelContents, "simple"), BlockModel.class);
      return new PerspectiveAwareModel(unbakedHeldModel, unbakedSimpleModel);
    }
  }
}
