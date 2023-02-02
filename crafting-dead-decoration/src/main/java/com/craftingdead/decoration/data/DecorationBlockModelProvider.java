package com.craftingdead.decoration.data;

import com.craftingdead.decoration.CraftingDeadDecoration;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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

    this.textureVariant(DecorationBlocks.COMPUTER_1, "computer");
    this.textureVariant(DecorationBlocks.COMPUTER_2, "computer");
    this.textureVariant(DecorationBlocks.COMPUTER_3, "computer");

    this.textureVariant(DecorationBlocks.YELLOW_GAS_TANK, "gas_tank");
    this.textureVariant(DecorationBlocks.BLUE_GAS_TANK, "gas_tank");
    this.textureVariant(DecorationBlocks.GRAY_GAS_TANK, "gas_tank");

    this.textureVariant(DecorationBlocks.LAPTOP_1, "laptop");
    this.textureVariant(DecorationBlocks.LAPTOP_2, "laptop");
    this.textureVariant(DecorationBlocks.LAPTOP_3, "laptop");
  }

  private void textureVariant(RegistryObject<? extends Block> block, String model) {
    this.singleTexture(block.getId().toString(), this.modLoc("block/" + model),
        this.modLoc("block/" + block.getId().getPath().toString()));
  }

  private void plankBarricades(String variant) {
    for (int i = 1; i <= 3; i++) {
      var name = "%s:block/%s_plank_barricade_%s".formatted(CraftingDeadDecoration.ID, variant, i);
      this.singleTexture(name, this.modLoc("block/plank_barricade_" + i),
          this.modLoc("block/%s_plank_barricade".formatted(variant)));
    }
  }
}
