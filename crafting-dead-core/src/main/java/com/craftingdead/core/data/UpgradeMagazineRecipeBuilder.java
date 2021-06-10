package com.craftingdead.core.data;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.craftingdead.core.world.item.crafting.ModRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class UpgradeMagazineRecipeBuilder {

  private final Ingredient magazine;
  private final Item nextTier;
  private final Advancement.Builder advancement = Advancement.Builder.advancement();
  private String group;

  private UpgradeMagazineRecipeBuilder(Ingredient magazine, Item nextTier) {
    this.magazine = magazine;
    this.nextTier = nextTier;
  }

  public static UpgradeMagazineRecipeBuilder create(Ingredient magazine, Item nextTier) {
    return new UpgradeMagazineRecipeBuilder(magazine, nextTier);
  }

  public UpgradeMagazineRecipeBuilder unlockedBy(String id, ICriterionInstance criterion) {
    this.advancement.addCriterion(id, criterion);
    return this;
  }

  public UpgradeMagazineRecipeBuilder group(String group) {
    this.group = group;
    return this;
  }

  public void save(Consumer<IFinishedRecipe> consumer) {
    this.save(consumer, ForgeRegistries.ITEMS.getKey(this.nextTier));
  }

  public void save(Consumer<IFinishedRecipe> consumer, String id) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.nextTier);
    if ((new ResourceLocation(id)).equals(resourcelocation)) {
      throw new IllegalStateException(
          "Shapeless Recipe " + id + " should remove its 'save' argument");
    } else {
      this.save(consumer, new ResourceLocation(id));
    }
  }

  public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
    this.ensureValid(id);
    this.advancement.parent(new ResourceLocation("recipes/root"))
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
        .rewards(AdvancementRewards.Builder.recipe(id))
        .requirements(IRequirementsStrategy.OR);
    consumer.accept(new UpgradeMagazineRecipeBuilder.Result(id, this.nextTier,
        this.group == null ? "" : this.group, this.magazine, this.advancement,
        new ResourceLocation(id.getNamespace(), "recipes/"
            + this.nextTier.getItemCategory().getRecipeFolderName() + "/" + id.getPath())));
  }

  private void ensureValid(ResourceLocation id) {
    if (this.advancement.getCriteria().isEmpty()) {
      throw new IllegalStateException("No way of obtaining recipe " + id);
    }
  }

  public static class Result implements IFinishedRecipe {

    private final ResourceLocation id;
    private final Item nextTier;
    private final String group;
    private final Ingredient magazine;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, Item nextTier,
        String group, Ingredient magazine, Advancement.Builder advancement,
        ResourceLocation advancementId) {
      this.id = id;
      this.nextTier = nextTier;
      this.group = group;
      this.magazine = magazine;
      this.advancement = advancement;
      this.advancementId = advancementId;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
      if (!this.group.isEmpty()) {
        json.addProperty("group", this.group);
      }

      json.add("magazine", this.magazine.toJson());

      JsonObject nextTierJson = new JsonObject();
      nextTierJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.nextTier).toString());

      json.add("nextTier", nextTierJson);
    }

    @Override
    public IRecipeSerializer<?> getType() {
      return ModRecipeSerializers.UPGRADE_MAGAZINE.get();
    }

    @Override
    public ResourceLocation getId() {
      return this.id;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return this.advancement.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return this.advancementId;
    }
  }
}
