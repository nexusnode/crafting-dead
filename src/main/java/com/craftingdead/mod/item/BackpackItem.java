package com.craftingdead.mod.item;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.entity.CorpseEntity;
import com.craftingdead.mod.inventory.InventoryCorpse;
import com.craftingdead.mod.potion.ModEffects;
import com.craftingdead.mod.test.Backpack;
import com.craftingdead.mod.test.BackpackContainer;
import com.craftingdead.mod.test.BackpackInventory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class BackpackItem extends Item  {



    public BackpackItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES));
        setRegistryName("backpack");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player,
                                                    Hand handIn) {

        ItemStack itemstack = player.getHeldItem(handIn);


        player.openContainer(new SimpleNamedContainerProvider((id, playerInventory, playerEntity) -> {
            return BackpackContainer.createGeneric9X1(id, playerInventory);
        }, new StringTextComponent("test")));

        if (!(player instanceof ServerPlayerEntity)){
            return new ActionResult(ActionResultType.SUCCESS, itemstack);
        }
        /*
        try {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Nullable
                @Override
                public BackpackContainer createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return BackpackContainer.createGeneric9X1(id,playerInventory);
                }
                @Override
                public ITextComponent getDisplayName() {
                    return itemstack.getDisplayName();
                }
            });
        }catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }*/
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        return stack;
    }
}
