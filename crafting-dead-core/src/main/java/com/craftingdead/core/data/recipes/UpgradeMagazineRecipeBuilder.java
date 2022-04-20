/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.data.recipes;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.craftingdead.core.world.item.crafting.ModRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
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

  public UpgradeMagazineRecipeBuilder unlockedBy(String id, CriterionTriggerInstance criterion) {
    this.advancement.addCriterion(id, criterion);
    return this;
  }

  public UpgradeMagazineRecipeBuilder group(String group) {
    this.group = group;
    return this;
  }

  public void save(Consumer<FinishedRecipe> consumer) {
    this.save(consumer, ForgeRegistries.ITEMS.getKey(this.nextTier));
  }

  public void save(Consumer<FinishedRecipe> consumer, String id) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.nextTier);
    if ((new ResourceLocation(id)).equals(resourcelocation)) {
      throw new IllegalStateException(
          "Shapeless Recipe " + id + " should remove its 'save' argument");
    } else {
      this.save(consumer, new ResourceLocation(id));
    }
  }

  public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
    this.ensureValid(id);
    this.advancement.parent(new ResourceLocation("recipes/root"))
        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
        .rewards(AdvancementRewards.Builder.recipe(id))
        .requirements(RequirementsStrategy.OR);
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

  public static class Result implements FinishedRecipe {

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
    public RecipeSerializer<?> getType() {
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
