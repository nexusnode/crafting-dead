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
import net.minecraft.client.renderer.model.ItemModelGenerator;
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

@SuppressWarnings("deprecation")
public class ComplexModel implements IModelGeometry<ComplexModel> {

  private static final ItemModelGenerator MODEL_GENERATOR = new ItemModelGenerator();

  private final IUnbakedModel heldModel;
  private final IUnbakedModel simpleModel;

  public ComplexModel(IUnbakedModel complexModel, IUnbakedModel simpleModel) {
    this.heldModel = complexModel;
    this.simpleModel = simpleModel;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {
    IBakedModel bakedSimpleModel = null;
    // Hackery to bake generated item models
    if (this.simpleModel instanceof BlockModel) {
      BlockModel blockmodel = (BlockModel) this.simpleModel;
      if (blockmodel.getRootModel().name.equals("generation marker")) {
        bakedSimpleModel = MODEL_GENERATOR
            .makeItemModel(spriteGetter, blockmodel)
            .bake(bakery, blockmodel, spriteGetter, modelTransform, modelLocation, false);
      }
    }

    if (bakedSimpleModel == null) {
      bakedSimpleModel = this.simpleModel.bake(bakery, spriteGetter, modelTransform, modelLocation);
    }

    return new BakedComplexModel(
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

  public static class BakedComplexModel implements IBakedModel {

    private static final ItemOverrideList OVERRIDE_LIST = new ModelOverrideHandler();

    private final IBakedModel complexModel;
    private final IBakedModel simpleModel;
    private final TextureAtlasSprite particle;
    private final LazyOptional<IAnimationController> animationController;

    public BakedComplexModel(IBakedModel complexModel, IBakedModel simpleModel,
        TextureAtlasSprite particle) {
      this(complexModel, simpleModel, particle, LazyOptional.empty());
    }

    public BakedComplexModel(IBakedModel complexModel, IBakedModel simpleModel,
        TextureAtlasSprite particle, LazyOptional<IAnimationController> animationController) {
      this.complexModel = complexModel;
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
        return this.complexModel.handlePerspective(cameraTransformType, mat);
      } else {
        return this.simpleModel.handlePerspective(cameraTransformType, mat);
      }

    }

    @Override
    public ItemOverrideList getOverrides() {
      return OVERRIDE_LIST;
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
  }

  private static final class ModelOverrideHandler extends ItemOverrideList {

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack,
        @Nullable World world, @Nullable LivingEntity entity) {
      BakedComplexModel model = (BakedComplexModel) originalModel;
      return new BakedComplexModel(
          model.complexModel
              .getOverrides()
              .getModelWithOverrides(model.complexModel, itemStack, world, entity),
          model.simpleModel, model.particle,
          itemStack.getCapability(ModCapabilities.ANIMATION_CONTROLLER));
    }
  }

  public static final class Loader implements IModelLoader<ComplexModel> {

    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public ComplexModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      IUnbakedModel complex = deserializationContext
          .deserialize(JSONUtils.getJsonObject(modelContents, "held"), BlockModel.class);
      IUnbakedModel simple = deserializationContext
          .deserialize(JSONUtils.getJsonObject(modelContents, "simple"), BlockModel.class);
      return new ComplexModel(complex, simple);
    }
  }
}
