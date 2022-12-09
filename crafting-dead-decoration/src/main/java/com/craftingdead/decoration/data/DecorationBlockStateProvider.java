package com.craftingdead.decoration.data;

import com.craftingdead.decoration.CraftingDeadDecoration;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import com.craftingdead.decoration.world.level.block.DoubleBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlockStateProvider extends BlockStateProvider {

  public DecorationBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, CraftingDeadDecoration.ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    this.horizontalBlock(DecorationBlocks.WOODEN_PALLET);
    this.horizontalBlock(DecorationBlocks.STACKED_WOODEN_PALLETS);
    this.horizontalBlock(DecorationBlocks.CRATE_ON_WOODEN_PALLET);
    this.horizontalBlock(DecorationBlocks.SECURITY_CAMERA);
    this.horizontalBlock(DecorationBlocks.WASHING_MACHINE);
    this.horizontalBlock(DecorationBlocks.BROKEN_WASHING_MACHINE);
    this.horizontalBlock(DecorationBlocks.LIGHT_SWITCH);
    this.horizontalBlock(DecorationBlocks.ELECTRICAL_SOCKET);
    this.horizontalBlock(DecorationBlocks.ABANDONED_CAMPFIRE);

    this.horizontalBlock(DecorationBlocks.CLOTHING_RACK.get(),
        state -> state.getValue(DoubleBlock.PART) == DoubleBlock.Part.LEFT
            ? this.blockModel("clothing_rack_left")
            : this.blockModel("clothing_rack_right"),
        90);
    

  }

  private void horizontalBlock(RegistryObject<? extends Block> block) {
    this.horizontalBlock(block.get(), block.getId().getPath());
  }

  private void horizontalBlock(Block block, String path) {
    var model = this.blockModel(path);
    this.horizontalBlock(block, model);
    this.simpleBlockItem(block, model);
  }

  private ModelFile blockModel(String path) {
    return this.model("block/" + path);
  }

  private ModelFile model(String path) {
    return this.models().getExistingFile(new ResourceLocation(CraftingDeadDecoration.ID, path));
  }
}
