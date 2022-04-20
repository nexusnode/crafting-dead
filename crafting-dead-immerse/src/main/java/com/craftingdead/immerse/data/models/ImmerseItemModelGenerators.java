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

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.craftingdead.immerse.world.item.ImmerseItems;
import com.google.gson.JsonElement;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ImmerseItemModelGenerators {

  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;

  public ImmerseItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
    this.output = output;
  }

  private void generateFlatItem(Item item, ModelTemplate model) {
    model.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.output);
  }

  public void run() {
    this.generateFlatItem(ImmerseItems.POWER_BAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANDY_BAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_CORN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_CORN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_BEANS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_BEANS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_TUNA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_TUNA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_PEACHES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_PEACHES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_PASTA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_PASTA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_BACON.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_BACON.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_CUSTARD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_CUSTARD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_PICKLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_PICKLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_DOG_FOOD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_DOG_FOOD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CANNED_TOMATO_SOUP.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.OPEN_CANNED_TOMATO_SOUP.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.MRE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ORANGE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ROTTEN_ORANGE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.PEAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ROTTEN_PEAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.RICE_BAG.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ROTTEN_APPLE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.NOODLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ROTTEN_MELON_SLICE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.BLUEBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ROTTEN_BLUEBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.RASPBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.ROTTEN_RASPBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.RANCH_CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CHEESY_CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.SALTED_CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.POPCORN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.NUTTY_CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.EMERALD_CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.FLAKE_CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.CAN_OPENER.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.SCREWDRIVER.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(ImmerseItems.MULTI_TOOL.get(), ModelTemplates.FLAT_ITEM);
  }
}
