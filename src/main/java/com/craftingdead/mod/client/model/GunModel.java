package com.craftingdead.mod.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.animation.IGunAnimation;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

@SuppressWarnings("deprecation")
public class GunModel implements IModelGeometry<GunModel> {

  private final IUnbakedModel base;

  public GunModel(IUnbakedModel base) {
    this.base = base;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {
    return new BakedGunModel(
        this.base.func_225613_a_(bakery, spriteGetter, modelTransform, modelLocation));
  }

  @Override
  public Collection<Material> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<Pair<String, String>> missingTextureErrors) {
    Set<Material> materials = new HashSet<>();
    materials.addAll(this.base.func_225614_a_(modelGetter, missingTextureErrors));
    return materials;
  }

  public static class BakedGunModel implements IBakedModel {

    private final IBakedModel base;

    public BakedGunModel(IBakedModel base) {
      this.base = base;
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
      this.base.handlePerspective(cameraTransformType, mat);
      mat.func_227860_a_();
      switch (cameraTransformType) {
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          IGunAnimation animation = ((ClientDist) CraftingDead.getInstance().getModDist())
              .getAnimationManager()
              .getCurrentAnimation(Minecraft.getInstance().player.getHeldItemMainhand());
          if (animation != null) {
            animation.apply(mat, Minecraft.getInstance().getRenderPartialTicks());
          }
          break;
        default:
          break;
      }
      return this;
    }

    @Override
    public ItemOverrideList getOverrides() {
      return ItemOverrideList.EMPTY;
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
      return new GunModel(base);
    }
  }
}
