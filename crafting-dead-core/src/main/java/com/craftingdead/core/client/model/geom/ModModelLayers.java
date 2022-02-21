/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.model.geom;

import java.util.Set;
import com.craftingdead.core.CraftingDead;
import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

  private static final String DEFAULT_LAYER = "main";
  private static final Set<ModelLayerLocation> allModels = Sets.newHashSet();

  public static final ModelLayerLocation MUZZLE_FLASH = register("muzzle_flash");
  public static final ModelLayerLocation PARACHUTE = register("parachute");
  public static final ModelLayerLocation HANDCUFFS = register("handcuffs");
  public static final ModelLayerLocation C4_EXPLOSIVE = register("c4_explosive");
  public static final ModelLayerLocation CYLINDER_GRENADE = register("cylinder_grenade");
  public static final ModelLayerLocation FRAG_GRENADE = register("frag_grenade");
  public static final ModelLayerLocation SLIM_GRENADE = register("slim_grenade");

  private static ModelLayerLocation register(String model) {
    return register(model, DEFAULT_LAYER);
  }

  private static ModelLayerLocation register(String model, String layer) {
    var location = createLocation(model, layer);
    if (!allModels.add(location)) {
      throw new IllegalStateException("Duplicate registration for " + location);
    } else {
      return location;
    }
  }

  private static ModelLayerLocation createLocation(String model, String layer) {
    return new ModelLayerLocation(new ResourceLocation(CraftingDead.ID, model), layer);
  }
}
