package com.craftingdead.survival.client.model.geom;

import java.util.Set;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class SurvivalModelLayers {


  private static final String DEFAULT_LAYER = "main";
  private static final Set<ModelLayerLocation> allModels = Sets.newHashSet();

  public static final ModelLayerLocation SUPPLY_DROP = register("supply_drop");

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
    return new ModelLayerLocation(new ResourceLocation(CraftingDeadSurvival.ID, model), layer);
  }
}
