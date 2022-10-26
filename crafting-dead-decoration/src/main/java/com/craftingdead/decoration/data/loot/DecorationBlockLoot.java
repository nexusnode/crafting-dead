package com.craftingdead.decoration.data.loot;

import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlockLoot extends BlockLoot {

  @Override
  protected void addTables() {
    for (var entry : DecorationBlocks.deferredRegister.getEntries()) {
      this.dropSelf(entry.get());
    }
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return DecorationBlocks.deferredRegister.getEntries().stream()
        .map(RegistryObject::get)
        .toList();
  }
}
