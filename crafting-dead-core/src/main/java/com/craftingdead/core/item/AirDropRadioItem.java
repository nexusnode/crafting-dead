package com.craftingdead.core.item;

import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.entity.SupplyDropEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AirDropRadioItem extends Item {

  private final ResourceLocation lootTable;

  public AirDropRadioItem(AirDropRadioItem.Properties properties) {
    super(properties);
    this.lootTable = properties.lootTable;
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    World world = context.getWorld();
    BlockPos blockPos = context.getPos();
    ItemStack itemStack = context.getItem();
    SupplyDropEntity airDropEntity =
        new SupplyDropEntity(ModEntityTypes.supplyDrop, world, this.lootTable, random.nextLong(),
            blockPos.getX(), blockPos.getY() + 25.0D, blockPos.getZ());
    world.addEntity(airDropEntity);
    itemStack.shrink(1);
    return ActionResultType.SUCCESS;
  }

  public static class Properties extends Item.Properties {

    private ResourceLocation lootTable;

    public ResourceLocation getLootTable() {
      return lootTable;
    }

    public Properties setLootTable(ResourceLocation lootTable) {
      this.lootTable = lootTable;
      return this;
    }
  }
}
