package com.craftingdead.decoration.world.item;

import com.craftingdead.decoration.CraftingDeadDecoration;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DecorationItems {

  public static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDeadDecoration.ID);

  public static final CreativeModeTab TAB =
      new CreativeModeTab(CraftingDeadDecoration.ID) {

        @Override
        public ItemStack makeIcon() {
          return new ItemStack(STACKED_WOODEN_PALLETS.get());
        }
      };

  public static final RegistryObject<BlockItem> WOODEN_PALLET =
      deferredRegister.register("wooden_pallet",
          () -> new BlockItem(DecorationBlocks.WOODEN_PALLET.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> STACKED_WOODEN_PALLETS =
      deferredRegister.register("stacked_wooden_pallets",
          () -> new BlockItem(DecorationBlocks.STACKED_WOODEN_PALLETS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> CRATE_ON_WOODEN_PALLET =
      deferredRegister.register("crate_on_wooden_pallet",
          () -> new BlockItem(DecorationBlocks.CRATE_ON_WOODEN_PALLET.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> SECURITY_CAMERA =
      deferredRegister.register("security_camera",
          () -> new BlockItem(DecorationBlocks.SECURITY_CAMERA.get(),
              new Item.Properties().tab(TAB)));
}
