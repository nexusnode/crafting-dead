package com.craftingdead.decoration.world.level.block;

import com.craftingdead.decoration.CraftingDeadDecoration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlocks {

  public static final DeferredRegister<Block> deferredRegister =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CraftingDeadDecoration.ID);

  public static final RegistryObject<Block> WOODEN_PALLET =
      deferredRegister.register("wooden_pallet",
          () -> new WoodenPalletBlock(
              BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                  .strength(2.0F, 3.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion()
                  .lightLevel(__ -> 1),
              WoodenPalletBlock.SINGLE_SHAPE));

  public static final RegistryObject<Block> STACKED_WOODEN_PALLETS =
      deferredRegister.register("stacked_wooden_pallets",
          () -> new WoodenPalletBlock(
              BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                  .strength(2.0F, 3.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion()
                  .lightLevel(__ -> 1),
              WoodenPalletBlock.STACKED_SHAPE));

  public static final RegistryObject<Block> CRATE_ON_WOODEN_PALLET =
      deferredRegister.register("crate_on_wooden_pallet",
          () -> new WoodenPalletBlock(
              BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                  .strength(2.0F, 3.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion()
                  .lightLevel(__ -> 1),
              Shapes.block()));

  public static final RegistryObject<Block> SECURITY_CAMERA =
      deferredRegister.register("security_camera",
          () -> new SecurityCameraBlock(
              BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                  .strength(20.5F)
                  .sound(SoundType.WOOD)
                  .noOcclusion()
                  .lightLevel(__ -> 1)));
}
