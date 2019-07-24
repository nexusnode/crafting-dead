package com.craftingdead.mod.capability.triggerable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class DefaultTriggerable implements ITriggerable {

  private static final Logger logger = LogManager.getLogger();

  @Override
  public void tick(ItemStack itemStack, Entity entity) {}

  @Override
  public void setTriggerPressed(boolean triggerPressed, ItemStack itemStack, Entity entity) {
    logger.info("Trigger pressed: {}", triggerPressed);
  }
}
