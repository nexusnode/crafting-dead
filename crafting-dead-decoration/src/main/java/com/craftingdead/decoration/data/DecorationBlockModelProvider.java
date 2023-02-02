package com.craftingdead.decoration.data;

import com.craftingdead.decoration.CraftingDeadDecoration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DecorationBlockModelProvider extends BlockModelProvider {

  public DecorationBlockModelProvider(DataGenerator generator,
      ExistingFileHelper existingFileHelper) {
    super(generator, CraftingDeadDecoration.ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    this.plankBarricades("oak");
    this.plankBarricades("spruce");
    this.plankBarricades("birch");
    this.plankBarricades("dark_oak");
  }

  private void plankBarricades(String variant) {
    for (int i = 1; i <= 3; i++) {
      var name = "%s:block/%s_plank_barricade_%s".formatted(CraftingDeadDecoration.ID, variant, i);
      this.singleTexture(name, this.modLoc("block/plank_barricade_" + i),
          new ResourceLocation(CraftingDeadDecoration.ID,
              "block/%s_plank_barricade".formatted(variant)));
    }
  }
}
