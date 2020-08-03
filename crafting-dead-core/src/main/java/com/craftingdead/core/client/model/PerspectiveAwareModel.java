package com.craftingdead.core.client.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.core.client.util.RenderUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
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
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
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

/**
 * A model that can be rendered as a different model depending on the view perspective.
 */
public class PerspectiveAwareModel implements IModelGeometry<PerspectiveAwareModel> {

  private final Map<ItemCameraTransforms.TransformType, IUnbakedModel> models;

  public PerspectiveAwareModel(Map<ItemCameraTransforms.TransformType, IUnbakedModel> models) {
    this.models = models;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {

    Map<ItemCameraTransforms.TransformType, IBakedModel> bakedModels =
        this.models.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
          IUnbakedModel model = entry.getValue();
          if (model instanceof BlockModel) {
            BlockModel blockModel = (BlockModel) model;
            // Generates a default 2D model if the model has a generation marker
            if (RenderUtil.hasGenerationMarker(blockModel)) {
              return RenderUtil
                  .generateSpriteModel(blockModel, bakery, spriteGetter, modelTransform,
                      modelLocation);
            }
          }
          return model.bakeModel(bakery, spriteGetter, modelTransform, modelLocation);
        }, (u, v) -> {
          throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, () -> new EnumMap<>(ItemCameraTransforms.TransformType.class)));

    return new BakedPerspectiveAwareModel(bakedModels,
        spriteGetter.apply(owner.resolveTexture("particle")));
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<Pair<String, String>> missingTextureErrors) {
    Set<Material> materials = new HashSet<>();
    materials.add(owner.resolveTexture("particle"));
    materials
        .addAll(this.models
            .values()
            .stream()
            .flatMap(
                model -> model.getTextures(modelGetter, missingTextureErrors).stream())
            .collect(Collectors.toSet()));
    return materials;
  }

  public static class BakedPerspectiveAwareModel implements IBakedModel {

    private static final ItemOverrideList OVERRIDE_LIST = new ModelOverrideHandler();

    private final Map<ItemCameraTransforms.TransformType, IBakedModel> bakedModels;
    private final TextureAtlasSprite particle;

    public BakedPerspectiveAwareModel(
        Map<ItemCameraTransforms.TransformType, IBakedModel> bakedModels,
        TextureAtlasSprite particle) {
      this.bakedModels = bakedModels;
      this.particle = particle;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        Random rand) {
      return this.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
        @Nonnull Random rand, @Nonnull IModelData extraData) {
      return this.bakedModels
          .get(ItemCameraTransforms.TransformType.NONE)
          .getQuads(state, side, rand, extraData);
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
    public boolean func_230044_c_() {
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
      return this
          .getModelForPerspective(cameraTransformType)
          .handlePerspective(cameraTransformType, mat);
    }

    public IBakedModel getModelForPerspective(
        ItemCameraTransforms.TransformType cameraTransformType) {
      return this.bakedModels
          .getOrDefault(cameraTransformType,
              this.bakedModels.get(ItemCameraTransforms.TransformType.NONE));
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
      BakedPerspectiveAwareModel perspectiveModel = (BakedPerspectiveAwareModel) originalModel;
      Map<ItemCameraTransforms.TransformType, IBakedModel> bakedModels =
          perspectiveModel.bakedModels
              .entrySet()
              .stream()
              .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                IBakedModel model = entry.getValue();
                return model.getOverrides().getModelWithOverrides(model, itemStack, world, entity);
              }, (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
              }, () -> new EnumMap<>(ItemCameraTransforms.TransformType.class)));
      return new BakedPerspectiveAwareModel(bakedModels, perspectiveModel.particle);
    }
  }

  public static final class Loader implements IModelLoader<PerspectiveAwareModel> {

    public static final Loader INSTANCE = new Loader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public PerspectiveAwareModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      Map<ItemCameraTransforms.TransformType, IUnbakedModel> models =
          new EnumMap<>(ItemCameraTransforms.TransformType.class);
      models
          .put(TransformType.NONE, deserializationContext
              .deserialize(JSONUtils.getJsonObject(modelContents, "model"), BlockModel.class));

      JsonArray modelsJson = modelContents.getAsJsonArray("perspective_overrides");
      for (JsonElement element : modelsJson) {
        JsonObject modelJson = element.getAsJsonObject();
        BlockModel model = deserializationContext
            .deserialize(JSONUtils.getJsonObject(modelJson, "model"), BlockModel.class);
        JsonArray perspectives = modelJson.getAsJsonArray("perspectives");
        for (JsonElement perspectiveJson : perspectives) {
          Perspective perpective = Perspective.fromKey(perspectiveJson.getAsString());
          if (perpective != null) {
            models.compute(perpective.getTransformType(), (transform, existingModel) -> {
              if (existingModel != null) {
                throw new IllegalStateException("Multiple models specified for same perspective");
              }
              return model;
            });
          }
        }
      }
      return new PerspectiveAwareModel(models);
    }
  }

  public static enum Perspective {
    THIRD_PERSON_LEFT_HAND(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
        "thirdperson_lefthand"), THIRD_PERSON_RIGHT_HAND(
            ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
            "thirdperson_righthand"), FIRST_PERSON_LEFT_HAND(
                ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                "firstperson_lefthand"), FIRST_PERSON_RIGHT_HAND(
                    ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                    "firstperson_righthand"), HEAD(ItemCameraTransforms.TransformType.HEAD,
                        "head"), GUI(ItemCameraTransforms.TransformType.GUI, "gui"), GROUND(
                            ItemCameraTransforms.TransformType.GROUND,
                            "ground"), FIXED(ItemCameraTransforms.TransformType.FIXED, "fixed");

    private final ItemCameraTransforms.TransformType transformType;
    private final String key;

    private Perspective(ItemCameraTransforms.TransformType transformType, String key) {
      this.transformType = transformType;
      this.key = key;
    }

    public ItemCameraTransforms.TransformType getTransformType() {
      return transformType;
    }

    public String getKey() {
      return key;
    }

    public static Perspective fromTransformType(
        ItemCameraTransforms.TransformType transformType) {
      for (Perspective perspective : values()) {
        if (perspective.transformType == transformType) {
          return perspective;
        }
      }
      return null;
    }

    public static Perspective fromKey(String key) {
      for (Perspective perspective : values()) {
        if (perspective.key.equals(key)) {
          return perspective;
        }
      }
      return null;
    }
  }
}
