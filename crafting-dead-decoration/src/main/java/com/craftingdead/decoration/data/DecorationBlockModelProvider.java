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

    this.computer(DecorationBlocks.COMPUTER_1);
    this.computer(DecorationBlocks.COMPUTER_2);
    this.computer(DecorationBlocks.COMPUTER_3);

    this.gasTank(DecorationBlocks.YELLOW_GAS_TANK);
    this.gasTank(DecorationBlocks.BLUE_GAS_TANK);
    this.gasTank(DecorationBlocks.GRAY_GAS_TANK);
  }

  private void gasTank(RegistryObject<? extends Block> block) {
    this.singleTexture(block.getId().toString(), this.modLoc("block/gas_tank"),
        this.modLoc("block/" + block.getId().getPath().toString()));
  }

  private void computer(RegistryObject<? extends Block> block) {
    this.singleTexture(block.getId().toString(), this.modLoc("block/computer"),
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
