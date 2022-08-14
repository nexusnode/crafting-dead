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

package com.craftingdead.immerse.data.models;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.item.ImmerseItems;
import com.google.gson.JsonElement;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ImmerseItemModelGenerators {

  private static final ModelTemplate FRUIT =
      new ModelTemplate(Optional.of(new ResourceLocation(CraftingDeadImmerse.ID, "item/fruit")),
          Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
  private static final ModelTemplate FOOD_BAR =
      new ModelTemplate(Optional.of(new ResourceLocation(CraftingDeadImmerse.ID, "item/food_bar")),
          Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
  private static final ModelTemplate CANNED_FOOD = new ModelTemplate(
      Optional.of(new ResourceLocation(CraftingDeadImmerse.ID, "item/canned_food")),
      Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
  private static final ModelTemplate DRINK_BOTTLE_ONE = new ModelTemplate(
      Optional.of(new ResourceLocation(CraftingDeadImmerse.ID, "item/drink_bottle_1")),
      Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
  private static final ModelTemplate FOOD_BOX =
      new ModelTemplate(Optional.of(new ResourceLocation(CraftingDeadImmerse.ID, "item/food_box")),
          Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
  private static final ModelTemplate FOOD_BAG =
      new ModelTemplate(Optional.of(new ResourceLocation(CraftingDeadImmerse.ID, "item/food_bag")),
          Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);


  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;

  public ImmerseItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
    this.output = output;
  }

  public void run() {
    this.generateFlatItem(ImmerseItems.RICE_BAG.get());
    this.generateFlatItem(ImmerseItems.NOODLES.get());
    this.generateFlatItem(ImmerseItems.ROTTEN_MELON_SLICE.get());
    this.generateFlatItem(ImmerseItems.BLUEBERRY.get());
    this.generateFlatItem(ImmerseItems.ROTTEN_BLUEBERRY.get());
    this.generateFlatItem(ImmerseItems.RASPBERRY.get());
    this.generateFlatItem(ImmerseItems.ROTTEN_RASPBERRY.get());
    this.generateFlatItem(ImmerseItems.CAN_OPENER.get());
    this.generateFlatItem(ImmerseItems.SCREWDRIVER.get());
    this.generateFlatItem(ImmerseItems.MULTI_TOOL.get());

    this.generateItemModel(ImmerseItems.ORANGE.get(), FRUIT);
    this.generateItemModel(ImmerseItems.ROTTEN_ORANGE.get(), FRUIT);
    this.generateItemModel(ImmerseItems.PEAR.get(), FRUIT);
    this.generateItemModel(ImmerseItems.ROTTEN_PEAR.get(), FRUIT);
    this.generateItemModel(ImmerseItems.ROTTEN_APPLE.get(), FRUIT);

    this.generateItemModel(ImmerseItems.POWER_BAR.get(), FOOD_BAR);
    this.generateItemModel(ImmerseItems.CANDY_BAR.get(), FOOD_BAR);

    this.generateItemModel(ImmerseItems.CANNED_SWEETCORN.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_SWEETCORN.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_BEANS.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_BEANS.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_TUNA.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_TUNA.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_PEACHES.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_PEACHES.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_PASTA.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_PASTA.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_CORNED_BEEF.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_CORNED_BEEF.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_CUSTARD.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_CUSTARD.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_PICKLES.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_PICKLES.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_DOG_FOOD.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_DOG_FOOD.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.CANNED_TOMATO_SOUP.get(), CANNED_FOOD);
    this.generateItemModel(ImmerseItems.OPEN_CANNED_TOMATO_SOUP.get(), CANNED_FOOD);

    this.generateItemModel(ImmerseItems.EMPTY_WATER_BOTTLE.get(), DRINK_BOTTLE_ONE);
    this.generateItemModel(ImmerseItems.WATER_BOTTLE.get(), DRINK_BOTTLE_ONE);

    this.generateItemModel(ImmerseItems.CEREAL.get(), FOOD_BOX);
    this.generateItemModel(ImmerseItems.NUTTY_CEREAL.get(), FOOD_BOX);
    this.generateItemModel(ImmerseItems.EMERALD_CEREAL.get(), FOOD_BOX);
    this.generateItemModel(ImmerseItems.FLAKE_CEREAL.get(), FOOD_BOX);

    this.generateItemModel(ImmerseItems.CHIPS.get(), FOOD_BAG);
    this.generateItemModel(ImmerseItems.RANCH_CHIPS.get(), FOOD_BAG);
    this.generateItemModel(ImmerseItems.CHEESY_CHIPS.get(), FOOD_BAG);
    this.generateItemModel(ImmerseItems.SALTED_CHIPS.get(), FOOD_BAG);
  }

  private void generateFlatItem(Item item) {
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.output);
  }

  private void generateItemModel(Item item, ModelTemplate template) {
    var texture = TextureMapping.getItemTexture(item);
    template.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.defaultTexture(texture).put(TextureSlot.PARTICLE, texture), this.output);
  }
}
