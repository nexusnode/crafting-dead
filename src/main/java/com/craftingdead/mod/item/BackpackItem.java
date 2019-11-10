package com.craftingdead.mod.item;


import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.mod.type.Backpack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// TODO BAG: When you raise your backpack, you’ll be able to create a chest
public class BackpackItem extends Item {

  private Backpack backpack;

  public BackpackItem(Backpack backpack) {
    super(new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_WEARABLE));
    this.backpack = backpack;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    ItemStack itemStack = player.getHeldItem(handIn);
    player.openContainer(new SimpleNamedContainerProvider(
        this.backpack
            .getContainerProvider(this.readInventory(itemStack, this.backpack.getInventorySize())),
        this.getDisplayName(itemStack)));
    return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    System.out.println("test");
    return stack;
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {
    if (ItemStack.areItemsEqual(oldStack, newStack)) {
      return false;
    }
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
      ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  private IInventory readInventory(ItemStack itemStack, int size) {
    CompoundNBT compound = itemStack.getOrCreateChildTag("inventory");
    NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(compound.getCompound("inventory"), items);
    Inventory backpackInventory = new Inventory(items.toArray(new ItemStack[0]));
    backpackInventory
        .addListener((inventory) -> this.saveInventory(backpackInventory, size, compound));
    return backpackInventory;
  }

  private void saveInventory(IInventory inventory, int size, CompoundNBT compound) {
    NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      items.set(i, inventory.getStackInSlot(i));
    }
    compound.put("inventory",
        ItemStackHelper.saveAllItems(compound.getCompound("inventory"), items));
  }
}
