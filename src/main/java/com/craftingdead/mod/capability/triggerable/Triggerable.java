package com.craftingdead.mod.capability.triggerable;

import com.craftingdead.mod.capability.SimpleCapability;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface Triggerable extends SimpleCapability {

	void update(ItemStack itemStack, Entity entity);

	void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity);

}
