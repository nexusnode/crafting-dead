package com.craftingdead.mod.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.mod.type.Vest;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class VestItem extends Item {

  private Vest vest;
  
  public VestItem(Vest vest, Properties properties) {
    super(properties);
    this.vest = vest;
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity ePlayer, Hand playerHand) {
    ItemStack stack = ePlayer.getHeldItem(playerHand);
    ePlayer.openContainer(new SimpleNamedContainerProvider(this.vest.getContainerProvider(this.readInventory(stack, 
        this.vest.getInventorySize())), this.getDisplayName(stack)));
    return new ActionResult<>(ActionResultType.SUCCESS, stack);
  }
  
  private IInventory readInventory(ItemStack itemStack, int size) {
    CompoundNBT compound = itemStack.getOrCreateChildTag("inventory");
    NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(compound.getCompound("inventory"), items);
    Inventory vestInventory = new Inventory(items.toArray(new ItemStack[0]));
    vestInventory.addListener((inventory) -> this.saveInventory(vestInventory, size, compound));
    return vestInventory;
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    if (ItemStack.areItemsEqual(oldStack, newStack)) {
      return false;
    }
    return true;
  }
  
  private void saveInventory(IInventory inventory, int size, CompoundNBT compound) {
    NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      items.set(i, inventory.getStackInSlot(i));
    }
    compound.put("inventory", ItemStackHelper.saveAllItems(compound.getCompound("inventory"), items));
  }


}
