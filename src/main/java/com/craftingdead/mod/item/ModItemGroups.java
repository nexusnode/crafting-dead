package com.craftingdead.mod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

    public static final ItemGroup CRAFTING_DEAD_CONSUMABLES =
            (new ItemGroup("craftingdead_consumables") {

                @Override
                public ItemStack createIcon() {
                    return new ItemStack(ModItems.powerBar);
                }
            });

    public static final ItemGroup CRAFTING_DEAD_MISC =
            (new ItemGroup("craftingdead_misc") {

                @Override
                public ItemStack createIcon() {
                    return new ItemStack(ModItems.canOpener);
                }
            });

    public static final ItemGroup CRAFTING_DEAD_MED =
            (new ItemGroup("craftingdead_med") {

                @Override
                public ItemStack createIcon() {
                    return new ItemStack(ModItems.medpack);
                }
            });
}
