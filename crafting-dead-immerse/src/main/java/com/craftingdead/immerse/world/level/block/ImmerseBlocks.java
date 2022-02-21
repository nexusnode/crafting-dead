package com.craftingdead.immerse.world.level.block;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseBlocks {

  public static final DeferredRegister<Block> blocks =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CraftingDeadImmerse.ID);

  public static final RegistryObject<Block> BASE_CENTER =
      blocks.register("base_center",
          () -> new BaseCenterBlock(BlockBehaviour.Properties.copy(Blocks.BEDROCK).noOcclusion()));
}
