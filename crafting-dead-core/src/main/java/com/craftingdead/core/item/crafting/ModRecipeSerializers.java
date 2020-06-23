package com.craftingdead.core.item.crafting;

import com.craftingdead.core.CraftingDead;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {

  public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
      new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, CraftingDead.ID);

  public static final RegistryObject<SpecialRecipeSerializer<UpgradeMagazineRecipe>> UPGRADE_MAGAZINE =
      RECIPE_SERIALIZERS
          .register("upgrade_magazine",
              () -> new SpecialRecipeSerializer<>(UpgradeMagazineRecipe::new));
}
