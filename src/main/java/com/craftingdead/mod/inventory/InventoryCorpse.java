package com.craftingdead.mod.inventory;

import com.craftingdead.mod.entity.EntityCorpse;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.INBTSerializable;

public class InventoryCorpse implements IInventory, INBTSerializable<NBTTagCompound> {

	private final EntityCorpse corpse;

	private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);

	private boolean dirty;

	public InventoryCorpse(EntityCorpse corpse) {
		this.corpse = corpse;
	}

	@Override
	public String getName() {
		return this.corpse.getCustomNameTag() + "'s Corpse";
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemStack = ItemStackHelper.getAndSplit(this.items, index, count);
		if (!itemStack.isEmpty()) {
			this.markDirty();
		}
		return itemStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		this.dirty = true;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(this.corpse) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		;
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStackHelper.saveAllItems(nbt, this.items);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.items);
	}

	public void dropAllItems() {
		this.items.forEach((itemStack) -> {
			this.corpse.world.spawnEntity(
					new EntityItem(this.corpse.world, this.corpse.posX, this.corpse.posY, this.corpse.posZ, itemStack));
		});
		this.clear();
	}

	/**
	 * Copy the passed list into this inventory
	 * 
	 * @param otherItems - the list to copy from <b>(must be the same size)</b>
	 */
	public void copy(NonNullList<ItemStack> otherItems) {
		if (otherItems.size() != this.items.size())
			throw new IllegalStateException("Size must be the same");
		for (int i = 0; i < otherItems.size(); i++) {
			this.items.set(i, otherItems.get(i));
		}
	}

	public boolean isDirty() {
		return this.dirty;
	}

}
