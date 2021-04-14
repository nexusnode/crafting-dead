package com.craftingdead.core.item.crafting;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.item.gun.magazine.IMagazine;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class DuplicateMagazineRecipe extends SpecialRecipe {

  public DuplicateMagazineRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingInventory inventory, World world) {
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      switch (i) {
        case 0:
        case 2:
        case 6:
        case 8:
          if (!Tags.Items.NUGGETS_IRON.contains(inventory.getItem(i).getItem())) {
            return false;
          }
          break;
        case 4:
          if (!inventory.getItem(i).getCapability(ModCapabilities.MAGAZINE).isPresent()) {
            return false;
          }
          break;
        default:
          if (!inventory.getItem(i).isEmpty()) {
            return false;
          }
          break;
      }
    }
    return true;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory) {
    NonNullList<ItemStack> remainingItems =
        NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);

    for (int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = inventory.getItem(i);
      if (item.getCapability(ModCapabilities.MAGAZINE).isPresent()) {
        remainingItems.set(i, item.copy());
      } else if (item.hasContainerItem()) {
        remainingItems.set(i, item.getContainerItem());
      }
    }

    return remainingItems;
  }

  @Override
  public ItemStack assemble(CraftingInventory inventory) {
    ItemStack result = inventory.getItem(4).copy();
    ModCapabilities.getExpected(ModCapabilities.MAGAZINE, result, IMagazine.class).setSize(0);
    return result;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height == 9;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipeSerializers.DUPLICATE_MAGAZINE.get();
  }
}
