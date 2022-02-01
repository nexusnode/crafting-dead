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
