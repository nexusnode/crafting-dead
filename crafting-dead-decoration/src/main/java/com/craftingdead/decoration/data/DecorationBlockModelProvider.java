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
    this.modelVariant("oak", "plank_barricade", 3);
    this.modelVariant("spruce", "plank_barricade", 3);
    this.modelVariant("birch", "plank_barricade", 3);
    this.modelVariant("dark_oak", "plank_barricade", 3);

    this.textureVariant(DecorationBlocks.COMPUTER_1, "computer");
    this.textureVariant(DecorationBlocks.COMPUTER_2, "computer");
    this.textureVariant(DecorationBlocks.COMPUTER_3, "computer");

    this.textureVariant(DecorationBlocks.YELLOW_GAS_TANK, "gas_tank");
    this.textureVariant(DecorationBlocks.BLUE_GAS_TANK, "gas_tank");
    this.textureVariant(DecorationBlocks.GRAY_GAS_TANK, "gas_tank");

    this.textureVariant(DecorationBlocks.LAPTOP_1, "laptop");
    this.textureVariant(DecorationBlocks.LAPTOP_2, "laptop");
    this.textureVariant(DecorationBlocks.LAPTOP_3, "laptop");

    this.modelVariant("ripped", "office_chair", 3);
  }

  private void textureVariant(RegistryObject<? extends Block> block, String model) {
    this.singleTexture(block.getId().toString(), this.modLoc("block/" + model),
        this.modLoc("block/" + block.getId().getPath().toString()));
  }

  private void modelVariant(String variant, String model, int count) {
    for (int i = 1; i <= count; i++) {
      var name = "%s:block/%s_%s_%s".formatted(CraftingDeadDecoration.ID, variant, model, i);
      this.singleTexture(name, this.modLoc("block/%s_%s".formatted(model, i)),
          this.modLoc("block/%s_%s".formatted(variant, model)));
    }
  }
}
