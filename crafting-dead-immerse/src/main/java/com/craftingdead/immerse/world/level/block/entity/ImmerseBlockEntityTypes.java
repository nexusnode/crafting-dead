package com.craftingdead.immerse.world.level.block.entity;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseBlockEntityTypes {

  public static final DeferredRegister<BlockEntityType<?>> blockEntityTypes =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CraftingDeadImmerse.ID);

  public static final RegistryObject<BlockEntityType<BaseCenterBlockEntity>> BASE_CENTER =
      blockEntityTypes.register("base_center",
          () -> BlockEntityType.Builder
              .of(BaseCenterBlockEntity::new, ImmerseBlocks.BASE_CENTER.get())
              .build(null));
}
