package com.craftingdead.mod.tileentity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.block.ModBlocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModTileEntityTypes {

  private static final List<TileEntityType<?>> toRegister = new ArrayList<>();

  public static TileEntityType<?> loot = null;

  public static void initialize() {
    loot = add("loot",
        TileEntityType.Builder.create(TileEntityLoot::new, ModBlocks.residentialLoot).build(null));
  }

  public static void register(RegistryEvent.Register<TileEntityType<?>> event) {
    toRegister.forEach(event.getRegistry()::register);
  }

  private static <T extends TileEntity> TileEntityType<T> add(String registryName,
      TileEntityType<T> tileEntityType) {
    toRegister
        .add(tileEntityType.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName)));
    return tileEntityType;
  }
}
