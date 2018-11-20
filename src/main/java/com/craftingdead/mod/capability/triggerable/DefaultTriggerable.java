package com.craftingdead.mod.capability.triggerable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class DefaultTriggerable implements Triggerable {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public NBTTagCompound writeNBT(EnumFacing side) {
		return new NBTTagCompound();
	}

	@Override
	public void readNBT(EnumFacing side, NBTTagCompound nbt) {
		;
	}

	@Override
	public void update(ItemStack itemStack, Entity entity) {
		;
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity) {
		LOGGER.info("Trigger pressed: {}", triggerPressed);
	}

}
