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

package com.craftingdead.core.client.renderer.entity.layers;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.equipment.Clothing;
import com.craftingdead.core.world.item.equipment.Equipment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ClothingLayer<T extends LivingEntity, M extends HumanoidModel<T>>
    extends AbstractClothingLayer<T, M> {

  public ClothingLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Override
  protected ResourceLocation getClothingTexture(LivingEntity livingEntity, String skinType) {
    final var living = LivingExtension.get(livingEntity);
    if (living == null) {
      return null;
    }
    return living.getEquipmentInSlot(Equipment.Slot.CLOTHING, Clothing.class)
        .map(clothing -> clothing.getTexture(skinType))
        .orElse(null);
  }
}
