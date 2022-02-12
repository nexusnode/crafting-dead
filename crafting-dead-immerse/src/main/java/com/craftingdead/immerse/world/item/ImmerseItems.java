package com.craftingdead.immerse.world.item;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.action.ImmerseActionTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseItems {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDeadImmerse.ID);

  public static final RegistryObject<BlueprintItem> BASE_CENTER_BLUEPRINT =
      ITEMS.register("base_center_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BASE_CENTER, new Item.Properties()));

  public static final RegistryObject<BlueprintItem> CAMPFIRE_BLUEPRINT =
      ITEMS.register("campfire_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_CAMPFIRE, new Item.Properties()));

  public static final RegistryObject<BlueprintItem> OAK_PLANKS_WALL_BLUEPRINT =
      ITEMS.register("oak_planks_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_PLANKS_WALL, new Item.Properties()));
}
