package com.craftingdead.mod.item;


import com.craftingdead.mod.container.BackpackContainer;
import com.craftingdead.mod.inventory.BackpackInventory;
import com.craftingdead.mod.type.Backpack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BackpackItem extends Item  {

    public BackpackItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ModItemGroups.CRAFTING_DEAD_WEAPON));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player,
                                                    Hand handIn) {

        ItemStack itemstack = player.getHeldItem(handIn);

        if (!(player instanceof ServerPlayerEntity)){
            return new ActionResult(ActionResultType.SUCCESS, itemstack);
        }
        try {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new BackpackContainer(id,playerInventory,new BackpackInventory(itemstack,Backpack.SMALL.getInventorySize()), Backpack.SMALL);
                }
                @Override
                public ITextComponent getDisplayName() {
                    return itemstack.getDisplayName();
                }
            });
        }catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        return stack;
    }
}
